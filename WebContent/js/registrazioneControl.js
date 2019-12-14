/**
 * @author Eugenio
 */
(function($) {
  "use strict"; // Start of use strict
  var sub = false
  $(document).ready(function(){
	  
	  /**
	   * Funzione che blocca il submit
	   */
	  $(document).submit(function(){
			  return sub; 
	  });
	  console.log("ciao mondo");
	  /**
	   * Funzione che permette di eseguire la registrazione del medico fa prima dei controlli e se nella pagina è tutto ok invia i dati alla servlet
	   */
	  registraMedico();
	});
	
  /**
   * Funzione che permette di eseguire la registrazione del medico fa prima dei controlli e se nella pagina è tutto ok invia i dati alla servlet
   */
	function registraMedico(){
		$("#registrazioneMedicoButton").click(function(){
			var valid = checkValidity()
			if (!valid [0])
				{
					alert(valid[1])
				}
			else
				{
					sub = true;
					$(document).submit();
				}
			
		});
	}
	function checkValidity() {
		var valido=[true];
		var expCodiceFiscale=new RegExp("^[a-zA-Z]{6}[0-9]{2}[a-zA-Z][0-9]{2}[a-zA-Z][0-9]{3}[a-zA-Z]$");
		var expNome=RegExp("^[A-Z][a-zA-Z ]*$");
		var expCognome=RegExp("^[A-Z][a-zA-Z ]*$");
		var expSesso=RegExp("^[MF]$");
		var expEmail=RegExp("^[A-Za-z0-9_.-]+@[a-zA-Z.]{2,}\\.[a-zA-Z]{2,3}$");
		var expPassword=RegExp("^[a-zA-Z0-9]*$");
		var codiceFiscale = $("#codiceFiscale").val();
		var nome = $("#nome").val();
		var cognome = $("#cognome").val();
		var sesso = $("input[name='sesso']:checked").val();
		var email = $("#email").val();
		var password = $("#password").val();
		console.log(expNome.test(nome))
		console.log(nome.length<2)
		console.log(nome.length>30)
		
		
		if (!expCodiceFiscale.test(codiceFiscale)||codiceFiscale.length!=16)
			valido=[false,"formato codiceFiscale non valido"];
		else if (!expNome.test(nome)||nome.length<2||nome.length>30)
			valido=[false,"formato nome non valido"];
		else if (!expCognome.test(cognome)||cognome.length<2||cognome.length>30)
			valido=[false,"formato cognome non valido"];
		else if (!expPassword.test(password)||password.length<6||password.length>20)
			valido=[false,"formato password non valido"];
		else if (!expSesso.test(sesso)||sesso.length!=1)
			valido=[false,"formato sesso non valido"];
		else if (!expEmail.test(email))
			valido=[false,"formato email non valido"];
		
		return valido;
	}

  

})(jQuery); // End of use strict

	