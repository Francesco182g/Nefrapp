<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@  taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

	<!-- session attribute -->
	<c:set var="utente" value='${sessionScope["utente"]}' />
	<c:set var="notifica" value="${param.notifica}"/>
	<c:choose>
		<c:when test="${not empty utente}">
			<c:set var="nome" value="${sessionScope['utente'].nome}"></c:set>
			<c:set var="cognome" value="${sessionScope['utente'].cognome}"></c:set>
		</c:when>

	</c:choose>
	<c:set var="isPaziente" value='${sessionScope["isPaziente"]}' />
	<c:set var="isMedico" value='${sessionScope["isMedico"]}' />
	<c:set var="isAmministratore"
		value='${sessionScope["isAmministratore"]}' />
	<c:set var="accessDone" value='${sessionScope["accessDone"]}' />
	<c:set var="notificheMessaggi"
		value='${sessionScope["notificheMessaggi"] }' />
	<c:set var="notificheAnnunci"
		value='${sessionScope["notificheAnnunci"] }' />
	<c:set var="notifichePianoTerapeutico"
		value='${sessionScope["notifichePianoTerapeutico"] }' />
	<c:set var="totaleNotifiche"
		value='${notificheMessaggi+notificheAnnunci+notifichePianoTerapeutico}' />
	<!-- Topbar -->
	<nav
		class="navbar navbar-expand navbar-light bg-white topbar mb-4 static-top shadow">
		<!-- Sidebar Toggle (Topbar) -->
          <button id="sidebarToggleTop" class="btn btn-link d-md-none rounded-circle mr-3">
            <i class="fa fa-bars"></i>
          </button>

		<!-- Dropdown Toggle (Hamburger) -->
	<!-- <div class="dropdown d-md-none" id="mobileMenu">
		<button class="d-md-none dropdown-toggle" aria-expanded="false"
			type="button" id="dropdownMenuButton" data-toggle="dropdown">
			<i class="fa fa-bars"></i>
		</button>
		
		<div class="dropdown-menu d-md-none" aria-labelledby="dropdownMenuButton">
			<c:choose>
				<c:when test='${isPaziente==true && accessDone==true}'>
					<div class="sidebar-heading">Area Personale</div>
					
					<a class="dropdown-item"
						href="./GestionePaziente?operazione=visualizzaProfilo">
						Dati Anagrafici</a>

					<div class="sidebar-heading">Area Medica</div>

					<a class="dropdown-item"
						href="./inserimentoParametriView.jsp">
						Inserimento Scheda Parametri</a>

					<a class="dropdown-item"
						href="./parametri?operazione=visualizzaScheda"> 
						Visualizza Schede Parametri</a>

					<a class="dropdown-item"
						href='./piano?operazione=visualizza&CFPaziente=${sessionScope["utente"].codiceFiscale}'>
						Visualizza Piano Terapeutico
					</a>

					<div class="sidebar-heading">Area Comunicazioni</div>

					<a class="dropdown-item"
						href="./messaggio?operazione=caricaDestinatariMessaggio"> 
						Invia Messaggio</a>

					<a class="dropdown-item"
						href="./messaggio?operazione=visualizzaElencoMessaggio">
						Leggi Messaggi</a>

					<a class="dropdown-item"
						href="./annuncio?operazione=visualizzaPersonali"> 
						Leggi Annunci</a>
				</c:when>
				
				<c:when test='${isMedico==true && accessDone==true}'>
					<div class="sidebar-heading">Area Personale</div>
					
					<a class="dropdown-item"
						href="./GestioneMedico?operazione=visualizzaProfilo">Dati Anagrafici
					</a>
					
					<div class="sidebar-heading">Area Medica</div>
					
					<a class="dropdown-item"
						href="./GestioneMedico?operazione=VisualizzaPazientiSeguiti">
						Visualizza Lista Pazienti</a>
	
					<a class="dropdown-item"
						href="./registraPazienteMedico.jsp"> Registra paziente
					</a>
					
					<div class="sidebar-heading">Area Comunicazioni</div>
					
					<a class="dropdown-item"
						href="./messaggio?operazione=caricaDestinatariMessaggio"> Invia Messaggio
					</a>
					
					<a class="dropdown-item"
						href="./messaggio?operazione=visualizzaElencoMessaggio"> 
						Leggi Messaggio</a>
	
					<a class="dropdown-item"
						href="./annuncio?operazione=caricaDestinatariAnnuncio">
						Pubblica Annuncio</a>
				</c:when>

				<c:when test='${isAmministratore==true && accessDone==true}'>
					<a class="dropdown-item" href="./registraMedico.jsp"> Registra
						Medico</a>

					<a class="dropdown-item"
						href="./resetPasswordAmministratoreView.jsp"> Modifica
						Password</a>
				</c:when>

				<c:otherwise>
					<a class="nav-link" href="login.jsp"> Login </a>
				</c:otherwise>
				
			</c:choose>
			
			<div class="sidebar-heading">Extra</div>

			<a class="dropdown-item" href="#"> Conosci il prodotto</a> 
			
			<a class="dropdown-item" href="./team.jsp"> Conosci il team! </a>
		
		</div>
	</div> -->

	<!-- Topbar Navbar -->
		<ul class="navbar-nav ml-auto">
		
			<c:choose>
				<c:when test="${accessDone}">
					<!-- Nav Item - Campanellina -->
					<li class="nav-item dropdown no-arrow mx-1"><a
						class="nav-link dropdown-toggle" href="#" id="alertsDropdown"
						role="button" data-toggle="dropdown" aria-haspopup="true"
						aria-expanded="false"> <i class="fas fa-bell fa-fw"></i> <span
							class="badge badge-danger badge-counter">${totaleNotifiche}</span>
					</a> <!-- Dropdown - Numero di notifiche e dropdown  -->
						<div
							class="dropdown-list dropdown-menu dropdown-menu-right shadow animated--grow-in"
							aria-labelledby="alertsDropdown">
							<h6 class="dropdown-header">Notifiche</h6>
							<a class="dropdown-item d-flex align-items-center"
								href="./messaggio?operazione=visualizzaElencoMessaggio">
								<div class="mr-3">
									<div class="icon-circle bg-primary">
										<i class="fas fa-file-alt text-white"></i>
									</div>
								</div>
								<div>
									<div class="small text-gray-500">Messaggi</div>
									<span class="font-weight-bold">Hai ${notificheMessaggi}
										messaggi non letti</span>
								</div>
							</a>
							<c:choose>
								<c:when test="${isPaziente}">
									<a class="dropdown-item d-flex align-items-center" href="./annuncio?operazione=">
										<div class="mr-3">
											<div class="icon-circle bg-success">
												<i class="fas fa-donate text-white"></i>
											</div>
										</div>
										<div>
											<div class="small text-gray-500">Annunci</div>
											<span class="font-weight-bold">Hai ${notificheAnnunci}
												annunci non letti</span>
										</div>
									</a>
									<a class="dropdown-item d-flex align-items-center"
										href='./piano?operazione=visualizza&CFPaziente=${sessionScope["utente"].codiceFiscale}'>
										<div class="mr-3">
											<div class="icon-circle bg-warning">
												<i class="fas fa-exclamation-triangle text-white"></i>
											</div>
										</div>
										<div>
											<div class="small text-gray-500">PianoTerapeutico</div>
											<c:choose>
												<c:when test="${notifichePianoTerapeutico==0}">
													<span class="font-weight-bold">Il tuo piano
														terapeutico non è stato aggiornato recentemente.</span>
												</c:when>
												<c:otherwise>
													<span class="font-weight-bold">Il tuo piano
														terapeutico è stato aggiornato.</span>
												</c:otherwise>
											</c:choose>
										</div>
									</a>
								</c:when>
							</c:choose>
							<a class="dropdown-item text-center small text-gray-500" href="#">Show
								All Alerts</a>
						</div></li>

					<li class="nav-item dropdown no-arrow"><a
						class="nav-link dropdown-toggle" href="#" id="userDropdown"
						role="button" data-toggle="dropdown" aria-haspopup="true"
						aria-expanded="false"> <span
							class="mr-2 d-none d-lg-inline text-gray-600 small">${nome}
								${cognome}</span> <img class="img-profile rounded-circle"
							src="./img/userIcon.png">
					</a> <!-- Including servlet --> <!-- Dropdown - User Information, azioni che potrebbe fare l'utente -->
						<div
							class="dropdown-menu dropdown-menu-right shadow animated--grow-in"
							aria-labelledby="userDropdown">
							<c:if test="${isPaziente}">
								<a class="dropdown-item"
									href="./GestionePaziente?operazione=visualizzaProfilo"> <i
									class="fas fa-user fa-sm fa-fw mr-2 text-gray-400"></i> Profilo
									Utente
								</a>
							</c:if>

							<c:if test="${isMedico}">
								<a class="dropdown-item"
									href="./GestioneMedico?operazione=visualizzaProfilo"> <i
									class="fas fa-user fa-sm fa-fw mr-2 text-gray-400"></i> Profilo
									Utente
								</a>
							</c:if>
							<div class="dropdown-divider"></div>
							<a class="dropdown-item" href="#" data-toggle="modal"
								data-target="#logoutModal"> <i
								class="fas fa-sign-out-alt fa-sm fa-fw mr-2 text-gray-400"></i>
								Logout
							</a>
						</div></li>
				</c:when>
				<c:otherwise>

					<li class="button"><a href="./login.jsp"
						class="btn btn-primary btn-user btn-block"> <i
							class="fas fa-sign-in-alt"></i> Login
					</a></li>

				</c:otherwise>
			</c:choose>

		</ul>

	</nav>


	<!-- End of Topbar -->
	<!-- Logout Modal-->
	<div class="modal fade" id="logoutModal" tabindex="-1" role="dialog"
		aria-labelledby="exampleModalLabel" aria-hidden="true">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="exampleModalLabel">Pronto ad
						uscire?</h5>
					<button class="close" type="button" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">×</span>
					</button>
				</div>
				<div class="modal-body">Seleziona "logout" qui sotto se sei
					pronto a terminare la tua sessione corrente.</div>
				<div class="modal-footer">
					<button class="btn btn-secondary" type="button"
						data-dismiss="modal">Cancel</button>
					<form action="./GestioneAccesso?operazione=logout" method="post">
						<button class="btn btn-primary">Logout</button>
					</form>
				</div>
			</div>
		</div>
	</div>
	
	
	<%@include file="../includes/avvisi.jsp"%>
	
					