/**
 * @author Davide Bendetto Strianese
 */

$(document).ready(function(){
  	$("#isRegistrato").change(function(){
  		$("#nome").prop("readonly", true)
  		$("#cognome").prop("readonly", true)
  		$("#sessoM").hide()
  		$("#sessoF").hide()
  		$("#dataNascita").prop("readonly", true)
  		$("#residenza").prop("readonly", true)
  		$("#email").prop("readonly", true)
  		$("#password").prop("readonly", true)
	});
});
			
$(document).ready(function(){
	$("#notRegistrato").change(function(){
    	$("#nome").prop("readonly", false)
    	$("#cognome").prop("readonly", false)
    	$("#sessoM").show()
    	$("#sessoF").show()
    	$("#dataNascita").prop("readonly", false)
    	$("#residenza").prop("readonly", false)
    	$("#email").prop("readonly", false)
    	$("#password").prop("readonly", false)
		});
});