<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>


<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
<script src="${contextPath}/resources/lib/jquery-3.3.1.min.js"></script>
<script src="${contextPath}/resources/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${contextPath}/resources/css/styles/DataTables/datatables.min.js"></script>
<script type="text/javascript" src="${contextPath}/resources/css/styles/DataTables/DataTables-1.10.15/js/dataTables.bootstrap.js"></script>
<script src="${contextPath}/resources/js/userMgt.js"></script>
<script src="${contextPath}/resources/js/HoldOn.js"></script>
<title>Epis Offline</title>

<link href="${contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">

</head>
<body>
<div id="wrapper">
<jsp:include page="../layout/header.jsp"></jsp:include>
<jsp:include page="../layout/menu.jsp"></jsp:include>
	<header class="header_page"></header>

<form id="reportFrom" method="post" class="form-horizontal" role="form" >
	<div id="page-content-wrapper">
	<input name="rptCode" id="rptCode" value="${paymentResultReq.rptCode}" type="hidden">
		<br />
		<div class="container-fluid">
		<div class="panel-heading bHead" style="background-color: #ee7600;">จัดการข้อมูล</div>
		<div class="panel">
		<br />
			<div class="row">
				<div class="col-md-12 col-sm-12">
<!-- 					<div class="form-group"> -->
<!-- 						<label class="col-md-2 control-label right">ชื่อ : </label> -->
<!-- 						<div class="col-md-3 right"> -->
<!-- 							<input type="text" id="name" class="form-control"></input> -->
<!-- 						</div> -->
<!-- 						<div class="col-md-2 left"> -->
<!-- 							<button type="button" id="search" class="btn btn-primary btn3d" onclick="searchClick()">ค้นหา</button> -->
<!-- 							<button type="button" id="report" class="btn btn-primary btn3d" onclick="reportClick()">Report Excel</button> -->
<!-- 						</div> -->
<!-- 					</div> -->
					<div class="panel panel-default glasshd">
						<div class="panel-heading">
							<h4 class="panel-title">
								<a data-toggle="collapse" href="#collapse2">Sync Data Online</a>
							</h4>
						</div>
						<div id="collapse2" class="panel-collapse collapse in">
							<div class="panel-body">
								<div class="glass">
									<div class="form-group">
										<label class="col-sm-4 control-label right">1. Sync MapGL<font color="red">*</font> : </label>
										<div class="col-sm-4">
											<button type="button" id="glBtn" class="btn btn-success"><i class="fa fa-fw fa-check"></i>Click</button>
										</div>
									</div><br>
									
<!-- 									</div> -->
<!-- 									<div class="glass"> -->
									<div class="form-group">
										<label class="col-sm-4 control-label right">2. Master Data<font color="red">*</font> : </label>
										<div class="col-sm-4">
											<button type="button" id="msBtn" class="btn btn-success"><i class="fa fa-fw fa-check"></i>Click</button>
										</div>
									</div><br>
<!-- 									</div> -->
<!-- 									<div class="glass"> -->
									<div class="form-group">
										<label class="col-sm-4 control-label right">3. Sync User<font color="red">*</font> : </label>
										<div class="col-sm-4">
											<button type="button" id="userBtn" class="btn btn-success"><i class="fa fa-fw fa-check"></i>Click</button>
										</div>
									</div><br>
									
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
<!-- 			<br /> -->
			
<!-- 			<div class="row"> -->
<!-- 			    <div class="col-md-12 col-sm-12"> -->
<!-- 			        <div class="glass"> -->
<!-- 			            <div class="">          -->
<!-- 			                <div class="table-responsive">  -->
<!-- 			                    <table id="userList" class="table table-striped table-hover"> -->
<!-- 			                        <thead> -->
<!-- 			                            <tr> -->
<!-- 			                                <th style="text-align: center;" width="10%">#</th> -->
<!-- 							                <th style="text-align: center;" width="40%">Name</th> -->
<!-- 							                <th style="text-align: center;" width="30%">Role</th> -->
<!-- 							                <th style="text-align: center;" width="20%">Action</th> -->
<!-- 			                            </tr> -->
<!-- 			                        </thead> -->
<!-- 			                        <tbody> -->
<!-- 			                        </tbody> -->
<!-- 			                    </table> -->
<!-- 			                </div> -->
<!-- 			            </div> -->
<!-- 			        </div> -->
<!-- 			    </div> -->
<!-- 			</div> -->
			
		</div>
		</div>
	</div>
</form>
</div>
</body>
<jsp:include page="../layout/footer.jsp"></jsp:include>
</html>