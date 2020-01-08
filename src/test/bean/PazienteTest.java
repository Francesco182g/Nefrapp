package test.bean;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import bean.Medico;
import bean.Paziente;

class PazienteTest {

	private final LocalDate dataNascitaPaziente = LocalDate.parse("1967-07-11");
	private final String residenzaPaziente = "Via Roma, 22, Scafati, 80030, NA";
	private final ArrayList<String> medici = new ArrayList<>();

	@Test
	void testPazienteCostruttoreVuoto() {
		Paziente paziente = new Paziente();
		assertNotNull(paziente);
	}
	
	//Test per i metodi di get
	
	@Test
	void testGetCodiceFiscale() {
		Paziente paziente = new Paziente("M", "DWNRRT85E18I483W", "Robert", "Downey", "iron.man@live.it", residenzaPaziente, "Milano", dataNascitaPaziente, true, medici);
		assertEquals("DWNRRT85E18I483W", paziente.getCodiceFiscale());
	}
		
	@Test
	void testGetNome() {
		Paziente paziente = new Paziente("M", "DWNRRT85E18I483W", "Robert", "Downey", "iron.man@live.it", residenzaPaziente, "Milano", dataNascitaPaziente, true, medici);
		assertEquals("Robert", paziente.getNome());
	}
		
	@Test
	void testGetCognome() {
		Paziente paziente = new Paziente("M", "DWNRRT85E18I483W", "Robert", "Downey", "iron.man@live.it", residenzaPaziente, "Milano", dataNascitaPaziente, true, medici);
		assertEquals("Downey", paziente.getCognome());
	}
		
	@Test
	void testGetEmail() {
		Paziente paziente = new Paziente("M", "DWNRRT85E18I483W", "Robert", "Downey", "iron.man@live.it", residenzaPaziente, "Milano", dataNascitaPaziente, true, medici);
		assertEquals("iron.man@live.it", paziente.getEmail());
	}
		
	@Test
	void testGetSesso() {
		Paziente paziente = new Paziente("M", "DWNRRT85E18I483W", "Robert", "Downey", "iron.man@live.it", residenzaPaziente, "Milano", dataNascitaPaziente, true, medici);
		assertEquals("M", paziente.getSesso());
	}
		
	@Test
	void testGetResidenza() {

		Paziente paziente = new Paziente("M", "DWNRRT85E18I483W", "Robert", "Downey", "iron.man@live.it", residenzaPaziente, "Milano", dataNascitaPaziente, true, medici);
		assertEquals("Via Roma, 22, Scafati, 80030, NA", paziente.getResidenza());
	}
		
	@Test
	void testGetDataDiNascita() {
		Paziente paziente = new Paziente("M", "DWNRRT85E18I483W", "Robert", "Downey", "iron.man@live.it", residenzaPaziente, "Milano", dataNascitaPaziente, true, medici);
		assertEquals(dataNascitaPaziente, paziente.getDataDiNascita());
	}
		
	@Test
	void testGetLuogoDiNascita() {
		Paziente paziente = new Paziente("M", "DWNRRT85E18I483W", "Robert", "Downey", "iron.man@live.it", residenzaPaziente, "Milano", dataNascitaPaziente, true, medici);
		assertEquals("Milano", paziente.getLuogoDiNascita());
	}
	
	@Test
	void testGetDataFormattata() {
		Paziente paziente = new Paziente("M", "DWNRRT85E18I483W", "Robert", "Downey", "iron.man@live.it", residenzaPaziente, "Milano", dataNascitaPaziente, true, medici);
		assertEquals("11/07/1967",paziente.getDataFormattata());
	}
		
	//Test per i metodi di set
		
	@Test
	void testSetCodiceFiscale() {

		Paziente paziente = new Paziente("M", "", "Robert", "Downey", "iron.man@live.it", residenzaPaziente, "Milano", dataNascitaPaziente, true, medici);
		paziente.setCodiceFiscale("DWNRRT85E18I483W");
		assertEquals("DWNRRT85E18I483W", paziente.getCodiceFiscale());
	}

	@Test
	void testSetNome() {
		Paziente paziente = new Paziente("M", "DWNRRT85E18I483W", "", "Downey", "iron.man@live.it", residenzaPaziente, "Milano", dataNascitaPaziente, true, medici);
		paziente.setNome("Robert");
		assertEquals("Robert", paziente.getNome());
	}

	@Test
	void testSetCognome() {
		Paziente paziente = new Paziente("M", "DWNRRT85E18I483W", "Robert", "", "iron.man@live.it", residenzaPaziente, "Milano", dataNascitaPaziente, true, medici);
		paziente.setCognome("Downey");
		assertEquals("Downey", paziente.getCognome());
	}

