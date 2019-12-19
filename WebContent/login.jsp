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
		<title>Login - Nefrapp</title>
    	
    	<!-- Custom fonts for this template-->
    	<link href="../vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">
    	<link href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i" rel="stylesheet">

    	<!-- Custom styles for this template-->
   	 	<link href="../css/sb-admin-2.min.css" rel="stylesheet">	
   	 	
 	 	<script src="../vendor/jquery/jquery.min.js"></script>
   	 	
   	 	
   	 	<!-- Script per controllo del form -->
   	 	<script src="../js/ParameterControl.js"></script>
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
	
	
							  <div class="container login">
							
							    <div class="my-5">
							      <div class="card-body p-0 d-flex justify-content-center">
							        <!-- Nested Row within Card Body -->
							        <div class="row">
							          <div class="col-sm-12">
							            <div class="card o-hidden border-0 shadow-sm p-5">
							              <div class="text-center">
							                <h1 class="h4 text-gray-900 mb-4">Effettua il login</h1>
							              </div>
							              <form class="user" method="post" action="./GestioneAccesso">
							              		<input type="hidden" name="operazione" value="loginUtente">
												<div class="form-group">
							                  <input type="text" class="form-control form-control-user" name="codiceFiscale" id="codiceFiscale" placeholder="Codice fiscale" required="required" maxlength="16" min="16" max="16">
							                </div>
							                <div class="form-group">
							                   <input type="password" class="form-control form-control-user" name="password" id="password" placeholder="Password" required="required" min="6" max="20"> 
							                </div>
							                <div class="p-6">
								                  <a href="#">
								                  Password dimenticata?
								                  </a>
								            </div>
								            <div class="mt-6 p-6">
								                  <button class="btn btn-primary btn-user" type="submit" id="loginUtente" style="float:right">
								                  Accedi
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