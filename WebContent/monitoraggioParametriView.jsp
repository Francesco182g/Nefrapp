
<%@  taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
    	<meta http-equiv="X-UA-Compatible" content="IE=edge">
    	<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    	<meta name="description" content="">
    	<meta name="author" content="">
		<title>Scheda Parametri - Nefrapp</title>
    	
    	<!-- Custom fonts for this template-->
    	<link href="./vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">
    	<link href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i" rel="stylesheet">

    	<!-- Custom styles for this template-->
   	 	<link href="./css/sb-admin-2.min.css" rel="stylesheet">	
   	 	
   	 	<!-- DatePicker -->
   	 	
		<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
		<script type="text/javascript" src="./js/bootstrap-datepicker.js"></script>
		<link rel="stylesheet" type="text/css" href="./css/bootstrap-datepicker.css" >
		<script src="./js/dataPicker.js"></script>

  		</head>
	
	<body id="page-top">
<%-- 	<jsp:include page="/parametri">
   <jsp:param name="codiceFiscale" value="NTNNTN80A01B963Y"/>
   <jsp:param name="flag" value="1"/>
  </jsp:include> --%>
	
		<!-- Page Wrapper -->
	    <div id="wrapper">
			<%@include file="../includes/sidebar.jsp" %>		
	
	        <!-- Content Wrapper -->
	        <div id="content-wrapper" class="d-flex flex-column">
	
	            <!-- Main Content -->
	            <div id="content">
					<%@include file="../includes/header.jsp" %>
	
	
	                <!-- Begin Page Content -->
        	<div class="container-fluid">

          <!-- Page Heading -->
          <c:set var="paziente" value='${sessionScope["paziente"]}'/>
          <c:set var="medico" value='${sessionScope["medico"]}'/>
          <c:set var="scheda" value='${requestScope["schedaParametri"]}'/>
          
          <h1 class="h3 mb-2 text-gray-800">Scheda parametri</h1>
          
          <c:choose>
          <c:when test="${scheda.size()==0}">
          	<h2 class="h3 mb-2 text-gray-800">Nessuna scheda presente</h2>
          </c:when>
          
          <c:otherwise>
          <!-- DataTales Example -->
          <div class="card shadow mb-4">
            
            <div class="card-body">
              <div class="table-responsive">
                <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
                  <thead>
                    <tr>
                      <th>Peso(Kg)</th>
                      <th>Pressione arteriosa minima(mmHg)</th>
                      <th>Pressione arteriosa massima(mmHg)</th>
                      <th>Scarico iniziale(ml)</th>
                      <th>Ultrafiltrato(ml)</th>
                      <th>Tempo di sosta</th>
                      <th>Carico diurno(ml)</th>
                      <th>Scarico diurno(ml)</th>
                      <th>Data</th>
                      <!-- Possibile aggiunta di atri campi -->
                    </tr>
                  </thead>
                  <tfoot> <!-- Sarebbe il footer della tabella-->
                    <tr>
                      <th>Peso(Kg)</th>
                      <th>Pressione arteriosa minima(mmHg)</th>
                      <th>Pressione arteriosa massima(mmHg)</th>
                      <th>Scarico iniziale(ml)</th>
                      <th>Ultrafiltrato(ml)</th>
                      <th>Tempo di sosta</th>
                      <th>Carico diurno(ml)</th>
                      <th>Scarico diurno(ml)</th>
                      <th>Data</th>
                      <!-- Possibile aggiunta di altri campi -->
                    </tr>
                  </tfoot>
                  <tbody>
                  	
                  	
                  	
                  	<!-- Inizio iterazione dei risultati ottenuti dalla servlet (parametri inseriti dal paziente) -->
             		<c:forEach items="${scheda}" var="item">
                    <tr>
                      <td>${item.peso}</td>
                      <td>${item.paMin}</td>
                      <td>${item.paMax}</td>
                      <td>${item.scaricoIniziale}</td>
                      <td>${item.UF}</td>
                      <td>${item.tempoSosta}</td>
                      <td>${item.scarico}</td>
                      <td>${item.carico}</td>
                      <td>${item.dataFormattata}</td>
                    </tr>
                    </c:forEach>
                  </tbody>
                 </table>
                 <c:choose>
                 <c:when test='${medico!=null}'>
                 <form action="./GestioneParametri" method="post" id="rangeForm">
                 <div class="dates" style="margin-top:100px;color:#2471a3;">
					<label>Data inizio</label>
    				<input type="text" style="width:200px;background-color:#aed6f1;" class="form-control" id="dataInizio" name="event_date" placeholder="DD-MM-YYYY" autocomplete="off">
    				<label>Data fine</label>
    				<input type="text" style="width:200px;background-color:#aed6f1;" class="form-control" id="dataFine" name="event_date" placeholder="DD-MM-YYYY" autocomplete="off">
    				<input type="hidden" name="operazione"  value="download">
    				<input type="hidden" name="CFPaziente" value="${scheda.get(0).getPazienteCodiceFiscale()}">
  				</div>
  				</form>
  				<div class="my-2"></div>
  				<button type="submit" form="rangeForm" class="btn btn-info btn-icon-split">Scarica report</button>
  				</c:when>
  				</c:choose>
  			</div>
            </div>
          </div>
          </c:otherwise>
          </c:choose>

        </div>
	                <!-- /.container-fluid -->
	
	            </div>
	            <!-- End of Main Content -->
				<%@include file="../includes/footer.jsp" %>	
	        </div>
	        <!-- End of Content Wrapper -->
	
	    </div>
	    <!-- End of Page Wrapper -->
	
	    <!-- Scroll to Top Button-->
	    <a class="scroll-to-top rounded" href="#page-top">
	        <i class="fas fa-angle-up"></i>
	    </a>
	   	
	</body>
</html>
