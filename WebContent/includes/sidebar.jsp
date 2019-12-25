<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">
<meta name="description">
<meta name="author">


<!-- Custom fonts for this template-->
<link href="./vendor/fontawesome-free/css/all.min.css" rel="stylesheet"
	type="text/css">
<link
	href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i"
	rel="stylesheet">

<!-- Custom styles for this template-->
<link href="./css/sb-admin-2.min.css" rel="stylesheet">
</head>
<%@  taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<body id="page-top">

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

		<!-- Divider -->
		<hr class="sidebar-divider my-0">

		<!-- Nav Item - Dashboard -->

		
		<c:choose>
			<c:when test='${isPaziente==true && accessDone==true}'>

				<li class="nav-item"><form action="./GestioneAccesso" method="post">
					<input type="hidden" name="operazione" value="logout">
					<button class="btn btn-primary">Logout immediato (temp. per testing)</button>
				</form></li>

				<li class="nav-item"><a class="nav-link" href="./profilo.jsp"> <i
						class="fas fa-fw fa-tachometer-alt"></i> <span>Dati
							Anagrafici</span></a></li>

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

				<li class="nav-item"><a class="nav-link"
					href="./messaggio?operazione=caricaDestinatariMessaggio"> <i
						class="fas fa-fw fa-tachometer-alt"></i> <span>Invia
							Messaggio</span></a></li>
				<li class="nav-item"><a class="nav-link"
					href="./messaggio?operazione=visualizzaElencoMessaggio"> <i
						class="fas fa-fw fa-tachometer-alt"></i> <span>Leggi
							Messaggi</span></a></li>
			</c:when>

			<c:when test='${isMedico==true && accessDone==true}'>

				<li class="nav-item"><form action="./GestioneAccesso" method="post">
					<input type="hidden" name="operazione" value="logout">
					<button class="btn btn-primary">Logout immediato
						(temp. per testing)</button>
				</form></li>

				<li class="nav-item"><a class="nav-link" href="/ModificaAccountMedicoView.jsp"> <i
						class="fas fa-fw fa-tachometer-alt"></i> <span>Dati
							Anagrafici</span></a></li>

				<li class="nav-item"><a class="nav-link"
					href="./GestioneMedico?operazione=VisualizzaPazientiSeguiti"> <i
						class="fas fa-fw fa-tachometer-alt"></i> <span>Visualizza
							Lista Pazienti</span></a></li>

				<li class="nav-item"><a class="nav-link"
					href="./registraPazienteMedico.jsp"> <i
						class="fas fa-fw fa-tachometer-alt"></i> <span>Registra
							paziente</span></a></li>

				<li class="nav-item"><a class="nav-link"
					href="./messaggio?operazione=caricaDestinatariMessaggio">
						<i class="fas fa-fw fa-tachometer-alt"></i> <span>Invia
							Messaggio</span>
				</a></li>
				<li class="nav-item"><a class="nav-link"
					href="./messaggio?operazione=visualizzaElencoMessaggio"> <i
						class="fas fa-fw fa-tachometer-alt"></i> <span>Leggi
							Messaggio</span></a></li>

				<li class="nav-item"><a class="nav-link"
					href="../parametri?operazione=visualizzaScheda"> <i
						class="fas fa-fw fa-tachometer-alt"></i> <span>Pubblica
							Annuncio</span></a></li>

			</c:when>
			<c:when test='${isAmministratore==true && accessDone==true}'>
				<li class="nav-item"><form action="./GestioneAccesso" method="post">
					<input type="hidden" name="operazione" value="logout">
					<button class="btn btn-primary">Logout immediato (temp.
						per testing)</button>
				</form></li>

				<li class="nav-item"><a class="nav-link"
					href="./registraMedico.jsp"> <i
						class="fas fa-fw fa-tachometer-alt"></i> <span>Registra
							Medico</span></a></li>

				<li class="nav-item"><a class="nav-link"
					href="../parametri?operazione=visualizzaScheda"> <i
						class="fas fa-fw fa-tachometer-alt"></i> <span>Modifica
							account Medico</span></a></li>

				<li class="nav-item"><a class="nav-link"
					href="../parametri?operazione=visualizzaScheda"> <i
						class="fas fa-fw fa-tachometer-alt"></i> <span>Rimuovi
							Medico</span></a></li>

				<li class="nav-item"><a class="nav-link"
					href="../parametri?operazione=visualizzaScheda"> <i
						class="fas fa-fw fa-tachometer-alt"></i> <span>Modifica
							Password Paziente</span></a></li>

				<li class="nav-item"><a class="nav-link"
					href="../parametri?operazione=visualizzaScheda"> <i
						class="fas fa-fw fa-tachometer-alt"></i> <span>Rimuovi
							account Paziente</span></a></li>
			</c:when>
			<c:otherwise>
			<li class="nav-item"><a class="nav-link"
					href="login.jsp"> <i
						class="fas fa-fw fa-tachometer-alt"></i> <span>Login</span></a></li>
			</c:otherwise>
		</c:choose>
		<li class="nav-item"><a class="nav-link" href="#"> <i
						class="fas fa-fw fa-tachometer-alt"></i> <span>Conosci il
							prodotto</span></a></li>
				<li class="nav-item"><a class="nav-link" href="./team.jsp">
						<i class="fas fa-fw fa-tachometer-alt"></i> <span>Conosci
							il team!</span>
				</a></li>

		<!-- Divider -->


		<!-- Heading -->
		

		<!-- Nav Item - Pages Collapse Menu -->

		<!-- Nav Item - Utilities Collapse Menu -->


		<!-- Divider -->
		<hr class="sidebar-divider">

		<!-- Heading -->
		<div class="sidebar-heading">Addons</div>

		<!-- Nav Item - Pages Collapse Menu -->
		<li class="nav-item active"><a class="nav-link" href="#"
			data-toggle="collapse" data-target="#collapsePages"
			aria-expanded="true" aria-controls="collapsePages"> <i
				class="fas fa-fw fa-folder"></i> <span>Pages</span>
		</a>
			<div id="collapsePages" class="collapse show"
				aria-labelledby="headingPages" data-parent="#accordionSidebar">
				<div class="bg-white py-2 collapse-inner rounded">
					<h6 class="collapse-header">Login Screens:</h6>
					<a class="collapse-item" href="login.jsp">Login</a>
					<!--dovrebbe essere visualizzabile solo al medico e all'admin 
                    <a class="collapse-item" href="register.html">Register</a>-->
					<a class="collapse-item" href="forgot-password.html">Forgot
						Password</a>
					<div class="collapse-divider"></div>
					<h6 class="collapse-header">Other Pages:</h6>
					<a class="collapse-item" href="404.html">404 Page</a> <a
						class="collapse-item active" href="blank.html">Blank Page</a>
				</div>
			</div></li>

		<!-- Nav Item - Charts -->
		

		<!-- Nav Item - Tables -->
		

		<!-- Divider -->
		<hr class="sidebar-divider d-none d-md-block">

		<!-- Sidebar Toggler (Sidebar) -->
		<div class="text-center d-none d-md-inline">
			<button class="rounded-circle border-0" id="sidebarToggle"></button>
		</div>

	</ul>
	<!-- End of Sidebar -->
	<!-- Core plugin JavaScript-->


	<!-- Custom scripts for all pages-->




	<!-- Page level plugins -->

	<!-- Page level custom scripts -->
</html>