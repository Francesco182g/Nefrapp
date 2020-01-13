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
<title>Login - Nefrapp</title>

<%@include file="./includes/cssLinks.jsp"%>
</head>

<body id="page-top">
	<!-- Page Wrapper -->
	<div id="wrapper">
		<%@include file="../includes/sidebar.jsp"%>

		<!-- Content Wrapper -->
		<div id="content-wrapper" class="d-flex flex-column">

			<!-- Main Content -->
			<div id="content">
				<%@include file="../includes/header.jsp"%>
				<c:if test="${accessDone == true}">
					<c:redirect url="./dashboard.jsp"></c:redirect>
				</c:if>

				<!-- Begin Page Content -->
				<div class="container-fluid">


					<div class="container login">
						
						<div class="my-5">
							<div class="card-body p-0 d-flex justify-content-center">
								<!-- Nested Row within Card Body -->
								<div class="row">
									<div class="col-sm-12">
										<div class="card o-hidden border-0 shadow-sm p-5">
											<div class="text-center">
												<h1 class="h4 text-gray-900 mb-4">Effettua il login</h1>
											</div>
											<form class="user" method="post" action="./GestioneAccesso">
												<input type="hidden" name="operazione" value="loginUtente">
												<div class="form-group">
													<input type="text" class="form-control form-control-user"
														name="codiceFiscale" id="codiceFiscale"
														placeholder="Codice fiscale" required="required"
														>
												</div>
												<div class="form-group">
													<input type="password"
														class="form-control form-control-user" name="password"
														id="password" placeholder="Password" required="required"
														>
												</div>
												<div class="p-6">
													<a href="./richiestaResetView.jsp"> Password
														dimenticata? </a>
												</div>
												<button class="btn btn-primary btn-user btn-block mt-3"
													id="accediAdminButton">Accedi</button>

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
			<%@include file="../includes/footer.jsp"%>
		</div>
		<!-- End of Content Wrapper -->

	</div>
	<!-- End of Page Wrapper -->

	<!-- Scroll to Top Button-->
	<a class="scroll-to-top rounded" href="#page-top"> <i
		class="fas fa-angle-up"></i>
	</a>
</body>
<%@include file="./includes/scripts.jsp"%>
<script src="./js/ParameterControl.js"></script>

</html>