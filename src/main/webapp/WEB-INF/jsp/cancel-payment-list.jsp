<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="../layout/header.jsp"></jsp:include>
<jsp:include page="../layout/menu.jsp"></jsp:include>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>

<script type="text/javascript">
var PLS_SELECT = 'กรุณาเลือก';
</script>

<script src="${contextPath}/resources/lib/jquery-3.3.1.min.js"></script>
<script src="${contextPath}/resources/js/bootstrap.min.js"></script>
<link href="${contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
<link href="${contextPath}/resources/css/maximus.css" rel="stylesheet">
<script type="text/javascript" src="${contextPath}/resources/css/styles/DataTables/datatables.min.js"></script>
<script type="text/javascript" src="${contextPath}/resources/css/styles/DataTables/DataTables-1.10.15/js/dataTables.bootstrap.js"></script>
<script type="text/javascript" src="${contextPath}/resources/css/styles/Dialog/bootbox.min.js"></script>
<!-- <script type="text/javascript" src="https://cdn.datatables.net/1.10.16/css/jquery.dataTables.min.css"></script> -->
	
<script type="text/javascript" src="${contextPath}/resources/js/cancel-payment.js"></script>
<script src="${contextPath}/resources/js/utils.js" type="text/javascript"></script>
<script src="${contextPath}/resources/lib/autoNumeric-1.7.4.js"></script>
<title>Epis Offline</title>

</head>
<body>
	<header class="header_page"></header>
	<!-- main panel -->
	<form id="cancelForm" method="post" class="form-horizontal" role="form">
		<input  type="hidden" name="documentNo" id="documentNo" value="">
		<input  type="hidden" name="chkPaymentType" id="chkPaymentType" value="">
		<input type="hidden" name="${_csrf.parameterName}" id="loginName" value="${_csrf.token}" />
	</form>
	<div  style="padding: 30px 10px 50px 50px">
		<h1 class="page-header"></h1>
	  <div name="error" id="error"  class="alert alert-danger alert-dismissable fade in" style="display: none; font-size: 150%;">
	    <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
	    <strong>Error : </strong> รหัสผ่านไม่ถูกต้อง
	  </div>
	  <div name="success" id="success"  class="alert alert-success alert-dismissable fade in" style="display: none; font-size: 150%;">
	    <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
	    <strong>ยกเลิกรายการสำเร็จ</strong>
	  </div>
	  <div name="notClear" id="notClear"  class="alert alert-danger alert-dismissable fade in" style="display: none; font-size: 150%;">
	    <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
	    <strong>ไม่สามารถยกเลิกได้ เนื่องจากรายการนี้ถูกส่งข้อมูลไปยังระบบออนไลน์แล้ว</strong>
	  </div>
	  <div name="notClear2" id="notClear2"  class="alert alert-danger alert-dismissable fade in" style="display: none; font-size: 150%;">
	    <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
	    <strong>ไม่สามารถยกเลิกได้ เนื่องจากรายการนี้ยกเลิกสำเร็จแล้ว</strong>
	  </div>
		<ul class="list-inline">
			<li id="li1">ค้นหาข้อมูลการชำระบริการ</li> >>
			<li id="li2">ระบุเหตุผลการยกเลิกชำระ</li> >>
			<li id="li3">ผลการยกเลิกการชำระ</li>
		</ul>

		<div class="panel">
			<div id="panel1">
				<div class="panel-heading" >ค้นหาข้อมูล</div>
				<div class="panel-body">
					<div class="row">
						<div class="form-group col-md-6">
							<label class="col-md-5 control-label text-right">เลขที่ใบแจ้งค่าบริการ</label>
							<div class="col-md-6">
								<input type="text" id="billNumber" class="form-control text-left">
							</div>
						</div>
						<div class="form-group col-md-6">
							<label class="col-md-2 control-label text-right">เลขที่ใบเสร็จรับเงิน</label>
							<div class="col-md-6">
								<input type="text" id="receiptNumber" class="form-control">
							</div>
						</div>
					</div>
<!-- 					<div class="row"> -->
<!-- 						<div class="form-group col-md-6"> -->
<!-- 							<label class="col-sm-5 control-label right">BarCode:</label> -->
<!-- 							<div class="col-sm-6"> -->
<!-- 								<input class="form-control" type="text" id="barCode" name="barCode" onchange="setDataBC()"> -->
<!-- 							</div> -->
<!-- 						</div> -->
<!-- 					</div> -->
	
				</div>
				
				<div class="box-footer">
					<div class="row">
						<!-- Button -->
						<div class="col-md-12 text-center">
							<button id="search" name="search" class="btn btn-primary" onclick="search()" style="width: 7%">ค้นหา</button>
							<button id="clear" name="clear" class="btn btn-danger" onclick="clearCriteria()" style="width: 7%">ลบ</button>
						</div>
					</div>
				</div>
			</div>

			<div id="panel2">
				<div class="panel-heading">เหตุผลการยกเลิก</div>
				<div class="panel-body">
					<div class="row">
						<div class="form-group col-md-12">
							<label class="col-md-4 control-label text-right">เหตุผลยกเลิกรับชำระ<span style="color: red"> *</span></label>
							<div class="col-md-4">
								<select class="form-control" id="problemCancel" name="userGroup">
