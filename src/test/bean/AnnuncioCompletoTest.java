package test.bean;

import static org.junit.jupiter.api.Assertions.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import org.junit.jupiter.api.Test;

import bean.AnnuncioCompleto;

public class AnnuncioCompletoTest {
	private HashMap<String, Boolean> destinatari = new HashMap<String, Boolean>();
	
	//Test per il costruttore vuoto
		
			@Test
			void testAnnuncioCompletoCostruttoreVuoto() {
				AnnuncioCompleto annuncio=new AnnuncioCompleto();
				assertNotNull(annuncio);
			}
						 
	// Test per i metodi di get
			
			@Test
			void testGetMedico() {
				AnnuncioCompleto annuncio=new AnnuncioCompleto("DCPLRD71M12C129X","Lorem ipsum dolor sit amet, consectetur adipiscing elit volutpat.",
						"Il file in allegato contiene istruzioni per i pazienti su come effettuare la dialisi peritoneale.","codiceallegato",
						"dialisi-peritoneale.pdf",ZonedDateTime.now(ZoneId.of("Europe/Rome")),destinatari);
				assertEquals("DCPLRD71M12C129X",annuncio.getMedico());
			}
			
			@Test
			void testGetTitolo() {
				AnnuncioCompleto annuncio=new AnnuncioCompleto("DCPLRD71M12C129X","Lorem ipsum dolor sit amet, consectetur adipiscing elit volutpat.",
						"Il file in allegato contiene istruzioni per i pazienti su come effettuare la dialisi peritoneale.","codiceallegato",
						"dialisi-peritoneale.pdf",ZonedDateTime.now(ZoneId.of("Europe/Rome")),destinatari);
				assertEquals("Lorem ipsum dolor sit amet, consectetur adipiscing elit volutpat.",annuncio.getTitolo());
			}
			
			@Test
			void testGetTesto() {
				AnnuncioCompleto annuncio=new AnnuncioCompleto("DCPLRD71M12C129X","Lorem ipsum dolor sit amet, consectetur adipiscing elit volutpat.",
						"Il file in allegato contiene istruzioni per i pazienti su come effettuare la dialisi peritoneale.","codiceallegato",
						"dialisi-peritoneale.pdf",ZonedDateTime.now(ZoneId.of("Europe/Rome")),destinatari);
				assertEquals("Il file in allegato contiene istruzioni per i pazienti su come effettuare la dialisi peritoneale.",annuncio.getTesto());
			}
			
			@Test
			void testGetCorpoAllegato() {
				AnnuncioCompleto annuncio=new AnnuncioCompleto("DCPLRD71M12C129X","Lorem ipsum dolor sit amet, consectetur adipiscing elit volutpat.",
						"Il file in allegato contiene istruzioni per i pazienti su come effettuare la dialisi peritoneale.","codiceallegato",
						"dialisi-peritoneale.pdf",ZonedDateTime.now(ZoneId.of("Europe/Rome")),destinatari);
				assertEquals("codiceallegato",annuncio.getCorpoAllegato());
			}
			
			@Test
			void testGetNomeAllegato() {
				AnnuncioCompleto annuncio=new AnnuncioCompleto("DCPLRD71M12C129X","Lorem ipsum dolor sit amet, consectetur adipiscing elit volutpat.",
						"Il file in allegato contiene istruzioni per i pazienti su come effettuare la dialisi peritoneale.","codiceallegato",
						"dialisi-peritoneale.pdf",ZonedDateTime.now(ZoneId.of("Europe/Rome")),destinatari);
				assertEquals("dialisi-peritoneale.pdf",annuncio.getNomeAllegato());
			}
			
			@Test
			void testGetData() {
				AnnuncioCompleto annuncio=new AnnuncioCompleto("DCPLRD71M12C129X","Lorem ipsum dolor sit amet, consectetur adipiscing elit volutpat.",
						"Il file in allegato contiene istruzioni per i pazienti su come effettuare la dialisi peritoneale.","codiceallegato",
						"dialisi-peritoneale.pdf",ZonedDateTime.now(ZoneId.of("Europe/Rome")),destinatari);
				assertEquals(ZonedDateTime.now(ZoneId.of("Europe/Rome")),annuncio.getData());
			}
			
			@Test
			void testGetDataFormattata() {
				DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
				AnnuncioCompleto annuncio=new AnnuncioCompleto("DCPLRD71M12C129X","Lorem ipsum dolor sit amet, consectetur adipiscing elit volutpat.",
						"Il file in allegato contiene istruzioni per i pazienti su come effettuare la dialisi peritoneale.","codiceallegato",
						"dialisi-peritoneale.pdf",ZonedDateTime.now(ZoneId.of("Europe/Rome")),destinatari);
				assertEquals(ZonedDateTime.now(ZoneId.of("Europe/Rome")).format(format),annuncio.getDataFormattata());
			}
			
