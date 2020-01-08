package test.bean;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import bean.Medico;

class MedicoTest {
	private final LocalDate dataNascitaMedico = LocalDate.parse("1967-07-11");
	private final LocalDate dataNull = null;
	private final String residenzaMedico = "Via Roma, 22, Scafati, 80030, NA";

	@Test
	void testMedicoCostruttoreVuoto() {
		Medico medico = new Medico();
		assertNotNull(medico);
	}
	
	//Test per i metodi di get
	
	@Test
	void testGetCodiceFiscale() {
		Medico medico = new Medico("M", residenzaMedico, dataNascitaMedico, 
									"GRMBNN67L11B519R", "Geremia", "Bernini", "G.Bernini67@gmail.com", "Campobasso");
		assertEquals("GRMBNN67L11B519R", medico.getCodiceFiscale());
	}
		
	@Test
	void testGetNome() {
		Medico medico = new Medico("M", residenzaMedico, dataNascitaMedico, 
				"GRMBNN67L11B519R", "Geremia", "Bernini", "G.Bernini67@gmail.com", "Campobasso");
		assertEquals("Geremia", medico.getNome());
	}
		
	@Test
	void testGetCognome() {
		Medico medico = new Medico("M", residenzaMedico, dataNascitaMedico, 
				"GRMBNN67L11B519R", "Geremia", "Bernini", "G.Bernini67@gmail.com", "Campobasso");
		assertEquals("Bernini", medico.getCognome());
	}
		
	@Test
	void testGetEmail() {
		Medico medico = new Medico("M", residenzaMedico, dataNascitaMedico, 
				"GRMBNN67L11B519R", "Geremia", "Bernini", "G.Bernini67@gmail.com", "Campobasso");
		assertEquals("G.Bernini67@gmail.com", medico.getEmail());
	}
		
	@Test
	void testGetSesso() {
		Medico medico = new Medico("M", residenzaMedico, dataNascitaMedico, 
				"GRMBNN67L11B519R", "Geremia", "Bernini", "G.Bernini67@gmail.com", "Campobasso");
		assertEquals("M", medico.getSesso());
	}
		
	@Test
	void testGetResidenza() {
		Medico medico = new Medico("M", residenzaMedico, dataNascitaMedico, 
				"GRMBNN67L11B519R", "Geremia", "Bernini", "G.Bernini67@gmail.com", "Campobasso");
		assertEquals("Via Roma, 22, Scafati, 80030, NA", medico.getResidenza());
	}
		
	@Test
	void testGetDataDiNascita() {
		Medico medico = new Medico("M", residenzaMedico, dataNascitaMedico, 
				"GRMBNN67L11B519R", "Geremia", "Bernini", "G.Bernini67@gmail.com", "Campobasso");
		assertEquals(dataNascitaMedico, medico.getDataDiNascita());
	}
		
	@Test
	void testGetLuogoDiNascita() {
		Medico medico = new Medico("M", residenzaMedico, dataNascitaMedico, 
				"GRMBNN67L11B519R", "Geremia", "Bernini", "G.Bernini67@gmail.com", "Campobasso");
		assertEquals("Campobasso", medico.getLuogoDiNascita());
	}
	
	@Test
	void testGetDataFormattata() {
		Medico medico = new Medico("M", residenzaMedico, dataNascitaMedico, 
				"GRMBNN67L11B519R", "Geremia", "Bernini", "G.Bernini67@gmail.com", "Campobasso");
		assertEquals("11/07/1967",medico.getDataFormattata());
	}
		
	//Test per i metodi di set
		
	@Test
	void testSetCodiceFiscale() {
		Medico medico = new Medico("M", residenzaMedico, dataNascitaMedico, 
				"", "Geremia", "Bernini", "G.Bernini67@gmail.com", "Campobasso");
		medico.setCodiceFiscale("GRMBNN67L11B519R");
		assertEquals("GRMBNN67L11B519R", medico.getCodiceFiscale());
	}

	@Test
	void testSetNome() {
		Medico medico = new Medico("M", residenzaMedico, dataNascitaMedico, 
				"GRMBNN67L11B519R", "", "Bernini", "G.Bernini67@gmail.com", "Campobasso");
		medico.setNome("Geremia");
		assertEquals("Geremia", medico.getNome());
	}

	@Test
	void testSetCognome() {
		Medico medico = new Medico("M", residenzaMedico, dataNascitaMedico, 
				"GRMBNN67L11B519R", "Geremia", "", "G.Bernini67@gmail.com", "Campobasso");
		medico.setCognome("Bernini");
		assertEquals("Bernini", medico.getCognome());
	}

	@Test
	void testSetEmail() {
		Medico medico = new Medico("M", residenzaMedico, dataNascitaMedico, 
				"GRMBNN67L11B519R", "Geremia", "Bernini", "", "Campobasso");
		medico.setEmail("G.Bernini67@gmail.com");
		assertEquals("G.Bernini67@gmail.com", medico.getEmail());
	}

	@Test
	void testSetSesso() {
		Medico medico = new Medico("", residenzaMedico, dataNascitaMedico, 
				"GRMBNN67L11B519R", "Geremia", "Bernini", "G.Bernini67@gmail.com", "Campobasso");
		medico.setSesso("M");
		assertEquals("M", medico.getSesso());
	}

	
	@Test
	void testSetResidenza() {
		Medico medico = new Medico("M", "", dataNascitaMedico, 
				"GRMBNN67L11B519R", "Geremia", "Bernini", "G.Bernini67@gmail.com", "Campobasso");
		medico.setResidenza(residenzaMedico);
		assertEquals(residenzaMedico, medico.getResidenza());
	}
	
	@Test
	void testSetDataDiNascita() {
		Medico medico = new Medico("M", residenzaMedico, dataNull, 
				"GRMBNN67L11B519R", "Geremia", "Bernini", "G.Bernini67@gmail.com", "Campobasso");
		medico.setDataDiNascita(dataNascitaMedico);
		assertEquals(dataNascitaMedico, medico.getDataDiNascita());
	}
	
	@Test
	void testSetLuogoDiNascita() {
		Medico medico = new Medico("M", residenzaMedico, dataNascitaMedico, 
				"GRMBNN67L11B519R", "Geremia", "Bernini", "G.Bernini67@gmail.com", "");
		medico.setLuogoDiNascita("Campobasso");
		assertEquals("Campobasso", medico.getLuogoDiNascita());
	}

	//Test per il toString
	@Test
	void testToString() {
		Medico medico = new Medico("M", residenzaMedico, dataNascitaMedico, 
				"GRMBNN67L11B519R", "Geremia", "Bernini", "G.Bernini67@gmail.com", "Campobasso");
		String toStringBean = medico.toString();
		String toStringTest = "Medico [sesso=" + medico.getSesso() + 
								", residenza=" + medico.getResidenza() + 
								", dataDiNascita=" + medico.getDataDiNascita() +
								", email=" + medico.getEmail() +
								", luogoDiNascita=" + medico.getLuogoDiNascita() +
								", codiceFiscale=" + medico.getCodiceFiscale() + 
								", nome=" + medico.getNome() + 
								", cognome=" + medico.getCognome() + "]";
		assertEquals(toStringBean, toStringTest);
	}
}