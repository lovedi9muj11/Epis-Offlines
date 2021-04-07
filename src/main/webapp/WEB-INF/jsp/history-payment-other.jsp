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
	
<script src="${contextPath}/resources/js/history-payment-other.js" type="text/javascript"></script>
<script src="${contextPath}/resources/js/utils.js" type="text/javascript"></script>
<script src="${contextPath}/resources/lib/autoNumeric-1.7.4.js"></script>

<style>
	div.glass {
		height: 30% !important;
	}
</style>

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
						<label class="col-md-3 control-label text-right">เลขที่ลูกค้า</label>
						<div class="col-md-3">
							<input type="text" id="billAccount" name="billAccount" class="form-control text-left">
						</div>
						<div class="col-md-6">
							<button id="searchCriteria" name="searchCriteria" class="btn btn-primary" onclick="search()" style="width: 7%">ค้นหา</button>
							<button id="clearCriteria" name="clearCriteria" class="btn btn-danger" style="width: 7%">ลบ</button>
							<button name="clearCriteria" class="btn btn-info" style="width: 20%" onclick="findOtherCustomer()">ค้นหาสำหรับลูกค้าขาจร</button>
						</div>
					</div>
				</div>
<!-- 				<div class="row"> -->
<!-- 					<div class="form-group col-md-12"> -->
<!-- 						<label class="col-md-3 control-label text-right">เลขประจำตัวผู้เสียภาษี</label> -->
<!-- 						<div class="col-md-3"> -->
<!-- 							<input type="text" id="taxId" name="taxId" class="form-control text-left"> -->
<!-- 						</div> -->
<!-- 						<label class="col-md-3 control-label text-right">ชื่อลูกค้า/ชื่อนิติบุคคล/ราชการ</label> -->
<!-- 						<div class="col-md-3"> -->
<!-- 							<input type="text" id="custName" name="custName" class="form-control text-left"> -->
<!-- 						</div> -->
<!-- 					</div> -->
<!-- 				</div> -->

			</div>
			
<!-- 			<div class="box-footer"> -->
<!-- 				<div class="row" style="padding-bottom:10px"> -->
<!-- 					Button -->
<!-- 					<div class="col-md-12 text-center"> -->
<!-- 						<button id="searchCriteria" name="searchCriteria" class="btn btn-primary" onclick="search()" style="width: 7%">ค้นหา</button> -->
<!-- 						<button id="clearCriteria" name="clearCriteria" class="btn btn-danger" style="width: 7%">ลบ</button> -->
<!-- 					</div> -->
<!-- 				</div> -->
<!-- 			</div> -->
		</div>
		
		
		<div class="panel" id="addressCustomerPM">
			<div class="panel-heading">ประวัติการชำระ</div>
			<div class="panel-body">
				<div class="col-md-12">
					<div class="box box-solid">
						<!--<div class="box-header"></div>
						 /.box-header -->
						<div class="box-body">
							<table id="histroryPaymentTB" class="table table-bordered">
							    <thead>
							        <tr>
							        	<th></th>
							         	<th style="text-align: center;width: 1%">#</th>  				                         
						                <th style="text-align: center;width: 11%">วันที่ทำรายการ</th>
						                <th style="text-align: center;;width: 11%">วันที่ออกใบเสร็จรับเงิน</th>
						                <th style="text-align: center;;width: 10%">เลขที่ใบเสร็จรับเงิน</th>
						                <th style="text-align: center;;width: 10%">เลขที่ลูกค้า</th>
						                <th style="text-align: center;;width: 10%">ชื่อลูกค้า</th>
						                <th style="text-align: center;;width: 10%">สถานที่รับชำระ</th>
						                <th style="text-align: center;;width: 8%">ผู้รับชำระ</th>
						                <th style="text-align: center;;width: 10%">ยอดเงินค่าบริการ</th>
						                <th style="text-align: center;;width: 10%">ส่วนลด</th>
						                <th style="text-align: center;;width: 10%">ส่วนลดพิเศษ</th>
						                <th style="text-align: center;;width: 20%">วิธีการรับชำระ</th>
						                <th style="text-align: center;;width: 5%">ยอดชำระ</th>
