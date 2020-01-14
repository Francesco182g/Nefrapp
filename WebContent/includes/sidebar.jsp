
<%@  taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!-- Sidebar -->
<ul
	class="navbar-nav bg-gradient-primary sidebar sidebar-dark accordion"
	id="accordionSidebar">

	<!-- Sidebar - Brand -->
	<a
		class="sidebar-brand d-flex align-items-center justify-content-center"
		href="./dashboard.jsp">
		<div class="sidebar-brand-icon rotate-n-15">
			<i class="fas fa-laugh-wink"></i>
		</div>
		<div class="sidebar-brand-text mx-3">
			Nefrapp <sup>10</sup>
		</div>
	</a>
	<br>
	<br>

	<!-- Nav Item - Dashboard -->


	<c:choose>
		<c:when test='${isPaziente==true && accessDone==true}'>

			<div class="sidebar-heading">Area Personale</div>
			<li class="nav-item"><a class="nav-link"
				href="./GestionePaziente?operazione=visualizzaProfilo"> <i
					class="fas fa-fw fa-tachometer-alt"></i> <span>Dati
						Anagrafici</span></a></li>
			<hr class="sidebar-divider">
			<div class="sidebar-heading">Area Medica</div>
			<li class="nav-item"><a class="nav-link"
				href="./inserimentoParametriView.jsp"> <i
					class="fas fa-fw fa-tachometer-alt"></i> <span>Inserimento
						Scheda Parametri </span></a></li>

			<li class="nav-item"><a class="nav-link"
				href="./parametri?operazione=visualizzaScheda"> <i
					class="fas fa-fw fa-tachometer-alt"></i> <span>Visualizza
						Schede Parametri</span></a></li>

			<li class="nav-item"><a class="nav-link"
				href='./piano?operazione=visualizza&CFPaziente=${sessionScope["utente"].codiceFiscale}'>
					<i class="fas fa-fw fa-tachometer-alt"></i> <span>Visualizza
						Piano Terapeutico</span>
			</a></li>
			<hr class="sidebar-divider">
			<div class="sidebar-heading">Area Comunicazioni</div>
			<li class="nav-item"><a class="nav-link"
				href="./messaggio?operazione=caricaDestinatariMessaggio"> <i
					class="fas fa-fw fa-tachometer-alt"></i> <span>Invia
						Messaggio</span></a></li>
			<li class="nav-item"><a class="nav-link"
				href="./messaggio?operazione=visualizzaElencoMessaggio"> <i
					class="fas fa-fw fa-tachometer-alt"></i> <span>Leggi
						Messaggi</span></a></li>
			<li class="nav-item"><a class="nav-link"
				href="./annuncio?operazione=visualizzaPersonali"> <i
					class="fas fa-fw fa-tachometer-alt"></i> <span>Leggi Annunci</span></a></li>
		</c:when>

		<c:when test='${isMedico==true && accessDone==true}'>

			<div class="sidebar-heading">Area Personale</div>
			<li class="nav-item"><a class="nav-link"
				href="./GestioneMedico?operazione=visualizzaProfilo"> <i
					class="fas fa-fw fa-tachometer-alt"></i> <span>Dati
						Anagrafici</span></a></li>
						<hr class="sidebar-divider">
			<div class="sidebar-heading">Area Medica</div>
			<li class="nav-item"><a class="nav-link"
				href="./GestioneMedico?operazione=VisualizzaPazientiSeguiti"> <i
					class="fas fa-fw fa-tachometer-alt"></i> <span>Visualizza
						Lista Pazienti</span></a></li>

			<li class="nav-item"><a class="nav-link"
				href="./registraPazienteMedico.jsp"> <i
					class="fas fa-fw fa-tachometer-alt"></i> <span>Registra
						paziente</span></a></li>
			<hr class="sidebar-divider">
			<div class="sidebar-heading">Area Comunicazioni</div>
			<li class="nav-item"><a class="nav-link"
				href="./messaggio?operazione=caricaDestinatariMessaggio"> <i
					class="fas fa-fw fa-tachometer-alt"></i> <span>Invia
						Messaggio</span>
			</a></li>
			<li class="nav-item"><a class="nav-link"
				href="./messaggio?operazione=visualizzaElencoMessaggio"> <i
					class="fas fa-fw fa-tachometer-alt"></i> <span>Leggi
						Messaggio</span></a></li>

			<li class="nav-item"><a class="nav-link"
				href="./annuncio?operazione=caricaDestinatariAnnuncio"> <i
					class="fas fa-fw fa-tachometer-alt"></i> <span>Pubblica
						Annuncio</span></a></li>

		</c:when>
		<c:when test='${isAmministratore==true && accessDone==true}'>

			<li class="nav-item"><a class="nav-link"
				href="./registraMedico.jsp"> <i
					class="fas fa-fw fa-tachometer-alt"></i> <span>Registra
						Medico</span></a></li>
			<li class="nav-item"><a class="nav-link"
				href="./resetPasswordAmministratoreView.jsp"> <i
					class="fas fa-fw fa-tachometer-alt"></i> <span>Modifica
						Password</span></a></li>
		</c:when>
		<c:otherwise>
			<li class="nav-item"><a class="nav-link" href="login.jsp"> <i
					class="fas fa-fw fa-tachometer-alt"></i> <span>Login</span>
			</a></li>
		</c:otherwise>
	</c:choose>
	<hr class="sidebar-divider">
	<div class="sidebar-heading">Extra</div>
	<!--  <li class="nav-item"><a class="nav-link" href="#"> <i
			class="fas fa-fw fa-tachometer-alt"></i> <span>Conosci il
				prodotto</span></a></li>-->
	<li class="nav-item"><a class="nav-link" href="./team.jsp"> <i
			class="fas fa-fw fa-tachometer-alt"></i> <span>Conosci il
				team!</span>
	</a></li>

	<!-- Divider -->


	<!-- Heading -->


	<!-- Nav Item - Pages Collapse Menu -->

	<!-- Nav Item - Utilities Collapse Menu -->


	<!-- Divider -->

	<!-- Heading -->

	<!-- Nav Item - Pages Collapse Menu -->
	<!-- 		<li class="nav-item active"><a class="nav-link" href="#" -->
	<!-- 			data-toggle="collapse" data-target="#collapsePages" -->
	<!-- 			aria-expanded="true" aria-controls="collapsePages"> <i -->
	<!-- 				class="fas fa-fw fa-folder"></i> <span>Pages</span> -->
	<!-- 		</a> -->
	<!-- 			<div id="collapsePages" class="collapse show" -->
	<!-- 				aria-labelledby="headingPages" data-parent="#accordionSidebar"> -->
	<!-- 				<div class="bg-white py-2 collapse-inner rounded"> -->
	<!-- 					<h6 class="collapse-header">Login Screens:</h6> -->
	<!-- 					<a class="collapse-item" href="login.jsp">Login</a> -->
	<!-- 					dovrebbe essere visualizzabile solo al medico e all'admin 
<!--                     <a class="collapse-item" href="register.html">Register</a>-->
	<!-- 					<a class="collapse-item" href="forgot-password.html">Forgot -->
	<!-- 						Password</a> -->
	<!-- 					<div class="collapse-divider"></div> -->
	<!-- 					<h6 class="collapse-header">Other Pages:</h6> -->
	<!-- 					<a class="collapse-item" href="404.html">404 Page</a> <a -->
	<!-- 						class="collapse-item active" href="blank.html">Blank Page</a> -->
	<!-- 				</div> -->
	<!-- 			</div></li> -->

	<!-- Nav Item - Charts -->


	<!-- Nav Item - Tables -->


	<!-- Divider -->
	<hr class="sidebar-divider d-none d-md-block">

	<!-- Sidebar Toggler (Sidebar) -->
	<!-- 		<div class="text-center d-none d-md-inline"> -->
	<!-- 			<button class="rounded-circle border-0" id="sidebarToggle"></button> -->
	<!-- 		</div> -->
	<!-- Sidebar Toggler (Sidebar) -->
	<div class="text-center d-none d-md-inline">
		<button class="rounded-circle border-0" id="sidebarToggle"></button>
	</div>

</ul>


<!-- End of Sidebar -->