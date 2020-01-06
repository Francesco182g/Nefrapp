<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@  taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">
<meta name="description" content="">
<meta name="author" content="">
<title>Modifica Password - Nefrapp</title>

<!-- Custom fonts for this template-->

<!-- Custom styles for this template-->
<%@include file="./includes/cssLinks.jsp"%>
<link rel="stylesheet" type="text/css"
	href="./css/bootstrap-datepicker.css">
<!-- Script per la registrazione -->

</head>

<body id="page-top">

	<!-- Page Wrapper -->
	<div id="wrapper">
		<%@include file="./includes/sidebar.jsp"%>

		<!-- Content Wrapper -->
		<div id="content-wrapper" class="d-flex flex-column">

			<!-- Main Content -->
			<div id="content">
				<%@include file="./includes/header.jsp"%>
					

				<!-- Begin Page Content -->

				<div class="container-fluid">
					<div class="my-5">
						<div class="card-body p-0 d-flex justify-content-center">
							<!-- Nested Row within Card Body -->
							<div class="row">
								<div class="col-sm-12">
									<div class="card o-hidden border-0 shadow-sm p-5">
										<div class="text-center">
											<h1 class="h4 text-gray-900 mb-4">Modifica Password</h1> 
										</div>

										<form class="user" id="medico" method="post" action="./GestioneAmministratore">


											<input type="hidden" id="notifica"
												value="${requestScope.notifica}"> <input
												type="hidden" name="operazione" value="modifica"> <input
												type="hidden" name="tipoUtente" value="amministratore">
											
											<div class="form-group row col-lg-12">
												Vecchia Password: <input type="password"
													class="form-control form-control-user" name="vecchiaPassword"
													id="vecchiaPassword" placeholder="Vecchia Password" required="required" min="6" max="20"
													maxlength="20">
											</div>
											<div class="form-group row col-lg-12">
												Nuova Password: <input type="password"
													class="form-control form-control-user" name="nuovaPassword"
													id="nuovaPassword" placeholder="Nuova Password" required="required" min="6" max="20"
													maxlength="20">
											</div>
											<div class="form-group row col-lg-12">
												Conferma Password: <input type="password"
													class="form-control form-control-user" name="confermaPassword"
													id="confermaPassword" placeholder="Conferma Password" required="required" min="6"
													max="20" maxlength="20">
											</div>
											<div class="col-sm-5 sm-12"
												style="margin-left: auto; margin-right: auto; display: block; margin-top: 22%; margin-bottom: 0%">
												<button class="btn btn-primary btn-user btn-block"
													id="modificaPasswordAmministratoreButton">Modifica</button>
											</div>
										</form>

									</div>
								</div>
							</div>

						</div>
					</div>

				</div>

			</div>
			<!-- /.container-fluid -->

		</div>
		<!-- End of Main Content -->
	</div>
	<%@include file="./includes/footer.jsp"%>

	<!-- End of Content Wrapper -->

	<!-- End of Page Wrapper -->

	<!-- Scroll to Top Button-->
	<a class="scroll-to-top rounded" href="#page-top"> <i
		class="fas fa-angle-up"></i>
	</a>
</body>
<%@include file="./includes/scripts.jsp"%>
<script src="./js/ParameterControl.js"></script>
</html>