			@Test
			void testGetOraFormattata() {
				DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm");
				AnnuncioCompleto annuncio=new AnnuncioCompleto("DCPLRD71M12C129X","Lorem ipsum dolor sit amet, consectetur adipiscing elit volutpat.",
						"Il file in allegato contiene istruzioni per i pazienti su come effettuare la dialisi peritoneale.","codiceallegato",
						"dialisi-peritoneale.pdf",ZonedDateTime.now(ZoneId.of("Europe/Rome")),destinatari);
				assertEquals(ZonedDateTime.now(ZoneId.of("Europe/Rome")).format(format),annuncio.getOraFormattata());
			}
			
			@Test
			void testGetVisualizzato() {
				AnnuncioCompleto annuncio=new AnnuncioCompleto("DCPLRD71M12C129X","Lorem ipsum dolor sit amet, consectetur adipiscing elit volutpat.",
						"Il file in allegato contiene istruzioni per i pazienti su come effettuare la dialisi peritoneale.","codiceallegato",
						"dialisi-peritoneale.pdf",ZonedDateTime.now(ZoneId.of("Europe/Rome")),destinatari);
				assertEquals(false,annuncio.getVisualizzato());
			}
			
			@Test
			void testGetIdAnnuncio() {
				AnnuncioCompleto annuncio=new AnnuncioCompleto("DCPLRD71M12C129X","Lorem ipsum dolor sit amet, consectetur adipiscing elit volutpat.",
						"Il file in allegato contiene istruzioni per i pazienti su come effettuare la dialisi peritoneale.","codiceallegato",
						"dialisi-peritoneale.pdf",ZonedDateTime.now(ZoneId.of("Europe/Rome")),destinatari);
				annuncio.setIdAnnuncio("1234");
				assertEquals("1234",annuncio.getIdAnnuncio());
			}
			
			@Test
			void testGetPazientiView() {
				AnnuncioCompleto annuncio=new AnnuncioCompleto("DCPLRD71M12C129X","Lorem ipsum dolor sit amet, consectetur adipiscing elit volutpat.",
						"Il file in allegato contiene istruzioni per i pazienti su come effettuare la dialisi peritoneale.","codiceallegato",
						"dialisi-peritoneale.pdf",ZonedDateTime.now(ZoneId.of("Europe/Rome")),destinatari);
				assertEquals(destinatari,annuncio.getPazientiView());
			}
			
	//Test per i metodi di set
			
			@Test
			void testSetMedico() {
				AnnuncioCompleto annuncio=new AnnuncioCompleto("","Lorem ipsum dolor sit amet, consectetur adipiscing elit volutpat.",
						"Il file in allegato contiene istruzioni per i pazienti su come effettuare la dialisi peritoneale.","codiceallegato",
						"dialisi-peritoneale.pdf",ZonedDateTime.now(ZoneId.of("Europe/Rome")),destinatari);
				annuncio.setMedico("DCPLRD71M12C129X");
				assertEquals("DCPLRD71M12C129X",annuncio.getMedico());
			}
			
			@Test
			void testSetTitolo() {
				AnnuncioCompleto annuncio=new AnnuncioCompleto("DCPLRD71M12C129X","",
						"Il file in allegato contiene istruzioni per i pazienti su come effettuare la dialisi peritoneale.","codiceallegato",
						"dialisi-peritoneale.pdf",ZonedDateTime.now(ZoneId.of("Europe/Rome")),destinatari);
				annuncio.setTitolo("Lorem ipsum dolor sit amet, consectetur adipiscing elit volutpat.");
				assertEquals("Lorem ipsum dolor sit amet, consectetur adipiscing elit volutpat.",annuncio.getTitolo());
			}
			
			@Test
			void testSetTesto() {
				AnnuncioCompleto annuncio=new AnnuncioCompleto("DCPLRD71M12C129X","Lorem ipsum dolor sit amet, consectetur adipiscing elit volutpat.",
						"","codiceallegato",
						"dialisi-peritoneale.pdf",ZonedDateTime.now(ZoneId.of("Europe/Rome")),destinatari);
				annuncio.setTesto("Il file in allegato contiene istruzioni per i pazienti su come effettuare la dialisi peritoneale.");
				assertEquals("Il file in allegato contiene istruzioni per i pazienti su come effettuare la dialisi peritoneale.",annuncio.getTesto());
			}
			
