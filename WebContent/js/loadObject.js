/**
 * @author Eugenio
 */
(function($) {
  "use strict"; // Start of use strict
  var sub = false
  $(document).ready(function(){
	
	caricaDati();
  });
  
  function caricaDati(){
	  $.post("GestioneAmministratore",{operazione:"caricaMedPaz"},function(data){
			
		  loadTabellaMedici(data[0])
		  loadTabellaPazienti(data[1])
		  
		
	  }).fail(function(){alert("si è verificato un errore")});
  }
  
  
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
			riga +="<td><p>"+medici[i].codiceFiscale+"</p></td></tr>"
			riga +="<tr><td><p>Email: </p></td>"
			riga +="<td><p>"+medici[i].email+"</p></td></tr>"
			riga+="</table></div><div class='col-12 mt-3 d-flex justify-content-center'><button type='button' class='btn btn-primary btn-user mr-sm-5'>Modifica</button><button type='button' class='btn btn-danger btn-user '>Elimina</button></div>"
	  }

	  tabellaMedici.append(riga)
	  
  }
  function loadTabellaPazienti(pazienti){
	  var tabellaPazienti = $("#tabellaPazienti")
	  var riga = ""
	  for(var i = 0;i < pazienti.length;i++)
	  {
		  	riga +="<li class='list-group-item mt-3'><div class='row table-responsive'><div class='col-12 col-sm-6 col-md-9 text-center text-sm-left'><table>" 
		  	riga +="<tr><td><p>Nome: </p></td>"
		  	riga +="<td><p>"+pazienti[i].nome+"</p></td></tr>"
		  	riga +="<tr><td><p>Cognome: </p></td>"
			riga +="<td><p>"+pazienti[i].cognome+"</p></td></tr>"
			riga +="<tr><td><p>Codice Fiscale: </p></td>"
			riga +="<td><p>"+pazienti[i].codiceFiscale+"</p></td></tr>"
			riga +="<tr><td><p>Email: </p></td>"
			riga +="<td><p>"+pazienti[i].email+"</p></td></tr>"
			riga+="</table></div><div class='col-12 mt-3 d-flex justify-content-center'><button type='button' class='btn btn-primary btn-user mr-sm-5'>Modifica</button><button type='button' class='btn btn-danger btn-user eliminaButton'>Elimina</button></div>"
	  }

	  tabellaPazienti.append(riga)
	  
  }
 
})(jQuery); // End of use strict

	