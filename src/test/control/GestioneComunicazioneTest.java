package test.control;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import control.GestioneComunicazione;

class GestioneComunicazioneTest {
	
	private GestioneComunicazione servlet;
	
	@BeforeEach
	void setUp() throws Exception {
		servlet = new GestioneComunicazione();
	}

	//controllaFile è l'unico metodo di GestioneComunicazione che può essere testato singolarmente
	//tutti gli altri sono usati da GestioneMessaggi e GestioneAnnunci e sono chiamati nelle rispettive classi di testing.
	@Test
	void testControllaFile() {
		assertFalse(servlet.controllaFile("nomeFile.jpg", 0l));
		assertFalse(servlet.controllaFile("nomeFile", 1000l));
		assertFalse(servlet.controllaFile("nomeFile.txt", 1000L));
		assertFalse(servlet.controllaFile("nomeFile.jpg", 1000000000000000000L));
		assertTrue(servlet.controllaFile("nomeFile.jpg", 1000L));
	}

}
