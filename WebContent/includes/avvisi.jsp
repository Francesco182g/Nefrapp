<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@  taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!-- login -->
<c:if test="${notifica == 'datiLoginErrati'}">
	<div class="alert text-center alert-warning alert-dismissible fade show"
		role="alert">
		I dati da te inseriti non sono corretti!<br>
		<button type="button" class="close" data-dismiss="alert"
			aria-label="Close">
			<span aria-hidden="true">&times;</span>
		</button>
	</div>
</c:if>
<c:if test="${notifica == 'accountDisattivato'}">
	<div class="alert text-center alert-success alert-dismissible fade show"
		role="alert">
		Il tuo account è stato disattivato!<br>
		<button type="button" class="close" data-dismiss="alert"
			aria-label="Close">
			<span aria-hidden="true">&times;</span>
		</button>
	</div>
</c:if>
<!-- login -->



<!-- dashboard -->
<c:if test="${notifica == 'ModificaMedRiuscita'}">
	<div
		class="alert text-center alert-success alert-dismissible fade show"
		role="alert">
		L'account del medico è stato modificato con successo
		<button type="button" class="close" data-dismiss="alert"
			aria-label="Close">
			<span aria-hidden="true">&times;</span>
		</button>
	</div>
</c:if>

<c:if test="${notifica == 'ModificaPazRiuscita'}">
	<div
		class="alert text-center alert-success alert-dismissible fade show"
		role="alert">
		L'account del paziente è stato modificato con successo
		<button type="button" class="close" data-dismiss="alert"
			aria-label="Close">
			<span aria-hidden="true">&times;</span>
		</button>
	</div>
</c:if>

<c:if test="${notifica == 'messaggioInviato'}">
	<div class="alert text-center alert-success alert-dismissible fade show"
		role="alert">
		Messaggio inviato con successo!<br>
		<button type="button" class="close" data-dismiss="alert"
			aria-label="Close">
			<span aria-hidden="true">&times;</span>
		</button>
	</div>
</c:if>

<c:if test="${notifica == 'annuncioInviato'}">
	<div class="alert text-center alert-success alert-dismissible fade show"
		role="alert">
		Annuncio inviato con successo!<br>
		<button type="button" class="close" data-dismiss="alert"
			aria-label="Close">
			<span aria-hidden="true">&times;</span>
		</button>
	</div>
</c:if>

<c:if test="${notifica == 'resetSuccesso'}">
	<div class="alert text-center alert-primary fade show" role="alert">
		La tua password è stata reimpostata con successo!<br> Puoi
		accedere con la tua nuova password cliccando <a href="./login.jsp"><strong>qui</strong></a>
		<button type="button" class="close" data-dismiss="alert"
			aria-label="Close">
			<span aria-hidden="true">&times;</span>
		</button>
	</div>
</c:if>

<c:if test="${notifica == 'identificazioneSuccesso'}">
	<div class="alert text-center alert-primary fade show" role="alert">
		La richiesta di reset password è stata inviata con successo!<br>
		Se sei un medico, troverai il link per effettuare il reset nella tua
		casella di posta elettronica,<br> se sei un paziente, sarai
		contattato dall'amministratore di Nefrapp per ricevere la tua nuova
		password
		<button type="button" class="close" data-dismiss="alert"
			aria-label="Close">
			<span aria-hidden="true">&times;</span>
		</button>
	</div>
</c:if>
<c:if test="${notifica == 'annuncioRimosso'}">
	<div class="alert text-center alert-success alert-dismissible fade show"
		role="alert">
		Annuncio rimosso con successo!<br>
		<button type="button" class="close" data-dismiss="alert"
			aria-label="Close">
			<span aria-hidden="true">&times;</span>
		</button>
	</div>
</c:if>
<c:if test="${notifica == 'medicoRimosso'}">
	<div class="alert text-center alert-success alert-dismissible fade show"
		role="alert">
		Medico rimosso con successo!<br>
		<button type="button" class="close" data-dismiss="alert"
			aria-label="Close">
			<span aria-hidden="true">&times;</span>
		</button>
	</div>
</c:if>
<c:if test="${notifica == 'pazienteRimosso'}">
	<div class="alert text-center alert-success alert-dismissible fade show"
		role="alert">
		Paziente rimosso con successo!<br>
		<button type="button" class="close" data-dismiss="alert"
			aria-label="Close">
			<span aria-hidden="true">&times;</span>
		</button>
	</div>
</c:if>
<c:if test="${notifica == 'ModificaAdmnRiuscita'}">
	<div class="alert text-center alert-success alert-dismissible fade show"
		role="alert">
		Password dell'amministratore modificata con successo!<br>
		<button type="button" class="close" data-dismiss="alert"
			aria-label="Close">
			<span aria-hidden="true">&times;</span>
		</button>
	</div>
</c:if>
<!-- dashboard -->



<!-- modificaAccountMedicoView -->
<c:if test="${notifica == 'ParamErr'}">
	<div
		class="alert text-center alert-warning alert-dismissible fade show"
		role="alert">
		Uno dei parametri non è corretto si prega di riattivare
		javaScript per attivare i controlli<br>
		<button type="button" class="close" data-dismiss="alert"
			aria-label="Close">
			<span aria-hidden="true">&times;</span>
		</button>
	</div>
</c:if>

<c:if test="${notifica == 'PassErr'}">
	<div
		class="alert text-center alert-warning alert-dismissible fade show"
		role="alert">
		Le password non corrispondono si prega di riattivare javaScript
		per attivare i controlli<br>
		<button type="button" class="close" data-dismiss="alert"
			aria-label="Close">
			<span aria-hidden="true">&times;</span>
		</button>
	</div>

</c:if>
<!-- modificaAccountMedicoView -->



<!-- monitoraggioParametriView -->
<c:if test="${notifica == 'schedaInserita'}">
	<div class="alert alert-success alert-dismissible fade show"
		role="alert">
		Scheda parametri inserita con successo!
		<button type="button" class="close" data-dismiss="alert"
			aria-label="Close">
			<span aria-hidden="true">&times;</span>
		</button>
	</div>
</c:if>
<!-- monitoraggioParametriView -->


<!-- profilo -->
<c:if test="${notifica == 'modificaEffettuata'}">
	<div class="alert text-center alert-success alert-dismissible fade show"
		role="alert">Le modifiche sono state effettuate con successo!<br>
		<button type="button" class="close" data-dismiss="alert"
			aria-label="Close">
			<span aria-hidden="true">&times;</span>
		</button>
	</div>
</c:if>
<!-- profilo -->
