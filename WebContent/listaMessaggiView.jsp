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
		<title>Messaggi - Nefrapp</title>
    	
    	<!-- Custom fonts for this template-->
    	<link href="./vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">
    	<link href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i" rel="stylesheet">

    	<!-- Custom styles for this template-->
   	 	<link href="./css/sb-admin-2.min.css" rel="stylesheet">	
   	 	
   	 	<!-- DatePicker -->
   	 	
		<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
	<script type="text/javascript" src="./js/bootstrap-datepicker.js"></script>
	<link rel="stylesheet" type="text/css" href="./css/bootstrap-datepicker.css" >
		
		<!-- Script per la registrazione -->
   	 	<script src="./js/dataPicker.js"></script>

  		
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
          <c:set var="messaggio" value='${requestScope["messaggio"]}'/>
          <h1 class="h3 mb-2 text-gray-800">Messaggi ricevuti: </h1>
          
          
          <!-- DataTales Example -->
          <div class="card shadow mb-4">
            
            <div class="card-body">
              <div class="table-responsive" id="tablecont">
                <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
                  <thead>
                    <tr>
                      <th>Mittente</th>
                      <th>Oggetto</th>
                      <th>Data</th>
                      <th>Ora</th>
                      
                      <!-- Possibile aggiunta di atri campi -->
                    </tr>
                  </thead>
                  <tfoot> <!-- Sarebbe il footer della tabella-->
<!--                     <tr> -->
<!--                       <th>Mittente</th> -->
<!--                       <th>Oggetto</th> -->
<!--                       <th>Data</th> -->
<!--                       <th>Ora</th> -->
<!--                       Possibile aggiunta di altri campi -->
<!--                     </tr> -->
                  </tfoot>
                  <tbody>
                  	
                  	
                  	
                  	<!-- Inizio iterazione dei risultati ottenuti dalla servlet (parametri inseriti dal paziente) -->
             		<c:forEach items="${messaggio}" var="item">
                    <tr class = "clickable-row riga-messaggio" data-href='./messaggio?operazione=visualizzaMessaggio&idMessaggio=${item.idMessaggio}'>
                      <c:set var="cognome" value="${item.codiceFiscaleMittente}" />
                      <td>Dott. ${requestScope[cognome]}</a></td>
                      <td>${item.oggetto}</td>
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
<!-- 	    spostare in un file -->
	    <script type="text/javascript">
	    jQuery(document).ready(function($) {
	        $(".clickable-row").click(function() {
	            window.location = $(this).data("href");
	        });
	    });
	    </script>
	
	    <!-- Scroll to Top Button-->
	    <a class="scroll-to-top rounded" href="#page-top">
	        <i class="fas fa-angle-up"></i>
	    </a>
	   	
	</body>
</html>
