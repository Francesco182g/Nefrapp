package test.model;

import static org.junit.jupiter.api.Assertions.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import bean.Annuncio;
import bean.AnnuncioCompleto;
import model.AnnuncioModel;


public class AnnuncioModelTest {
	private static HashMap<String, Boolean> destinatari;
	private static String idAnnuncio = "";
	private static final String medico="GRMBNN67L11B516R";
	private static final String titolo="Lorem ipsum dolor sit amet, consectetur adipiscing elit volutpat.";
	private static final String testo="Il file in allegato contiene istruzioni per i pazienti su come effettuare la dialisi peritoneale.";
	private static final String corpoAllegato="codiceallegato";
	private static final String nomeAllegato="dialisi-peritoneale.pdf";
	private static final String codiceFiscalePaziente="BNCLRD67A01F205I";
	//serve che questo paziente non sia nel database per rendere attendibile il test di counNonLetti
	
	private static ZonedDateTime data=null;
		
	@BeforeEach
	void setUp() throws Exception {
		data=ZonedDateTime.now(ZoneId.of("Europe/Rome"));
		destinatari = new HashMap<String, Boolean>();
		destinatari.put(codiceFiscalePaziente, false);
		AnnuncioCompleto annuncio=new AnnuncioCompleto(medico,titolo,testo,corpoAllegato,nomeAllegato,data,destinatari);
		idAnnuncio = AnnuncioModel.addAnnuncio(annuncio);
	}

	@AfterEach
	void tearDown() throws Exception {
		AnnuncioModel.deleteAnnuncioById(idAnnuncio);
	}

	@Test
	void testGetAnnuncioById() {
		Annuncio annuncio=AnnuncioModel.getAnnuncioById(idAnnuncio);
		assertNotNull(annuncio);
		assertEquals(annuncio.getIdAnnuncio(),idAnnuncio);
		assertEquals(annuncio.getMedico(),medico);
		assertEquals(annuncio.getTitolo(),titolo);
		assertEquals(annuncio.getTesto(),testo);
		assertEquals(annuncio.getCorpoAllegato(),corpoAllegato);
		assertEquals(annuncio.getNomeAllegato(),nomeAllegato);
		assertEquals(annuncio.getData(),data);
		assertEquals(annuncio.getPazientiView(),destinatari);
	}
	
	@Test
	void testUpdateAnnuncio() {
		Annuncio daAggiornare=AnnuncioModel.getAnnuncioById(idAnnuncio);
		assertNotNull(daAggiornare);
		daAggiornare.setTitolo("Nuovo annuncio");
		daAggiornare.setTesto("Nuove medicine in commercio");
		AnnuncioModel.updateAnnuncio(daAggiornare.getIdAnnuncio(), daAggiornare.getMedico(), daAggiornare.getTitolo(), daAggiornare.getTesto(), daAggiornare.getCorpoAllegato(), daAggiornare.getNomeAllegato(), daAggiornare.getData(), daAggiornare.getPazientiView());
		daAggiornare=AnnuncioModel.getAnnuncioById(idAnnuncio);
		assertNotNull(daAggiornare);
		assertEquals(daAggiornare.getTitolo(),"Nuovo annuncio");
		assertEquals(daAggiornare.getTesto(),"Nuove medicine in commercio");
	}
	
	@Test
	void testGetAnnuncioByCFMedico() {
		ArrayList<Annuncio> annunci=AnnuncioModel.getAnnunciByCFMedico(medico);
		for(Annuncio a:annunci) {
			assertEquals(a.getMedico(),medico);
		}
	}
	
	//TODO Questi tre test
	
	@Test
	void testGetAnnuncioByCFPaziente() {
		ArrayList<Annuncio> annunci=AnnuncioModel.getAnnunciByCFMedico(codiceFiscalePaziente);
		boolean destinatarioGiusto = true;
		
		for (Annuncio a : annunci) {
			if (!a.getPazientiView().containsKey(codiceFiscalePaziente)) {
				destinatarioGiusto = false;
			}
		}
		
		assertTrue(destinatarioGiusto);
	}
	
	@Test
	void testCountAnnunciNonLetti() {
		int nonLetti = AnnuncioModel.countAnnunciNonLetti(codiceFiscalePaziente);
		
		assertEquals(nonLetti, 1);
	}
	
	@Test
	void testSetVisualizzato() {
		AnnuncioModel.setVisualizzatoAnnuncio(idAnnuncio, codiceFiscalePaziente, true);
		
		boolean visualizzato = false;
		Annuncio annuncio = AnnuncioModel.getAnnuncioById(idAnnuncio);
		System.out.println("pazientiview" + annuncio.getPazientiView());
		Iterator<Entry<String, Boolean>> it = annuncio.getPazientiView().entrySet().iterator();
		Entry<String, Boolean> temp; 
				
		while (it.hasNext()) {
			temp = it.next();
			if (temp.getKey().equals(codiceFiscalePaziente)) {
				visualizzato = temp.getValue();
				break;
			}
		}
		
		assertEquals(true, visualizzato);
	}
}
