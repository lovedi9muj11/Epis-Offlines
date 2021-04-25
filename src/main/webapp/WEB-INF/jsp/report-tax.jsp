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
<!-- <script type="text/javascript" -->
<!-- 	src="https://cdn.datatables.net/1.10.16/css/jquery.dataTables.min.css"></script> -->
<script src="${contextPath}/resources/js/report-tax.js"></script>
<title>EPIS Offline</title>

</head>

<body>
	<header class="header_page"></header>
	<!-- main panel -->
	<div style="padding: 30px 10px 50px 50px">
		<h1 class="page-header"></h1>
		<div class="panel" id="panel1">
			
				<div class="panel-heading">รายงานภาษีขายใบเสร็จรับเงิน/ใบกำกับภาษีเต็มรูป</div>
				<input type="hidden" name="userName" id="userName" value="${pageContext.request.userPrincipal.name}" />
				<div class="panel-body">
				<form name="reportTaxForm" id="reportTaxForm"  class="form-horizontal" role="form">
				<input name="rptCode" id="rptCode" value="ExcelFull" type="hidden">
				<input type="hidden" name="flagPage" id="flagPage" value="IBACSS" />
				<input type="hidden" name="typePrint" id="typePrint" value="RF" />
					<div class="row">
						<div class="form-group col-md-6">
							<label class="col-md-2 control-label text-right">วันที่ชำระ
								:</label>
							<div class="col-md-10">
								<div class='col-md-6'>
									<input type='date' class="form-control" id="dateFrom"
										name="dateFrom" />
								</div>
								<div class="col-md-3">
									<select class="form-control" name="dateFromHour"
										id="dateFromHour">
										<option value="00">00</option>
										<option value="01">01</option>
										<option value="02">02</option>
										<option value="03">03</option>
										<option value="04">04</option>
										<option value="05">05</option>
										<option value="06">06</option>
										<option value="07">07</option>
										<option value="08">08</option>
										<option value="09">09</option>
										<option value="10">10</option>
										<option value="11">11</option>
										<option value="12">12</option>
										<option value="13">13</option>
										<option value="14">14</option>
										<option value="15">15</option>
										<option value="16">16</option>
										<option value="17">17</option>
										<option value="18">18</option>
										<option value="19">19</option>
										<option value="20">20</option>
										<option value="21">21</option>
										<option value="22">22</option>
										<option value="23">23</option>
									</select>

								</div>
								<div class="col-md-3">
									<select class="form-control" name="dateFromMinute"
										id="dateFromMinute">
										<option value="00">00</option>
										<option value="15">15</option>
										<option value="30">30</option>
										<option value="45">45</option>
										<option value="59">59</option>
									</select>
								</div>

							</div>
						</div>

						<div class="form-group col-md-6">
							<label class="col-md-2 control-label text-right">ถึงวันที่
								:</label>
							<div class="col-md-10">
								<div class='col-md-6'>
									<input type='date' class="form-control" id="dateTo"
										name="dateTo" />
								</div>
								<div class="col-md-3">
									<select class="form-control" name="dateToHour" id="dateToHour">
										<option value="00">00</option>
										<option value="01">01</option>
										<option value="02">02</option>
										<option value="03">03</option>
										<option value="04">04</option>
										<option value="05">05</option>
										<option value="06">06</option>
										<option value="07">07</option>
										<option value="08">08</option>
										<option value="09">09</option>
										<option value="10">10</option>
										<option value="11">11</option>
										<option value="12">12</option>
										<option value="13">13</option>
										<option value="14">14</option>
										<option value="15">15</option>
										<option value="16">16</option>
										<option value="17">17</option>
										<option value="18">18</option>
										<option value="19">19</option>
										<option value="20">20</option>
										<option value="21">21</option>
										<option value="22">22</option>
										<option value="23">23</option>
									</select>
								</div>
								<div class="col-md-3">
									<select class="form-control" name="dateToMinute"
										id="dateToMinute">
										<option value="00">00</option>
										<option value="15">15</option>
										<option value="30">30</option>
										<option value="45">45</option>
										<option value="59">59</option>
									</select>
								</div>

							</div>
						</div>

					</div>
<!-- 					<div class="row"> -->
<!-- 					<div class="col-md-4"></div> -->
<!-- 						<div class="form-group col-md-6"> -->
<!-- 							<label class="col-md-2 control-label text-right">ประเภทใบเสร็จ -->
<!-- 								:</label> -->
<!-- 							<div class="col-md-6"> -->
<!-- 								<select class="form-control" id="typePrint" name="typePrint"> -->
<!-- 									<option value="RF">รายการภาษีแบบเต็ม</option> -->
<!-- 									<option value="RS">รายการภาษีแบบย่อ</option> -->
<!-- 								</select> -->
<!-- 							</div> -->
<!-- 						</div> -->
<!-- 					</div> -->
				</form>
					<div class="box-footer" style="padding-bottom: 20px">
						<div class="row">
							<!-- Button -->
							<div class="col-md-12 text-center">
								<button id="search" name="search" class="btn btn-primary" 
									onclick="search()" style="width: 7%">ค้นหา</button>
								<button id="clear" name="clear" class="btn btn-danger"
									style="width: 7%">ลบ</button>
							</div>
						</div>
					</div>
					<div class="row" style="padding-bottom: 10px; padding-right: 2px">
						<div class="col-md-12 text-right">
							<button 
								class="btn btn-warning glyphicon glyphicon-print"  onclick="reportPDF()"
								style="width: 7%"> PDF</button>
							<button id="excel" name="excel"   onclick="reportExcel()" 
								class="btn btn-success glyphicon glyphicon-print"
								 style="width: 7%"> Excel</button>
						</div>
					</div>

					<div class="row">
						<div class="col-md-12">
							<div class="box box-solid">
								<!--<div class="box-header"></div>
						 /.box-header -->
								<div class="box-body">
									<table id="reportTax" class="table table-bordered">
										<thead>
											<tr>
												<th style="text-align: center;">ลำดับที่</th>
												<th style="text-align: center;">วันเดือนปี</th>
												<th style="text-align: center;">เลขที่ใบกำกับภาษี</th>
												<th style="text-align: center;">ชื่อผู้ซื้อบริการ/ผู้รับบริการ</th>
												<th style="text-align: center;">เลขที่ประจำตัวผู้เสียภาษี</th>
												<th style="text-align: center;">สาขาที่</th>
												<th style="text-align: center;">มูลค่าสินค้าหรือบริการ</th>
												<th style="text-align: center;">จำนวนเงินภาษีมูลค่าเพิ่ม</th>
												<th style="text-align: center;">จำนวนเงินรวม</th>
												<th style="text-align: center;">สถานะ</th>
											</tr>
										</thead>
									</table>
								</div>
							</div>
						</div>
					</div>

				</div>
<!-- 			</form> -->
		</div>
	</div>
</body>
<jsp:include page="../layout/footer.jsp"></jsp:include>
</html>
