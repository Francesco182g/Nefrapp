package test.model;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import org.bson.Document;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;

import bean.Amministratore;
import bean.Utente;
import control.GestioneAccesso;
import control.GestioneAmministratore;
import model.AmministratoreModel;
import model.DriverConnection;
import model.MedicoModel;
import model.PazienteModel;
import utility.CriptazioneUtility;


class AmministratoreModelTest {
	private GestioneAmministratore servlet;
	private MockHttpServletRequest request;
	private MockHttpServletResponse response;
	private static Utente admin;
	private MockHttpSession session;
	private String password = CriptazioneUtility.criptaConMD5("Pippo1234");

	
	
	@BeforeAll
	void setUpBeforeClass() throws Exception {
		admin = new Amministratore("FLPBRZ62F17F876F", "Filippo", "Carbosiero", "f.carbosiero@live.it");
		servlet = new GestioneAmministratore();
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		session  = new  MockHttpSession();
		session.setAttribute("utente", admin);
		request.setSession(session);
		MongoCollection<Document> amministratore = DriverConnection.getConnection().getCollection("Amministratore");
		Document doc = new Document("CodiceFiscale", "FLPBRZ61A45F234F").append("Password", password);
		amministratore.insertOne(doc);
	}

	@AfterAll
	void tearDownAfterClass() throws Exception {
		session.invalidate();
		MongoCollection<Document> amministratore = DriverConnection.getConnection().getCollection("Amministratore");
		BasicDBObject document = new BasicDBObject();
		document.put("CodiceFiscale", "FLPBRZ61A45F234F");
		amministratore.deleteOne(document);	
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testGetAmministratoreByCFPassword() {
		Amministratore adminTest = AmministratoreModel.getAmministratoreByCFPassword("FLPBRZ62F17F876F", password);
		assertNotNull(adminTest);
		assertEquals(adminTest.getCodiceFiscale(), "FLPBRZ62F17F876F");	
		assertEquals(adminTest.getNome(), "Filippo");
		assertEquals(adminTest.getCognome(), "Carbosiero");
		assertEquals(adminTest.getEmail(), "f.carbosiero@live.it");
	}
	
	@Test
	void testGetAmministratoreByCF() {
		Amministratore adminTest = AmministratoreModel.getAmministratoreByCF("FLPBRZ62F17F876F");
		assertNotNull(adminTest);
		assertEquals(adminTest.getCodiceFiscale(), "FLPBRZ62F17F876F");	
		assertEquals(adminTest.getNome(), "Filippo");
		assertEquals(adminTest.getCognome(), "Carbosiero");
		assertEquals(adminTest.getEmail(), "f.carbosiero@live.it");
		
	}


	@Test
	void testGetPassword() {
		String passwordTest = AmministratoreModel.getPassword("FLPBRZ62F17F876F");
		assertEquals(passwordTest, password);
	}

	@Test
	void testUpdateAmministratore() {
		String nuovaPassword = CriptazioneUtility.criptaConMD5("Pippo5678");
		AmministratoreModel.updateAmministratore("FLPBRZ62F17F876F", nuovaPassword);
		Amministratore adminTest = AmministratoreModel.getAmministratoreByCFPassword("FLPBRZ62F17F876F", nuovaPassword);
		assertNotNull(adminTest);
		assertEquals(adminTest.getCodiceFiscale(), "FLPBRZ62F17F876F");	
		assertEquals(adminTest.getNome(), "Filippo");
		assertEquals(adminTest.getCognome(), "Carbosiero");
		assertEquals(adminTest.getEmail(), "f.carbosiero@live.it");
	}
}