<!-- 									<option value="">== เลือก ==</option> -->
<!-- 									<option value="01">รับชำระผิดบริการ </option> -->
<!-- 									<option value="02">ชื่อ-ที่อยู่ ไม่ถูกต้อง</option> -->
								</select>
							</div>
						</div>
					</div>
				</div>
	
				<div class="box-footer">
					<div class="row">
						<!-- Button -->
						<div class="col-md-12 text-center">
							<button id="submitCancelPM" name="submitCancelPM" class="btn btn-primary example3" onclick="showReasonCancel()" style="width: 7%">ตกลง</button>
						</div>
					</div>
				</div>
			</div>
			
			
			<div class="row">
				<div class="col-md-12">
					<div class="box box-solid">
						<!--<div class="box-header"></div>
						 /.box-header -->
						<div class="box-body">
							<table id="cancelPaymentTB" class="table table-bordered" data-maintain-selected="true">
								<thead>
							        <tr>
							         	<th id="formate"></th>  
								        <th id="radioSelect"></th>  
								        <th style="text-align: center;">#</th>   				                         
						                <th style="text-align: center;">เลขที่ใบเสร็จรับเงิน</th>
						                <th style="text-align: center;">วันที่ออกใบเสร็จ</th>
						                <th style="text-align: center;">วันที่ทำรายการ</th>
						                <th style="text-align: center;">เลขที่ลูกค้า</th>
						                <th style="text-align: center;">ชื่อลูกค้า</th>
						                <th style="text-align: center; width: 250px;">วิธีการชำระ</th>
						                <th style="text-align: center;">จำนวนเงิน</th>
						                <th style="text-align: center;">สถานที่รับชำระ</th>
						                <th style="text-align: center;">ผู้รับชำระ</th>
						                <th style="text-align: center;">สถานะ</th>
							        </tr>
						    	</thead>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	
	<!-- dialog confirm authentication.. -->
	<div class="modal fade"  role="dialog" aria-labelledby="mySmallModalLabel" aria-hidden="true" id="mi-modal" >
	  <div class="modal-dialog modal-sm" style="width:450px">
	    <div class="modal-content">
	      <div class="modal-header">
	        <h4 class="modal-title" id="myModalLabel">ยืนยันตัวตน</h4>
	      </div>
	      	<div class="modal-body">
				<div class="row">
					<div class="form-group col-md-12">
						<label class="col-md-4 control-label">ชื่อเข้าระบบ</label>
						<div class="col-md-8">
							<input type="text" id="userName" name="userName" class="form-control">
						</div>
					</div>
					<div class="form-group col-md-12">
						<label class="col-md-4 control-label">รหัสเข้าระบบ</label>
						<div class="col-md-8">
							<input type="password" id="password" name="password" class="form-control">
						</div>
					</div>
				</div>
	     	</div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-primary" id="modal-btn-si">ตกลง</button>
	        <button type="button" class="btn btn-danger" id="modal-btn-no">ยกเลิก</button>
	      </div>
	    </div>
	  </div>
	</div>
	
	<div class="modal fade"  role="dialog" aria-labelledby="mySmallModalLabel" aria-hidden="true" id="reason-cancel" >
	  <div class="modal-dialog modal-sm" style="width:650px">
	    <div class="modal-content">
	      <div class="modal-header">
	        <h4 class="modal-title" id="myModalLabel">แก้ไขข้อมูลชื่อ - ที่อยู่</h4>
	      </div>
		<div class="modal-body">
			<div class="row">
				<div class="form-group col-md-12">
					<label class="col-md-3 control-label text-right">ชื่อ<span style="color: red"> *</span></label>
					<div class="col-md-9">
						<input type="text" name="fullName" id="fullName" class="form-control text-left">
					</div>
				</div>
				<div class="form-group col-md-12" id="addressShow" name="addressShow">
					<label class="col-md-3 control-label text-right">ที่อยู่ลูกค้า <span style="color: red"> *</span></label>
					<div class="col-md-9">
						<textarea id="address" name="address" class="form-control" rows="5"></textarea>
					</div>
				</div>
			</div>
     	</div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-primary" id="modal-btn-reason-yes" onclick="modalConfirmReason(true)">ตกลง</button>
	        <button type="button" class="btn btn-danger" id="modal-btn-reason-no" onclick="modalConfirmReason(false)">ยกเลิก</button>
	      </div>
	    </div>
	  </div>
	</div>

</body>
<jsp:include page="../layout/footer.jsp"></jsp:include>
</html>
