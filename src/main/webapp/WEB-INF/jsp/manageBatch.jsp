<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
<script type="text/javascript">
var PLS_SELECT = 'กรุณาเลือก';
var PLS_SELECT_ALL_MONTH = 'ทุกเดือน';
var PLS_SELECT_ALL_TYPE = 'ประเภท';
var PLS_SELECT_MONTH = 'เดือน';
var PLS_SELECT_ALL_MONTH_LAST_DAY = 'ทุกสิ้นเดือน';
var PLS_SELECT_ALL_DAY = 'ทุกวัน';
var PLS_SELECT_ALL_HOUR = 'ทุกชั่วโมง';
var PLS_SELECT_ALL_MINUTE = 'ทุกนาที';
</script>

<script src="${contextPath}/resources/lib/jquery-3.3.1.min.js"></script>
<script src="${contextPath}/resources/lib/select2.min.js"></script>
<script src="${contextPath}/resources/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${contextPath}/resources/css/styles/DataTables/datatables.min.js"></script>
<script type="text/javascript" src="${contextPath}/resources/css/styles/DataTables/DataTables-1.10.15/js/dataTables.bootstrap.js"></script>
<script src="${contextPath}/resources/js/manageBatch.js"></script>
<script src="${contextPath}/resources/js/HoldOn.js"></script>
<%-- <script src="${contextPath}/resources/js/utils.js"></script> --%>
<title>Epis Offline</title>

<link href="${contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
<link href="${contextPath}/resources/css/select2.min.css" rel="stylesheet">

</head>
<body>
	<div id="wrapper">
		<jsp:include page="../layout/header.jsp"></jsp:include>
		<jsp:include page="../layout/menu.jsp"></jsp:include>
		<header class="header_page"></header>

		<form id="masterFrom" method="post" class="form-horizontal" role="form">
			<input type="hidden" id="yearNow"/>
			<input type="hidden" id="monthdd"/>
			<input type="hidden" id="daydd"/>
			<input type="hidden" id="hourdd"/>
			<input type="hidden" id="mindd"/>
			<div id="page-content-wrapper">
				<br />
				<div class="row">
					<div class="col-md-12 col-sm-12">
						<div class="form-group">
<!-- 							<label class="col-md-2 control-label right">ชื่อ : </label> -->
<!-- 							<div class="col-md-3 right"> -->
<!-- 								<input type="text" id="name" class="form-control"></input> -->
<!-- 							</div> -->
							<label class="col-md-5 control-label right">ประเภท : </label> 
							<select class="groupType col-md-2" name="groupType" id="groupType" list="groupTypeDropdown" listKey="value" listValue="name">
							</select>
						</div>

<!-- 						<div class="form-group"> -->
<!-- 							<div class="col-md-12 center "> -->
<!-- 								<button type="button" id="search" class="btn btn-primary " onclick="searchClick()">ค้นหา</button> -->
<%-- 								<a class="btn btn-success " href="${contextPath}/create-master-data">สร้าง</a> --%>
<!-- 							</div> -->
<!-- 						</div> -->
					</div>
				</div>
				<br />
				<div class="container-fluid">
					<div class="panel-heading bHead" style="background-color: #ee7600;">จัดการตั้งค่า Run Auto</div>
					<div class="panel">
						<br />
						<div class="row">
							<div class="col-md-12 col-sm-12">
								<div class="glass">
									<div class="form-group">
										<label class="col-sm-2 col-sm-offset-1 control-label">ตั้งค่าวัน/เวลา : </label>
<!-- 										<label class="col-sm-1 col-sm-offset-1 control-label">เดือน : </label> -->
										<label class="col-sm-1 control-label">เดือน : </label>
										<div class="col-sm-2">
											<select class="groupType col-md-6" name="month" id="month" list="groupTypeDropdown" listKey="value" listValue="name">
											</select>
										</div>
										
										<div class="col-md-1">
											<label class="control-label right"><input type="radio" name="radioHour" id="radioHour" value="1" onclick="batchHMode('1')" checked> เวลา </label>
											<label class="control-label right"><input type="radio" name="radioHour" id="radioHour2" value="2" onclick="batchHMode('2')"> ชั่วโมง </label>
										</div>
										<div class="col-sm-2">
											<select class="groupType col-md-6" name="hour" id="hour" list="groupTypeDropdown" listKey="value" listValue="name">
											</select>
										</div>
									</div>
									
									<div class="form-group">
										<label class="col-md-1 col-sm-offset-3 control-label right">วันที่ : </label>
										<div class="col-sm-2">
											<select class="groupType col-md-6" name="date" id="date" list="groupTypeDropdown" listKey="value" listValue="name">
											</select>
										</div>
										
<!-- 										<label class="col-md-1 control-label right">นาที : </label> -->
										<div class="col-md-1">
											<label class="control-label right"><input type="radio" name="radioMin" id="radioMin" value="1" checked disabled> เวลา </label>
											<label class="control-label right"><input type="radio" name="radioMin" id="radioMin2" value="2" disabled> นาที </label>
										</div>
										<div class="col-sm-2">
											<select class="groupType col-md-6" name="minute" id="minute" list="groupTypeDropdown" listKey="value" listValue="name">
											</select>
										</div>
									</div>
									
									<div class="form-group">
										<label class="col-sm-6 control-label">หมายเหตุ : </label>
										<div class="col-sm-2">
											<label class="control-label">*เวลา - กำหนดเป็นช่วงเวลา</label>
										</div>
									</div>
									
									<div class="form-group">
										<label class="col-sm-6 control-label"></label>
										<div class="col-sm-3">
											<label class="control-label">*ชั่วโมง/นาที - กำหนดเป็นทุกๆ กี่ชั่วโมง/ทุกๆ กี่นาที</label>
										</div>
									</div>
								</div>
							</div>
						</div>
						<div class="form-group">
							<div class="col-sm-12 center split">
								<button type="button" id="saveBtn" class="btn btn-success"><i class="fa fa-fw fa-save"></i>บันทึก</button>
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