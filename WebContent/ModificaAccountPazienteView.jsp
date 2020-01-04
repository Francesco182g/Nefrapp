<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@  taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
    	<meta http-equiv="X-UA-Compatible" content="IE=edge">
    	<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    	<meta name="description" content="">
    	<meta name="author" content="luca Esposito, Silvio Di Martino">
		<title>Modifica Paziente - Nefrapp</title>
    	
    	<%@include file="./includes/cssLinks.jsp"%>
		
		<link rel="stylesheet" type="text/css" href="./css/bootstrap-datepicker.css" >
   	 	<!-- Script per la registrazione -->

  

   	
   	 	
	</head>

	<body id="page-top">
	
		<!-- Page Wrapper -->
	    <div id="wrapper">
			<%@include file="./includes/sidebar.jsp" %>		
				
	        <!-- Content Wrapper -->
	        <div id="content-wrapper" class="d-flex flex-column">
	
	            <!-- Main Content -->
	            <div id="content">
					<%@include file="./includes/header.jsp" %>
					 	<%String intestazione=""; %>
   	 	<%String azione=""; %>
   	 	<c:if test="${isAmministratore}">
   	 		<%azione="./GestioneAmministratore"; intestazione="Modifica Paziente";%>
   	 	</c:if>
   	 	<c:if test="${isPaziente}">
   	 		<c:set var ="azione" value="./GestionePaziente?operazione=modificaAccount"></c:set>
   	 		<c:set var = "nome" value="${utente.nome}"></c:set>
            <c:set var = "cognome" value="${utente.cognome}"></c:set>
			<c:set var= "sesso" value="${utente.sesso}"></c:set>
			<c:set var= "codiceFiscale" value="${utente.codiceFiscale}"></c:set>
			<c:set var= "email" value="${utente.email}"></c:set>
            <c:set var= "dataDiNascita" value="${utente.getDataFormattata()}"></c:set>
            <c:set var= "luogoDiNascita" value="${utente.luogoDiNascita}"></c:set>
            <c:set var= "residenza" value="${utente.residenza}"></c:set>
   	 	</c:if>
	
	                <!-- Begin Page Content -->
	                <div class="container-fluid">
							   
							    <div class="my-5">
							      <div class="card-body p-0 d-flex justify-content-center">
							        <!-- Nested Row within Card Body -->
							        <div class="row">
							          <div class="col-sm-12">
							            <div class="card o-hidden border-0 shadow-sm p-5">
							              <div class="text-center">
							                <h1 class="h4 text-gray-900 mb-4"><%=intestazione %></h1>
							              </div>
							              <form id="modificaForm" class="user" method="post" action="${azione}">
											
											<input type="hidden" name="operazione" value="modifica">
											<input type="hidden" name="tipoUtente" value="paziente">
											<input type="hidden" id="notifica" value="${requestScope.notifica}">
											<div class="form-group row col-lg-12">
							                Codice Fiscale:
							                  <input type="text" class="form-control form-control-user" name="codiceFiscale" id="codiceFiscale" value="${codiceFiscale}" readonly>
							                </div>
							                <div class="form-group row">
							                  <div class="col-sm-6 mb-3 mb-sm-0">
							                  Nome:
							                    <input type="text" class="form-control form-control-user" name="nome" id="nome" value="${nome}" required="required" min="2" max="30" maxlength="30">
							                  </div>
							                  <div class="col-sm-6">
							                  Cognome:
							                    <input type="text" class="form-control form-control-user" name="cognome" id="cognome" value="${cognome}" required="required" min="2" max="30" maxlength="30">
							                  </div>
							                </div>
        		0					        <c:if test="${sesso eq 'M' }">
											<div class="form-group row">
													<div class="col-lg-4 col-mb-12 col-sm-4">
							                    	Sesso:
							                    	</div>
							                    	<div class="col-lg-4 col-mb-12 col-sm-12">
							                    	<input type="radio"  name="sesso" value="M" checked="checked"> Maschio
							                    	</div>
							                    	<div class="col-lg-4 col-mb-12 col-sm-12">
                    								<input type="radio" name="sesso" value="F"> Femmina
                    								</div>
                    								<br>	
                  							</div>
                  							</c:if>
                  							
                  							 <c:if test="${sesso eq 'F' }">
											<div class="form-group row">
													<div class="col-lg-4 col-mb-12 col-sm-4">
							                    	Sesso:
							                    	</div>
							                    	<div class="col-lg-4 col-mb-12 col-sm-12">
							                    	<input type="radio"  name="sesso" value="M"> Maschio
							                    	</div>
							                    	<div class="col-lg-4 col-mb-12 col-sm-12">
                    								<input type="radio" name="sesso" value="F" checked="checked"> Femmina
                    								</div>
                    								<br>	
                  							</div>
                  							</c:if>
                  							 <c:if test="${empty sesso}">
											<div class="form-group row">
													<div class="col-lg-4 col-mb-12 col-sm-4">
							                    	Sesso:
							                    	</div>
							                    	<div class="col-lg-4 col-mb-12 col-sm-12">
							                    	<input type="radio"  id="M" name="sesso" value="M"> Maschio
							                    	</div>
							                    	<div class="col-lg-4 col-mb-12 col-sm-12">
                    								<input type="radio" id="F" name="sesso" value="F" checked="checked"> Femmina
                    								</div>
                    								<br>	
                  							</div>
                  							</c:if>
                  							
                  							<div class="form-group row col-lg-12">
                  							Data di nascita:
							                  <input type="text" class="form-control form-control-user" id="dataDiNascita" name="dataDiNascita"  value="${dataDiNascita}"  autocomplete="off">
							                </div>
							                <div class="form-group row col-lg-12">
                  							Luogo di Nascita:
							                  <input type="text" class="form-control form-control-user" name="luogoDiNascita" id="luogoDiNascita"  value="${luogoDiNascita}" >
							                </div>
							                <div class="form-group row col-lg-12">
                  							Residenza:
							                  <input type="text" class="form-control form-control-user" name="residenza" id="residenza"  value="${residenza}" min="5" max="50" maxlength="50" >
							                </div>
                  							<div class="form-group row col-lg-12">
                  							Email:
							                  <input type="email" class="form-control form-control-user" id="email" name="email" placeholder="Email"  value="${email}"  min="6" max="50" maxlength="50">
							                </div>
							                <div class="form-group row col-lg-12">
							                Password:
							                   <input type="password" class="form-control form-control-user" name="password" id="password" placeholder="Password" min="6" max="20" maxlength="20"> 
							                </div>
							                <div class="form-group row col-lg-12">
							                Conferma Password:
							                   <input type="password" class="form-control form-control-user" name="confermaPsw" id="confermaPsw" placeholder="Conferma Password" min="6" max="20" maxlength="20"> 
							                </div>
							                <div class="col-sm-5 sm-12" style="margin-left:auto;margin-right:auto;display:block;margin-top:22%;margin-bottom:0%">
							                <button type="submit" class="btn btn-primary btn-user btn-block" id="registrazioneButton" form="modificaForm">
							                  Modifica
							                </button>
							               </div>
							              </form>
							              
							            </div>
							          </div>
							        </div>
							        
							      </div>
							    </div>
						
						  </div>
	
	                </div>
	                <!-- /.container-fluid -->
	
	            </div>
	            <!-- End of Main Content -->
	           </div>
				<%@include file="./includes/footer.jsp" %>	
	        
	        <!-- End of Content Wrapper -->
	
	    <!-- End of Page Wrapper -->
	
	    <!-- Scroll to Top Button-->
	    <a class="scroll-to-top rounded" href="#page-top">
	        <i class="fas fa-angle-up"></i>
	    </a>
	</body>
<%@include file="./includes/scripts.jsp"%>
<script type="text/javascript" src="./js/bootstrap-datepicker.js"></script>
<script src="./js/ParameterControl.js"></script>
<script src="./js/dataPicker.js"></script>
<script src="./js/autoCompleteInput.js"></script>