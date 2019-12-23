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
		<title>Piano Terapeutico - Nefrapp</title>
    	
    	<!-- Custom fonts for this template-->
    	<link href="vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">
    	<link href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i" rel="stylesheet">

    	<!-- Custom styles for this template-->
   	 	<link href="css/sb-admin-2.min.css" rel="stylesheet">	
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
					
					<c:if test='${isPaziente == true}'>
	                	<div class="card shadow mb-4">
                			<div class="card-header py-3">
                  				<h6 class="m-0 font-weight-bold text-primary">Paziente</h6>
                			</div>
                			<div class="card-body">
                  				<p class="mb-0">${utente.nome} ${utente.cognome}</p>
                  			</div>
              			</div>
              		</c:if>
              		
              		<c:if test='${isMedico == true}'>
<!--               			inserire logica di visualizzazione da parte del medico -->
<!--               			 <div class="card shadow mb-4"> -->
<!--                 			<div class="card-header py-3"> -->
<!--                   				<h6 class="m-0 font-weight-bold text-primary">Paziente</h6> -->
<!--                 			</div> -->
<!--                 			<div class="card-body"> -->
<%--                   				<p class="mb-0">${paziente.nome} ${paziente.cognome}</p> --%>
<!--                   			</div> -->
<!--               			</div> -->
              		</c:if>
	                	
	                	<c:set var="pianoTerapeutico" value='${requestScope["pianoTerapeutico"]}'/>
	                	
	                	<c:if test='${pianoTerapeutico != null}'>
	                	<form action="/GestionePianoTerapeutico" method="post" id="modificaPiano">
	                	<input id="CFPaziente" type="hidden" value="${pianoTerapeutico.codiceFiscalePaziente}">
	                	<input id="operazione" type="hidden" value="modifica">
	                	<div class="card shadow mb-4">
                			<div class="card-header py-3">
                  				<h6 class="m-0 font-weight-bold text-primary">Diagnosi</h6>
                			</div>
							<div class="card-body">
               					<p class="mb-0">${pianoTerapeutico.diagnosi}</p>
               				</div>
              			</div>
              			
              			<div class="card shadow mb-4">
                			<div class="card-header py-3">
                  				<h6 class="m-0 font-weight-bold text-primary">Farmaci prescritti</h6>
                			</div>
							<div class="card-body">
               					<p class="mb-0">${pianoTerapeutico.farmaco}</p>
               				</div>
              			</div>
              			
              			<div class="card shadow mb-4">
                			<div class="card-header py-3">
                  				<h6 class="m-0 font-weight-bold text-primary">Data fine terapia</h6>
                			</div>
							<div class="card-body">
               					<p class="mb-0">${pianoTerapeutico.dataFormattata}</p>
               				</div>
              			</div>
              			</form>
              			<c:if test='${medico != null}'>
              			<div class="my-2"></div>
  						<button type="submit" class="btn btn-info btn-icon-split" id="bottoneModifica">Modifica</button>
              			<div class="my-2"></div>
  						<button type="submit" form="modificaPiano" class="btn btn-info btn-icon-split" id="bottoneConferma" disabled>Applica modifiche</button>
              			</c:if>
              			</c:if>
              			
              			<c:if test='${pianoTerapeutico == null}'>
              			<div class="card shadow mb-4">
                			<div class="card-header py-3">
                  				<h3 class="m-0 font-weight-bold text-primary">Piano terapeutico non presente</h3>
                			</div>
                		</div>
              			</c:if>
	
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
	    <script>
		    $(document).ready(function(){
		      	$("#bottoneModifica").click(function(){
		      		$("#diagnosi").prop("disabled", false)
		      		$("#farmaci").prop("disabled", false)
		      		$("#data").prop("disabled", false)
		      		$('#bottoneModifica').prop("disabled", true)
		      		$('#bottoneConferma').prop("disabled", false)
		    	});
		    });
	    
	    </script>
	    
	</body>
</html>