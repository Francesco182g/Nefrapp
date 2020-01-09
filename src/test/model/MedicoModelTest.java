package test.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import bean.Medico;
import model.MedicoModel;

class MedicoModelTest {
	private static final LocalDate dataNascitaMedico = LocalDate.parse("1967-07-11");
	private static final String residenzaMedico = "Via Roma, 22, Scafati, 80030, NA";
	
	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}
	
	@Test
	void testGetMedicoByCF() {
		Medico medico = MedicoModel.getMedicoByCF("GRMBNN67L11B519R");
		assertNotNull(medico);
		assertEquals(medico.getNome(), "Geremia");
		assertEquals(medico.getCognome(), "Bernini");
		assertEquals(medico.getEmail(), "G.Bernini67@gmail.com");
		assertEquals(medico.getCodiceFiscale(), "GRMBNN67L11B519R");
		assertEquals(medico.getSesso(), "M");
		assertEquals(medico.getResidenza(), residenzaMedico);
		assertEquals(medico.getDataDiNascita(), dataNascitaMedico);
		assertEquals(medico.getLuogoDiNascita(), "Campobasso");
	}

	@Test
	void testGetMedicoByCFPassword() {
		Medico medico = MedicoModel.getMedicoByCFPassword("GRMBNN67L11B519R", "Quadri1234");
		assertNotNull(medico);
		assertEquals(medico.getNome(), "Geremia");
		assertEquals(medico.getCognome(), "Bernini");
		assertEquals(medico.getEmail(), "G.Bernini67@gmail.com");
		assertEquals(medico.getCodiceFiscale(), "GRMBNN67L11B519R");
		assertEquals(medico.getSesso(), "M");
		assertEquals(medico.getResidenza(), residenzaMedico);
		assertEquals(medico.getDataDiNascita(), dataNascitaMedico);
		assertEquals(medico.getLuogoDiNascita(), "Campobasso");
	}
	
	@Test
	void testRemoveMedico() {
		MedicoModel.removeMedico("GRMBNN67L11B519R");
		Medico medico = MedicoModel.getMedicoByCF("GRMBNN67L11B519R");
		assertNull(medico);
		
	}
	
	//TODO riaggiungere il medico rimosso
	
	@Test
	void testAddMedico() {
		Medico daAggiungere = new Medico("M", residenzaMedico, dataNascitaMedico, 
				"CRLBNN67L11B519R", "Carlo", "Bernini", "C.Bernini67@gmail.com", "Campobasso");
		MedicoModel.addMedico(daAggiungere, "Quadri9999");
		
		Medico medico = MedicoModel.getMedicoByCFPassword("GRMBNN67L11B519R", "Quadri1234");
		assertNotNull(medico);
		assertEquals(medico.getNome(), "Carlo");
		assertEquals(medico.getCognome(), "Bernini");
		assertEquals(medico.getEmail(), "C.Bernini67@gmail.com");
		assertEquals(medico.getCodiceFiscale(), "CRLBNN67L11B519R");
		assertEquals(medico.getSesso(), "M");
		assertEquals(medico.getResidenza(), residenzaMedico);
		assertEquals(medico.getDataDiNascita(), dataNascitaMedico);
		assertEquals(medico.getLuogoDiNascita(), "Campobasso");
		
		MedicoModel.removeMedico(daAggiungere.getCodiceFiscale());
	}
	
	//TODO da finire

	@Test
	void testGetAllMedici() {
		fail("Not yet implemented");
	}

	@Test
	void testUpdateMedico() {
		fail("Not yet implemented");
	}

	@Test
	void testGetMediciByPazienteSeguito() {
		fail("Not yet implemented");
	}

	@Test
	void testGetMedicoByEmail() {
		fail("Not yet implemented");
	}

	@Test
	void testUpdatePasswordMedico() {
		fail("Not yet implemented");
	}

}
