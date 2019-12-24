/**
 * @author Eugenio
 */
(function($) {
  "use strict"; // Start of use strict
  var sub = false
  $(document).ready(function(){
	  checkNotifica();
	  var button1 = $("#registrazioneMedicoButton")
	  var button2 = $("#accediAdminButton") 
	  var button3 = $("#loginUtente")
	  var button4 = $("#resetPswButton")
	  var button5 = $("#richiestaReset")
	  var button6 = $("#inviaMessaggio")
	  
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
	  else if(button4.length>0)
		  {
		  	resetPasswordValidator(0)
		  }
	  else if(button5.length>0)
	  {
		  resetPasswordValidator(1)
	  }
	  else if(button6.length>0)
		  {
		  	inviaMessaggioValidator()
		  }
	 
	  
	  
	});

  /**
   * funzione che permette di validare l'invio di un messaggio
   */

  function inviaMessaggioValidator(){
	  $("#inviaMessaggio").click(function(){
			
			
		var valid = inviaMessaggioCheck()
		console.log(valid)
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
					$(this).prop("disabled",true);
					$(document).submit();
				}
			
		});
		
		$("#loginUtente").click(function(){
			//per chi fa la login utente , fate il controllo sul radio button prima di richiamare il metodo check cosi 
			//non si creano problemi con la pagina dell'amministratore se rimane la check altrimenti canellate il commento
			var valid = checkValidityLogin();
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
	 * funzione che controlla tutti i campi della form se un campo non è valido restituisce un arrai il cui primo elemnto è un booleano
	 * il secondo elemento indica quale campo non è valido e nel caso in cui tutti i campi siano validi viene restituito un 
	 * array di un elemento che contiente un solo elemento che è vero inquanto tutti i campi sono giusti.
	 */
	
	function checkValidityRegistrazioneMedico(){
		var valido=[true];
		var expCodiceFiscale=new RegExp("^[a-zA-Z]{6}[0-9]{2}[a-zA-Z][0-9]{2}[a-zA-Z][0-9]{3}[a-zA-Z]$");
		var expNome=RegExp("^[A-Z][a-zA-Z ']*$");
		var expCognome=RegExp("^[A-Z][a-zA-Z ']*$");
		var expSesso=RegExp("^[MF]$");
		var expDataDiNascita=RegExp("^(0[1-9]|1[0-9]|2[0-9]|3[01])/(0[1-9]|1[012])/[0-9]{4}$");
		var expLuogoDiNascita=RegExp("^[A-Z][a-zA-Z ']*$");
		var expResidenza=RegExp("^[A-Za-z ']{2,}[, ]+[0-9]{1,4}[, ]+[A-Za-z ']{2,}[, ]+[0-9]{5}[, ]+[A-Za-z]{2}$");
		var expEmail=RegExp("^[A-Za-z0-9_.-]+@[a-zA-Z.]{2,}\\.[a-zA-Z]{2,3}$");
		var expPassword=RegExp("^[a-zA-Z0-9]*$");
		var codiceFiscale = $("#codiceFiscale").val();
		var nome = $("#nome").val();
		var cognome = $("#cognome").val();
		var sesso = $("input[name='sesso']:checked").val();
		var dataDiNascita = $("#dataDiNascita").val();
		var luogoDiNascita = $("#luogoDiNascita").val();
		var residenza = $("#residenza").val();
		var email = $("#email").val();
		var password = $("#password").val();
		var confermaPsw = $("#confermaPsw").val();
		
		if (!expCodiceFiscale.test(codiceFiscale)||codiceFiscale.length!=16)
			valido=[false,"formato codiceFiscale non valido"];
		else if (!expNome.test(nome)||nome.length<2||nome.length>30)
			valido=[false,"formato nome non valido"];
		else if (!expCognome.test(cognome)||cognome.length<2||cognome.length>30)
			valido=[false,"formato cognome non valido"];
		else if (!expPassword.test(password)||password.length<6||password.length>20)
			valido=[false,"formato password non valido"];
		else if (confermaPsw != undefined)
			{
				if(!expPassword.test(confermaPsw)||confermaPsw.length<6||confermaPsw.length>20||confermaPsw!=password)
					valido=[false,"formato conferma password non valido"];
			}
		else if (!expSesso.test(sesso)||sesso.length!=1)
			valido=[false,"formato sesso non valido"];
		else if (!expEmail.test(email))
			valido=[false,"formato email non valido"];
		
		if(dataDiNascita.length!=0)
			if (!expDataDiNascita.test(dataDiNascita))
				valido=[false,"formato data di nascita non valido"];
		else if (luogoDiNascita.length!=0)
			if (!expLuogoDiNascita.test(luogoDiNascita) || luogoDiNascita.length < 5 || luogoDiNascita.length > 50)
				valido=[false,"formato luogo di nascita non valido"];
		else if (residenza.length!=0)
			if (!expResidenza.test(residenza) || residenza.length<5 || residenza.length>50)
				valido=[false,"formato residenza non valido"];
				
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
		if (!expCodiceFiscale.test(codiceFiscale)||codiceFiscale.length!=16)
			valido=[false,"formato codiceFiscale non valido"];
		else if (!expPassword.test(password)||password.length<6||password.length>20)
			valido=[false,"formato password non valido"];
		return valido;
	}
	/**
	 * funzione che verifica se c'è il campo nascosto notifica che viene utilizzato per stampare un 
	 * errore generato da una servlet
	 */
	function checkNotifica(){
		var notifica = $("#notifica").val()
		if (notifica != undefined)
			{
			if(notifica.length!=0)
			{
				alert(notifica)
			}
			}
		
	}
	/**
	 * la funzione serve a controllare la validita dei parametri della pagina resetPasswordView e 
	 * richiestaResetView il parametro num indica a quale pagina facciamo riferimento
	 */
	function resetPasswordValidator(num){
		if(num == 0)
			{
				$("#resetPswButton").click(function(){
					var valid = checkResetPsw()
					if (!valid [0])
					{
						sub = false;
						alert(valid[1])
					}
					else
					{
						console.log("controlli ok")
						sub = true;
						$(document).submit();
					}
				
				});
			}
		else if (num == 1)
			{
				$("#richiestaReset").click(function(){
					var valid = checkEmail()
					if (!valid [0])
					{
						sub = false;
						alert(valid[1])
					}
					else
					{
						console.log("controlli ok")
						sub = true;
						$(document).submit();
					}
			
				});
			}
		
		
	}
	/**
	 * funzione che permette di controlla se i parametri della form di modifica password sono corretti
	 */
	function checkResetPsw(){
		var codiceFiscale = $("#codiceFiscale").val()
		var email = $("#email").val()
		var password = $("#password").val()
		var confermaPsw = $("#confermaPsw").val()
		var expCodiceFiscale=new RegExp("^[a-zA-Z]{6}[0-9]{2}[a-zA-Z][0-9]{2}[a-zA-Z][0-9]{3}[a-zA-Z]$");
		var expEmail=RegExp("^[A-Za-z0-9_.-]+@[a-zA-Z.]{2,}\\.[a-zA-Z]{2,3}$");
		var expPassword=RegExp("^[a-zA-Z0-9]*$");
		var valido=[true];
		
		if (!expCodiceFiscale.test(codiceFiscale)||codiceFiscale.length!=16)
			valido=[false,"formato codiceFiscale non valido"];
		else if (!expPassword.test(password)||password.length<6||password.length>20)
			valido=[false,"formato password non valido"];
		else if (!expPassword.test(confermaPsw)||confermaPsw.length<6||confermaPsw.length>20||confermaPsw!=password)
			valido=[false,"formato conferma password non valido"];
		else if (!expEmail.test(email))
			valido=[false,"formato email non valido"];
		
		return valido
		
	}
	/**
	 * funzione che permette di controllare se la mail è valida
	 */
	function checkEmail(){
		var email = $("#email").val()
		var expEmail=RegExp("^[A-Za-z0-9_.-]+@[a-zA-Z.]{2,}\\.[a-zA-Z]{2,3}$");
		var valido=[true];
		
		if (!expEmail.test(email))
			valido=[false,"formato email non valido"];
		
		return valido
		
	}
	  /*
	   * Funzione che permette di ottenere l'estenzione di un file
	   * */
	  function getFileExtension(filename)
	  {
	    var ext = /^.+\.([^.]+)$/.exec(filename);
	    return ext == null ? "" : ext[1];
	  }
	  /*
	   * Funzione che controlla se tutti i campi dell'invio del messaggio sono validi
	   * */
		function inviaMessaggioCheck(){
			var valido=[true];
			var selettore = $('.selectpicker').val()
			var oggetto = $("#oggetto").val()
			var testo = $("#testo").val()
			var fileExt = getFileExtension($('#file').val());
			console.log(fileExt)
			
			//studiare l'inserimento di escape di sicurezza e sanitizzazione.
			//
			//direi che non puoi pickare singolarmente tutte le cose che possono stare in un messaggio, lol
			//e soprattutto non c'è da bloccare, c'è da sanitizzare
			//se scrivi <script> in un form di un sito fatto bene non ti fa esplodere la casa
			//semplicemente sotto il cofano sostituisce < con &lt e > con &gt
			//in modo che non sia valutato eseguibile dal js engine del browser 
			// ma sia comunque mostrato a schermo in quanto html valido.
			
			if (selettore.length<=0)
				valido=[false,"selezionare un destinatario"];
			else if (testo.length<1||testo.length>1000)
				valido=[false,"formato testo non valido"];
			else if (oggetto.length<1||oggetto.length>75)
				valido=[false,"formato oggetto non valido"];
			else if (fileExt!="jpg" && fileExt!="jpeg" && fileExt!="png" && fileExt!="pjpeg" && fileExt!="pjp" && fileExt!="jfif" && fileExt!="bmp" && fileExt!="") 
				valido = [false,"Il file deve essere di tipo BMP, PNG, JPG, JPEG, JFIF, PJPEG, o PJP"];
			else if(!$('#file').val().includes(".") && $('#file').val().length>0) {
				valido = [false,"Il file deve essere di tipo BMP, PNG, JPG, JPEG, JFIF, PJPEG, o PJP"] }
			
			//aggiungere conversione del testo qui 
			return valido;
			
		}
		
		/**
		 * converte i caratteri speciali <> & in caratteri unicode
		 */
		function escapehtml(s) {
		    return s.replace(/&/g,"&amp;").replace(/</g,"&lt;").replace(/>/g,"&gt;");
		}
	
})(jQuery); // End of use strict

	