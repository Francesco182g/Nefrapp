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
    	<meta name="author" content="Davide Benedetto Strianese">
		<title>Annunci - Nefrapp</title>
    	
    	<!-- Custom fonts for this template-->
    	<link href="./vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">
    	<link href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i" rel="stylesheet">

    	<!-- Custom styles for this template-->
   	 	<link href="./css/sb-admin-2.min.css" rel="stylesheet">	
  		<script type="text/javascript" src="./js/messaggi.js"></script>
		
	</head>
	
	<body id="page-top">	
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
          <c:set var="messaggio" value='${requestScope["annunci"]}'/>
          <h1 class="h3 mb-2 text-gray-800">Annunci personali: </h1>
          
          
          <!-- DataTales Example -->
          <div class="card shadow mb-4">
            
            <div class="card-body">
              <div class="table-responsive" id="tablecont">
                <table class="table" id="dataTable" width="100%" cellspacing="0">
                  <thead>
                    <tr>
                      <th>Medico</th>
                      <th>Titolo</th>
                      <th>Data</th>
                      <th>Ora</th>
                      
                      <!-- Possibile aggiunta di atri campi -->
                    </tr>
                  </thead>
                  <tbody>
                  	
                  	
                  	
                  	<!-- Inizio iterazione dei risultati ottenuti dalla servlet) -->
             		<c:forEach items="${annunci}" var="item">
             		
             		<c:set var="cognome" value="${item.getMedico().getCodiceFiscale()}" />
                    <tr class = "clickable-row riga-messaggio" style="font-weight: bolder" data-href='./gestioneAnnunci?operazione=visualizza&idAnnuncio=${item.idAnnuncio}'>
                    
                      
                      <td width = "300px">Dott. ${item.getMedico().getCognome()}</a></td>
                      <td>${item.titolo}</td>
                      <td>${item.dataFormattata}</td>
                      <td>${item.oraFormattata}</td>
                    </tr>	</c:forEach>
                  </tbody>
                 </table>
  			</div>
            </div>
          </div>

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