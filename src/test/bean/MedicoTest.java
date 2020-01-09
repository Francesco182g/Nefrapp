package test.bean;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import bean.Medico;

class MedicoTest {
	private static final LocalDate dataNascitaMedico = LocalDate.parse("1967-07-11");
	private static final LocalDate dataNull = null;
	private static final String residenzaMedico = "Via Roma, 22, Scafati, 80030, NA";

	@Test
	void testMedicoCostruttoreVuoto() {
		Medico medico = new Medico();
		assertNotNull(medico);
	}
	
	//Test per i metodi di get
	
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