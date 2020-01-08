package test.bean;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import bean.Amministratore;

class AmministratoreTest {

	//Test per il costruttore vuoto
		@Test
		void testAmministratoreCostruttoreVuoto() {
			Amministratore amministratoreEmpity = new Amministratore();
			assertNotNull(amministratoreEmpity);
		}

		//Test per i metodi di get
		
		@Test
		void testGetCodiceFiscale() {
			Amministratore amministratore = new Amministratore("SNSNCL92D19I483U", "Nicola", "Sansone", "nico.sansone@live.it");
			assertEquals("SNSNCL92D19I483U", amministratore.getCodiceFiscale());
		}
		
		@Test
		void testGetNome() {
			Amministratore amministratore = new Amministratore("SNSNCL92D19I483U", "Nicola", "Sansone", "nico.sansone@live.it");
			assertEquals("Nicola", amministratore.getNome());
		}
		
		@Test
		void testGetCognome() {
			Amministratore amministratore = new Amministratore("SNSNCL92D19I483U", "Nicola", "Sansone", "nico.sansone@live.it");
			assertEquals("Sansone", amministratore.getCognome());
		}
		
		@Test
		void testGetEmail() {
			Amministratore amministratore = new Amministratore("SNSNCL92D19I483U", "Nicola", "Sansone", "nico.sansone@live.it");
			assertEquals("nico.sansone@live.it", amministratore.getEmail());
		}

		//Test per i metodi di set
		
		@Test
		void testSetCodiceFiscale() {
			Amministratore amministratore = new Amministratore("", "Nicola", "Sansone", "nico.sansone@live.it");
			amministratore.setCodiceFiscale("SNSNCL92D19I483U");
			assertEquals("SNSNCL92D19I483U", amministratore.getCodiceFiscale());
		}

		@Test
		void testSetNome() {
			Amministratore amministratore = new Amministratore("SNSNCL92D19I483U", "", "Sansone", "nico.sansone@live.it");
			amministratore.setNome("Nicola");
			assertEquals("Nicola", amministratore.getNome());
		}

		@Test
		void testSetCognome() {
			Amministratore amministratore = new Amministratore("SNSNCL92D19I483U", "Nicola", "", "nico.sansone@live.it");
			amministratore.setCognome("Sansone");
			assertEquals("Sansone", amministratore.getCognome());
		}

		@Test
		void testSetEmail() {
			Amministratore amministratore = new Amministratore("SNSNCL92D19I483U", "Nicola", "Sansone", "");
			amministratore.setEmail("nico.sansone@live.it");
			assertEquals("nico.sansone@live.it", amministratore.getEmail());
		}
		
		//Test per il toString
		@Test
		void testToString() {
			Amministratore amministratore = new Amministratore("SNSNCL92D19I483U", "Nicola", "Sansone", "");
			String toStringBean = amministratore.toString();
			String toStringTest = "bean.Amministratore [codiceFiscale=" + amministratore.getCodiceFiscale() + 
									", nome=" + amministratore.getNome() + 
									", cognome=" + amministratore.getCognome() + 
									", email=" + amministratore.getEmail() + "]";
			assertEquals(toStringBean, toStringTest);
					
		}
}
