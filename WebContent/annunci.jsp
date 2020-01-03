<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Annunci - Nefrapp</title>
<%@include file="./includes/cssLinks.jsp"%>
</head>
<body>
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
					<c:set var="annunci" value='${requestScope["annunci"]}' />
					<h1 class="h3 mb-2 text-gray-800">Annunci:</h1>


						<!-- Inizio iterazione dei risultati ottenuti dalla servlet) -->
						<c:set var="count" value="0" scope="page" />
						<c:forEach items="${annunci}" var="item" end="50">
							<c:set var="dottore" value="${requestScope[item.medico]}" />
							<div class="card shadow mb-4">
								<h5 class="card-header text-center">
									${item.titolo}</h5>
								<div class="card-body">
									<p class="card-text">${item.testo}</p>
									<c:if test='${item.nomeAllegato!=null && item.nomeAllegato!= ""}'>
									<form id="downloadForm${count}" method="POST" action="./annuncio">
										<input type="hidden" name="operazione" id="operazione" value="generaDownload"/>
										<input type="hidden" name="id" id="id" value="${item.idAnnuncio}"/>
										<p><i class="fas fa-download"></i> &emsp;
										<button type="submit" form="downloadForm${count}" class="download btn btn-link">${item.nomeAllegato}</button><p>
									</form>
									</c:if>
									<p class="card-text float-right">Dott. ${dottore}<br>${item.dataFormattata},
										${item.oraFormattata}</p>
								</div>
							</div>
							<c:set var="count" value="${count + 1}" scope="page"/>
						</c:forEach>

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
<script src="./js/annunci.js"></script>
</html>