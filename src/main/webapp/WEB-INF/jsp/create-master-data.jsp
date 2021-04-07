<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
<script src="${contextPath}/resources/lib/jquery-3.3.1.min.js"></script>
<script src="${contextPath}/resources/lib/select2.min.js"></script>
<script src="${contextPath}/resources/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${contextPath}/resources/css/styles/DataTables/datatables.min.js"></script>
<script type="text/javascript" src="${contextPath}/resources/css/styles/DataTables/DataTables-1.10.15/js/dataTables.bootstrap.js"></script>
<script src="${contextPath}/resources/js/master-data.js"></script>
<script src="${contextPath}/resources/js/HoldOn.js"></script>
<title>Epis Offline</title>

<link href="${contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
<link href="${contextPath}/resources/css/select2.min.css" rel="stylesheet">

</head>
<body>
	<div id="wrapper">
		<jsp:include page="../layout/header.jsp"></jsp:include>
		<jsp:include page="../layout/menu.jsp"></jsp:include>
		<header class="header_page"></header>

		<form id="createmasterFrom" method="post" class="form-horizontal"
			role="form">
			<div id="page-content-wrapper">
				<br />
				<div class="container-fluid">
					<div class="panel-heading bHead" style="background-color: #ee7600;">เพิ่มข้อมูล</div>
					<div class="panel">
						<br />
						<div class="row">
							<div class="col-md-12 col-sm-12">
							
								<div class="form-group">
									<label class="col-md-2 control-label right">value : </label>
									<div class="col-md-2 right">
										<input type="text" id="valueKey" class="form-control"></input>
									</div>
									
								</div>
								<div class="form-group">
									
									<label class="col-md-2 control-label right">text : </label>
									<div class="col-md-2 right">
										<input type="text" id="text" class="form-control"></input>
									</div>
									
								</div>
								<div class="form-group">
									<label class="col-md-2 control-label right">ประเภท : </label>
									<div class="col-sm-6">
										<select class="groupType col-md-3" name="groupType" id="groupType" list="groupTypeDropdown" listKey="value" listValue="name">
										</select>
										<a class="btn btn-success" href="${contextPath}/create-master-data-group">สร้างประเภท</a>
									</div>
								</div>

								<div class="form-group">
									<div class="col-md-12 center ">
										<button type="button" id="search" class="btn btn-primary " onclick="submitMaterdata()">บันทึก</button>
<%-- 										<a class="btn btn-success " href="${contextPath}/create-master-data-group">สร้างประเภท</a> --%>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</form>
	</div>
</body>
<jsp:include page="../layout/footer.jsp"></jsp:include>
</html>