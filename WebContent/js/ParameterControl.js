/**
 * @author Eugenio
 */
var CFMsg = "Il formato del Codice Fiscale non è valido";
var nomeMsg = "Il formato del nome non è valido";
var cognomeMsg = "Il formato del cognome non è valido";
var passwordMsg = "La password deve essere compresa tra 6 e 20 caratteri";
var dataMsg = "Inserire la data in formato valido<br>es. 01/01/1970";
var luogoNascitaMsg = "Il formato del luogo di nascita non è valido";
var residenzaMsg = "Inserire l'indirizzo di residenza in formato valido<br>es. Via Roma, 23, Scafati, 80030, SA";
var sessoMsg = "Errore nella selezione del sesso";
var emailMsg = "L'indirizzo email inserito non è valido";
var confermaPasswordMsg = "Le due password inserite non coincidono";
var destinatarioMsg = "Selezionare un destinatario";
var testoMsg = "Inserire un testo valido.<br><br>Il testo deve essere compreso tra 1 e 1000 caratteri di lunghezza";
// l'ho chiamata genericamente intestazione perché questo messaggio è sia in
// annunci che in messaggi
var oggettoMsg = "Inserire un'intestazione valida.<br><br>L'intestazione deve essere compresa tra 1 e 75 caratteri di lunghezza";

