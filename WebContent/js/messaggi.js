//rende cliccabili le righe della tabella in listaMessaggiView
jQuery(document).ready(function($) {
    $(".clickable-row").click(function() {
        window.location = $(this).data("href");
    });
});
	    
	    
//seleziona il destinatario del messaggio in caso si arrivi alla pagina di invio messaggi dal tasto Rispondi 	
function selezionaDestinatario (valore) {
 		if (valore != null && valore!= "") {
 	    	var element = document.getElementById("selectMedico");
 	    	element.value = valore;
		}
	}