//effettua la rimozione di annunci composti di solo allegato 
//nel caso si esca dalla pagina senza inviare l'annuncio
window.onbeforeunload = function() {
	$.ajax({
		type : 'POST',
		url : 'GestioneAnnunci',
		data : {
			'operazione' : 'rimuoviAnnuncioIncompleto'
		},
		success : function(data) {
			console.log("eliminazione effettuata")
		},
		async : false
	});

	return null;
};

// permette l'invio di un annuncio completo disabilitando l'evento soprastante
function abilitaInvio() {
	window.onbeforeunload = null;
}

$(document).ready(function() {
	var select = document.getElementById("selectPaziente");

	if (select.type == "select-multiple") {
		for (var i = 0; i < select.options.length; i++) {
			select.options[i].selected = true;
		}
	}
});
