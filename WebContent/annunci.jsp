<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
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

						<c:forEach items="${annunci}" var="item">
							<c:set var="dottore" value="${requestScope[item.medico]}" />
							<div class="card shadow mb-4">
								<h5 class="card-header text-center">
									${item.titolo}</h5>
								<div class="card-body">
									<p class="card-text">${item.testo}</p>
									<c:if test="${item.corpoAllegato!=null}">
									<p><i class="fas fa-download"></i> &emsp;
									<a id="download" download="" href="">${item.nomeAllegato}</a><p>
									</c:if>
									<p class="card-text float-right">Dott. ${dottore}<br>${item.dataFormattata},
										${item.oraFormattata}</p>
								</div>
							</div>

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
</html>