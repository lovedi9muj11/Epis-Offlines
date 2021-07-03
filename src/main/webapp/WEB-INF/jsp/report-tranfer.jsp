<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="../layout/header.jsp"></jsp:include>
<jsp:include page="../layout/menu.jsp"></jsp:include>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>

<script type="text/javascript">
	var ORDER_PAYMENT = '(รายการรับชำระ)';
	var ORDER_CANCEL = '(รายการยกเลิกรับชำระ)';
</script>

<script src="${contextPath}/resources/lib/jquery-3.3.1.min.js"></script>
<script src="${contextPath}/resources/js/bootstrap.min.js"></script>
<link href="${contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
<link href="${contextPath}/resources/css/maximus.css" rel="stylesheet">
<script type="text/javascript" src="${contextPath}/resources/css/styles/DataTables/datatables.min.js"></script>
<script type="text/javascript" src="${contextPath}/resources/css/styles/DataTables/DataTables-1.10.15/js/dataTables.bootstrap.js"></script>
<!-- <script type="text/javascript" src="https://cdn.datatables.net/1.10.16/css/jquery.dataTables.min.css"></script> -->
<script src="${contextPath}/resources/js/report-tranfer.js"></script>
<script src="${contextPath}/resources/js/utils.js" type="text/javascript"></script>
<script src="${contextPath}/resources/lib/autoNumeric-1.7.4.js"></script>
<title>EPIS Offline</title>

</head>

<body>
	<header class="header_page"></header>
		<input type="hidden" value="${pageContext.request.userPrincipal.name}" name="userLogin" id="userLogin">
