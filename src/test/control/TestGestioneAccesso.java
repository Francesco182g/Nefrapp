package test.control;

import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import javax.servlet.ServletException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import control.GestioneAccesso;

class TestGestioneAccesso {

	private GestioneAccesso servlet;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		servlet = new GestioneAccesso();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testAccessoSenzaOperazione() throws ServletException, IOException {
		servlet.doGet(request, response);
		assertEquals("./paginaErrore.jsp?notifica=eccezione", response.getRedirectedUrl());
	}
	
	@Test
	void TC_GA_1_1_LoginAmministratore() throws ServletException, IOException {
		request.setParameter("operazione", "loginAdmin");
		request.setParameter("codiceFiscale", "FLPBRZ61A45F254F");
		request.setParameter("password","Pippo1234");
		servlet.doGet(request, response);
		System.out.println(response.getRedirectedUrl());
		assertEquals("./loginAmministratore.jsp?notifica=datiLoginErrati", response.getRedirectedUrl());
	}
	@Test
	void TC_GA_1_2_LoginAmministratore() throws ServletException, IOException {
		request.setParameter("operazione", "loginAdmin");
		request.setParameter("codiceFiscale", "FF123FA45FF23334F");
		request.setParameter("password","Pippo1234");
		servlet.doGet(request, response);
		System.out.println(response.getRedirectedUrl());
		assertEquals("./loginAmministratore.jsp?notifica=datiLoginErrati", response.getRedirectedUrl());
	}
	@Test
	void TC_GA_1_3_LoginAmministratore() throws ServletException, IOException {
		request.setParameter("operazione", "loginAdmin");
		request.setParameter("codiceFiscale", "FF123FA45FF23*4F");
		request.setParameter("password","Pippo1234");
		servlet.doGet(request, response);
		System.out.println(response.getRedirectedUrl());
		assertEquals("./loginAmministratore.jsp?notifica=datiLoginErrati", response.getRedirectedUrl());
	}
	@Test
	void TC_GA_1_4_LoginAmministratore() throws ServletException, IOException {
		request.setParameter("operazione", "loginAdmin");
		request.setParameter("codiceFiscale", "FLPBRZ61A45F254F");
		request.setParameter("password","Pippo1234");
		servlet.doGet(request, response);
		System.out.println(response.getRedirectedUrl());
		assertEquals("./loginAmministratore.jsp?notifica=datiLoginErrati", response.getRedirectedUrl());
	}
	@Test
	void TC_GA_1_5_LoginAmministratore() throws ServletException, IOException {
		request.setParameter("operazione", "loginAdmin");
		request.setParameter("codiceFiscale", "FLPBRZ61A45F234F");
		request.setParameter("password","Pipo");
		servlet.doGet(request, response);
		System.out.println(response.getRedirectedUrl());
		assertEquals("./loginAmministratore.jsp?notifica=datiLoginErrati", response.getRedirectedUrl());
	}
	@Test
	void TC_GA_1_6_LoginAmministratore() throws ServletException, IOException {
		request.setParameter("operazione", "loginAdmin");
		request.setParameter("codiceFiscale", "FLPBRZ61A45F234F");
		request.setParameter("password","SperoCheQuestaSiaUnaBuonaPassword");
		servlet.doGet(request, response);
		System.out.println(response.getRedirectedUrl());
		assertEquals("./loginAmministratore.jsp?notifica=datiLoginErrati", response.getRedirectedUrl());
	}
	
	@Test
	void TC_GA_1_7_LoginAmministratore() throws ServletException, IOException {
		request.setParameter("operazione", "loginAdmin");
		request.setParameter("codiceFiscale", "FLPBRZ61A45F234F");
		request.setParameter("password","Pippo12__90");
		servlet.doGet(request, response);
		System.out.println(response.getRedirectedUrl());
		assertEquals("./loginAmministratore.jsp?notifica=datiLoginErrati", response.getRedirectedUrl());
	}
	
	@Test
	void TC_GA_1_8_LoginAmministratore() throws ServletException, IOException {
		request.setParameter("operazione", "loginAdmin");
		request.setParameter("codiceFiscale", "FLPBRZ61A45F234F");
		request.setParameter("password","Pippo1290");
		servlet.doGet(request, response);
		System.out.println(response.getRedirectedUrl());
		assertEquals("./loginAmministratore.jsp?notifica=datiLoginErrati", response.getRedirectedUrl());
	}
	