			@Test
			void testSetCorpoAllegato() {
				AnnuncioCompleto annuncio=new AnnuncioCompleto("DCPLRD71M12C129X","Lorem ipsum dolor sit amet, consectetur adipiscing elit volutpat.",
						"Il file in allegato contiene istruzioni per i pazienti su come effettuare la dialisi peritoneale.","",
						"dialisi-peritoneale.pdf",ZonedDateTime.now(ZoneId.of("Europe/Rome")),destinatari);
				annuncio.setCorpoAllegato("codiceallegato");
				assertEquals("codiceallegato",annuncio.getCorpoAllegato());
			}
			
			@Test
			void testSetNomeAllegato() {
				AnnuncioCompleto annuncio=new AnnuncioCompleto("DCPLRD71M12C129X","Lorem ipsum dolor sit amet, consectetur adipiscing elit volutpat.",
						"Il file in allegato contiene istruzioni per i pazienti su come effettuare la dialisi peritoneale.","codiceallegato",
						"",ZonedDateTime.now(ZoneId.of("Europe/Rome")),destinatari);
				annuncio.setNomeAllegato("dialisi-peritoneale.pdf");
				assertEquals("dialisi-peritoneale.pdf",annuncio.getNomeAllegato());
			}
			
			@Test
			void testSetData() {
				AnnuncioCompleto annuncio=new AnnuncioCompleto("DCPLRD71M12C129X","Lorem ipsum dolor sit amet, consectetur adipiscing elit volutpat.",
						"Il file in allegato contiene istruzioni per i pazienti su come effettuare la dialisi peritoneale.","codiceallegato",
						"dialisi-peritoneale.pdf",null,destinatari);
				annuncio.setData(ZonedDateTime.now(ZoneId.of("Europe/Rome")));
				assertEquals(ZonedDateTime.now(ZoneId.of("Europe/Rome")),annuncio.getData());
			}
						
			@Test
			void testSetVisualizzato() {
				AnnuncioCompleto annuncio=new AnnuncioCompleto("DCPLRD71M12C129X","Lorem ipsum dolor sit amet, consectetur adipiscing elit volutpat.",
						"Il file in allegato contiene istruzioni per i pazienti su come effettuare la dialisi peritoneale.","codiceallegato",
						"dialisi-peritoneale.pdf",ZonedDateTime.now(ZoneId.of("Europe/Rome")),destinatari);
				annuncio.setVisualizzato(true);
				assertEquals(true,annuncio.getVisualizzato());
			}
			
			@Test
			void testSetIdAnnuncio() {
				AnnuncioCompleto annuncio=new AnnuncioCompleto("DCPLRD71M12C129X","Lorem ipsum dolor sit amet, consectetur adipiscing elit volutpat.",
						"Il file in allegato contiene istruzioni per i pazienti su come effettuare la dialisi peritoneale.","codiceallegato",
						"dialisi-peritoneale.pdf",ZonedDateTime.now(ZoneId.of("Europe/Rome")),destinatari);
				annuncio.setIdAnnuncio("1234");
				assertEquals("1234",annuncio.getIdAnnuncio());
			}
			
			@Test
			void testSetPazientiView() {
				AnnuncioCompleto annuncio=new AnnuncioCompleto("DCPLRD71M12C129X","Lorem ipsum dolor sit amet, consectetur adipiscing elit volutpat.",
						"Il file in allegato contiene istruzioni per i pazienti su come effettuare la dialisi peritoneale.","codiceallegato",
						"dialisi-peritoneale.pdf",ZonedDateTime.now(ZoneId.of("Europe/Rome")),destinatari);
				destinatari.putIfAbsent("CRRSRA30Q69Z420J", false);
				annuncio.setPazientiView(destinatari);
				assertEquals(destinatari,annuncio.getPazientiView());
			}
			
	// Test per il toString
			
			@Test
			void testToString() {
				AnnuncioCompleto annuncio=new AnnuncioCompleto("DCPLRD71M12C129X","Lorem ipsum dolor sit amet, consectetur adipiscing elit volutpat.",
						"Il file in allegato contiene istruzioni per i pazienti su come effettuare la dialisi peritoneale.","codiceallegato",
						"dialisi-peritoneale.pdf",ZonedDateTime.now(ZoneId.of("Europe/Rome")),destinatari);
				String toStringBean= annuncio.toString();
				String toStringTest= "Annuncio [idAnnuncio=" + annuncio.getIdAnnuncio() + ", medico=" + annuncio.getMedico() 
						+ ", pazienti=" + annuncio.getPazientiView() + ", titolo=" + annuncio.getTitolo() + ", testo=" 
						+ annuncio.getTesto() + ", allegato=" + annuncio.getNomeAllegato() + ", data=" + annuncio.getData() 
						+ ", visualizzato=" + annuncio.getVisualizzato() + "]";
				assertEquals(toStringBean, toStringTest);
			}
}
