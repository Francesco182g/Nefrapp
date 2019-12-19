/**
 * @author Eugenio
 * questo file js serve a creare i datapicker per ogni input che lo utilizza 
 */

(function($) {
  "use strict"; // Start of use strict
  $(document).ready(function(){
	 
	  	$('#dataDiNascita').datepicker({
			'format': 'dd/mm/yyyy',
			'autoclose': true
		});
		
	  	$('#dataInizio').datepicker({
			'format': 'dd/mm/yyyy',
			'autoclose': true
		});
	  		
	  	$('#dataFine').datepicker({
			'format': 'dd/mm/yyyy',
			'autoclose': true
		});
	  		
		
  
  });
 
})(jQuery); // End of use strict
