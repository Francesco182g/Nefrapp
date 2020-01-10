package test.bean;

import static org.junit.jupiter.api.Assertions.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import org.junit.jupiter.api.Test;

import bean.MessaggioCompleto;

class MessaggioCompletoTest {
	private HashMap<String, Boolean> destinatariView = new HashMap<String, Boolean>();
	
	
	// Test per il costruttore vuoto
	@Test
	void testMessaggioCompletoCostruttoreVuoto() {
		MessaggioCompleto messaggioCompletoEmpty = new MessaggioCompleto();
		assertNotNull(messaggioCompletoEmpty);
	}

	// Test per i metodi di get

	@Test
	void testGetCodiceFiscaleMittente() {
		MessaggioCompleto MessaggioCompleto = new MessaggioCompleto("ABCD1111DFS12", "visita",
				"Gentile dottor Rossi,le allego una copia del mio ultimo racconto breve, intitolato “il Paziente Irriverente”",
				"codiceallegato", "ilpazienteirriverente.pdf", ZonedDateTime.now(ZoneId.of("Europe/Rome")),
				destinatariView);
		assertEquals("ABCD1111DFS12", MessaggioCompleto.getCodiceFiscaleMittente());
	}

	@Test
	void testGetOggetto() {
		MessaggioCompleto MessaggioCompleto = new MessaggioCompleto("ABCD1111DFS12", "visita",
				"Gentile dottor Rossi,le allego una copia del mio ultimo racconto breve, intitolato “il Paziente Irriverente”",
				"codiceallegato", "ilpazienteirriverente.pdf", ZonedDateTime.now(ZoneId.of("Europe/Rome")),
				destinatariView);
		assertEquals("visita", MessaggioCompleto.getOggetto());
	}

	@Test
	void testGetTesto() {
		MessaggioCompleto MessaggioCompleto = new MessaggioCompleto("ABCD1111DFS12", "visita",
				"Gentile dottor Rossi,le allego una copia del mio ultimo racconto breve, intitolato “il Paziente Irriverente”",
				"codiceallegato", "ilpazienteirriverente.pdf", ZonedDateTime.now(ZoneId.of("Europe/Rome")),
				destinatariView);
		assertEquals(
				"Gentile dottor Rossi,le allego una copia del mio ultimo racconto breve, intitolato “il Paziente Irriverente”",
				MessaggioCompleto.getTesto());
	}

	@Test
	void testGetCorpoAllegato() {
		MessaggioCompleto MessaggioCompleto = new MessaggioCompleto("ABCD1111DFS12", "visita",
				"Gentile dottor Rossi,le allego una copia del mio ultimo racconto breve, intitolato “il Paziente Irriverente”",
				"codiceallegato", "ilpazienteirriverente.pdf", ZonedDateTime.now(ZoneId.of("Europe/Rome")),
				destinatariView);
		assertEquals("codiceallegato", MessaggioCompleto.getCorpoAllegato());
	}

	@Test
	void testGetNomeAllegato() {
		MessaggioCompleto MessaggioCompleto = new MessaggioCompleto("ABCD1111DFS12", "visita",
				"Gentile dottor Rossi,le allego una copia del mio ultimo racconto breve, intitolato “il Paziente Irriverente”",
				"codiceallegato", "ilpazienteirriverente.pdf", ZonedDateTime.now(ZoneId.of("Europe/Rome")),
				destinatariView);
		assertEquals("ilpazienteirriverente.pdf", MessaggioCompleto.getNomeAllegato());
	}

	@Test
	void testGetOraFormattata() {
		HashMap<String, Boolean> destinatariView = new HashMap<String, Boolean>();
		MessaggioCompleto MessaggioCompleto = new MessaggioCompleto("ABCD1111DFS12", "visita",
				"Gentile dottor Rossi,le allego una copia del mio ultimo racconto breve, intitolato “il Paziente Irriverente”",
				"codiceallegato", "ilpazienteirriverente.pdf", ZonedDateTime.now(ZoneId.of("Europe/Rome")),
				destinatariView);
		DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm");
		assertEquals(ZonedDateTime.now(ZoneId.of("Europe/Rome")).format(format), MessaggioCompleto.getOraFormattata());
	}

	@Test
	void testGetData() {
		MessaggioCompleto MessaggioCompleto = new MessaggioCompleto("ABCD1111DFS12", "visita",
				"Gentile dottor Rossi,le allego una copia del mio ultimo racconto breve, intitolato “il Paziente Irriverente”",
				"codiceallegato", "ilpazienteirriverente.pdf", ZonedDateTime.now(ZoneId.of("Europe/Rome")),
				destinatariView);
		assertEquals(ZonedDateTime.now(ZoneId.of("Europe/Rome")), MessaggioCompleto.getData());
	}

