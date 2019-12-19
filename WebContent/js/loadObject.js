/**
 * @author Eugenio
 */
(function($) {
  "use strict"; // Start of use strict
  var sub = false
  $(document).ready(function(){
	  $.post("GestioneAmministratore",{operazione:"caricaMedPaz"},function(data){
		  console.log(data)
		  
		  loadTabellaMedici(data[0])
		
	  }).fail(function(){alert("si è verificato un errore")});
	});
  
  function loadTabellaMedici(medici){
	  var tabellaMedici = $("#tabMedici")
	  console.log(tabellaMedici)
	  var riga = ""
	  for(var i = 0;i < medici.length;i++)
		  {
		  	riga +="<tr><td>"+medici[i].codiceFiscale+"</td>"
		  	riga +="<td>"+medici[i].nome+"</td>"
		  	riga +="<td>"+medici[i].cognome+"</td>"
		  	riga +="<td>"+medici[i].email+"</td>"
		  	riga +="<td>"+Date(medici[i].dataDiNascita)+"</td>"
		  	riga +="<td>"+medici[i].luogoDiNascita+"</td>"
		  	riga +="<td>"+medici[i].residenza+"</td>"
		  	riga +="<td>"+medici[i].sesso+"</td></tr>"
		  }
	  console.log(riga)
	  tabellaMedici.append(riga)
	  
  }
 
})(jQuery); // End of use strict

	