/**
 * @author Eugenio
 */
(function($) {
  "use strict"; // Start of use strict
  var sub = false
  $(document).ready(function(){
	  $.post("GestioneAmministratore",{operazione:"caricaMedPaz"},function(data){
		  console.log(data)
		
	  }).fail(function(){alert("si è verificato un errore")});
	});
 
})(jQuery); // End of use strict

	