	@Test
	void testGetDataFormattata() {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		MessaggioCompleto MessaggioCompleto = new MessaggioCompleto("ABCD1111DFS12", "visita",
				"Gentile dottor Rossi,le allego una copia del mio ultimo racconto breve, intitolato “il Paziente Irriverente”",
				"codiceallegato", "ilpazienteirriverente.pdf", ZonedDateTime.now(ZoneId.of("Europe/Rome")),
				destinatariView);
		assertEquals(ZonedDateTime.now(ZoneId.of("Europe/Rome")).format(format), MessaggioCompleto.getDataFormattata());
	}

	@Test
	void testGetVisualizzato() {
		MessaggioCompleto MessaggioCompleto = new MessaggioCompleto("ABCD1111DFS12", "visita",
				"Gentile dottor Rossi,le allego una copia del mio ultimo racconto breve, intitolato “il Paziente Irriverente”",
				"codiceallegato", "ilpazienteirriverente.pdf", ZonedDateTime.now(ZoneId.of("Europe/Rome")),
				destinatariView);
		assertEquals(false, MessaggioCompleto.getVisualizzato());
	}

	@Test
	void testGetIDMessaggio() {
		MessaggioCompleto MessaggioCompleto = new MessaggioCompleto("ABCD1111DFS12", "visita",
				"Gentile dottor Rossi,le allego una copia del mio ultimo racconto breve, intitolato “il Paziente Irriverente”",
				"codiceallegato", "ilpazienteirriverente.pdf", ZonedDateTime.now(ZoneId.of("Europe/Rome")),
				destinatariView);
		MessaggioCompleto.setIdMessaggio("0123");
		assertEquals("0123", MessaggioCompleto.getIdMessaggio());
	}

	@Test
	void testGetDestinatariView() {
		MessaggioCompleto MessaggioCompleto = new MessaggioCompleto("ABCD1111DFS12", "visita",
				"Gentile dottor Rossi,le allego una copia del mio ultimo racconto breve, intitolato “il Paziente Irriverente”",
				"codiceallegato", "ilpazienteirriverente.pdf", ZonedDateTime.now(ZoneId.of("Europe/Rome")),
				destinatariView);
		assertEquals(destinatariView, MessaggioCompleto.getDestinatariView());

	}
	// Test per i metodi di set

	@Test
	void testSetCodiceFiscaleMittente() {
		MessaggioCompleto MessaggioCompleto = new MessaggioCompleto("", "visita",
				"Gentile dottor Rossi,le allego una copia del mio ultimo racconto breve, intitolato “il Paziente Irriverente”",
				"codiceallegato", "ilpazienteirriverente.pdf", ZonedDateTime.now(ZoneId.of("Europe/Rome")),
				destinatariView);
		MessaggioCompleto.setCodiceFiscaleMittente("ABCD1111DFS12");
		assertEquals("ABCD1111DFS12", MessaggioCompleto.getCodiceFiscaleMittente());
	}

	@Test
	void testSetOggetto() {
		MessaggioCompleto MessaggioCompleto = new MessaggioCompleto("ABCD1111DFS12", "",
				"Gentile dottor Rossi,le allego una copia del mio ultimo racconto breve, intitolato “il Paziente Irriverente”",
				"codiceallegato", "ilpazienteirriverente.pdf", ZonedDateTime.now(ZoneId.of("Europe/Rome")),
				destinatariView);
		MessaggioCompleto.setOggetto("visita");
		assertEquals("visita", MessaggioCompleto.getOggetto());
	}

	@Test
	void testSetTesto() {
		MessaggioCompleto MessaggioCompleto = new MessaggioCompleto("ABCD1111DFS12", "visita", "", "codiceallegato",
				"ilpazienteirriverente.pdf", ZonedDateTime.now(ZoneId.of("Europe/Rome")), destinatariView);
		MessaggioCompleto.setTesto(
				"Gentile dottor Rossi,le allego una copia del mio ultimo racconto breve, intitolato “il Paziente Irriverente”");
		assertEquals(
				"Gentile dottor Rossi,le allego una copia del mio ultimo racconto breve, intitolato “il Paziente Irriverente”",
				MessaggioCompleto.getTesto());
	}

	@Test
	void testSetCorpoAllegato() {
		MessaggioCompleto MessaggioCompleto = new MessaggioCompleto("ABCD1111DFS12", "visita",
				"Gentile dottor Rossi,le allego una copia del mio ultimo racconto breve, intitolato “il Paziente Irriverente”",
				"", "ilpazienteirriverente.pdf", ZonedDateTime.now(ZoneId.of("Europe/Rome")), destinatariView);
		MessaggioCompleto.setCorpoAllegato("codiceallegato");
		assertEquals("codiceallegato", MessaggioCompleto.getCorpoAllegato());
	}

