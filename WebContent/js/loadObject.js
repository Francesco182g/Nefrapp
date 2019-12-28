/**
 * @author Eugenio
 */
(function($) {
	"use strict"; // Start of use strict
	var sub = false
	
	$(document).ready(function() {
		
		var admin = $("#admin")
		var paziente = $("#paziente")
		var medico = $("#medico")
		var caricaAnnuncio = $("#caricaAnnuncio")
		var caricaMessaggio = $("#caricaMessaggio")
		
		console.log(caricaAnnuncio)
		if (admin.length != 0) {
			caricaDatiAdmin();
		} else if (paziente.length != 0) {
			caricaDatiPaziente()
		} else if (medico.length != 0) {

			caricaDatiMedico()
		}else if (caricaAnnuncio.length != 0)
			{
				loadAnnuncio()
			}
		else if (caricaMessaggio.length != 0)
		{
			loadMessaggio()
		}
		

	});
	
	function loadMessaggio()
	{
		var $messaggio =$("#messaggio");
	    $messaggio.fileinput({
	        theme: "fas",
	        dropZoneEnabled: false,
	        language:"it",
	        allowedFileExtensions: ["bmp","jpeg","jfif","pjpeg","pjp", "jpg", "gif", "png"],
	        uploadUrl: "GestioneMessaggi",
	        showUpload: false, // hide upload button
	        showRemove: false, // hide remove button
	        overwriteInitial: false, // append files to initial preview
	        minFileCount: 1,
	        maxFileCount: 5,
	        initialPreviewAsData: true,
	        uploadExtraData: function(previewId, index) {
	            return {operazione: "caricaAllegato"};
	        }
	    }).on("filebatchselected", function(event, files) {
	        $messaggio.fileinput("upload");
	    }).on('fileuploaded', function(event, previewId, index, fileId) {
	       console.log("caricato")
	    });
	}
	
	function loadAnnuncio()
	{
		var $annuncio =$("#annuncio");
	    $annuncio.fileinput({
	        theme: "fas",
	        dropZoneEnabled: false,
	        language:"it",
	        allowedFileExtensions: ["bmp","jpeg","jfif","pjpeg","pjp", "jpg", "gif", "png", "pdf"],
	        uploadUrl: "GestioneAnnunci",
	        showUpload: false, // hide upload button
	        showRemove: false, // hide remove button
	        overwriteInitial: false, // append files to initial preview
	        minFileCount: 1,
	        maxFileCount: 5,
	        initialPreviewAsData: true,
	        
	        uploadExtraData: function(previewId, index) {
	            return {operazione: "caricaAllegato"};
	        }
	    }).on("filebatchselected", function(event, files) {
	        $annuncio.fileinput("upload");
	    }).on('fileuploaded', function(event, previewId, index, fileId) {
	        console.log('File uploaded', previewId, index, fileId);
	        console.log("pre",previewId)
	        console.log("ind",index)
	        console.log("fileId",fileId)
	        
	    });
	}
	/**
	 * funzione che esegue una chimata asincrona per poter prendere i dati del
	 * paziente dalla servlet e caricarli nella dashboard del paziente
	 */
	function caricaDatiPaziente(){
		$.post("GestionePianoTerapeutico",{operazione : "visualizza",tipo :"asincrona"},function(data){
			var diagnosi= $("#diagnosi")
			var farmaco= $("#farmaco")
			var dataFine= $("#data")
			$(diagnosi).html(data.diagnosi)
			$(farmaco).html(data.farmaco)
			$(dataFine).html(data.dataFineTerapia.day+"/"+data.dataFineTerapia.month+"/"+data.dataFineTerapia.year)
			
		}).fail(function(){
			alert("si è verificato un errore")
		})
		$.post("GestioneAnnunci",{operazione : "visualizzaPersonali",tipo :"asincrona"},function(data){
			console.log(data)
				loadTabellaAnnunci(data)
			
		}).fail(function(){
			alert("si è verificato un errore")
		})
	}
	/**
	 * funzione che esegue una chimata asincrona per poter prendere i dati del
	 * medico dalla servlet e caricarli nella dashboard del medico
	 */
	function caricaDatiMedico(){
		console.log("Ciao sto caricando i dati del medico")
		$.post("GestioneMedico",{operazione : "VisualizzaPazientiSeguiti",tipo :"asincrona"},function(data){
			console.log("OPERAZIONE ESEGUITA CON SUCCESSO")
			console.log(data)
			loadTabellaPazienti(data,false)
		}).fail(function(){
			alert("si è verificato un errore")
		})
		$.post("GestioneAnnunci",{operazione : "visualizzaPersonali",tipo :"asincrona"},function(data){
			console.log(data)
			loadTabellaAnnunci(data)
			
		}).fail(function(){
			alert("si è verificato un errore")
		})
	}
	
	/**
	 * funzione che esegue una chimata asincrona per poter prendere i dati del
	 * paziente e del medico dalla servlet e caricarli nella dashboard dell'admin
	 */
	function caricaDatiAdmin() {
		$.post("GestioneAmministratore", {
			operazione : "caricaMedPaz"
		}, function(data) {
			loadTabellaMedici(data[0])
			loadTabellaPazienti(data[1],true)

			$(".eliminaButtonMedico").click(function() {
				$("#eliminazione").children().remove()
				var id = $(this).attr("id")
				var cf = $(this).parents().find(".cfMed")[id].firstChild.data
				addConfermaEliminazione("medico")
				$("#confermaEliminazione").click(function() {
					eliminaMedico(cf)
				})

			});
			$(".eliminaButtonPaziente").click(function() {
				$("#eliminazione").children().remove()
				var id = $(this).attr("id")
				var cf = $(this).parents().find(".cfPaz")[id].firstChild.data
				addConfermaEliminazione("paziente")
				$("#confermaEliminazione").click(function() {
					eliminaPaziente(cf)
				})

			});
			$(".modificaMedicoButton").click(function() {

				var id = $(this).attr("id")
				var medico = JSON.stringify(data[0][id])
				sessionStorage.setItem("modMedico", medico)
				window.location.href = "ModificaAccountMedicoView.jsp"

			});
			$(".modificaPazienteButton").click(function() {

				var id = $(this).attr("id")
				var paziente = JSON.stringify(data[1][id])
				sessionStorage.setItem("modPaziente", paziente)
				window.location.href = "ModificaAccountPazienteView.jsp"

			});
		}).fail(function() {
			alert("si è verificato un errore")
		});
	}

	/**
	 * Funzione che manda una richiesta asincrona per eliminare il medico dal
	 * database
	 */
	function eliminaMedico(cf) {
		$.post("GestioneAmministratore", {
			operazione : "rimuoviAccount",
			codiceFiscale : cf,
			tipo : "medico"
		}, function(data) {
			location.reload();
		}).fail(function() {
			alert("si è verificato un errore")
		});
	}
	/**
	 * Funzione che manda una richiesta asincrona per eliminare il paziente dal
	 * database
	 */
	function eliminaPaziente(cf) {
		$.post("GestioneAmministratore", {
			operazione : "rimuoviAccount",
			codiceFiscale : cf,
			tipo : "paziente"
		}, function(data) {
			location.reload();
		}).fail(function() {
			alert("si è verificato un errore")
		});
	}
	/**
	 * funzione che carica i dati del medico nella tabella
	 */
	function loadTabellaMedici(medici) {
		var tabellaMedici = $("#tabellaMedici")
		var riga = ""
		for (var i = 0; i < medici.length; i++) {
			riga += "<li class='list-group-item mt-3'><div class='row table-responsive'><div class='col-12 col-sm-6 col-md-9 text-center text-sm-left'><table>"
			riga += "<tr><td><p>Nome: </p></td>"
			riga += "<td><p>" + medici[i].nome + "</p></td></tr>"
			riga += "<tr><td><p>Cognome: </p></td>"
			riga += "<td><p>" + medici[i].cognome + "</p></td></tr>"
			riga += "<tr><td><p>Codice Fiscale: </p></td>"
			riga += "<td><p class='cfMed'>" + medici[i].codiceFiscale
					+ "</p></td></tr>"
			riga += "<tr><td><p>Email: </p></td>"
			riga += "<td><p>" + medici[i].email + "</p></td></tr>"
			riga += "</table></div><div class='col-12 mt-3 d-flex justify-content-center'><button type='button' id = '"
					+ i
					+ "' class='btn btn-primary btn-user mr-sm-5 modificaMedicoButton'>Modifica</button><button type='button' data-toggle='modal' data-target='#eliminaModal' id = '"
					+ i
					+ "' class='btn btn-danger btn-user eliminaButtonMedico'>Elimina</button></div>"
		}

		tabellaMedici.append(riga)

	}
	/**
	 * funzione che carica gli annunci nella tabella
	 */
	function loadTabellaAnnunci(medici) {
		var tabellaMedici = $("#tabellaAnnunci")
		var riga = ""
		for (var i = 0; i < medici.length; i++) {
			riga += "<li class='list-group-item mt-3'><div class='row table-responsive'><div class='col-12 col-sm-6 col-md-9 text-center text-sm-left'><table>"
			riga += "<tr ><td><p>Titolo: </p></td>"
			riga += "<td><p>" + medici[i].titolo + "</p></td></tr>"
			riga += "<tr ><td><p>Testo: </p></td>"
			riga += "<td><p>" + medici[i].testo + "</p></td></tr>"
			
			riga += "</table></div><div class='col-12 mt-3 d-flex justify-content-center'><button type='button' id = '"
					+ i
					+ "' class='btn btn-primary btn-user mr-sm-5 modificaMedicoButton'>Vedi annuncio (WP)</button>"
		}

		tabellaMedici.append(riga)

	}

	/**
	 * funzione che permette di aggiungere una finestra di conferma quando si
	 * preme il tasto elimina
	 */
	function addConfermaEliminazione(utente) {
		var moduloDiConferma = "<div class='modal fade' id='eliminaModal' tabindex='-1' role='dialog' aria-labelledby='exampleModalLabel' aria-hidden='true'>"
				+ "<div class='modal-dialog' role='document'>"
				+ "<div class='modal-content'>"
				+ "<div class='modal-header'>"
				+ "<h5 class='modal-title' id='exampleModalLabel'>Sicuro di voler eliminare il "
				+ utente
				+ "?</h5>"
				+ "<button class='close' type='button' data-dismiss='modal'aria-label='Close'>"
				+ "<span aria-hidden='true'>×</span></button>"
				+ "</div><div class='modal-body'>Seleziona 'Elimina' qui sotto se sei pronto ad eliminare il "
				+ utente
				+ ".</div>"
				+ "<div class='modal-footer'><button class='btn btn-secondary' type='button' data-dismiss='modal'>Esci</button>"
				+ "<button class='btn btn-primary' data-dismiss='modal' id = 'confermaEliminazione'>Elimina</button></form></div></div></div></div>"
		$("#eliminazione").append(moduloDiConferma)

	}

	/**
	 * funzione che carica i dati del paziente nella tabella
	 */
	function loadTabellaPazienti(pazienti,bottoni) {
		var tabellaPazienti = $("#tabellaPazienti")
		var riga = ""
		for (var i = 0; i < pazienti.length; i++) {
			riga += "<li class='list-group-item mt-3'><div class='row table-responsive'><div class='col-12 col-sm-6 col-md-9 text-center text-sm-left tab'><table>"
			riga += "<tr><td><p>Nome: </p></td>"
			riga += "<td><p>" + pazienti[i].nome + "</p></td></tr>"
			riga += "<tr><td><p>Cognome: </p></td>"
			riga += "<td><p>" + pazienti[i].cognome + "</p></td></tr>"
			riga += "<tr><td><p>Codice Fiscale: </p></td>"
			riga += "<td><p class='cfPaz'>" + pazienti[i].codiceFiscale
					+ "</p></td></tr>"
			riga += "<tr><td><p>Email: </p></td>"
			riga += "<td><p>" + pazienti[i].email + "</p></td></tr>"
			riga += "</table></div>"
			if(bottoni)
				{
				riga +="<div class='col-12 mt-3 d-flex justify-content-center'><button type='button' id = '"
					+ i
					+ "' class='btn btn-primary btn-user mr-sm-5 modificaPazienteButton'>Modifica</button><button type='button' data-toggle='modal' data-target='#eliminaModal' id = '"
					+ i
					+ "' class='btn btn-danger btn-user eliminaButtonPaziente'>Elimina</button></div>"
				}
			else
				{
					riga+="<div class='col-12 mt-3 d-flex justify-content-center'><a href='GestionePianoTerapeutico?operazione=visualizza&CFPaziente="+pazienti[i].codiceFiscale+"' class='btn mr-sm-5 btn-info btn-icon-split'>" +
							"<span class='icon text-white-50'><i class='fas fa-info-circle'></i></span><span class='text'>Piano terapeutico</span></a>" +
							"<a href='GestioneParametri?operazione=visualizzaScheda&CFPaziente="+pazienti[i].codiceFiscale+"'class='btn btn-info btn-icon-split'>" +
									"<span class='icon text-white-50'><i class='fas fa-info-circle'></i></span><span class='text'>Scheda parametri</span></a></div>"
				}
		}

		tabellaPazienti.append(riga)

	}

})(jQuery); // End of use strict

