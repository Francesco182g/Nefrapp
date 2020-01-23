package test.control;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import javax.servlet.ServletException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import control.GestioneComunicazione;

class GestioneComunicazioneTest {

  private GestioneComunicazione servlet;
  private MockHttpServletRequest request;
  private MockHttpServletResponse response;

  @BeforeEach
  void setUp() throws Exception {
    servlet = new GestioneComunicazione();
  }

  //controllaFile è l'unico metodo di GestioneComunicazione che può essere testato singolarmente
  //tutti gli altri sono usati da GestioneMessaggi e GestioneAnnunci e sono chiamati nelle 
  //rispettive classi di testing.
  @Test
  void testControllaFile() {
    assertFalse(servlet.controllaFile("nomeFile.jpg", 0L));
    assertFalse(servlet.controllaFile("nomeFile", 1000L));
    assertFalse(servlet.controllaFile("nomeFile.txt", 1000L));
    assertFalse(servlet.controllaFile("nomeFile.jpg", 1000000000000000000L));
    assertTrue(servlet.controllaFile("nomeFile.jpg", 1000L));
  }
  
  @Test
  void testNessunMetodo() throws ServletException, IOException {
    servlet.doGet(request, response);
    servlet.doPost(request, response);
    
    assertTrue(true);
  }

}
