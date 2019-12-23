	<%@  taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
<title>Messaggi - Nefrapp</title>

<!-- Custom fonts for this template-->
<link href="./vendor/fontawesome-free/css/all.min.css" rel="stylesheet"
	type="text/css">
<link
	href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i"
	rel="stylesheet">

<!-- Custom styles for this template-->
<link href="./css/sb-admin-2.min.css" rel="stylesheet">

<!-- DatePicker -->

<script type="text/javascript"
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<script type="text/javascript" src="./js/bootstrap-datepicker.js"></script>
<link rel="stylesheet" type="text/css"
	href="./css/bootstrap-datepicker.css">

<!-- Script per la registrazione -->
<script src="./js/dataPicker.js"></script>


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


				<!-- Begin Page Content -->
				<div class="container-fluid">

					<!-- Page Heading -->
					<c:set var="messaggio" value='${requestScope["messaggio"]}' />
					<h1 class="h3 mb-2 text-gray-800">Messaggio ricevuto:</h1>


					<!-- DataTales Example -->
					<div class="card shadow mb-4">

						<div class="card-body">
							<div class="table-responsive">
								<table class="table table-bordered" id="dataTable" width="100%"
									cellspacing="0">
									<thead>
										<tr>
											<th>Mittente</th>
											<th>Oggetto</th>
											<th>Data</th>
											<th>Ora</th>

											<!-- Possibile aggiunta di atri campi -->
										</tr>
									</thead>
									<tbody>
										<tr>
											<td>Dott. ${param['cognome']}</td>
											<td>${messaggio.oggetto}</td>
											<td>${messaggio.dataFormattata}</td>
											<td>${messaggio.oraFormattata}</td>
										</tr>
									</tbody>
								</table>

								<table class="table table-bordered" id="dataTable" width="100%"
									cellspacing="0">
									<thead>
										<tr>
											<th>Testo</th>
										</tr>
									</thead>

									<tbody>
										<tr>
											<td>${messaggio.testo}</td>
										</tr>
									</tbody>
								</table>
								<table class="table table-bordered" id="dataTable" width="100%"
									cellspacing="0">
									<thead>
										<tr>
											<th>Allegato</th>
										</tr>
									</thead>

									<tbody>
										<tr>
											<td>
											<c:if test="${messaggio.allegato!=null}">
												<img class="img-fluid" src="data:image/jpeg;base64, ${messaggio.allegato}">
											</c:if>
											<c:if test="${messaggio.allegato==null}">
												Nessun allegato
											</c:if>
											</td>
										</tr>
									</tbody>
								</table>
							</div>
							<a class="btn btn-primary float-right" 
							href="./comunicazione?operazione=caricaDestinatariMessaggio&destinatario=${messaggio.codiceFiscaleMittente}" 
							role="button">Rispondi</a>
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
</html>
