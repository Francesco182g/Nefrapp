/**
 * @author Eugenio
 */
(function($) {
  "use strict"; // Start of use strict
  var sub = false
  $(document).ready(function(){
	
	caricaDati();
	
  });
  
  /**
   * funzione che esegue una chimata asincrona per poter prensere i dati del paziente e del medico dalla 
   * servlet
   */
  function caricaDati(){
	  $.post("GestioneAmministratore",{operazione:"caricaMedPaz"},function(data){
		  console.log(data)
		  loadTabellaMedici(data[0])
		  loadTabellaPazienti(data[1])
		  
		  $(".eliminaButtonMedico").click(function(){
			  $("#eliminazione").children().remove()
				var id = $(this).attr("id")
				var cf = $(this).parents().find(".cfMed")[id].firstChild.data
				addConfermaEliminazione("medico")
				$("#confermaEliminazione").click(function(){
					eliminaMedico(cf)
				})
				
			});
		  $(".eliminaButtonPaziente").click(function(){
			  $("#eliminazione").children().remove()
				var id = $(this).attr("id")
				var cf = $(this).parents().find(".cfPaz")[id].firstChild.data
				addConfermaEliminazione("paziente")
				$("#confermaEliminazione").click(function(){
					eliminaPaziente(cf)
				})
				
			});
		  $(".modificaMedicoButton").click(function(){
			  	
				var id = $(this).attr("id")
				var medico = JSON.stringify(data[0][id])
				sessionStorage.setItem("modMedico",medico)
				window.location.href="ModificaAccountMedicoView.jsp"
				
			});
		  $(".modificaPazienteButton").click(function(){
			  	
				var id = $(this).attr("id")
				var paziente = JSON.stringify(data[1][id])
				sessionStorage.setItem("modPaziente",paziente)
				window.location.href="ModificaAccountPazienteView.jsp"
				
			});
	  }).fail(function(){alert("si è verificato un errore")});
  }
  
  /**
   * Funzione che manda una richiesta asincrona per eliminare il medico dal database
   */
  function eliminaMedico(cf){
	  $.post("GestioneAmministratore",{operazione:"rimuoviAccount",codiceFiscale:cf,tipo:"medico"},function(data){
		  location.reload();
	  }).fail(function(){alert("si è verificato un errore")});
  }
  /**
   * Funzione che manda una richiesta asincrona per eliminare il paziente dal database 
   */
  function eliminaPaziente(cf){
	  $.post("GestioneAmministratore",{operazione:"rimuoviAccount",codiceFiscale:cf,tipo:"paziente"},function(data){
		  location.reload();
	  }).fail(function(){alert("si è verificato un errore")});
  }
  /**
   * funzione che carica i dati del medico nella tabella
   */
  function loadTabellaMedici(medici){
	  var tabellaMedici = $("#tabellaMedici")
	  var riga = ""
	  for(var i = 0;i < medici.length;i++)
	  {
		  	riga +="<li class='list-group-item mt-3'><div class='row table-responsive'><div class='col-12 col-sm-6 col-md-9 text-center text-sm-left'><table>" 
		  	riga +="<tr><td><p>Nome: </p></td>"
		  	riga +="<td><p>"+medici[i].nome+"</p></td></tr>"
		  	riga +="<tr><td><p>Cognome: </p></td>"
			riga +="<td><p>"+medici[i].cognome+"</p></td></tr>"
			riga +="<tr><td><p>Codice Fiscale: </p></td>"
			riga +="<td><p class='cfMed'>"+medici[i].codiceFiscale+"</p></td></tr>"
			riga +="<tr><td><p>Email: </p></td>"
			riga +="<td><p>"+medici[i].email+"</p></td></tr>"
			riga+="</table></div><div class='col-12 mt-3 d-flex justify-content-center'><button type='button' id = '"+i+"' class='btn btn-primary btn-user mr-sm-5 modificaMedicoButton'>Modifica</button><button type='button' data-toggle='modal' data-target='#eliminaModal' id = '"+i+"' class='btn btn-danger btn-user eliminaButtonMedico'>Elimina</button></div>"
	  }

	  tabellaMedici.append(riga)
	  
  }

  
  /**
   * funzione che permette di aggiungere una finestra di conferma quando si preme il tasto elimina
   */
  function addConfermaEliminazione(utente){
		  var moduloDiConferma = "<div class='modal fade' id='eliminaModal' tabindex='-1' role='dialog' aria-labelledby='exampleModalLabel' aria-hidden='true'>" +
	 			"<div class='modal-dialog' role='document'>" +
	 				"<div class='modal-content'>" +
	 					"<div class='modal-header'>" +
	 						"<h5 class='modal-title' id='exampleModalLabel'>Sicuro di voler eliminare il "+utente+"?</h5>" +
	 							"<button class='close' type='button' data-dismiss='modal'aria-label='Close'>" +
	 								"<span aria-hidden='true'>×</span></button>" +
	 									"</div><div class='modal-body'>Seleziona 'Elimina' qui sotto se sei pronto ad eliminare il "+utente+".</div>" +
	 									"<div class='modal-footer'><button class='btn btn-secondary' type='button' data-dismiss='modal'>Esci</button>" +
	 									"<button class='btn btn-primary' data-dismiss='modal' id = 'confermaEliminazione'>Elimina</button></form></div></div></div></div>"
	 $("#eliminazione").append(moduloDiConferma)
	
  }
  
 
  /**
   * funzione che carica i dati del paziente nella tabella
   */
  function loadTabellaPazienti(pazienti){
	  var tabellaPazienti = $("#tabellaPazienti")
	  var riga = ""
	  for(var i = 0;i < pazienti.length;i++)
	  {
		  	riga +="<li class='list-group-item mt-3'><div class='row table-responsive'><div class='col-12 col-sm-6 col-md-9 text-center text-sm-left tab'><table>" 
		  	riga +="<tr><td><p>Nome: </p></td>"
		  	riga +="<td><p>"+pazienti[i].nome+"</p></td></tr>"
		  	riga +="<tr><td><p>Cognome: </p></td>"
			riga +="<td><p>"+pazienti[i].cognome+"</p></td></tr>"
			riga +="<tr><td><p>Codice Fiscale: </p></td>"
			riga +="<td><p class='cfPaz'>"+pazienti[i].codiceFiscale+"</p></td></tr>"
			riga +="<tr><td><p>Email: </p></td>"
			riga +="<td><p>"+pazienti[i].email+"</p></td></tr>"
			riga+="</table></div><div class='col-12 mt-3 d-flex justify-content-center'><button type='button' id = '"+i+"' class='btn btn-primary btn-user mr-sm-5 modificaPazienteButton'>Modifica</button><button type='button' data-toggle='modal' data-target='#eliminaModal' id = '"+i+"' class='btn btn-danger btn-user eliminaButtonPaziente'>Elimina</button></div>"
	  }

	  tabellaPazienti.append(riga)
	  
  }
  
})(jQuery); // End of use strict

	