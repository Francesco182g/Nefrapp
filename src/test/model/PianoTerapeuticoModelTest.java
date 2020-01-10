package test.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.bson.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;

import bean.Medico;
import bean.PianoTerapeutico;
import model.DriverConnection;
import model.MedicoModel;
import model.PianoTerapeuticoModel;

class PianoTerapeuticoModelTest {
	private static final String CODICE_FISCALE_PAZIENTE = "CRRSRA90A50A091Q";
	private static final String DIAGNOSI = "Devi prendere i farmaci sottostanti due volte al giorno";
	private static final String FARMACO = "Lasix 30mm";
	private static final LocalDate FINE_TERAPIA = LocalDate.parse("2020-07-11");
	private static final Boolean VISUALIZZATO = false;
	
	@BeforeEach
	void setUp() {
		PianoTerapeutico pianoTerapeutico = new PianoTerapeutico(CODICE_FISCALE_PAZIENTE, DIAGNOSI, FARMACO, FINE_TERAPIA);
		PianoTerapeuticoModel.addPianoTerapeutico(pianoTerapeutico);
	}
	
	@AfterEach
	void tearDown() {
		MongoCollection<Document> pazienti = DriverConnection.getConnection().getCollection("PianoTerapeutico");
		BasicDBObject document = new BasicDBObject();
		document.put("PazienteCodiceFiscale", CODICE_FISCALE_PAZIENTE);
		pazienti.deleteOne(document);
	}
	
	@Test
	void testGetPianoTerapeuticoByPaziente() {
		PianoTerapeutico pianoTerapeutico = PianoTerapeuticoModel.getPianoTerapeuticoByPaziente(CODICE_FISCALE_PAZIENTE);
		assertNotNull(pianoTerapeutico);
		assertEquals(pianoTerapeutico.getCodiceFiscalePaziente(), CODICE_FISCALE_PAZIENTE);
		assertEquals(pianoTerapeutico.getDiagnosi(), DIAGNOSI);
		assertEquals(pianoTerapeutico.getFarmaco(), FARMACO);
		assertEquals(pianoTerapeutico.getDataFineTerapia(), FINE_TERAPIA);
		assertEquals(pianoTerapeutico.getVisualizzato(), VISUALIZZATO);
	}
	
	@Test
	void testUpdatePianoTerapeutico() {
		PianoTerapeutico daAggiornare = PianoTerapeuticoModel.getPianoTerapeuticoByPaziente(CODICE_FISCALE_PAZIENTE);
		assertNotNull(daAggiornare);
		daAggiornare.setDiagnosi("Devi prendere i farmaci sottostanti una volta al giorno");
		daAggiornare.setFarmaco("Lasix 50mm");
		PianoTerapeuticoModel.updatePianoTerapeutico(daAggiornare);
		daAggiornare = PianoTerapeuticoModel.getPianoTerapeuticoByPaziente(CODICE_FISCALE_PAZIENTE);
		assertNotNull(daAggiornare);
		assertEquals(daAggiornare.getDiagnosi(), "Devi prendere i farmaci sottostanti una volta al giorno");
		assertEquals(daAggiornare.getFarmaco(), "Lasix 50mm");
	}
	
	@Test
	void testIsPianoTerapeuticoVisualizzato() {
		Boolean isVisualizzato = PianoTerapeuticoModel.isPianoTerapeuticoVisualizzato(CODICE_FISCALE_PAZIENTE);
		assertEquals(isVisualizzato, false);
	}
	
	@Test
	void testSetVisualizzatoPianoTerapeutico() {
		PianoTerapeuticoModel.setVisualizzatoPianoTerapeutico(CODICE_FISCALE_PAZIENTE, true);
		PianoTerapeutico pianoTerapeutico = PianoTerapeuticoModel.getPianoTerapeuticoByPaziente(CODICE_FISCALE_PAZIENTE);
		assertEquals(pianoTerapeutico.getVisualizzato(), true);
	}

}