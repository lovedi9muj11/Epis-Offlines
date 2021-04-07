<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />


<!DOCTYPE html>
<html>
<head>
<script>var ctx = "${pageContext.request.contextPath}"</script>
<script type="text/javascript">
var DEFALUT = 'Defalut';
</script>

<script src="${contextPath}/resources/lib/jquery-3.3.1.min.js"></script>
<script src="${contextPath}/resources/js/bootstrap.min.js"></script>
<script src="${contextPath}/resources/js/confirm-password.js"></script>
<script src="${contextPath}/resources/lib/sweetalert.min.js"></script>
<script src="${contextPath}/resources/js/HoldOn.js"></script>
<title>Epis Offline</title>

<script type="text/javascript">
var CONFIRM_SAVE = 'เปลี่ยนรหัสสำเร็จ ';
var CONFIRM_OK = 'ตกลง';
var CONFIRM_YES = 'yes';
var CONFIRM_NO = 'no';
</script>

<style type="text/css">

/* .swal-modal { */
/*   background-color: rgba(63,255,106,0.69); */
/*   border: 3px solid white; */
/* } */

</style>

<link href="${contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
<link href="${contextPath}/resources/css/styles/style.css" rel="stylesheet" media="screen" type="text/css">
<link href="${contextPath}/resources/css/styles/welcome.css" rel="stylesheet" media="screen" type="text/css">
<link href="${contextPath}/resources/css/styles/dashboard.css" rel="stylesheet" media="screen" type="text/css">
<link href="${contextPath}/resources/css/styles/display-tag.css" rel="stylesheet" media="screen" type="text/css">
<link href="${contextPath}/resources/css/styles/bootstrap/css/bootstrap.css" rel="stylesheet" media="screen" type="text/css">
<link href="${contextPath}/resources/css/styles/font-awesome-4.7.0/css/font-awesome.css" rel="stylesheet" media="screen" type="text/css">
<link href="${contextPath}/resources/css/styles/jquery-ui-1.11.4.custom/jquery-ui.css" rel="stylesheet" media="screen" type="text/css">
<link href="${contextPath}/resources/css/styles/style-menu.css" rel="stylesheet" media="screen" type="text/css">
<link href="${contextPath}/resources/css/styles/bootstrap-modify.css" rel="stylesheet" media="screen" type="text/css">
<link href="${contextPath}/resources/css/styles/bootstrap-fileinput/css/fileinput.min.css" rel="stylesheet" media="screen" type="text/css">

</head>
<body>
	<div id="wrapper">
		<jsp:include page="../layout/header.jsp"></jsp:include>
		<header class="header_page"></header>

		<form id="confirmFrom" method="post" class="form-horizontal" role="form">
			<div id="page-content-wrapper">
				<br />
<!-- 				<div class="row"> -->
<!-- 					<div class="col-md-12 col-sm-12"> -->
<!-- 						<div class="form-group"> -->
<!-- 							<label class="col-md-5 control-label right">Password : </label> -->
<!-- 						</div> -->
<!-- 					</div> -->
<!-- 				</div> -->
				<br />
				<div class="container-fluid">
					<div class="panel">
						<br />
						<div class="row">
							<div class="col-md-12 col-sm-12">
								<div class="glass">
									<div class="form-group">
										<label class="col-sm-5 control-label">รหัส : </label>
										<div class="col-sm-2">
											<input class="form-control" type="password" id="password" name="password" maxlength="21">
										</div>
										<div class="hide" id="error-password"><label class="error" style="color: red;">กรุณากรอกข้อมูล</label></div>
										<div class="hide" id="error-password-incorrect"><label class="error" style="color: red;">รหัสไม่ถูกต้อง</label></div>
									</div>
									
									<div class="form-group">
										<label class="col-md-5 control-label">รหัสใหม่: </label>
										<div class="col-sm-2">
											<input class="form-control" type="password" id="newPassword" name="newPassword" maxlength="21">
										</div>
										<div class="hide" id="error-newPassword"><label class="error" style="color: red;">กรุณากรอกข้อมูล</label></div>
										<div class="hide" id="error-newPassword-as"><label class="error" style="color: red;">รหัสไม่ตรงกัน</label></div>
										<div class="hide" id="error-newPassword-minl"><label class="error" style="color: red;">กรุณาระบุอย่างน้อย 8 ตัว</label></div>
										<div class="hide" id="error-password-format"><label class="error" style="color: red;">รูปแบบ รหัสไม่ถูกต้อง</label></div>
									</div>
									
									<div class="form-group">
										<label class="col-md-5 control-label">ยืนยันรหัส: </label>
										<div class="col-sm-2">
											<input class="form-control" type="password" id="cfNewPassword" name="cfNewPassword" maxlength="21">
										</div>
										<div class="hide" id="error-cfNewPassword"><label class="error" style="color: red;">กรุณากรอกข้อมูล</label></div>
										<div class="hide" id="error-newPassword-as2"><label class="error" style="color: red;">รหัสไม่ตรงกัน</label></div>
										<div class="hide" id="error-newPassword-minl2"><label class="error" style="color: red;">กรุณาระบุอย่างน้อย 8 ตัว</label></div>
										<div class="hide" id="error-password-format2"><label class="error" style="color: red;">รูปแบบ รหัสไม่ถูกต้อง</label></div>
									</div>
								</div>
							</div>
						</div>
						<div class="form-group">
							<div class="col-sm-12 center split">
								<button type="button" id="saveBtn" class="btn btn-success"><i class="fa fa-fw fa-check"></i>ยืนยัน</button>
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