window.onbeforeunload = function() {
	$.ajax({
		  type: 'POST',
		  url: 'GestioneAnnunci',
		  data: {'operazione' : 'rimuoviAnnuncioIncompleto'},
		  success: function(data) {
				console.log("eliminazione effettuata")
			},
		  async:false
		});
	
	return null;
};

$( "#inviaAnnuncio" ).click(function() {
	var form = document.getElementById("formAnnunci")
	
	if (form.oggetto.value=="" || form.testo.value=="" || 
		form.selectPaziente.value == undefined ||
		form.selectPaziente.value == "") {	
		
		return;
	} else {
		window.onbeforeunload = null;
		form.submit();
	}
});