<%-- 			<input type="hidden" value="${pageContext.request.userPrincipal.name}" name="roleLogin" id="roleLogin"> --%>
		<!-- main panel -->
		<div style="padding: 30px 10px 50px 50px">
			<h1 class="page-header"></h1>
			<div class="panel" id="panel1">
				<div class="panel-heading">ประวัติการส่งข้อมูลเข้าสู่ระบบออนไลน์</div>
				<input type="hidden" name="userName" id="userName" value="${pageContext.request.userPrincipal.name}"/>
				<div class="panel-body">
					<sec:authorize access="hasAuthority('SUP')">
					<div class="row">
						<div class="form-group col-md-6">
							<label class="col-md-4 control-label text-right">วันที่ชำระ :</label>
							<div class='col-md-4'>
								<input type='date' class="form-control" id="dateFrom" name="dateFrom"/>
							</div>
						</div>
						<div class="form-group col-md-6">
						<label class="col-md-2 control-label text-right">เริ่มเวลา :</label>
							<div class="col-md-2">
								<select class="form-control" name="dateFromHour" id="dateFromHour">
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
							<div class="col-md-2">
								<select class="form-control" name="dateFromMinute" id="dateFromMinute">
									<option value="00">00</option>
									<option value="15">15</option>
									<option value="30">30</option>
									<option value="45">45</option>
									<option value="59">59</option>
								</select>
							</div>
								
							<div class="hide" id="error-end-date" style="font-size: 16px;">
							<div class="col-md-2"></div>
                                <label class="col-md-10 error"> <font color="red">วันที่เริ่มต้นต้องน้อยกว่าหรือเท่ากับวันที่สิ้นสุด</font></label>
                            </div>
                            
							<label class="col-md-2 control-label text-right">ถึงเวลา :</label>
								<div class="col-md-2">
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
								<div class="col-md-2">
									<select class="form-control" name="dateToMinute" id="dateToMinute">
										<option value="00">00</option>
										<option value="15">15</option>
										<option value="30">30</option>
										<option value="45">45</option>
										<option value="59">59</option>
									</select>
								</div>
								
								<div class="hide" id="error-end-date2" style="font-size: 16px; red;">
								<div class="col-md-2"></div>
	                                <label class="col-md-10 error"> <font color="red">วันที่สิ้นสุดต้องมากกว่าหรือเท่ากับวันที่เริ่มต้น</font></label>
	                            </div>
						</div>
					</div>
					</sec:authorize>
					
					<sec:authorize access="hasAuthority('USER')">
					<div class="row">
						<div class="form-group col-md-6">
							<label class="col-md-4 control-label text-right">วันที่ชำระ :</label>
							<div class='col-md-4'>
								<input type='date' class="form-control" id="dateFrom" name="dateFrom"/>
							</div>
						</div>
						<div class="form-group col-md-6">
						<label class="col-md-2 control-label text-right">เริ่มเวลา :</label>
							<div class="col-md-2">
								<select class="form-control" name="dateFromHour" id="dateFromHour" disabled>
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
							<div class="col-md-2">
								<select class="form-control" name="dateFromMinute" id="dateFromMinute" disabled>
									<option value="00">00</option>
									<option value="15">15</option>
									<option value="30">30</option>
									<option value="45">45</option>
									<option value="59">59</option>
								</select>
							</div>
							
							<div class="hide" id="error-end-date" style="font-size: 16px;">
							<div class="col-md-2"></div>
                                <label class="col-md-10 error"> <font color="red">วันที่เริ่มต้นต้องน้อยกว่าหรือเท่ากับวันที่สิ้นสุด</font></label>
                            </div>
                            
							<label class="col-md-2 control-label text-right">ถึงเวลา :</label>
								<div class="col-md-2">
									<select class="form-control" name="dateToHour" id="dateToHour" disabled>
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
								<div class="col-md-2">
									<select class="form-control" name="dateToMinute" id="dateToMinute" disabled>
										<option value="00">00</option>
										<option value="15">15</option>
										<option value="30">30</option>
										<option value="45">45</option>
										<option value="59">59</option>
									</select>
								</div>
								
								<div class="hide" id="error-end-date2" style="font-size: 16px; red;">
								<div class="col-md-2"></div>
	                                <label class="col-md-10 error"> <font color="red">วันที่สิ้นสุดต้องมากกว่าหรือเท่ากับวันที่เริ่มต้น</font></label>
	                            </div>
						</div>
					</div>
					</sec:authorize>
				</div>
	
				<div class="box-footer" style="padding-bottom: 20px">
					<div class="row">
						<!-- Button -->
						<div class="col-md-12 text-center">
							<button id="search" name="search" class="btn btn-primary" style="width: 7%" onclick="search()">ค้นหา</button>
	
							<button id="clear" name="clear" class="btn btn-danger" style="width: 7%" onclick="clearCriteria()">ลบ</button>
						</div>
					</div>
				</div>
				<form id="paymentFrom" method="post" class="form-horizontal" role="form">
					<input type="hidden" name="dateFromHidden" id="dateFromHidden">
					<input type="hidden" name="dateToHidden" id="dateToHidden">
					<input type="hidden" name="dateToHidden2" id="dateToHidden2">
					<input type="hidden" name="machinePaymentNameHidden" id="machinePaymentNameHidden">
					<input type="hidden" name="accountIdHidden" id="accountIdHidden">
					<input type="hidden" name="authoritiesHidden" id="authoritiesHidden">
					<input type="hidden" value="IBACSS" name="serviceType" id="serviceType">
					
				</form>
				<div class="row" style="padding-bottom: 10px;padding-right: 2px">
					<div class="col-md-12 text-right">
					
					</div>
				</div>
	
				<div class="row" >
					<div class="col-md-12">
						<div class="box box-solid">
							<!--<div class="box-header"></div>
							 /.box-header -->
							<div class="box-body">
								<table id="reportPaymentTb" class="table table-bordered" style="padding-bottom: 10px;margin-left: 1px">
									<thead>
										<tr>
											<th style="text-align: center;width: 5%">ลำดับที่</th>
											<th style="text-align: center;width: 7%">วันที่ส่งข้อมูลเข้าออนไลน์</th>
											<th style="text-align: center;width: 12%">วันที่ออกใบเสร็จรับเงิน</th>	
											<th style="text-align: center;width: 10%">จำนวนรายการที่ส่งสำเร็จ</th>
											<th style="text-align: center;width: 10%">จำนวนรายการที่ส่งไม่สำเร็จ</th>
<!-- 											<th style="text-align: center;width: 10%">หน่วยงานรับรายได้</th> -->
											<th style="text-align: center;width: 15%">ดำเนินการโดย</th>
											
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
		        <h4 class="modal-title" id="myModalLabel">รายละเอียดรายการส่งข้อมูลไม่สำเร็จ</h4>
		      </div>
		      	<div class="modal-body">
					<div class="row">
						<div class="form-group col-md-12">
							<table id="reportRecriptTb" class="table table-bordered" style="padding-bottom: 10px;margin-left: 20px;">
									<thead>
										<tr>
											<th style="text-align: center;width: 5%">ลำดับที่</th>
											<th style="text-align: center;width: 10%">เลขที่ใบแจ้งค่าใช้บริการ</th>
											<th style="text-align: center;width: 20%">เลขที่ใบเสร็จรับเงิน</th>	
																				
										</tr>
									</thead>
								</table>
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
