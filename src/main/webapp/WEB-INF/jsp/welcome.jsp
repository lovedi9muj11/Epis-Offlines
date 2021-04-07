<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="../layout/header.jsp"></jsp:include>
<jsp:include page="../layout/menu.jsp"></jsp:include>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html>
<head>
	<script src="${contextPath}/resources/lib/jquery-3.3.1.min.js"></script>
	<script src="${contextPath}/resources/js/bootstrap.min.js"></script>
    <title>Epis Offline</title>

    <link href="${contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">

</head>
<body>
	 <header class="header_page"></header>
	
	<!-- main panel -->
	<div id="page-content-wrapper">
<!-- 		<div id="header-wrapper"> -->
		<div id="" class="row">
			<div class="col-lg-12">
				<div class="col-md-12 text-center">
					<font color="gray" style="font-size: 3em;">ระบบบูรณาการการรับชำระค่าใช้บริการ (EPIS)</font>
<!-- 					<font color="black">EPIS Offline</font> -->
				</div>
			</div>
		</div>
<!-- 		</div> -->
	</div>
</body>
<jsp:include page="../layout/footer.jsp"></jsp:include> 
</html>
