<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">
<meta name="description" content="">
<meta name="author" content="">
<title>Home - Nefrapp</title>
<%@include file="./includes/cssLinks.jsp"%>
</head>
<%@  taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<body id="page-top">
	<!-- session attribute -->
	<!-- Page Wrapper -->
	<div id="wrapper">
		<%@include file="../includes/sidebar.jsp"%>

		<!-- Content Wrapper -->
		<div id="content-wrapper" class="d-flex flex-column">

			<!-- Main Content -->
			<div id="content">
				<%@include file="../includes/header.jsp"%>
				
				<c:if test="${notifica == 'identificazioneSuccesso'}">
					<div class="alert alert-success fade show"
						role="alert">
						La richiesta di reset password è stata inviata con successo!<br>
						Se sei un medico, troverai il link per effettuare il reset nella tua casella di posta elettronica,<br>
						se sei un paziente, sarai contattato dall'amministratore di Nefrapp per ricevere la tua nuova password.
						<button type="button" class="close" data-dismiss="alert"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
					</div>
				</c:if>
					
				
				<c:if test="${notifica == 'resetSuccesso'}">
					<div class="alert alert-primary fade show"
						role="alert">
						La tua password è stata reimpostata con successo!<br>
						Puoi accedere con la tua nuova password cliccando <a href="./login.jsp"><strong>qui</strong></a>
						<button type="button" class="close" data-dismiss="alert"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
					</div>
				</c:if>
				

				<!-- Begin Page Content -->
				<div class="container-fluid">
							    <c:if test="${notifica == 'ModificaMedRiuscita'}">
					<div class="alert alert-success fade show"
						role="alert">
						L'account del medico è stato modificato con successo<br>
						<button type="button" class="close" data-dismiss="alert"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
					</div>
				</c:if>
							    <c:if test="${notifica == 'ModificaPazRiuscita'}">
					<div class="alert alert-success fade show"
						role="alert">
						L'account del paziente è stato modificato con successo<br>
						<button type="button" class="close" data-dismiss="alert"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
					</div>
				</c:if>
					<c:choose>

						<c:when test="${isAmministratore==true && accessDone==true}">
							<div id="admin"></div>
							<div class="row">
								<div class="col-sm-6 mt-3 ">
									<div class="card ">
										<div class="card-header py-3">
											<h6 class="m-0 font-weight-bold text-primary">Medici</h6>
										</div>
										<div class="card-body">
											<ul class="list-group pull-down pre-scrollable"
												id="tabellaMedici">
											</ul>
										</div>
										<div class="col-sm-12 mb-3 d-flex justify-content-center">
											<a href="./registraMedico.jsp"
												class="btn btn-primary btn-user btn-circle btn-xl"><i
												class="fas fa-user-plus"></i></a>
										</div>
									</div>

								</div>
								<div class="col-sm-6 mt-3">
									<div class="card">
										<div class="card-header py-3">
											<h6 class="m-0 font-weight-bold text-primary">Pazienti</h6>
										</div>
										<div class="card-body">
											<ul class="list-group pull-down pre-scrollable"
												id="tabellaPazienti">
											</ul>
										</div>
									</div>
								</div>
							</div>

						</c:when>
						<c:when test="${isPaziente==true && accessDone==true}">
							<div id="paziente"></div>
							<div class="row">
								<div class="col-sm-8 mt-3 ">
									<div class="card ">
										<div class="card-header py-3">
											<h6 class="m-0 font-weight-bold text-primary">Piano
												Terapeutico</h6>
										</div>
										<div class="card-body">
											<div class="card shadow mb-4">
												<div class="card-header py-3">
													<h6 class="m-0 font-weight-bold text-primary">Diagnosi</h6>
												</div>
												<div class="card-body">
													<p class="mb-0" id="diagnosi"></p>
												</div>
											</div>

											<div class="card shadow mb-4">
												<div class="card-header py-3">
													<h6 class="m-0 font-weight-bold text-primary">Farmaci
														prescritti</h6>
												</div>
												<div class="card-body">

													<p class="mb-0" id="farmaco"></p>

												</div>
											</div>

											<div class="card shadow mb-4">
												<div class="card-header py-3">
													<h6 class="m-0 font-weight-bold text-primary">Data
														fine terapia</h6>
												</div>
												<div class="card-body">
													<p class="mb-0" id="data"></p>
												</div>
											</div>

										</div>

									</div>

								</div>
								<div class="col-sm-4 mt-3">
									<div class="card">
										<div class="card-header py-3">
											<h6 class="m-0 font-weight-bold text-primary">Annunci</h6>
										</div>
										<div class="card-body">

											<ul class="list-group pull-down pre-scrollable"
												id="tabellaAnnunci">
												
											</ul>
										</div>
									</div>
								</div>
							</div>

						</c:when>
						<c:when test="${isMedico==true && accessDone==true}">
							<div id="medico"></div>
							<div class="row">
								<div class="col-sm-8 mt-3 ">
									<div class="card ">
										<div class="card-header py-3">
											<h6 class="m-0 font-weight-bold text-primary">Pazienti
												seguiti</h6>
										</div>
										<div class="card-body">
											<ul class="list-group pull-down pre-scrollable"
												id="tabellaPazienti">
											</ul>

										</div>

									</div>

								</div>
								<div class="col-sm-4 mt-3">
									<div class="card">
										<div class="card-header py-3">
											<h6 class="m-0 font-weight-bold text-primary">Annunci</h6>
										</div>
										<div class="card-body">
											<ul class="list-group pull-down pre-scrollable"
												id="tabellaAnnunci">
											</ul>


										</div>
										<div class="col-sm-12 mb-3 d-flex justify-content-center">
											<a href="./annuncio?operazione=caricaDestinatariAnnuncio"
												class="btn btn-primary btn-user btn-circle btn-xl"><i
												class="fas fa-file-medical"></i></a>
										</div>

									</div>

								</div>
							</div>

						</c:when>
					</c:choose>



					<div class="container login">

						<div class="card o-hidden border-0 shadow-lg my-5"></div>

					</div>


				</div>
				<!-- /.container-fluid -->

			</div>
			<!-- End of Main Content -->
			<%@include file="../includes/footer.jsp"%>
		</div>
		<!-- End of Content Wrapper -->
		<div id="eliminazione"></div>
	</div>
	<!-- End of Page Wrapper -->

	<!-- Scroll to Top Button-->
	<a class="scroll-to-top rounded" href="#page-top"> <i
		class="fas fa-angle-up"></i>
	</a>
</body>
<%@include file="./includes/scripts.jsp"%>
<script src="./js/loadObject.js"></script>
</html>