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

		if (admin.length != 0) {
			caricaDatiAdmin();
		} else if (paziente.length != 0) {
			caricaDatiPaziente()
		} else if (medico.length != 0) {

			caricaDatiMedico()
		} else if (caricaAnnuncio.length != 0) {
			loadAnnuncio()
		} else if (caricaMessaggio.length != 0) {
			loadMessaggio()
		}

	});

	/**
	 * Funzione che permette di caricare lo script per l'insermento dell' file all'interno della view messaggi
	 */
	function loadMessaggio() {
		var $messaggio = $("#messaggio");
		$messaggio.fileinput(
				{
					theme : "fas",
					dropZoneEnabled : false,
					language : "it",
					allowedFileExtensions : [ "bmp", "jpeg", "jfif", "pjpeg",
							"pjp", "jpg", "gif", "png" ],
					uploadUrl : "GestioneMessaggi",
					showUpload : false, // hide upload button
					showRemove : false, // hide remove button
					overwriteInitial : false, // append files to initial
												// preview
					minFileCount : 1,
					maxFileCount : 1,
					initialPreviewAsData : true,
					uploadExtraData : function(previewId, index) {
						return {
							operazione : "caricaAllegato"
						};
					}
				}).on("filebatchselected", function(event, files) {
			$messaggio.fileinput("upload");
			$("#inviaMessaggio").attr("disabled", true);
				}).on('fileuploaded', function(event, previewId, index, fileId) {
				$("#inviaMessaggio").attr("disabled", false);
				$(".kv-file-remove").click(function() {
					$.post("GestioneMessaggi", {
						operazione : "rimuoviAllegato"
					}, function(data) {
						console.log("eliminazione effettuata")
					}).fail(function() {
						console.log("si è verificato un errore nella rimozione degli allegati")
					})
				})
		});
	}
	/**
	 * Funzione che permette di customizzare gli alert
	 */
	function customAlert(msg)
	  {
		  $.alert({
			    title:msg,
			    content: 'Questo box si chiudera automaticamente entro 6 secondi se non premi su ok',
			    type: 'red',
			    typeAnimated: true,
			    autoClose: 'ok|6000',
			    
			});
	  }
	/**
	 * Funzione che permette di caricare lo script per l'insermento dell' file all'interno della view annunci
	 */
	function loadAnnuncio() {
		var $annuncio = $("#annuncio");
		$annuncio.fileinput(
				{
					theme : "fas",
					dropZoneEnabled : false,
					language : "it",
					allowedFileExtensions : [ "bmp", "jpeg", "jfif", "pjpeg",
							"pjp", "jpg", "gif", "png", "pdf" ],
					uploadUrl : "GestioneAnnunci",
					showUpload : false, // hide upload button
					showRemove : false, // hide remove button
					showCancel : false,
					overwriteInitial : false, // append files to initial
												// preview
					minFileCount : 1,
					maxFileCount : 1,
					uploadExtraData : function(previewId, index) {
						return {
							operazione : "caricaAllegato"
						};
					}
				}).on("filebatchselected", function(event, files) {
			$annuncio.fileinput("upload");
			$("#inviaAnnuncio").attr("disabled", true);
		}).on('fileuploaded', function(event, previewId, index, fileId) {

			$("#inviaAnnuncio").attr("disabled", false);
			$(".kv-file-remove").click(function() {
				$.post("GestioneAnnunci", {
					operazione : "rimuoviAllegato"
				}, function(data) {
					console.log("eliminazione effettuata")
				}).fail(function() {
					console.log("si è verificato un errore nella rimozione dell'allegato")
				})
			})
		})

	}
	/**
	 * funzione che esegue una chimata asincrona per poter prendere i dati del
	 * paziente dalla servlet e caricarli nella dashboard del paziente
	 */
	function caricaDatiPaziente() {
		$.post(
				"GestionePianoTerapeutico",
				{
					operazione : "visualizza",
					tipo : "asincrona"
				},
				function(data) {
					var diagnosi = $("#diagnosi")
					var farmaco = $("#farmaco")
					var dataFine = $("#data")
					$(diagnosi).html(data.diagnosi)
					$(farmaco).html(data.farmaco)
					$(dataFine).html(
							data.dataFineTerapia.day + "/"
									+ data.dataFineTerapia.month + "/"
									+ data.dataFineTerapia.year)

				}).fail(function() {
			console.log("si è verificato un errore nella visualizzazione del piano terapeutico")
		})
		$.post("GestioneAnnunci", {
			operazione : "visualizzaPersonali",
			tipo : "asincrona"
		}, function(data) {

			loadTabellaAnnunci(data,false)

		}).fail(function() {
			console.log("si è verificato un errore nella visualizzazione degli annunci")
		})
	}
	/**
	 * funzione che esegue una chimata asincrona per poter prendere i dati del
	 * medico dalla servlet e caricarli nella dashboard del medico
	 */
	function caricaDatiMedico() {

		$.post("GestioneMedico", {
			operazione : "VisualizzaPazientiSeguiti",
			tipo : "asincrona"
		}, function(data) {

			loadTabellaPazienti(data, false)
		}).fail(function() {
			console.log("si è verificato nella visualizzazione dei pazienti seguiti")
		})
		$.post("GestioneAnnunci", {
			operazione : "visualizzaPersonali",
			tipo : "asincrona"
		}, function(data) {

			loadTabellaAnnunci(data,true)
			$(".eliminaButtonAnnuncio").click(function() {
				$("#eliminazione").children().remove()
				var id = $(this).attr("id")
				console.log(id)
				console.log(data[id].idAnnuncio)
				addConfermaEliminazione("annuncio")
				$("#confermaEliminazione").click(function() {
					$.post("GestioneMedico", {
						operazione : "eliminaAnnuncio",
						identificatore:data[id].idAnnuncio
					}, function(data){
						window.location.href = "dashboard.jsp"
					}).fail(function(){
						console.log("Si è verificato un errore nella gestioneMedico")
					})
				})

			});

		}).fail(function() {
			console.log("si è verificato un errore nella visuallizzazione dei dati personali")
		})
	}

	/**
	 * funzione che esegue una chimata asincrona per poter prendere i dati del
	 * paziente e del medico dalla servlet e caricarli nella dashboard
	 * dell'admin
	 */
	function caricaDatiAdmin() {
		$.post("GestioneAmministratore", {
			operazione : "caricaMedPaz"
		}, function(data) {
			loadTabellaMedici(data[0])
			loadTabellaPazienti(data[1], true)

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
			console.log("si è verificato un errore nel caricamento dei dati dei medici e pazienti")
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
			console.log("si è verificato un errore nella rimozione dell'account del medico")
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
			console.log("si è verificato un errore nella rimozione dell'account del paziente")
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
	function loadTabellaAnnunci(annunci,medico) {
		var tabellaMedici = $("#tabellaAnnunci")
		console.log(annunci)
		var riga = ""
		var scarica = ""
		if(medico)
			{
			for (var i = 0; i < annunci.length; i++) {
				if(annunci[i].nomeAllegato !=null)
					{
						scarica+="<a href='GestioneAnnunci?operazione=generaDownload&id="+annunci[i].idAnnuncio+"' class='btn mr-sm-5 btn-info btn-icon-split'><span class='icon text-white-50'><i class='fas fa-download'></i></span><span class='text'>Scarica allegato</span></a>"
					}
				riga += " <div class='card mt-3'><div class='card-header py-3'><h6 class='m-0 font-weight-bold text-primary'>"+annunci[i].titolo + "</h6></div>"
						+ "<div class='card-body'><p>" + annunci[i].testo+ "</p></div>"
						+"<div class='col-12 mt-3 mb-3 d-flex justify-content-center'>"+scarica+"<button type='button' data-toggle='modal' data-target='#eliminaModal' id = '"
					+ i
					+ "' class='btn btn-danger btn-user eliminaButtonAnnuncio'>Elimina</button></div></div>"
					scarica = ""
			}

			}
		else
			{
			for (var i = 0; i < annunci.length; i++) {
				if(annunci[i].nomeAllegato !=null)
				{
					scarica+="<a href='GestioneAnnunci?operazione=generaDownload&id="+annunci[i].idAnnuncio+"' class='btn mr-sm-5 btn-info btn-icon-split'><span class='icon text-white-50'><i class='fas fa-download'></i></span><span class='text'>Scarica allegato</span></a>"
				}
				riga += " <div class='card mt-3'><div class='card-header py-3'>"
						+ "<h6 class='m-0 font-weight-bold text-primary'>"
						+ annunci[i].titolo + "</h6></div>"
						+ "<div class='card-body'><p>" + annunci[i].testo
						+ "</p></div><div class='col-12 mt-3 mb-3 d-flex justify-content-center'>"+scarica+"</div></div>"
						scarica = ""
			}

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
				+ "<h5 class='modal-title' id='exampleModalLabel'>Sicuro di voler eliminare "
				+ utente
				+ "?</h5>"
				+ "<button class='close' type='button' data-dismiss='modal'aria-label='Close'>"
				+ "<span aria-hidden='true'>×</span></button>"
				+ "</div><div class='modal-body'>Seleziona 'Elimina' qui sotto se sei pronto ad eliminare  "
				+ utente
				+ ".</div>"
				+ "<div class='modal-footer'><button class='btn btn-secondary' type='button' data-dismiss='modal'>Esci</button>"
				+ "<button class='btn btn-primary' data-dismiss='modal' id = 'confermaEliminazione'>Elimina</button></form></div></div></div></div>"
		$("#eliminazione").append(moduloDiConferma)

	}

	/**
	 * funzione che carica i dati del paziente nella tabella
	 */
	function loadTabellaPazienti(pazienti, bottoni) {
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
			if (bottoni) {
				riga += "<div class='col-12 mt-3 d-flex justify-content-center'><button type='button' id = '"
						+ i
						+ "' class='btn btn-primary btn-user mr-sm-5 modificaPazienteButton'>Modifica</button><button type='button' data-toggle='modal' data-target='#eliminaModal' id = '"
						+ i
						+ "' class='btn btn-danger btn-user eliminaButtonPaziente'>Elimina</button></div>"
			} else {
				riga += "<div class='col-12 mt-3 d-flex justify-content-center'><a href='GestionePianoTerapeutico?operazione=visualizza&CFPaziente="
						+ pazienti[i].codiceFiscale
						+ "' class='btn mr-sm-5 btn-info btn-icon-split'>"
						+ "<span class='icon text-white-50'><i class='fas fa-info-circle'></i></span><span class='text'>Piano terapeutico</span></a>"
						+ "<a href='GestioneParametri?operazione=visualizzaScheda&CFPaziente="
						+ pazienti[i].codiceFiscale
						+ "'class='btn btn-info btn-icon-split'>"
						+ "<span class='icon text-white-50'><i class='fas fa-info-circle'></i></span><span class='text'>Scheda parametri</span></a></div>"
			}
		}

		tabellaPazienti.append(riga)

	}

})(jQuery); // End of use strict

