<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="../layout/header.jsp"></jsp:include>
<jsp:include page="../layout/menu.jsp"></jsp:include>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
<!-- <script src="https://code.jquery.com/jquery-1.12.4.js"></script> -->
<script src="${contextPath}/resources/lib/jquery-3.3.1.min.js"></script>
<script src="${contextPath}/resources/js/bootstrap.min.js"></script>
<link href="${contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
<link href="${contextPath}/resources/css/maximus.css" rel="stylesheet">
<script type="text/javascript" src="${contextPath}/resources/css/styles/DataTables/datatables.min.js"></script>
<script type="text/javascript" src="${contextPath}/resources/css/styles/DataTables/DataTables-1.10.15/js/dataTables.bootstrap.js"></script>
<!-- <script type="text/javascript" src="https://cdn.datatables.net/1.10.16/css/jquery.dataTables.min.css"></script> -->
	
<script src="${contextPath}/resources/js/histrory-payment.js" type="text/javascript"></script>
<script src="${contextPath}/resources/js/utils.js" type="text/javascript"></script>
<script src="${contextPath}/resources/lib/autoNumeric-1.7.4.js"></script>

<title>Epis Offline</title>

</head>
<body>
	<header class="header_page"></header>
	<!-- main panel -->
	<div  style="padding: 30px 10px 50px 50px">
		<h1 class="page-header"></h1>
		<div class="panel" id="criteriaHistroryPM">
			<div class="panel-heading">ค้นหาข้อมูล</div>
			<div class="panel-body">
				<div class="row">
					<div class="form-group col-md-12">
						<label class="col-md-4 control-label text-right">เลขที่ลูกค้า</label>
						<div class="col-md-4">
							<input type="text" id="billAccount" name="billAccount" class="form-control text-left">
						</div>
					</div>
				</div>

			</div>
			
			<div class="box-footer">
				<div class="row" style="padding-bottom:10px">
					<!-- Button -->
					<div class="col-md-12 text-center">
						<button id="searchCriteria" name="searchCriteria" class="btn btn-primary" onclick="search()" style="width: 7%">ค้นหา</button>
						<button id="clearCriteria" name="clearCriteria" class="btn btn-danger" style="width: 7%">ลบ</button>
					</div>
				</div>
			</div>
		</div>
		
		
		<div class="panel" id="addressCustomerPM">
			<div class="panel-heading">ประวัติการชำระ</div>
			<div class="panel-body">
				<div class="col-md-12">
					<div class="box box-solid">
						<!--<div class="box-header"></div>
						 /.box-header -->
						<div class="box-body">
							<table id="histroryPaymentTB" class="table table-bordered" cellspacing="0" width="100%">
						    <thead>
						        <tr>
						         	<th style="text-align: center;width: 1%">#</th>
					                <th style="text-align: center;width: 11%">วันที่ทำรายการ</th>
					                <th style="text-align: center;;width: 11%">วันที่ออกใบเสร็จรับเงิน</th>
					                <th style="text-align: center;;width: 10%">เลขที่ใบเสร็จรับเงิน</th>
					                <th style="text-align: center;;width: 10%">เลขที่ลูกค้า</th>
					                <th style="text-align: center;;width: 10%">ชื่อลูกค้า</th>
					                <th style="text-align: center;;width: 10%">สถานที่รับชำระ</th>
					                <th style="text-align: center;;width: 5%">ผู้รับชำระ</th>
					                <th style="text-align: center;;width: 10%">เลขที่ใบแจ้งค่าใช้บริการ</th>
					                <th style="text-align: center;;width: 10%">รอบการใช้งาน</th>
					                <th style="text-align: center;;width: 10%">ยอดเงินตามใบแจ้งค่าบริการ</th>
					                <th style="text-align: center;;width: 10%">วิธีการรับชำระ</th>
					                <th style="text-align: center;;width: 5%">ยอดชำระ</th>
					                <th style="text-align: center;;width: 20%">ภาษีมูลค่าเพิ่ม</th>
					                <th style="text-align: center;;width: 5%">สถานะ</th>
					                <th style="text-align: center;;width: 3%">หมายเหตุ</th>
						        </tr>
						    </thead>
						</table>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<div class="modal fade"  role="dialog" aria-labelledby="mySmallModalLabel" aria-hidden="true" id="remake_dialog">
	  <div class="modal-dialog modal-sm" style="width:600px">
	    <div class="modal-content">
	      <div class="modal-header">
	        <h4 class="modal-title" id="myModalLabel">หมายเหตุ</h4>
	      </div>
	      	<div class="modal-body">
				<div class="row">
					<div class="form-group col-md-12">
<!-- 						<input type="text" id="remake" name="remake" class="form-control" > -->
						<label id="remake" class="control-label"></label>
					</div>
				</div>
	     	</div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-primary" id="modal-btn-si" onclick="closeDialog()">ตกลง</button>
	      </div>
	    </div>
	  </div>
	</div>


</body>
<jsp:include page="../layout/footer.jsp"></jsp:include>
</html>