(function($) {
	"use strict"; // Start of use strict
	var sub = false
	checkNotifica();
	$(document).ready(function() {

		var button1 = $("#registrazioneMedicoButton")
		var button2 = $("#accediAdminButton")
		var button3 = $("#loginUtente")
		var button4 = $("#resetPswButton")
		var button5 = $("#richiestaReset")
		var button6 = $("#inviaMessaggio")
		var button7 = $("#inviaAnnuncio")
		var button8 = $("#registrazioneButton")
		var button9 = $("#inserimentoSchedaButton")
		var button10 = $("#modificaPasswordAmministratoreButton")

		$(document).submit(function() {
			return sub;
		});
		if (button1.length > 0) {
			console.log("tasto premuto1")
			registraMedicoValidator();
		} else if (button2.length > 0 || button3.length > 0) {
			console.log("tasto premuto2")
			loginValidator();
		} else if (button4.length > 0) {
			console.log("tasto premuto3")
			resetPasswordValidator(0)
		} else if (button5.length > 0) {
			console.log("tasto premuto4")
			resetPasswordValidator(1)
		} else if (button6.length > 0) {
			console.log("tasto premuto5")
			inviaMessaggioValidator()
		} else if (button7.length > 0) {
			console.log("tasto premuto6")
			inviaAnnuncioValidator()
		} else if (button8.length > 0) {
			console.log("tasto premuto7")
			registrazioneValidator();
		} else if (button9.length > 0) {
			console.log("tasto premuto7")
			inserimentoScheda();
		} else if (button10.length > 0) {
			console.log("tasto premuto10")
			modificaPasswordAmministratoreValidator();
		}

	});

	/**
	 * Funzione che permette di customizzare gli alert
	 */
	function customAlert(msg) {
		$.alert({
			title : "",
			content : msg,
			type : 'red',
			typeAnimated : true,
			autoClose : 'ok|6000',

		});
	}

	/**
	 * Funzione che permette di validare la registrazione del paziente e anche
	 * la validazione delle modifiche sia di medico che paziente
	 */
	function registrazioneValidator() {
		$("#registrazioneButton").click(function() {

			var valid = registrazioneCheck()

			if (!valid[0]) {
				sub = false;

				customAlert(valid[1])
			} else {
				sub = true;
				$(document).submit();
			}

		});
	}
	/**
	 * Funzione che effettua la verifica dei campi di registrazione paziente e
	 * modifica paziente e medico restituisce una lista contenente un valore
	 * booleano che indica se i campi sono validi o meno in caso di campo
	 * invalido ti quale campo non è valido.
	 */
	function registrazioneCheck() {

		var valido = [ true ];
		var expCodiceFiscale = new RegExp(
				"^[a-zA-Z]{6}[0-9]{2}[a-zA-Z][0-9]{2}[a-zA-Z][0-9]{3}[a-zA-Z]$");
		var expNome = RegExp("^[A-Z][a-zA-Z ']*$");
		var expCognome = RegExp("^[A-Z][a-zA-Z ']*$");
		var expSesso = RegExp("^[MF]$");
		var expDataDiNascita = RegExp("^(0[1-9]|1[0-9]|2[0-9]|3[01])/(0[1-9]|1[012])/[0-9]{4}$");
		var expLuogoDiNascita = RegExp("^[A-Z][a-zA-Z ']*$");
		var expResidenza = RegExp("^[A-Za-z ']{2,}[, ]+[0-9]{1,4}[, ]+[A-Za-z ']{2,}[, ]+[0-9]{5}[, ]+[A-Za-z]{2}$");
		var expEmail = RegExp("^[A-Za-z0-9_.-]+@[a-zA-Z.]{2,}\\.[a-zA-Z]{2,3}$");
		var expPassword = RegExp("^[a-zA-Z0-9]*$");
		var codiceFiscale = $("#codiceFiscale").val();
		var nome = $("#nome").val();
		var cognome = $("#cognome").val();
		var sesso = $("input[name='sesso']:checked").val();
		var dataDiNascita = $("#dataDiNascita").val();
		var luogoDiNascita = $("#luogoDiNascita").val();
		var residenza = $("#residenza").val();
		var email = $("#email").val();
		var password = $("#password").val();
		var registrato = $("input[name='registrato']:checked").val()
		var medico = $("#medico").val();
		var confermaPsw = $("#confermaPsw").val();
		var operatore = $("#operatore").val();

		if (operatore == undefined) {
			if (medico == undefined) {
				if (registrato == "No" || registrato == undefined) {
					if (!expCodiceFiscale.test(codiceFiscale)
							|| codiceFiscale.length != 16)
						valido = [ false, CFMsg ];
					else if (!expNome.test(nome) || nome.length < 2
							|| nome.length > 30)
						valido = [ false, nomeMsg ];
					else if (!expCognome.test(cognome) || cognome.length < 2
							|| cognome.length > 30)
						valido = [ false, cognomeMsg ];
					else if (!expPassword.test(password) || password.length < 6
							|| password.length > 20)
						valido = [ false, passwordMsg ];
					else if (!expDataDiNascita.test(dataDiNascita))
						valido = [ false, dataMsg ];
					else if (!expLuogoDiNascita.test(luogoDiNascita)
							|| luogoDiNascita.length < 3
							|| luogoDiNascita.length > 50)
						valido = [ false, luogoNascitaMsg ];
					else if (!expResidenza.test(residenza)
							|| residenza.length < 5 || residenza.length > 50)
						valido = [ false, residenzaMsg ];

					else if (!expSesso.test(sesso) || sesso.length != 1)
						valido = [ false, sessoMsg ];

					else if (confermaPsw != undefined) {
						if (!expPassword.test(confermaPsw)
								|| confermaPsw.length < 6
								|| confermaPsw.length > 20
								|| confermaPsw != password)
							valido = [ false, confermaPasswordMsg ];
					}

					else if (email.length != 0) {
						if (!expEmail.test(email))
							valido = [ false, emailMsg ];
					}

				} else {
					if (!expCodiceFiscale.test(codiceFiscale)
							|| codiceFiscale.length != 16)
						valido = [ false, CFMsg ];
					else if (!expLuogoDiNascita.test(luogoDiNascita)
							|| luogoDiNascita.length < 3
							|| luogoDiNascita.length > 50)
						valido = [ false, luogoNascitaMsg ];

				}
			} else {
				if (!expCodiceFiscale.test(codiceFiscale)
						|| codiceFiscale.length != 16)
					valido = [ false, CFMsg ];
				else if (!expNome.test(nome) || nome.length < 2
						|| nome.length > 30)
					valido = [ false, nomeMsg ];
				else if (!expCognome.test(cognome) || cognome.length < 2
						|| cognome.length > 30)
					valido = [ false, cognomeMsg ];
				else if (!expPassword.test(password) || password.length < 6
						|| password.length > 20)
					valido = [ false, passwordMsg ];
				else if (!expSesso.test(sesso) || sesso.length != 1)
					valido = [ false, sessoMsg ];
				else if (!expEmail.test(email))
					valido = [ false, emailMsg ];
				else if (!expDataDiNascita.test(dataDiNascita))
					valido = [ false, dataMsg ];
				else if (!expLuogoDiNascita.test(luogoDiNascita)
						|| luogoDiNascita.length < 3
						|| luogoDiNascita.length > 50)
					valido = [ false, luogoNascitaMsg ];
				else if (!expResidenza.test(residenza) || residenza.length < 5
						|| residenza.length > 50)
					valido = [ false, residenzaMsg ];
				else if (confermaPsw != undefined) {
					if (!expPassword.test(confermaPsw)
							|| confermaPsw.length < 6
							|| confermaPsw.length > 20
							|| confermaPsw != password)
						valido = [ false, confermaPasswordMsg ];
				}

			}
		} else {
			if (medico == undefined) {

				if (!expCodiceFiscale.test(codiceFiscale)
						|| codiceFiscale.length != 16)
					valido = [ false, CFMsg ];
				else if (!expNome.test(nome) || nome.length < 2
						|| nome.length > 30)
					valido = [ false, nomeMsg ];
				else if (!expCognome.test(cognome) || cognome.length < 2
						|| cognome.length > 30)
					valido = [ false, cognomeMsg ];
				else if (!expDataDiNascita.test(dataDiNascita))
					valido = [ false, dataMsg ];
				else if (!expLuogoDiNascita.test(luogoDiNascita)
						|| luogoDiNascita.length < 3
						|| luogoDiNascita.length > 50)
					valido = [ false, luogoNascitaMsg ];
				else if (!expResidenza.test(residenza) || residenza.length < 5
						|| residenza.length > 50)
					valido = [ false, residenzaMsg ];

				else if (!expSesso.test(sesso) || sesso.length != 1)
					valido = [ false, sessoMsg ];
				else if (password.length > 0) {
					if (!expPassword.test(password) || password.length < 6
							|| password.length > 20) {
						valido = [ false, passwordMsg ];
					}

					else if (!expPassword.test(confermaPsw)
							|| confermaPsw.length < 6
							|| confermaPsw.length > 20
							|| confermaPsw != password)
						valido = [ false, confermaPasswordMsg ];
				}

				else if (email.length != 0) {
					if (!expEmail.test(email))
						valido = [ false, emailMsg ];
				}

			} else {
				if (!expCodiceFiscale.test(codiceFiscale)
						|| codiceFiscale.length != 16)
					valido = [ false, CFMsg ];
				else if (!expNome.test(nome) || nome.length < 2
						|| nome.length > 30)
					valido = [ false, nomeMsg ];
				else if (!expCognome.test(cognome) || cognome.length < 2
						|| cognome.length > 30)
					valido = [ false, cognomeMsg ];
				else if (!expSesso.test(sesso) || sesso.length != 1)
					valido = [ false, sessoMsg ];
				else if (!expEmail.test(email))
					valido = [ false, emailMsg ];
				else if (!expDataDiNascita.test(dataDiNascita))
					valido = [ false, dataMsg ];
				else if (!expLuogoDiNascita.test(luogoDiNascita)
						|| luogoDiNascita.length < 3
						|| luogoDiNascita.length > 50)
					valido = [ false, luogoNascitaMsg ];
				else if (!expResidenza.test(residenza) || residenza.length < 5
						|| residenza.length > 50)
					valido = [ false, residenzaMsg ];
				else if (password.length > 0) {
					if (!expPassword.test(password) || password.length < 6
							|| password.length > 20)
						valido = [ false, passwordMsg ];
					else if (!expPassword.test(confermaPsw)
							|| confermaPsw.length < 6
							|| confermaPsw.length > 20
							|| confermaPsw != password)
						valido = [ false, confermaPasswordMsg ];
				}

			}
		}

		return valido;

	}
	/**
	 * funzione che permette di validare l'invio di un messaggio
	 */

	function inviaMessaggioValidator() {
		$("#inviaMessaggio").click(function() {

			var valid = inviaMessaggioCheck()

			if (!valid[0]) {
				sub = false;

				customAlert(valid[1])
			} else {
				sub = true;
				$(document).submit();
			}

		});

	}

	/**
	 * funzione che permette di validare l'invio di un annuncio
	 */

	function inviaAnnuncioValidator() {
		$("#inviaAnnuncio").click(function() {

			var valid = inviaMessaggioCheck()

			if (!valid[0]) {
				sub = false;

				customAlert(valid[1])
			} else {
				sub = true;
				$(document).submit();
			}

		});

	}

	/**
	 * Funzione che permette di eseguire la richiesta di registrazione del
	 * medico alla servlet però fa prima dei controlli e se nella pagina è tutto
	 * ok invia i dati alla servlet
	 */
	function registraMedicoValidator() {
		$("#registrazioneMedicoButton").click(function() {

			var valid = checkValidityRegistrazioneMedico();
			if (!valid[0]) {
				sub = false;

				customAlert(valid[1])
			} else {
				sub = true;
				$(document).submit();
			}

		});
	}

	/**
	 * Funzione che permette di eseguire la richiesta di login
	 * dell'amministratore e degli altri utenti alla servlet però fa prima dei
	 * controlli e se nella pagina è tutto ok invia i dati alla servlet
	 */

	function loginValidator() {
		$("#accediAdminButton").click(function() {
			var valid = checkValidityLogin();
			if (!valid[0]) {
				sub = false;
				customAlert(valid[1])
			} else {
				sub = true;

				$(document).submit();
			}

		});

		$("#loginUtente").click(function() {
			// per chi fa la login utente , fate il controllo sul radio button
			// prima di richiamare il metodo check cosi
			// non si creano problemi con la pagina dell'amministratore se
			// rimane la check altrimenti canellate il commento
			var valid = checkValidityLogin();
			if (!valid[0]) {
				sub = false;
				customAlert(valid[1])
			} else {
				sub = true;
				$(document).submit();
			}

		});
	}

	/**
	 * funzione che controlla tutti i campi della form se un campo non è valido
	 * restituisce un arrai il cui primo elemnto è un booleano il secondo
	 * elemento indica quale campo non è valido e nel caso in cui tutti i campi
	 * siano validi viene restituito un array di un elemento che contiente un
	 * solo elemento che è vero inquanto tutti i campi sono giusti.
	 */

	function checkValidityRegistrazioneMedico() {
		var valido = [ true ];
		var expCodiceFiscale = new RegExp(
				"^[a-zA-Z]{6}[0-9]{2}[a-zA-Z][0-9]{2}[a-zA-Z][0-9]{3}[a-zA-Z]$");
		var expNome = RegExp("^[A-Z][a-zA-Z ']*$");
		var expCognome = RegExp("^[A-Z][a-zA-Z ']*$");
		var expSesso = RegExp("^[MF]$");
		var expDataDiNascita = RegExp("^(0[1-9]|1[0-9]|2[0-9]|3[01])/(0[1-9]|1[012])/[0-9]{4}$");
		var expLuogoDiNascita = RegExp("^[A-Z][a-zA-Z ']*$");
		var expResidenza = RegExp("^[A-Za-z ']{2,}[, ]+[0-9]{1,4}[, ]+[A-Za-z ']{2,}[, ]+[0-9]{5}[, ]+[A-Za-z]{2}$");
		var expEmail = RegExp("^[A-Za-z0-9_.-]+@[a-zA-Z.]{2,}\\.[a-zA-Z]{2,3}$");
		var expPassword = RegExp("^[a-zA-Z0-9]*$");
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

		if (!expCodiceFiscale.test(codiceFiscale) || codiceFiscale.length != 16)
			valido = [ false, CFMsg ];
		else if (!expNome.test(nome) || nome.length < 2 || nome.length > 30)
			valido = [ false, nomeMsg ];
		else if (!expCognome.test(cognome) || cognome.length < 2
				|| cognome.length > 30)
			valido = [ false, cognomeMsg ];
		else if (!expPassword.test(password) || password.length < 6
				|| password.length > 20)
			valido = [ false, passwordMsg ];
		else if (!expSesso.test(sesso) || sesso.length != 1)
			valido = [ false, sessoMsg ];
		else if (!expEmail.test(email))
			valido = [ false, emailMsg ];
		if(dataDiNascita.length!=0)
			if (!expDataDiNascita.test(dataDiNascita))
				valido=[false,"formato data di nascita non valido"];
		else if (luogoDiNascita.length!=0)
			if (!expLuogoDiNascita.test(luogoDiNascita) || luogoDiNascita.length < 3 || luogoDiNascita.length > 50)
				valido=[false,"formato luogo di nascita non valido"];
		else if (residenza.length!=0)
			if (!expResidenza.test(residenza) || residenza.length<5 || residenza.length>50)
				valido=[false,"formato residenza non valido"];

		else if (confermaPsw != undefined) {
			if (!expPassword.test(confermaPsw) || confermaPsw.length < 6
					|| confermaPsw.length > 20 || confermaPsw != password)
				valido = [ false, confermaPasswordMsg ];
		}
		return valido;
	}
	/**
	 * funzione che controlla tutti i campi della form se un campo non è valido
	 * restituisce un arrai il cui primo elemnto è un booleano il secondo
	 * elemento indica quale campo non è valido e nel caso in cui tutti i campi
	 * siano validi viene restituito un array di un elemento che contiente un
	 * solo elemento che è vero inquanto tutti i campi sono giusti.
	 */
	function checkValidityLogin() {
		var valido = [ true ];
		var expCodiceFiscale = new RegExp(
				"^[a-zA-Z]{6}[0-9]{2}[a-zA-Z][0-9]{2}[a-zA-Z][0-9]{3}[a-zA-Z]$");
		var expPassword = RegExp("^[a-zA-Z0-9]*$");
		var codiceFiscale = $("#codiceFiscale").val();
		var password = $("#password").val();
		if (!expCodiceFiscale.test(codiceFiscale) || codiceFiscale.length != 16)
			valido = [ false, CFMsg ];
		else if (!expPassword.test(password) || password.length < 6
				|| password.length > 20)
			valido = [ false, passwordMsg ];
		return valido;
	}
	/**
	 * funzione che verifica se c'è il campo nascosto notifica che viene
	 * utilizzato per stampare un errore generato da una servlet
	 */
	function checkNotifica() {
		var notifica = $("#notifica").val()

		if (notifica != undefined) {
			if (notifica.length != 0) {
				customAlert(notifica)
			}
		}

	}
	/**
	 * la funzione serve a controllare la validita dei parametri della pagina
	 * resetPasswordView e richiestaResetView il parametro num indica a quale
	 * pagina facciamo riferimento
	 */
	function resetPasswordValidator(num) {
		if (num == 0) {
			$("#resetPswButton").click(function() {
				var valid = checkResetPsw()
				if (!valid[0]) {
					sub = false;
					customAlert(valid[1])
				} else {

					sub = true;
					$(document).submit();
				}

			});
		} else if (num == 1) {
			$("#richiestaReset").click(function() {
				var valid = checkCF()
				if (!valid[0]) {
					sub = false;
					customAlert(valid[1])
				} else {

					sub = true;
					$(document).submit();
				}

			});
		}

	}
	/**
	 * funzione che permette di controlla se i parametri della form di modifica
	 * password sono corretti
	 */
	function checkResetPsw() {
		var codiceFiscale = $("#codiceFiscale").val()
		var email = $("#email").val()
		var password = $("#password").val()
		var confermaPsw = $("#confermaPsw").val()
		var expCodiceFiscale = new RegExp(
				"^[a-zA-Z]{6}[0-9]{2}[a-zA-Z][0-9]{2}[a-zA-Z][0-9]{3}[a-zA-Z]$");
		var expEmail = RegExp("^[A-Za-z0-9_.-]+@[a-zA-Z.]{2,}\\.[a-zA-Z]{2,3}$");
		var expPassword = RegExp("^[a-zA-Z0-9]*$");
		var valido = [ true ];

		if (!expCodiceFiscale.test(codiceFiscale) || codiceFiscale.length != 16)
			valido = [ false, CFMsg ];
		else if (!expPassword.test(password) || password.length < 6
				|| password.length > 20)
			valido = [ false, passwordMsg ];
		else if (!expPassword.test(confermaPsw) || confermaPsw.length < 6
				|| confermaPsw.length > 20 || confermaPsw != password)
			valido = [ false, confermaPasswordMsg ];
		else if (!expEmail.test(email) || email.length > 50 || email.length < 6)
			valido = [ false, emailMsg ];

		return valido

	}
	/**
	 * funzione che permette di controllare se la mail è valida
	 */
	function checkEmail() {
		var email = $("#email").val()
		var expEmail = RegExp("^[A-Za-z0-9_.-]+@[a-zA-Z.]{2,}\\.[a-zA-Z]{2,3}$");
		var valido = [ true ];

		if (!expEmail.test(email))
			valido = [ false, emailMsg ];

		return valido

	}

	/**
	 * funzione che permette di controllare se il cf è valido
	 */
	function checkCF() {
		var cf = $("#codiceFiscale").val()
		var expCf = RegExp("^[a-zA-Z]{6}[0-9]{2}[a-zA-Z][0-9]{2}[a-zA-Z][0-9]{3}[a-zA-Z]$");
		var valido = [ true ];

		if (!expCf.test(cf))
			valido = [ false, CFMsg ];

		return valido

	}

	/*
	 * Funzione che permette di ottenere l'estenzione di un file
	 */
	function getFileExtension(filename) {
		var ext = /^.+\.([^.]+)$/.exec(filename);
		return ext == null ? "" : ext[1];
	}
	/*
	 * Funzione che controlla se tutti i campi dell'invio del messaggio sono
	 * validi
	 */
	function inviaMessaggioCheck() {
		var valido = [ true ];
		var selettore = $('.selectpicker').val()
		var oggetto = $("#oggetto").val()
		var testo = $("#testo").val()
		var fileExt = getFileExtension($('#file').val());

		if (selettore.length <= 0)
			valido = [ false, destinatarioMsg ];
		else if (testo.length < 1 || testo.length > 1000)
			valido = [ false, testoMsg ];
		else if (oggetto.length < 1 || oggetto.length > 75)
			valido = [ false, oggettoMsg ];

		return valido;

	}
	/**
	 * Funzione che permette di eseguire la registrazione del medico fa prima
	 * dei controlli e se nella pagina è tutto ok invia i dati alla servlet
	 */
	function inserimentoScheda() {
		$("#inserimentoSchedaButton").click(function() {
			var valid = checkValidity()
			if (!valid[0]) {
				customAlert(valid[1])
			} else {
				sub = true;
				$(document).submit();
			}

		});
	}
	function checkValidity() {
		var valido = [ true ];
		var expPeso = new RegExp("^[1-9]{1}[0-9]{1,2}(\,\d{1,2})?$");
		var expPaMax = RegExp("^[1-9]{1}[0-9]{1,2}$");
		var expPaMin = RegExp("^[1-9]{1}[0-9]{1,2}$");
		var expScaricoIniziale = RegExp("^[-+]{1}[1-9]{1}[0-9]{1,3}$|^$|^[1-9]{1}[0-9]{1,3}$");
		var expUF = RegExp("^[-+]{1}[1-9]{1}[0-9]{1,3}$|^[1-9]{1}[0-9]{1,3}$");
		var expTempoSosta = RegExp("^[1-9]{1}[0-9]{0,1}$");
		var expScarico = RegExp("^[1-9]{1}[0-9]{1,3}$");
		var expCarico = RegExp("^[1-9]{1}[0-9]{2,3}$")
		var peso = $("#Peso").val();
		var pamax = $("#PaMax").val();
		var pamin = $("#PaMin").val();
		var scaricoIniziale = $("#ScaricoIniziale").val();
		var uf = $("#UF").val();
		var tempoSosta = $("#TempoSosta").val();
		var scarico = $("#Scarico").val();
		var carico = $("#Carico").val();
		console.log(expPeso.test(peso))

		if (!expPeso.test(peso) || peso > 150 || peso < 30)
			valido = [ false, "formato peso non valido" ];

		else if (!expPaMax.test(pamax) || pamax < 79 || pamax > 221)
			valido = [ false, "formato pressione massima non valido" ];

		else if (!expPaMin.test(pamin) || pamin < 40 || pamin > 130)
			valido = [ false, "formato pressione minima non valido" ];

		else if ((!expScaricoIniziale.test(scaricoIniziale))
				&& (scaricoIniziale < -1000 || scaricoIniziale > 3000))
			valido = [ false, "formato scarico iniziale non valido" ];

		else if (!expUF.test(uf) || uf < -1000 || uf > 1500)
			valido = [ false, "formato UF non valido" ];

		else if (!expTempoSosta.test(tempoSosta) || tempoSosta > 24
				|| tempoSosta < 1)
			valido = [ false, "formato tempo di sosta non valido" ];

		else if (!expCarico.test(carico) || carico > 3000 || carico < 500)
			valido = [ false, "formato carico non valido" ];

		else if (!expScarico.test(scarico) || scarico > 3500 || scarico < 1)
			valido = [ false, "formato scarico non valido" ];

		return valido;
	}

	/**
	 * Funzione che permette di validare la modifica della password
	 * dell'amministratore
	 */
	function modificaPasswordAmministratoreValidator() {
		$("#modificaPasswordAmministratoreButton").click(function() {

			var valid = modificaPasswordCheck()

			if (!valid[0]) {
				sub = false;

				customAlert(valid[1])
			} else {
				sub = true;
				$(document).submit();
			}

		});
	}
	/**
	 * Funzione che effettua la verifica dei campi di modifica password
	 * amministratore.
	 */
	function modificaPasswordCheck() {
		var valido = [ true ];
		var expPassword = RegExp("^[a-zA-Z0-9]*$");
		var vecchiaPassword = $("#vecchiaPassword").val();
		var nuovaPassword = $("#nuovaPassword").val();
		var confermaPassword = $("#confermaPassword").val();
		if (!expPassword.test(vecchiaPassword) || vecchiaPassword.length < 6
				|| vecchiaPassword.length > 20)
			valido = [ false, passwordMsg ];
		else if (!expPassword.test(nuovaPassword) || nuovaPassword.length < 6
				|| nuovaPassword.length > 20)
			valido = [ false, passwordMsg ];
		else if (!expPassword.test(confermaPassword)
				|| confermaPassword.length < 6 || confermaPassword.length > 20
				|| confermaPassword != nuovaPassword)
			valido = [ false, confermaPasswordMsg ];

		return valido;
	}

})(jQuery); // End of use strict

