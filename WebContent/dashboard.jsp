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
		<title>Home - Nefrapp</title>
    	
    	<!-- Custom fonts for this template-->
    	<script src="./vendor/jquery/jquery.min.js"></script>
    	
	</head>
	<%@  taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
	

	<body id="page-top">
		<!-- session attribute -->
		<c:set var="paziente" value='${sessionScope["paziente"]}'/>
		<c:set var="amministratore" value='${sessionScope.amministratore}'/>
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
						<c:if test="${not empty amministratore}">
							<script src="./js/loadObject.js" ></script>	
						</c:if>	
	
							  <div class="container login">
							
							    <div class="card o-hidden border-0 shadow-lg my-5">
							     
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