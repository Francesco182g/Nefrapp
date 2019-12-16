/**
 * @author Eugenio
 */
(function($) {
  "use strict"; // Start of use strict
  var sub = false
  $(document).ready(function(){
	  var button1 = $("#registrazioneMedicoButton")
	  var button2 = $("#accediAdminButton") 
	  var button3 = $("#loginUtente")
	  $(document).submit(function(){
				  return sub; 
		  });
	  if(button1.length>0)
		  {
		  	registraMedicoValidator();
		  }
	  else if(button2.length>0 || button3.length>0)
	  	{
		  loginValidator();
	  	}
	  
	  
	});
	
  /**
   * Funzione che permette di eseguire la richiesta di registrazione del medico alla servlet però fa prima dei controlli 
   * e se nella pagina è tutto ok invia i dati alla servlet
   */
	function registraMedicoValidator(){
		$("#registrazioneMedicoButton").click(function(){
			var valid = checkValidityRegistrazioneMedico();
			if (!valid [0])
				{
					sub = false;
					alert(valid[1])
				}
			else
				{
					sub = true;
					$(document).submit();
				}
			
		});
	}
	
	  /**
	   * Funzione che permette di eseguire la richiesta di login dell'amministratore e degli altri utenti alla servlet però fa prima dei controlli 
	   * e se nella pagina è tutto ok invia i dati alla servlet
	   */

	function loginValidator(){
		$("#accediAdminButton").click(function(){
			var valid = checkValidityLogin();
			if (!valid [0])
				{
					sub = false;
					alert(valid[1])
				}
			else
				{
					sub = true;
					console.log("sono qui")
					$(document).submit();
				}
			
		});
		
		$("#loginUtente").click(function(){
			var valid = checkValidityLogin();
			if (!valid [0])
				{
					sub = false;
					alert(valid[1])
				}
			else
				{
					sub = true;
					console.log("sono qui")
					$(document).submit();
				}
			
		});
	}

	
	
	/**
	 * funzione che controlla tutti i campi della form se un campo non è valido restituisce un arrai il cui primo elemnto è un booleano
	 * il secondo elemento indica quale campo non è valido e nel caso in cui tutti i campi siano validi viene restituito un 
	 * array di un elemento che contiente un solo elemento che è vero inquanto tutti i campi sono giusti.
	 */
	function checkValidityRegistrazioneMedico() {
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
	/**
	 * funzione che controlla tutti i campi della form se un campo non è valido restituisce un arrai il cui primo elemnto è un booleano
	 * il secondo elemento indica quale campo non è valido e nel caso in cui tutti i campi siano validi viene restituito un 
	 * array di un elemento che contiente un solo elemento che è vero inquanto tutti i campi sono giusti.
	 */
	function checkValidityLogin() {
		var valido=[true];
		var expCodiceFiscale=new RegExp("^[a-zA-Z]{6}[0-9]{2}[a-zA-Z][0-9]{2}[a-zA-Z][0-9]{3}[a-zA-Z]$");
		var expPassword=RegExp("^[a-zA-Z0-9]*$");
		var codiceFiscale = $("#codiceFiscale").val();
		var password = $("#password").val();
		var operazione = $('[name="operazione"]').val();
		
		if (!$('[name="operazione"]').is(':checked'))
				{
				if(!(operazione=="amministratore"))
						valido=[false,"Paziente o medico?"];
				}
		
		if (!expCodiceFiscale.test(codiceFiscale)||codiceFiscale.length!=16)
			valido=[false,"formato codiceFiscale non valido"];
		else if (!expPassword.test(password)||password.length<6||password.length>20)
			valido=[false,"formato password non valido"];
		return valido;
	}
	
	/**
	  * Funzione che blocca il submit
	  */
	function bloccaSubmit()
	{
		  
	}
  

})(jQuery); // End of use strict

	