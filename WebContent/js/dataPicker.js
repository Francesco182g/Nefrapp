/**
 * 
 */

(function($) {
  "use strict"; // Start of use strict
  var sub = false
  $(document).ready(function(){
	 
	  		console.log($('#dataInizio'))
			$('#dataInizio').datepicker({
				'format': 'dd/mm/yyyy',
				'autoclose': true
			});
	  		$("#bottone").click(function(){
	  			console.log($('#dataInizio').val())
	  		})
		
  
  });
 
})(jQuery); // End of use strict

		