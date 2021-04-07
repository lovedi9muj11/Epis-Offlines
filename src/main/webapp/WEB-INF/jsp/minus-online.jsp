<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="../layout/header.jsp"></jsp:include>
<jsp:include page="../layout/menu.jsp"></jsp:include>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
<script src="${contextPath}/resources/lib/jquery-3.3.1.min.js"></script>
<script src="${contextPath}/resources/js/bootstrap.min.js"></script>
<link href="${contextPath}/resources/css/bootstrap.min.css"
	rel="stylesheet">
<link href="${contextPath}/resources/css/maximus.css" rel="stylesheet">
<script type="text/javascript"
	src="${contextPath}/resources/css/styles/DataTables/datatables.min.js"></script>
<script type="text/javascript"
	src="${contextPath}/resources/css/styles/DataTables/DataTables-1.10.15/js/dataTables.bootstrap.js"></script>
<script type="text/javascript"
	src="https://cdn.datatables.net/1.10.16/css/jquery.dataTables.min.css"></script>

<script src="${contextPath}/resources/js/minus-online.js"
	type="text/javascript"></script>
<script src="${contextPath}/resources/js/utils.js"
	type="text/javascript"></script>
<script src="${contextPath}/resources/js/autoNumeric-1.7.4.js"></script>

<title>Epis Offline</title>

</head>
<body>
	<header class="header_page"></header>
	<!-- main panel -->
	<div style="padding: 30px 10px 50px 50px">
		<h1 class="page-header"></h1>
		<div class="panel" id="criteriaHistroryPM">
			<div class="panel-heading">ค้นหาข้อมูล</div>
			<div class="panel-body">
				<div class="row">
					<div class="form-group col-md-12">
						<label class="col-md-4 control-label text-right">สถานะรายการ</label>
						<div class="col-md-4">
							<select class="form-control" id="clearing" name="clearing">
								<option value="N">ยังไม่ได้ส่งข้อมูลเข้าออนไลน์</option>
								<option value="Y">ส่งข้อมูลเข้าออนไลน์แล้ว</option>
							</select>
						</div>
					</div>
				</div>

			</div>

			<div class="box-footer">
				<div class="row" style="padding-bottom: 10px">
					<!-- Button -->
					<div class="col-md-12 text-center">
						<button id="searchCriteria" name="searchCriteria"
							class="btn btn-primary" onclick="search()" style="width: 7%">ค้นหา</button>
						<button id="clearCriteria" name="clearCriteria"
							class="btn btn-danger" style="width: 7%">ลบ</button>
					</div>
				</div>
			</div>
		</div>

		<div class="box-footer">
			<div class="row" style="padding-bottom: 10px">
				<!-- Button -->
				<div class="col-md-12 text-right">
					<button id="send" name="send" class="btn btn-success"
						onclick="dialogRemakes()" style="width: 7%">ส่งข้อมูลทั้งหมด</button>
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
							<table id="histroryPaymentTB" class="table table-bordered"
								cellspacing="0" width="100%">
								<thead>
									<tr>
										<th style="text-align: center; width: 1%"></th>
										<th style="text-align: center; width: 1%">#</th>
										<th style="text-align: center; width: 10%">วันที่ทำรายการ</th>
										<th style="text-align: center; width: 6%">วันที่ออกใบเสร็จรับเงิน</th>
										<th style="text-align: center; width: 6%">วันที่ส่งข้อมูลเข้าออนไลน์</th>
										<th style="text-align: center; width: 10%">เลขที่ใบเสร็จรับเงิน</th>
										<th style="text-align: center; width: 10%">สถานที่รับชำระ</th>
										<th style="text-align: center; width: 5%">ผู้รับชำระ</th>
										<th style="text-align: center; width: 10%">เลขที่ใบแจ้งค่าใช้บริการ</th>
										<th style="text-align: center; width: 10%">รอบการใช้งาน</th>
										<th style="text-align: center; width: 10%">ยอดเงินตามใบแจ้งค่าบริการ</th>
										<th style="text-align: center; width: 8%">วิธีการรับชำระ</th>
										<th style="text-align: center; width: 5%">ยอดชำระ</th>
										<th style="text-align: center; width: 20%">ภาษีมูลค่าเพิ่มจากการรับชำระ</th>
										<th style="text-align: center; width: 5%">สถานะ</th>
										<th style="text-align: center; width: 5%">หมายเหตุ</th>
										<th style="text-align: center; width: 5%">สถานะการส่งข้อมูล</th>
									</tr>
								</thead>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<div class="modal fade" role="dialog"
		aria-labelledby="mySmallModalLabel" aria-hidden="true"
		id="remake_dialog">
		<div class="modal-dialog modal-sm" style="width: 600px">
			<div class="modal-content">
				<div class="modal-header">
					<h4 class="modal-title" id="myModalLabel">หมายเหตุ</h4>
				</div>
				<div class="modal-body">
					<div class="row">
						<div class="form-group col-md-12">
							<input type="text" id="remake" name="remake" class="form-control"
								disabled="disabled">
						</div>
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-primary" id="modal-btn-si"
						onclick="closeDialog()">ตกลง</button>
				</div>
			</div>
		</div>
	</div>
	<div class="modal fade" role="dialog"
		aria-labelledby="mySmallModalLabel" aria-hidden="true"
		id="confrimDialog">
		<div class="modal-dialog modal-sm" style="width: 600px">
			<div class="modal-content">
				<div class="modal-header">
					<h4 class="modal-title" id="myModalLabel">ยืนยัน</h4>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-primary" id="modal-btn-si"
						onclick="confirmDialog()">ตกลง</button>
					<button type="button" class="btn btn-danger" id="modal-btn-si"
						onclick="closeDialogs()">ปิด</button>
				</div>
			</div>
		</div>
	</div>
	<div class="modal fade" role="dialog"
		aria-labelledby="mySmallModalLabel" aria-hidden="true" id="showLog">
		<div class="modal-dialog modal-sm" style="width: 600px">
			<div class="modal-content">
				<div class="modal-header">
					<h4 class="modal-title" id="myModalLabel">แสดงผล</h4>
				</div>
				<div class="modal-body">
					<div class="row">
						<div class="form-group col-md-12">
							<div class="alert alert-dark" role="alert">
								<p id="showLogs"></p>
								<p id="showLoge"></p>
							</div>
						</div>
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-danger" id="modal-btn-si"
						onclick="closeLog()">ปิด</button>
				</div>
			</div>
		</div>
	</div>
	<div class="modal fade" role="dialog"
		aria-labelledby="mySmallModalLabel" aria-hidden="true" id="errorShow">
		<div class="modal-dialog modal-sm" style="width: 600px">
			<div class="modal-content">
				<div class="modal-header">
					<h4 class="modal-title" id="myModalLabel">แสดงผล</h4>
				</div>
				<div class="modal-body">
								<p>กรุณาเลือกรายการที่ต้องส่งข้อมูล</p>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-danger" id="modal-btn-si"
						onclick="closeLogError()">ปิด</button>
				</div>
			</div>
		</div>
	</div>


</body>
<jsp:include page="../layout/footer.jsp"></jsp:include>
</html>
