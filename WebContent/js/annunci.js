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


function creaDownload(allegato, nomeAllegato) {
	const byteCharacters = atob(allegato);
	
	const byteNumbers = new Array(byteCharacters.length);
	for (let i = 0; i < byteCharacters.length; i++) {
	    byteNumbers[i] = byteCharacters.charCodeAt(i);
	}
	
	const byteArray = new Uint8Array(byteNumbers);	
	const blob = new Blob([byteArray], {type: "*"});
	
	blob.lastModifiedDate = new Date();
    blob.name = nomeAllegato;

	var url = window.URL.createObjectURL(blob);
	document.getElementById("download").href = url;
	document.getElementById("download").download = nomeAllegato;
}