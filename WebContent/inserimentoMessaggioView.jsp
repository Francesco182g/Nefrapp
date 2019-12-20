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
<title>Nefrapp</title>

<!-- Custom fonts for this template-->

<!-- Custom styles for this template-->
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

					<h1 class="h3 mb-2 text-gray-800">Nuovo Messaggio</h1>


					<div class="card shadow mb-4">
						<div class="card-body">
						Select in forma temporanea: per ora usare CTRL+click per selezionare e deselezionare destinatari<br><br>
							<form class="user" action="./messaggio" method="POST" >
								<input type="hidden" name="operazione" value="inviaMessaggio">
								<div class="form-group row">
									<div class="col-lg-2 col-sm-3 mb-3 mb-sm-12 row">
										<p style="text-align: center">Destinatari:</p>
									</div>
									<div class="col-lg-3 col-sm-3 mb-3 mb-sm-12 row">
										<div class="dropdown">
											<!-- Se utente==paziente, il pulsante mostra l'elenco dei medico, viceversa altrimenti -->
											<c:choose>
												<c:when test='${paziente!=null}'>
												    <select name="selectMedico" id="selectMedico" multiple>
												      <option value="" disabled>Scegli destinatari:</option>
												      	<c:set var="dottori" value='${requestScope["mediciCuranti"]}'/>
														<c:forEach items="${dottori}" var="item">
														<option value="${item.codiceFiscale}">${item.nome} ${item.cognome}</option></a> 
														</c:forEach>	
												    </select>
												</c:when>
												<c:when test='${medico!=null}'>
												<!--<c:set var="pazienti" value='${requestScope[pazientiSeguiti]}'/>-->
													<button class="btn btn-secondary dropdown-toggle"
														type="button" id="dropdownMenuButton"
														data-toggle="dropdown" aria-haspopup="true"
														aria-expanded="false">Elenco Pazienti</button>
													<div class="dropdown-menu"
														aria-labelledby="dropdownMenuButton">
												<!--<c:forEach items="${pazienti}" var="item">
														<a class="dropdown-item" href="#">${item.nome} ${item.cognome}</a>
												</c:forEach>-->
														 
													</div>
												</c:when>
											</c:choose>
										</div>
									</div>
<!-- 									<div class="col-lg-12 col-mb-12 col-sm-12 mb-12 mb-sm-12 row"> -->
<!-- 										<input type="text" class="form-control form-control-user" -->
<!-- 											id="cfdestinatario" name="cfdestinatario" -->
<!-- 											placeholder="Esempio: CRRSRA90A50A091Q" required="required"> -->
<!-- 									</div> -->
								</div>
								<!-- oggetto del messaggio -->
								<div class="form-group row">
									<div class="col-lg-2 col-sm-3 mb-3 mb-sm-12 row">
										<p style="text-align: center">Oggetto:</p>
									</div>
									<div class="col-lg-10 col-sm-12 mb-12 mb-sm-12 row">
										<input type="text" class="form-control form-control-user"
											id="oggeto" name="oggetto" required="required">
									</div>

								</div>
								<!-- Inizio area testo -->
								<div class="form-group row">
									<div class="col-lg-2 col-sm-3 mb-3 mb-sm-12 row">
										<p style="text-align: center">Testo:</p>
									</div>
									<div class="col-lg-12 col-mb-12 col-sm-12 mb-12 mb-sm-12 row">
										<textarea name="testo" maxlength="1000" id="testo" 
											class="form-control " placeholder="" required="required"
											style="resize: none; height: 180px"></textarea>
									</div>
								</div>
								<div class="form-group row">
									<div class="file-field">
										<div class="d-flex justify-content-center">
											<div class="btn btn-mdb-color btn-rounded float-left">
												<span>Allegato</span> <input type="file" name="allegato" id="allegato">
											</div>
										</div>
									</div>
								</div>
								<div class="form-group row">
									<button class="btn btn-primary btn-user" type="submit"
										id="inviaMessaggio" style="float: right">Invia
										Messaggio</button>
								</div>

							</form>
						</div>
					</div>
				</div>


				<!-- /.container-fluid -->

			</div>
			<!-- End of Main Content -->
			<%@include file="./includes/footer.jsp"%>
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