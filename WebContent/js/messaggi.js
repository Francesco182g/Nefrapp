//rende cliccabili le righe della tabella in listaMessaggiView
jQuery(document).ready(function($) {
    $(".clickable-row").click(function() {
        window.location = $(this).data("href");
    });
});

window.onbeforeunload = function() {
	$.ajax({
		  type: 'POST',
		  url: 'GestioneMessaggi',
		  data: {'operazione' : 'rimuoviMessaggioIncompleto'},
		  success: function(data) {
				console.log("eliminazione effettuata")
			},
		  async:false
		});
	
	return null;
};


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
	    
//seleziona il destinatario del messaggio in caso si arrivi alla pagina di invio messaggi dal tasto Rispondi 	
function selezionaDestinatario (valore) {
 		if (valore != null && valore!= "") {
 	    	var selMed = document.getElementById("selectMedico");
 	    	if (selMed != null && selMed != undefined)
 	    		selMed.value = valore;
 	    	
 	    	var selPaz = document.getElementById("selectPaziente");
 	    	if (selPaz != null && selPaz != undefined)
 	    		selPaz.value = valore;

		}
	}

