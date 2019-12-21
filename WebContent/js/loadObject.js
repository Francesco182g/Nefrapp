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
			
		  loadTabellaMedici(data[0])
		  loadTabellaPazienti(data[1])
		  
		  $(".eliminaButton").click(function(){
			  	
				console.log(this)
				console.log($(this).attr("id"))
				var id = $(this).attr("id")
				var cf = $(this).parents().find(".cf")[id].firstChild.data
				console.log(cf)
				console.log("premuto il tasto di eliminazione")
				eliminaMedico(cf)
			});
	  }).fail(function(){alert("si è verificato un errore")});
  }
  
  function eliminaMedico(cf){
	  $.post("GestioneMedico",{operazione:"elimina",codiceFiscale:cf},function(data){
			
		  console.log("eliminazione avvenuta con successo")
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
			riga +="<td><p class='cf'>"+medici[i].codiceFiscale+"</p></td></tr>"
			riga +="<tr><td><p>Email: </p></td>"
			riga +="<td><p>"+medici[i].email+"</p></td></tr>"
			riga+="</table></div><div class='col-12 mt-3 d-flex justify-content-center'><button type='button' class='btn btn-primary btn-user mr-sm-5'>Modifica</button><button type='button' id = '"+i+"'class='btn btn-danger btn-user eliminaButton'>Elimina</button></div>"
	  }

	  tabellaMedici.append(riga)
	  
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
			riga +="<td><p>"+pazienti[i].codiceFiscale+"</p></td></tr>"
			riga +="<tr><td><p>Email: </p></td>"
			riga +="<td><p>"+pazienti[i].email+"</p></td></tr>"
			riga+="</table></div><div class='col-12 mt-3 d-flex justify-content-center'><button type='button' class='btn btn-primary btn-user mr-sm-5'>Modifica</button><button type='button' class='btn btn-danger btn-user'>Elimina</button></div>"
	  }

	  tabellaPazienti.append(riga)
	  
  }
  function eliminazione(){
	  console.log("premuta eliminazione")
  }
})(jQuery); // End of use strict

	