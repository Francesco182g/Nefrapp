/**
 * @author Eugenio
 */
(function($) {
  "use strict"; // Start of use strict
  var sub = false
  $(document).ready(function(){
	  var modMedico =sessionStorage.getItem("modMedico")
	  var modPaziente = sessionStorage.getItem("modPaziente")

	  if (modMedico != null)
		  {
		  	if(modMedico!="")
			  {
		  		caricaMedico();
			  }
		  }
	  if (modPaziente != null )
	  {
		  if(modPaziente !="")
		  {
			  caricaPaziente();
		  }
			  
	  }
	  
  });
  
  function caricaMedico(){
		
		var medico = JSON.parse(sessionStorage.getItem("modMedico"))
		sessionStorage.setItem("modMedico","")
		console.log(medico)
		$("#nome").val(medico.nome);
		$("#cognome").val(medico.cognome);
		$("#codiceFiscale").val(medico.codiceFiscale);
		if(medico.dataDiNascita != undefined)
		{
			var date  = medico.dataDiNascita.day+"/"+medico.dataDiNascita.month+"/"+medico.dataDiNascita.year
			$("#dataDiNascita").val(date);
		}
		$("#luogoDiNascita").val(medico.luogoDiNascita);
		$("#residenza").val(medico.residenza);
		$("#email").val(medico.email);
		if (medico.sesso == "M")
		{
			$("#M").prop("checked", true);
			$("#F").prop("checked", false);
		}
	else
		{
			$("#M").prop("checked", false);
			$("#F").prop("checked", true);
		}
			
			
		
  }
  function caricaPaziente(){
		console.log(sessionStorage.getItem("modPaziente"))
		var paziente = JSON.parse(sessionStorage.getItem("modPaziente"))
		sessionStorage.setItem("modPaziente","")
		console.log(paziente)
		$("#nome").val(paziente.nome);
		$("#cognome").val(paziente.cognome);
		$("#codiceFiscale").val(paziente.codiceFiscale);
		if(paziente.dataDiNascita != undefined)
		{
			var date  = paziente.dataDiNascita.day+"/"+paziente.dataDiNascita.month+"/"+paziente.dataDiNascita.year
			$("#dataDiNascita").val(date);
		}
		$("#luogoDiNascita").val(paziente.luogoDiNascita);
		console.log(paziente.residenza)
		$("#residenza").val(paziente.residenza);
		$("#email").val(paziente.email);
		if (paziente.sesso == "M")
		{
			$("#M").prop("checked", true);
			$("#F").prop("checked", false);
		}
	else
		{
			$("#M").prop("checked", false);
			$("#F").prop("checked", true);
		}
}
    
})(jQuery); // End of use strict

	