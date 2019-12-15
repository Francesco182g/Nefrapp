/**
 * 
 * @author Antonio
 * 
 */

/**
 * Metodo che invia il form in base all'elemento cliccato
 * @param event utilizzato per ottenre il cf cliccato
 * 
 */
function submitListaPazienteForm(event) {
	$('#listaPazientiForm').append("<input type='hidden' name='flag' value='1'/>");   
	$('#listaPazientiForm').append("<input type='hidden' name='codiceFiscale' value='"+ event.id +"'/>");   
	$('#listaPazientiForm').submit();    
}	