	@Test
	void testSetEmail() {

		Paziente paziente = new Paziente("M", "DWNRRT85E18I483W", "Robert", "Downey", "", residenzaPaziente, "Milano", dataNascitaPaziente, true, medici);
		paziente.setEmail("iron.man@live.it");
		assertEquals("iron.man@live.it", paziente.getEmail());
	}

	@Test
	void testSetSesso() {

		Paziente paziente = new Paziente("M", "DWNRRT85E18I483W", "Robert", "Downey", "iron.man@live.it", residenzaPaziente, "Milano", dataNascitaPaziente, true, medici);
		paziente.setSesso("M");
		assertEquals("M", paziente.getSesso());
	}

	
	@Test
	void testSetResidenza() {
		Paziente paziente = new Paziente("M", "DWNRRT85E18I483W", "Robert", "Downey", "iron.man@live.it", "", "Milano", dataNascitaPaziente, true, medici);
		paziente.setResidenza(residenzaPaziente);
		assertEquals(residenzaPaziente, paziente.getResidenza());
	}
	
	@Test
	void testSetDataDiNascita() {
		Paziente paziente = new Paziente("M", "DWNRRT85E18I483W", "Robert", "Downey", "iron.man@live.it", residenzaPaziente, "Milano", null, true, medici);
		paziente.setDataDiNascita(dataNascitaPaziente);
		assertEquals(dataNascitaPaziente, paziente.getDataDiNascita());
	}
	
	@Test
	void testSetLuogoDiNascita() {
		Paziente paziente = new Paziente("M", "DWNRRT85E18I483W", "Robert", "Downey", "iron.man@live.it", residenzaPaziente, "", dataNascitaPaziente, true, medici);
		paziente.setLuogoDiNascita("Milano");
		assertEquals("Milano", paziente.getLuogoDiNascita());
	}
	
	@Test
	void testSetAttivo() {
		Paziente paziente = new Paziente("M", "DWNRRT85E18I483W", "Robert", "Downey", "iron.man@live.it", residenzaPaziente, "Milano", dataNascitaPaziente, true, medici);
		paziente.setAttivo(false);
		assertEquals(false, paziente.getAttivo());
	}
	@Test
	void testGetAttivo() {
		Paziente paziente = new Paziente("M", "DWNRRT85E18I483W", "Robert", "Downey", "iron.man@live.it", residenzaPaziente, "Milano", dataNascitaPaziente, true, medici);
		assertEquals(true, paziente.getAttivo());
	}
	
	@Test
	void testSetMedici() {
		Paziente paziente = new Paziente("M", "DWNRRT85E18I483W", "Robert", "Downey", "iron.man@live.it", residenzaPaziente, "Milano", dataNascitaPaziente, true, null);
		paziente.setMedici(medici);
		assertEquals(medici, paziente.getMedici());
	}
	
	@Test
	void testGetMedici() {
		Paziente paziente = new Paziente("M", "DWNRRT85E18I483W", "Robert", "Downey", "iron.man@live.it", residenzaPaziente, "Milano", dataNascitaPaziente, true, medici);
		assertEquals(medici, paziente.getMedici());
	}
	
	@Test
	void testAddMedici() {
		
		Paziente paziente = new Paziente("M", "DWNRRT85E18I483W", "Robert", "Downey", "iron.man@live.it", residenzaPaziente, "Milano", dataNascitaPaziente, true, medici);
		paziente.addMedico("GRMBNN67L11B519R");
		medici.add("GRMBNN67L11B519R");
		
		assertEquals(medici, paziente.getMedici());
	}
	
	


	//Test per il toString
	@Test
	void testToString() {
		Paziente paziente = new Paziente("M", "DWNRRT85E18I483W", "Robert", "Downey", "iron.man@live.it", residenzaPaziente, "Milano", dataNascitaPaziente, true, medici);
		String toStringBean = paziente.toString();
		String toStringTest ="Paziente [sesso=" + paziente.getSesso()+ ", email=" + paziente.getEmail() + ", residenza=" + paziente.getResidenza() + ", luogoDiNascita="
				+ paziente.getLuogoDiNascita() + ", dataDiNascita=" + paziente.getDataDiNascita() + ", attivo=" + paziente.getAttivo() + ", medici=" + paziente.getMedici()
				+ ", codiceFiscale=" + paziente.getCodiceFiscale() + ", nome=" + paziente.getNome() + ", cognome="
				+ paziente.getCognome() + "]";
		assertEquals(toStringBean, toStringTest);
	}
}
