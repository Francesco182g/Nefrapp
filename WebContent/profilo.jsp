
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
		<title>Profilo - Nefrapp</title>
    	
   	 	
   	 	<!-- DatePicker -->
   	 	
		
		<link rel="stylesheet" type="text/css" href="./css/bootstrap-datepicker.css" >
		
		<!-- Script per la registrazione -->
   	
  		
	</head>
	
	<body id="page-top">
	 <c:choose>     
         <c:when test = "${isPaziente == true}">
         	<c:set var = "azione" value="./ModificaAccountPazienteView.jsp"></c:set>
            <c:set var = "nome" value="${utente.nome}"></c:set>
            <c:set var = "cognome" value="${utente.cognome}"></c:set>
			<c:set var = "sesso" value="${utente.sesso}"></c:set>
			<c:set var = "codiceFiscale" value="${utente.codiceFiscale}"></c:set>
			<c:set var = "email" value="${utente.email}"></c:set>
            <c:set var = "dataDiNascita" value="${utente.dataDiNascita}"></c:set>
            <c:set var = "luogoDiNascita" value="${utente.luogoDiNascita}"></c:set>
            <c:set var = "residenza" value="${utente.residenza}"></c:set>
			<c:set var ="dottori" value='${requestScope["mediciCuranti"]}' />
         </c:when>
         
         <c:when test = "${isMedico == true}">
            <c:set var = "azione" value="./ModificaAccountMedicoView.jsp"></c:set>
            <c:set var = "nome" value="${utente.nome}"></c:set>
            <c:set var = "cognome" value="${utente.cognome}"></c:set>
    		<c:set var = "sesso" value="${utente.sesso}"></c:set>
			<c:set var = "codiceFiscale" value="${utente.codiceFiscale}"></c:set>
			<c:set var = "email" value="${utente.email}"></c:set>
            <c:set var = "dataDiNascita" value="${utente.dataDiNascita}"></c:set>
            <c:set var = "luogoDiNascita" value="${utente.luogoDiNascita}"></c:set>
            <c:set var = "residenza" value="${utente.residenza}"></c:set>
            <c:set var ="pazienti" value='${requestScope["pazientiSeguiti"]}' />
         </c:when> 
         
      </c:choose>
	
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
	                    <h1 class="h3 mb-4 text-gray-800">Profilo personale</h1>
          <div class="card shadow mb-4">
            
            <div class="card-body">
              <div class="table-responsive">
              	<form action="${azione }" >
                       	  <h2 class="h4 mb-4 text-gray-500" style="display:inline; margin-right: 150px;">Nome: <span class="h3 mb-4 text-gray-800" >${nome}</span></h2>

                      	  <h2 class="h4 mb-4 text-gray-500" style="display:inline">Cognome: <span class="h3 mb-4 text-gray-800" >${cognome}</span></h2><br><br>
		                      	  
                      	  <h2 class="h4 mb-4 text-gray-500">Sesso: <span class="h3 mb-4 text-gray-800">${sesso}</span></h2>
                      	  
                      	  <h2 class="h4 mb-4 text-gray-500">Codice fiscale: <span class="h3 mb-4 text-gray-800">${codiceFiscale}</span></h2>
                      	  
                      	  <h2 class="h4 mb-4 text-gray-500">Email: <span class="h3 mb-4 text-gray-800">${email}</span></h2>
                      	  
                      	  <h2 class="h4 mb-4 text-gray-500"style="display:inline; margin-right: 150px;">Data di nascita: <span class="h3 mb-4 text-gray-800">${dataDiNascita}</span></h2>                      	  
                      	  
                      	  <h2 class="h4 mb-4 text-gray-500" style="display:inline">Luogo di nascita: <span class="h3 mb-4 text-gray-800" >${luogoDiNascita}</span></h2><br><br>
                      	  
         				  <h2 class="h4 mb-4 text-gray-500">Residenza: <span class="h3 mb-4 text-gray-800">${residenza}</span></h2>
         				  
         				  <!-- se è loggato il paziente, mostra l'elenco dei medici che lo seguono -->
         				  <c:if test="${isPaziente}">
         				   <h2 class="h4 mb-4 text-gray-500">Seguito da:
         				     <c:forEach items="${dottori}" var="item" varStatus="loop">
         				     		<c:if test="${loop.index eq 0 }">
												<span class="h3 mb-4 text-gray-800">${item.nome} ${item.cognome} </span>
									</c:if>
									<c:if test="${loop.index gt 0}">		
												<h3 class="h3 mb-4 text-gray-800" style="margin-left: 10.6%; line-height: 1px;">${item.nome} ${item.cognome} </h3>
									</c:if>		
							</c:forEach>
							</h2>
         				  </c:if>
         				  
         				  <!-- se è loggato il medico, mostra l'elenco dei paziente che segue -->
         				  <c:if test="${isMedico}">
         				   <h2 class="h4 mb-4 text-gray-500">Segue:
         				     <c:forEach items="${pazienti}" var="item" varStatus="loop">
         				     		<c:if test="${loop.index eq 0}">
												<span class="h3 mb-4 text-gray-800">${item.nome} ${item.cognome} </span>
									</c:if>
										
									<c:if test="${loop.index gt 0}">		
												<h3 class="h3 mb-4 text-gray-800" style="margin-left: 78px; line-height: 1px;">${item.nome} ${item.cognome} </h3>
									</c:if>			
							</c:forEach>
							</h2>	
         				  </c:if>
         				  
         				  <c:if test="${isPaziente}">
 				  				<button type="button" class="btn btn-warning" data-toggle="modal" data-target="#disattivaModal" style="margin-left:70%;">Disattiva account</button> 
         				  </c:if>
         		  		 <button type="submit" class="btn btn-primary" style="float:right;">Modifica account</button>
         		   </form> 		 
                   </div>
                   </div>
                   </div>
					
					
					 </div>
	            <!-- End of Main Content -->
				<%@include file="../includes/footer.jsp" %>	
	        </div>
	        <!-- End of Content Wrapper -->
	
	    </div>
	    <!-- End of Page Wrapper -->
	    </div>
	    <!-- disattiva Modal-->
	<div class="modal fade" id="disattivaModal" tabindex="-1" role="dialog"
		aria-labelledby="exampleModalLabel" aria-hidden="true">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="exampleModalLabel">Sei sicuro?</h5>
					<button class="close" type="button" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">×</span>
					</button>
				</div>
				<div class="modal-body">Seleziona "Disattiva" qui sotto se sei
					sicuro di voler disattivare il tuo account.</div>
				<div class="modal-footer">
					<button class="btn btn-secondary" type="button"
						data-dismiss="modal">Annulla</button>
					<form action="./GestionePaziente" method="get">
						<input type="hidden" name="operazione" value="disattivaAccount">
						<button class="btn btn-warning">Disattiva</button>
					</form>
				</div>
			</div>
		</div>
	</div>
	    </body>
	    <script type="text/javascript" src="./js/bootstrap-datepicker.js"></script>
   	
   	 	<script src="./js/dataPicker.js"></script>
	    
	    </html>