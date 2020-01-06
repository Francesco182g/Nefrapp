<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"  errorPage="true"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
    	<meta http-equiv="X-UA-Compatible" content="IE=edge">
    	<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    	<meta name="description" content="">
    	<meta name="author" content="">
		<title>Home - Nefrapp</title>
    	
    	<%@include file="./includes/cssLinks.jsp"%>
	</head>
	<%@  taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
	

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
						<!-- 404 Error Text -->
          				<div class="text-center">
            				<div class="error mx-auto" data-text="500">500</div>
            					<p class="lead text-gray-800 mb-5">Page Not Found</p>
            					<p class="text-gray-500 mb-0">
            					<c:if test="${notifica == 'eccezione'}" >
	            					ERR#1: servlet exception<br><br><br>
	            					C'è stato un errore di sistema, ci dispiace per il contrattempo!
            					</c:if>
            					<c:if test="${notifica == 'noOperazione'}" >
	            					ERR#2: no operation selected<br><br><br>
	            					C'è stato un errore di sistema, ci dispiace per il contrattempo!
            					</c:if>
								<c:if test="${notifica == 'accessoNegato'}" >
	            					ERR#3: access policy violation<br><br><br>
	            					Ehi, tu! Sì, dico a te! Tu non puoi stare qui!
            					</c:if>
								<c:if test="${notifica == 'erroreDB'}" >
	            					ERR#4: database unavailable<br><br><br>
	            					Oh, no! È saltato il database! Ci stanno tracciando! Stacca stacca!
            					</c:if>
								</p>
            					<a href="dashboard.jsp">&larr; Torna alla dashboard</a>
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
	<%@include file="./includes/scripts.jsp"%>
</html>