	@Test
	void TC_GA_1_9_LoginAmministratore() throws ServletException, IOException {
		request.setParameter("operazione", "loginAdmin");
		request.setParameter("codiceFiscale", "FLPBRZ61A45F234F");
		request.setParameter("password","Pippo1234");
		servlet.doGet(request, response);
		System.out.println(response.getRedirectedUrl());
		assertEquals("./dashboard.jsp", response.getRedirectedUrl());
	}
	
	@Test
	void TC_GA_2_1_LogoutAmministratore() throws ServletException, IOException {
		request.setParameter("operazione", "logout");
		servlet.doGet(request, response);
		System.out.println(response.getRedirectedUrl());
		assertEquals("./dashboard.jsp", response.getRedirectedUrl());
	}
	
	@Test
	void TC_GP_1_1_LoginPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "login");
		request.setParameter("codiceFiscale", "BNCLRD67A01F205");
		request.setParameter("password","Fiori5678");
		servlet.doGet(request, response);
		System.out.println(response.getRedirectedUrl());
		assertEquals("./login.jsp?notifica=datiLoginErrati", response.getRedirectedUrl());
	}
	@Test
	void TC_GP_1_2_LoginPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "login");
		request.setParameter("codiceFiscale", "BNCLRD67A01F205IFD");
		request.setParameter("password","Fiori5678");
		servlet.doGet(request, response);
		System.out.println(response.getRedirectedUrl());
		assertEquals("./login.jsp?notifica=datiLoginErrati", response.getRedirectedUrl());
	}
	@Test
	void TC_GP_1_3_LoginPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "login");
		request.setParameter("codiceFiscale", "N0TV4L1DF0RM4TM8");
		request.setParameter("password","Fiori5678");
		servlet.doGet(request, response);
		System.out.println(response.getRedirectedUrl());
		assertEquals("./login.jsp?notifica=datiLoginErrati", response.getRedirectedUrl());
	}
	@Test
	void TC_GP_1_4_LoginPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "login");
		request.setParameter("codiceFiscale", "BNCDNC67A01F205I");
		request.setParameter("password","Fiori5678");
		servlet.doGet(request, response);
		System.out.println(response.getRedirectedUrl());
		assertEquals("./login.jsp?notifica=datiLoginErrati", response.getRedirectedUrl());
	}
	@Test
	void TC_Gp_1_5_LoginPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "login");
		request.setParameter("codiceFiscale", "BNCLRD67A01F205I");
		request.setParameter("password","Ave");
		servlet.doGet(request, response);
		System.out.println(response.getRedirectedUrl());
		assertEquals("./login.jsp?notifica=datiLoginErrati", response.getRedirectedUrl());
	}
	@Test
	void TC_GP_1_6_LoginPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "login");
		request.setParameter("codiceFiscale", "BNCLRD67A01F205I");
		request.setParameter("password","SperoCheQuestaSiaUnaBuonaPassword");
		servlet.doGet(request, response);
		System.out.println(response.getRedirectedUrl());
		assertEquals("./login.jsp?notifica=datiLoginErrati", response.getRedirectedUrl());
	}
	
	@Test
	void TC_GP_1_7_LoginPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "login");
		request.setParameter("codiceFiscale", "BNCLRD67A01F205I");
		request.setParameter("password","Password12__90");
		servlet.doGet(request, response);
		System.out.println(response.getRedirectedUrl());
		assertEquals("./login.jsp?notifica=datiLoginErrati", response.getRedirectedUrl());
	}
	
	@Test
	void TC_GP_1_8_LoginPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "login");
		request.setParameter("codiceFiscale", "BNCLRD67A01F205I");
		request.setParameter("password","Fiori5690");
		servlet.doGet(request, response);
		System.out.println(response.getRedirectedUrl());
		assertEquals("./login.jsp?notifica=datiLoginErrati", response.getRedirectedUrl());
	}
	

	@Test
	void TC_GP_1_9_LoginPaziente() throws ServletException, IOException {
		request.setParameter("operazione", "login");
		request.setParameter("codiceFiscale", "BNCLRD67A01F205I");
		request.setParameter("password","Fiori5678");
		servlet.doGet(request, response);
		System.out.println(response.getRedirectedUrl());
		assertEquals("./dashboard.jsp", response.getRedirectedUrl());
	}
	@Test
	void testLoginPazienteAccountDisattivato() throws ServletException, IOException {
		request.setParameter("operazione", "login");
		request.setParameter("codiceFiscale", "CRRSRA90A50A091Q");
		request.setParameter("password","password");
		
		servlet.doGet(request, response);
		System.out.println(response.getRedirectedUrl());
		assertEquals("./login.jsp?notifica=accountDisattivo", response.getRedirectedUrl());
	}
}
