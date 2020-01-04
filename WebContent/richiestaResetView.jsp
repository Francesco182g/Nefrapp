<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
    	<meta http-equiv="X-UA-Compatible" content="IE=edge">
    	<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    	<meta name="description" content="">
    	<meta name="author" content="Davide Benedetto Strianese, Sara Corrente">
		<title>Richiesta reset password - Nefrapp</title>

		<%@include file="./includes/cssLinks.jsp"%>
   	 	
   	 	
   	 	<!-- Script per controllo del form --> <!-- TODO da inserire -->
   	 	<script></script>
	</head>

	<body id="page-top">
		<!-- Page Wrapper -->
	    <div id="wrapper">
					
	        <!-- Content Wrapper -->
	        <div id="content-wrapper" class="d-flex flex-column">
	
	            <!-- Main Content -->
	            <div id="content">
					<%@include file="../includes/header.jsp" %>
	
	
	                <!-- Begin Page Content -->
	                <div class="container-fluid">
	
	
							  <div class="container login">
							
							    <div class="my-5">
							      <div class="card-body p-0 d-flex justify-content-center">
							        <!-- Nested Row within Card Body -->
							        <div class="row">
							          <div class="col-sm-12">
							            <div class="card o-hidden border-0 shadow-sm p-5">
							              <div class="text-center">
							                <h1 class="h4 text-gray-900 mb-4">Richiedi il reset</h1>
							              </div>
							              <form class="user" method="post" action="./GestioneResetPassword" > <!-- TODO controllo in js per il formato dell'email -->
												<div class="form-group">
												<input type=hidden name="operazione" value="identificaRichiedente">
							                  <input type="text" class="form-control form-control-user" name="codiceFiscale" id="codiceFiscale" placeholder="Codice Fiscale" required="required" min="6" max="50" maxlength="50">
							                </div>
							                
								            <div class="mt-6 p-6">
								                  <button class="btn btn-primary btn-user" type="submit" id="richiestaReset" style="float:right">
								                  Invia richiesta
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
				<%@include file="includes/footer.jsp" %>	
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
	<script src="./js/ParameterControl.js"></script>
</html>