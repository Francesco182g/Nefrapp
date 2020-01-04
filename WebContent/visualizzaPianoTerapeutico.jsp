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
    
    	<%@include file="./includes/cssLinks.jsp"%>
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
<!--                 			<div class="card-header
<%@include file="./includes/scripts.jsp"%>

<script src="./js/jquery-confirm.js"></script>
<script src="./js/ParameterControl.js"></script>

 py-3"> -->
<!--                   				<h6 class="m-0 font-weight-bold text-primary">Paziente</h6> -->
<!--                 			</div> -->
<!--                 			<div class="card-body"> -->
<%--                   				<p class="mb-0">${paziente.nome} ${paziente.cognome}</p> --%>
<!--                   			</div> -->
<!--               			</div> -->
              		</c:if>
	                	
	                	<c:set var="pianoTerapeutico" value='${requestScope["pianoTerapeutico"]}'/>
	                	
	                	<c:if test='${pianoTerapeutico != null}'>
	                	<form action="./GestionePianoTerapeutico" method="post" id="modificaPiano">
	                	<input name="CFPaziente" type="hidden" value="${pianoTerapeutico.codiceFiscalePaziente}">
	                	<input name="operazione" type="hidden" value="modifica">
	                	<div class="card shadow mb-4">
                			<div class="card-header py-3">
                  				<h6 class="m-0 font-weight-bold text-primary">Diagnosi</h6>
                			</div>
							<div class="card-body">
								<c:if test="${isMedico}">
								<div class="form-group row col-lg-12">
							    	<input type="text" class="form-control form-control-user" 
							    	id="diagnosi" name="diagnosi" value="${pianoTerapeutico.diagnosi}" required="required" disabled >
							    </div>
							    </c:if>
							    <c:if test="${isPaziente}">
               					<p class="mb-0">${pianoTerapeutico.diagnosi}</p>
               					</c:if>
               				</div>
              			</div>
              			
              			<div class="card shadow mb-4">
                			<div class="card-header py-3">
                  				<h6 class="m-0 font-weight-bold text-primary">Farmaci prescritti</h6>
                			</div>
							<div class="card-body">
								<c:if test="${isMedico}">
								<div class="form-group row col-lg-12">
							    	<input type="text" class="form-control form-control-user" id="farmaci" name="farmaci" value="${pianoTerapeutico.farmaco}" required="required" disabled >
							    </div>
								</c:if>
								<c:if test="${isPaziente}">
               					<p class="mb-0">${pianoTerapeutico.farmaco}</p>
               					</c:if>
               				</div>
              			</div>
              			
              			<div class="card shadow mb-4">
                			<div class="card-header py-3">
                  				<h6 class="m-0 font-weight-bold text-primary">Data fine terapia</h6>
                			</div>
							<div class="card-body">
								<c:if test="${isMedico}">
								<div class="form-group row col-lg-12">
							    	<input type="text" class="form-control form-control-user" id="data" name="data" value="${pianoTerapeutico.dataFormattata}" required="required" disabled >
							    </div>
								</c:if>
								<c:if test="${isPaziente}">
               					<p class="mb-0">${pianoTerapeutico.dataFormattata}</p>
               					</c:if>
               				</div>
              			</div>
              			</form>
              			<c:if test='${isMedico}'>
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
	</body>
	<%@include file="./includes/scripts.jsp"%>
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
</html>