<!-- 						                <th style="text-align: center;;width: 10%">ภาษีมูลค่าเพิ่มจากการรับชำระ</th> -->
						                <th style="text-align: center;;width: 5%">สถานะ</th>
						                <th style="text-align: center;;width: 3%">หมายเหตุ</th>
							        </tr>
							    </thead>
							    <tbody>
								</tbody>
							</table>
<!-- 							<table class="table " id="showResultTableRQ"> -->
<!-- 								<thead align="center"> -->
<!-- 									<tr align="center"> -->
<!-- 										<th style='text-align: center;'>ประเภทบริการ</th> -->
<!-- 										<th style='text-align: center;'>ชื่อบริการ</th> -->
<!-- 										<th style='text-align: center;'>จำนวนรายการ</th> -->
<!-- 										<th style='text-align: center;'>จำนวนเงิน</th> -->
<!-- 										<th style='text-align: center;'>ส่วนลดก่อน  VAT</th> -->
<!-- 										<th style='text-align: center;'>ภาษีมูลค่าเพิ่ม</th> -->
<!-- 										<th style='text-align: center;'>ส่วนลดพิเศษ</th> -->
<!-- 										<th style='text-align: center;'>ยอดเงินรวม</th> -->
<!-- 									</tr> -->
<!-- 								</thead> -->
<!-- 								<tbody> -->
<!-- 								</tbody> -->
<!-- 							</table> -->
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

	<div class="modal fade"  role="dialog" aria-labelledby="mySmallModalLabel" aria-hidden="true" id="other-customer" >
	  <div class="modal-dialog modal-sm" style="width:750px">
	    <div class="modal-content">
	      <div class="modal-header">
	        <h4 class="modal-title" id="myModalLabel"><span style="font-size: 18px;" class="hidden-xs glyphicon glyphicon-user"></span> ค้นหาลูกค้าขาจร</h4>
	      </div>
	      	<div class="modal-body">
				
				<div class="row">
					<div class="form-group col-md-12">
						<label class="col-md-4 control-label md-offset-2">เลขประจำตัวผู้เสียภาษี :</label>
						<div class="col-md-8">
							<input type="text" id="taxOtherId" name="taxOtherId" class="form-control" maxlength="13" onkeypress="checkTaxIdNumber(event)">
						</div>
					</div>
				</div>
				
				<div class="row">
					<div class="form-group col-md-12">
						<label class="col-md-4 control-label md-offset-2">ชื่อลูกค้า/ชื่อนิติบุคคล/ราชการ :</label>
						<div class="col-md-8">
							<input type="text" id="nameOtherId" name="nameOtherId" class="form-control">
						</div>
					</div>
				</div>
				
				<div class="row">
					<div class="form-group col-md-12 text-center">
				        <button type="button" class="btn btn-primary" id="modal-btn-si" onclick="modalConfirmOtherCust(true)">ค้นหา</button>
				        <button type="button" class="btn btn-danger" id="modal-btn-no" onclick="modalConfirmOtherCust(false)">ยกเลิก</button>
					</div>
				</div>
				
				<div class="row">
					<div class="col-md-12 col-sm-12">
						<div class="glass">
							<div class="table-responsive">
								<table id="custOtherList" class="table table-striped table-hover">
									<thead>
										<tr>
											<th style="text-align: center;" width="10%">#</th>
											<th style="text-align: center;" width="40%">ชื่อลูกค้า</th>
											<th style="text-align: center;" width="40%">TAX ID</th>
											<th style="text-align: center;" width="10%"></th>
										</tr>
									</thead>
									<tbody>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>

	     	</div>
<!-- 	      <div class="modal-footer"> -->
<!-- 	        <button type="button" class="btn btn-primary" id="modal-btn-si" onclick="modalConfirmOtherCust(true)">ตกลง</button> -->
<!-- 	        <button type="button" class="btn btn-danger" id="modal-btn-no" onclick="modalConfirmOtherCust(false)">ยกเลิก</button> -->
<!-- 	      </div> -->
	    </div>
	  </div>
	</div>

</body>
<jsp:include page="../layout/footer.jsp"></jsp:include>
</html>
