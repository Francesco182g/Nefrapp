/**
 * @author Sara
 */
(function($) {
  "use strict"; // Start of use strict
  var sub = false
  $(document).ready(function(){
	  
	  /**
	   * Funzione che blocca il submit
	   */
	  $(document).submit(function(){
			  return sub; 
	  });
	
	  /**
	   * Funzione che permette di eseguire la registrazione del medico fa prima dei controlli e se nella pagina è tutto ok invia i dati alla servlet
	   */
	  inserimentoScheda();
	});
	
  /**
   * Funzione che permette di eseguire la registrazione del medico fa prima dei controlli e se nella pagina è tutto ok invia i dati alla servlet
   */
	function inserimentoScheda(){
		$("#inserimentoSchedaButton").click(function(){
			var valid = checkValidity()
			if (!valid [0])
				{
					customAlert(valid[1])
				}
			else
				{
					sub = true;
					$(document).submit();
				}
			
		});
	}
	function checkValidity() {
		var valido=[true];
		var expPeso=new RegExp("^[1-9]{1}[0-9]{1,2}(\,\d{1,2})?$");
		var expPaMax=RegExp("^[1-9]{1}[0-9]{1,2}$");
		var expPaMin=RegExp("^[1-9]{1}[0-9]{1,2}$");
		var expScaricoIniziale=RegExp("^[-+]{1}[1-9]{1}[0-9]{1,3}$|^$|^[1-9]{1}[0-9]{1,3}$");
		var expUF=RegExp("^[-+]{1}[1-9]{1}[0-9]{1,3}$|^[1-9]{1}[0-9]{1,3}$");
		var expTempoSosta=RegExp("^[1-9]{1}[0-9]{0,1}$");
		var expScarico=RegExp("^[1-9]{1}[0-9]{1,3}$");
		var expCarico=RegExp("^[1-9]{1}[0-9]{2,3}$")
		var peso = $("#Peso").val();
		var pamax = $("#PaMax").val();
		var pamin = $("#PaMin").val();
		var scaricoIniziale = $("#ScaricoIniziale").val();
		var uf = $("#UF").val();
		var tempoSosta = $("#TempoSosta").val();
		var scarico=$("#Scarico").val();
		var carico=$("#Carico").val();
		console.log(expPeso.test(peso))
		
		if (!expPeso.test(peso)||peso>150||peso<30)
			valido=[false,"formato peso non valido"];
		
		else if (!expPaMax.test(pamax)||pamax<79||pamax>221)
			valido=[false,"formato pressione massima non valido"];
		
		else if (!expPaMin.test(pamin)||pamin<40||pamin>130)
			valido=[false,"formato pressione minima non valido"];
		
		else if ((!expScaricoIniziale.test(scaricoIniziale))&&(scaricoIniziale<-1000||scaricoIniziale>3000))
			valido=[false,"formato scarico iniziale non valido"];
		
		else if (!expUF.test(uf)||uf<-1000||uf>1500)
			valido=[false,"formato UF non valido"];
		
		else if (!expTempoSosta.test(tempoSosta)||tempoSosta>24||tempoSosta<1)
			valido=[false,"formato tempo di sosta non valido"];
		
		else if (!expCarico.test(carico)||carico>3000||carico<500)
			valido=[false,"formato carico non valido"];
		
		else if (!expScarico.test(scarico)||scarico>3500||scarico<1)
			valido=[false,"formato scarico non valido"];
		
		return valido;
	}
	function customAlert(msg)
	  {
		  $.alert({
			    title:"",
			    content: msg,
			    type: 'red',
			    typeAnimated: true,
			    autoClose: 'ok|6000',
			    
			});
	  }
  

})(jQuery); // End of use strict

	