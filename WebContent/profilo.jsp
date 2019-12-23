
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
		
		<!-- Script per la registrazione -->
   	 	<script src="./js/dataPicker.js"></script>

  		
	</head>
	
	<body id="page-top">
	<c:choose>     
         <c:when test = "${isPaziente == true}">
            <c:set var = "nome" value="${utente.nome}"></c:set>
            <c:set var = "cognome" value="${utente.cognome}"></c:set>
			<c:set var= "sesso" value="${utente.sesso}"></c:set>
			<c:set var= "codiceFiscale" value="${utente.codiceFiscale}"></c:set>
			<c:set var= "email" value="${utente.email}"></c:set>
            <c:set var= "dataDiNascita" value="${utente.dataDiNascita}"></c:set>
            <c:set var= "luogoDiNascita" value="${utente.luogoDiNascita}"></c:set>
            <c:set var= "residenza" value="${utente.residenza}"></c:set>
			<c:set var="dottori" value='${requestScope["mediciCuranti"]}' />
         </c:when>
         
         <c:when test = "${isMedico == true}">
            <c:set var = "nome" value="${utente.nome}"></c:set>
            <c:set var = "cognome" value="${utente.cognome}"></c:set>
    		<c:set var= "sesso" value="${utente.sesso}"></c:set>
			<c:set var= "codiceFiscale" value="${utente.codiceFiscale}"></c:set>
			<c:set var= "email" value="${utente.email}"></c:set>
            <c:set var= "dataDiNascita" value="${utente.dataDiNascita}"></c:set>
            <c:set var= "luogoDiNascita" value="${utente.luogoDiNascita}"></c:set>
            <c:set var= "residenza" value="${utente.residenza}"></c:set>
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
         				     		<c:if test="${loop.index eq 0}">
												<span class="h3 mb-4 text-gray-800">${item.nome} ${item.cognome} </span>
									</c:if>
									<c:if test="${loop.index gt 0 }">		
												<h3 class="h3 mb-4 text-gray-800" style="margin-left: 127px; line-height: 1px;">${item.nome} ${item.cognome} </h3>
									</c:if>			
							</c:forEach>
							</h2>	
         				  </c:if>
         				
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