	@Test
	void testSetNomeAllegato() {
		MessaggioCompleto MessaggioCompleto = new MessaggioCompleto("ABCD1111DFS12", "visita",
				"Gentile dottor Rossi,le allego una copia del mio ultimo racconto breve, intitolato “il Paziente Irriverente”",
				"codiceallegato", "", ZonedDateTime.now(ZoneId.of("Europe/Rome")), destinatariView);
		MessaggioCompleto.setNomeAllegato("ilpazienteirriverente.pdf");
		assertEquals("ilpazienteirriverente.pdf", MessaggioCompleto.getNomeAllegato());
	}

	@Test
	void testSetData() {
		MessaggioCompleto MessaggioCompleto = new MessaggioCompleto("ABCD1111DFS12", "visita",
				"Gentile dottor Rossi,le allego una copia del mio ultimo racconto breve, intitolato “il Paziente Irriverente”",
				"codiceallegato", "ilpazienteirriverente.pdf", null, destinatariView);
		MessaggioCompleto.setData(ZonedDateTime.now(ZoneId.of("Europe/Rome")));
		assertEquals(ZonedDateTime.now(ZoneId.of("Europe/Rome")), MessaggioCompleto.getData());
	}

	@Test
	void testSetVisualizzato() {
		MessaggioCompleto MessaggioCompleto = new MessaggioCompleto("ABCD1111DFS12", "visita",
				"Gentile dottor Rossi,le allego una copia del mio ultimo racconto breve, intitolato “il Paziente Irriverente”",
				"codiceallegato", "ilpazienteirriverente.pdf", ZonedDateTime.now(ZoneId.of("Europe/Rome")),
				destinatariView);
		MessaggioCompleto.setVisualizzato(true);
		assertEquals(true, MessaggioCompleto.getVisualizzato());
	}

	@Test
	void testSetIDMessaggio() {
		MessaggioCompleto MessaggioCompleto = new MessaggioCompleto("ABCD1111DFS12", "visita",
				"Gentile dottor Rossi,le allego una copia del mio ultimo racconto breve, intitolato “il Paziente Irriverente”",
				"codiceallegato", "ilpazienteirriverente.pdf", ZonedDateTime.now(ZoneId.of("Europe/Rome")),
				destinatariView);
		MessaggioCompleto.setIdMessaggio("0123");
		assertEquals("0123", MessaggioCompleto.getIdMessaggio());
	}

	/*
	@Test
	void testSetDestinatariView() {
		destinatariView.putIfAbsent("CRRSRA90A50A091Q", false);
		MessaggioCompleto MessaggioCompleto = new MessaggioCompleto("ABCD1111DFS12", "visita",
				"Gentile dottor Rossi,le allego una copia del mio ultimo racconto breve, intitolato “il Paziente Irriverente”",
				"codiceallegato", "ilpazienteirriverente.pdf", ZonedDateTime.now(ZoneId.of("Europe/Rome")),null );
		MessaggioCompleto.setDestinatariView(destinatariView);
		assertEquals(destinatariView, MessaggioCompleto.getDestinatariView());

	}*/

	// Test per il toString
	@Test
	void testToString() {
		MessaggioCompleto MessaggioCompleto = new MessaggioCompleto("ABCD1111DFS12", "visita",
				"Gentile dottor Rossi,le allego una copia del mio ultimo racconto breve, intitolato “il Paziente Irriverente”",
				"codiceallegato", "ilpazienteirriverente.pdf", ZonedDateTime.now(ZoneId.of("Europe/Rome")),
				destinatariView);
		String toStringBean = MessaggioCompleto.toString();
		String toStringTest = "bean.MessaggioCompleto [idMessaggio=" + MessaggioCompleto.getIdMessaggio()
				+ ", codiceFiscaleMittente=" + MessaggioCompleto.getCodiceFiscaleMittente() + ", oggetto="
				+ MessaggioCompleto.getOggetto() + ", testo=" + MessaggioCompleto.getTesto() + ", corpo allegato="
				+ MessaggioCompleto.getCorpoAllegato() + ", nome allegato=" + MessaggioCompleto.getNomeAllegato()
				+ ", data=" + MessaggioCompleto.getData() + ", data formattata=" + MessaggioCompleto.getDataFormattata()
				+ ", ora formattata=" + MessaggioCompleto.getOraFormattata() + ", visualizzato="
				+ MessaggioCompleto.getVisualizzato() + ", destinatari=" + MessaggioCompleto.getDestinatariView().toString() + "]";
		assertEquals(toStringBean, toStringTest);

	}
}
