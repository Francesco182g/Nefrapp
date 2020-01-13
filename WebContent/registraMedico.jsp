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
    	<meta name="author" content="">
		<title>Registrazione Medico - Nefrapp</title>
    	
    	<%@include file="./includes/cssLinks.jsp"%>
    	
	<link rel="stylesheet" type="text/css" href="./css/bootstrap-datepicker.css" >
   	 	<!-- Script per la registrazione -->

   	 	
   	 	<c:if test='${isAmministratore==false || isAmministratore==null}'>
			<c:redirect url = "/loginAmministratore.jsp"/>
		</c:if>
		
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
	
	
	                <!-- Begin Page Content -->
	                <div class="container-fluid">

							    <div class="my-5">
							      <div class="card-body p-0 d-flex justify-content-center">
							        <!-- Nested Row within Card Body -->
							        <div class="row">
							          <div class="col-sm-12">
							            <div class="card o-hidden border-0 shadow-sm p-5">
							              <div class="text-center">
							                <h1 class="h4 text-gray-900 mb-4">Registra Medico</h1>
							              </div>
							              <form class="user" method="post"  action="./GestioneRegistrazione">
							              	<input type="hidden" id="operazione" name="operazione" value="registraMedico">
											<div class="form-group row col-lg-12">
							                Codice Fiscale:
							                  <input type="text" class="form-control form-control-user" name="codiceFiscale" id="codiceFiscale" placeholder="Codice fiscale" required="required">
							                </div>
							                <div class="form-group row">
							                  <div class="col-sm-6 mb-3 mb-sm-0">
							                  Nome:
							                    <input type="text" class="form-control form-control-user" name="nome" id="nome" placeholder="Nome" required="required" >
							                  </div>
							                  <div class="col-sm-6">
							                  Cognome:
							                    <input type="text" class="form-control form-control-user" name="cognome" id="cognome" placeholder="Cognome" required="required">
							                  </div>
							                </div>
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
                  							<div class="form-group row col-lg-12">
                  							Data di nascita:
							                  <input type="text" class="form-control form-control-user" id="dataDiNascita" name="dataDiNascita" placeholder="DD-MM-YYYY" autocomplete="off">
							                </div>
							                <div class="form-group row col-lg-12">
                  							Luogo di Nascita:
							                  <input type="text" class="form-control form-control-user" name="luogoDiNascita" id="luogoDiNascita" placeholder="Luogo di nascita">
							                </div>
							                <div class="form-group row col-lg-12">
                  							Residenza:
							                  <input type="text" class="form-control form-control-user" id="residenza" name="residenza" placeholder="Es. Via Roma, 23, Scafati, 80030, SA" >
							                </div>
                  							<div class="form-group row col-lg-12">
                  							Email:
							                  <input type="email" class="form-control form-control-user" name="email" id="email" placeholder="Email" required="required" >
							                </div>
							                <div class="form-group row col-lg-12">
							                Password:
							                   <input type="password" class="form-control form-control-user" name="password" id="password" placeholder="Password" required="required"> 
							                </div>
							                <div class="col-sm-5 sm-12" style="margin-left:auto;margin-right:auto;display:block;margin-top:22%;margin-bottom:0%">
							                <button class="btn btn-primary btn-user btn-block" id="registrazioneMedicoButton">
							                  Registra Medico
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
</html>