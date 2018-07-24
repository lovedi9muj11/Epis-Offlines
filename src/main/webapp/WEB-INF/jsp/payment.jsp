<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="th.co.maximus.bean.MapGLBean"%>
<jsp:include page="../layout/header.jsp"></jsp:include>
<jsp:include page="../layout/menu.jsp"></jsp:include>
<%@ page import="th.co.maximus.bean.MasterDatasBean"%>
<%@ page import="java.util.List"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<html>
<head>
<script src="${contextPath}/resources/lib/jquery-3.3.1.min.js"></script>

<script src="${contextPath}/resources/js/bootstrap.min.js"></script>
<title>Payment</title>
<script type="text/javascript"
	src="${contextPath}/resources/js/typeahead.bundle.js"></script>
<script type="text/javascript"
	src="${contextPath}/resources/css/styles/DataTables/datatables.min.js"></script>
<script type="text/javascript"
	src="${contextPath}/resources/css/styles/DataTables/DataTables-1.10.15/js/dataTables.bootstrap.js"></script>
<script type="text/javascript"
	src="${contextPath}/resources/css/styles/Dialog/bootbox.min.js"></script>
<!-- <script type="text/javascript" src="https://cdn.datatables.net/1.10.16/css/jquery.dataTables.min.css"></script> -->
<link href="${contextPath}/resources/css/bootstrap.min.css"
	rel="stylesheet">
<link href="${contextPath}/resources/css/payment.css" rel="stylesheet">
<script src="${contextPath}/resources/js/utils.js"></script>
<script src="${contextPath}/resources/js/payment.js"></script>
<script src="${contextPath}/resources/js/autoNumeric-1.7.4.js"></script>
<%
	List<MasterDatasBean> masterBankCode = null;
%>
<%
	masterBankCode = (List<MasterDatasBean>) request.getAttribute("bank");
%>
%>
</head>
<body>
	<div class="container-fluid">

		<form name="paymentFrom" method="post" action="#" id="paymentFrom"
			class="form-horizontal">
			<div id="page-content-wrapper">
				<nav class="navbar navbar-default">
				<div class="container-fluid">
					<ul class="nav navbar-nav">
						<li class="active"><a href="#">รับชำระค่าบริการ ></a></li>
						<li><a href="#">ผลการรับชำระ </a></li>
					</ul>
				</div>
				</nav>
				<div class="row">
					<div class="col-md-12 col-sm-12">
						<div class="form-group" align="right">
							<div class="col-md-12 col-sm-12">
								<button name="submitFormPayment" type="button"
									id="submitFormPayment" class="btn btn-success btn-lg"
									onclick="submitForm()">
									<span class="glyphicon glyphicon-share"> บันทึกและพิมพ์</span>
								</button>
							</div>
						</div>
					</div>
				</div>

				<div class="row" style="margin-top: 20px;">
					<input type="hidden" id="userNames" name="userNames"
						value="${pageContext.request.userPrincipal.name}">
					<div class="col-md-12 col-sm-12">
						<div class="form-group">
							<div class="col-md-12 col-sm-12">
								<div class="panel">
									<div class="panel-heading">เพิ่มใบรับชำระค่าบริการ</div>
									<div class="panel-body">
										<div class="col-md-12 col-sm-12">
											<div class="row">
												<div class="form-group">
													<label class="col-sm-2 control-label right" for="custNo">เลขที่ลูกค้า
														:<span style="color: red;">*</span>
													</label>
													<div class="col-sm-2">
														<input class="form-control" type="text" id="custNo"
															name="custNo" placeholder="เลขที่ลูกค้า">
														<p id="sCustNo" style="color: red; display: none;">คุณยังไม่ได้กรอก
															เลขที่ลูกค้า</p>
													</div>
													<label class="col-sm-2 control-label right" for="custName">ชื่อ:</label>
													<div class="col-sm-2">
														<input class="form-control" type="text" id="custName"
															name="custName" placeholder="ชื่อ">

													</div>
													<label class="col-sm-2 control-label right"
														for="formGroupInputLarge">Tax ID :</label>
													<div class="col-sm-2">
														<input class="form-control" type="text" id="taxId"
															name="taxId" placeholder="Tax ID" maxlength="13">
													</div>
												</div>
											</div>
											<div class="row">
												<div class="form-group">
													<label class="col-sm-2 control-label right"
														for="formGroupInputLarge">ที่อยู่ :</label>
													<div class="col-sm-6">
														<textarea class="form-control" rows="3" id="custAddress"
															name="custAddress"></textarea>
													</div>
													<label class="col-sm-2 control-label right"
														for="formGroupInputLarge">สาขา : </label>
													<div class="col-sm-2">
														<input class="form-control" type="text" id="custBrach"
															name="custBrach" placeholder="สาขา" maxlength="5">
													</div>

												</div>

											</div>
											<div class="row">
												<div class="form-group left">
													<label class="col-sm-2 control-label right"
														for="formGroupInputLarge">กลุ่มผู้ใช้บริการ :<span
														style="color: red;">*</span></label>
													<div class="col-sm-2">
														<select class="form-control" id="userGroup"
															name="userGroup" onchange="autoSelect()">
															<option value="">== กรุณาเลือก ==</option>
															<option value="1">ธุรกิจทั่วไป</option>
															<option value="2">หน่วยงานรัฐ</option>
															<option value="3">บุคคลทั่วไป</option>
															<option value="4">Carrier/Operator/NON POTs</option>
															<option value="5">Mkt.Arm</option>
															<option value="6">ISP</option>
															<option value="7">Reseller/Agent</option>
															<option value="8">ธุรกิจ กสท</option>
															<option value="9">สถานฑูต/องค์กรระหว่างประเทศ</option>
														</select>
														<p id="suserGroup" style="color: red; display: none;">
															คุณยังไม่ได้เลือก กลุ่มผู้ใช้บริการ</p>
													</div>


													<div class="col-sm-2" hidden>
														<select class="form-control" id="debtCollection"
															name="debtCollection">
															<option value="">== กรุณาเลือก ==</option>
															<option value="ตร.">ตร.</option>
															<option value="กม.">กม.</option>
															<option value="ปง.">ปง.</option>
															<option value="ทต.">ทต.</option>
														</select>

													</div>
													<label class="col-sm-2 control-label right" for="invoiceNo">เลขที่ใบแจ้ง
														:<span style="color: red;">*</span>
													</label>
													<div class="col-sm-2">
														<input class="form-control" type="text" id="invoiceNo"
															name="c" placeholder="เลขที่ใบแจ้ง">
														<p id="sinvoiceNo" style="color: red; display: none;">
															คุณยังไม่ได้เลือก เลขที่ใบแจ้ง</p>

													</div>
													<label class="col-sm-2 control-label right"
														for="formGroupInputLarge">หมายเลขบริการ :</label>
													<div class="col-sm-2">
														<input class="form-control" type="text" id="serviceNo"
															name="serviceNo" placeholder="หมายเลขบริการ">
														<!-- 														<p id="sserviceNo" style="color: red;"> คุณยังไม่ได้เลือก หมายเลขบริการ</p> -->
													</div>
												</div>
											</div>
											<div class="row">
												<div class="form-group">

													<label class="col-sm-2 control-label right"
														for="formGroupInputLarge">รอบการใช้งานเริ่มต้น :<span
														style="color: red;">*</span></label>
													<div class="col-sm-2">
														<input class="form-control" type="date" id="startupDate"
															name="startupDate" onchange="datePriod()">
														<p id="sstartupDate" style="color: red; display: none;">
															คุณยังไม่ได้เลือก รอบการใช้งานเริ่มต้น</p>
														<p id="sstartupDate1" style="color: red; display: none;">
															รอบการใช้งานเริ่มต้นให้น้อยกว่า "รอบใช้งานสิ้นสุด"</p>
													</div>
													<label class="col-sm-2 control-label right"
														for="formGroupInputLarge">รอบการใช้งานสิ้นสุด : <span
														style="color: red; display: none;">*</span></label>
													<div class="col-sm-2">
														<input class="form-control" type="date" id="endDate"
															name="endDate" onchange="datePriod1()">
														<p id="sendDate" style="color: red; display: none;">
															คุณยังไม่ได้เลือก รอบการใช้งานสิ้นสุด</p>
														<p id="sendDate1" style="color: red; display: none;">
															รอบการใช้งานสิ้นสุดให้มากกว่า "รอบใช้งานเริ่มต้น"</p>
													</div>
												</div>
											</div>
											<div class="row">
												<div class="form-group">
													<label class="col-sm-2 control-label right"
														for="formGroupInputLarge">วันครบกำหนด :<span
														style="color: red; display: none;">*</span></label>
													<div class="col-sm-2">
														<input class="form-control" type="date" id="deadlines"
															name="deadlines">
														<p id="sdeadlines" style="color: red; display: none;">
															คุณยังไม่ได้เลือก วันครบกำหนด</p>
													</div>
													<label class="col-sm-2 control-label right"
														for="formGroupInputLarge">วันจัดทำใบแจ้งค่าใช้บริการ
														: <span style="color: red; display: none;">*</span>
													</label>
													<div class="col-sm-2">
														<input class="form-control" type="date" id="invoiceDate"
															name="invoiceDate">
														<p id="sinvoiceDate" style="color: red; display: none;">
															คุณยังไม่ได้เลือก วันจัดทำใบแจ้งค่าใช้บริการ</p>
													</div>
												</div>
											</div>

											<hr>
											<div class="row">
												<div class="col-sm-8"></div>
												<div class="form-group ">
													<label class="col-sm-2 control-label right"
														for="formGroupInputLarge">VAT RATE : </label>
													<div class="col-sm-2">
														<select class="form-control" id="vatrate" name="vatrate"
															onchange="findvatAmount()">
															<option value="7">7%</option>
															<!-- <option value="0">0%</option>
															<option value="NON VAT">NON VAT</option> -->
														</select>
													</div>
												</div>
											</div>
											<div class="row">
												<div class="col-sm-8"></div>
												<div class="form-group ">
													<label class="col-sm-2 control-label right"
														for="formGroupInputLarge">ยอดก่อนภาษี : </label>
													<div class="col-sm-2">
														<input class="form-control numeric2point" type="text"
															id="balanceBeforeTax" name="balanceBeforeTax" readonly="">
													</div>
												</div>
											</div>
											<div class="row">
												<div class="col-sm-8"></div>
												<div class="form-group ">
													<label class="col-sm-2 control-label right"
														for="formGroupInputLarge">ภาษีมูลค่าเพิ่ม : </label>
													<div class="col-sm-2">
														<input class="form-control numeric2point" type="text"
															id="vat" name="vat" readonly="">
													</div>
												</div>
											</div>
											<div class="row">
												<div class="col-sm-8"></div>
												<div class="form-group ">
													<label class="col-sm-2 control-label right"
														for="formGroupInputLarge">จำนวนเงินรวมภาษี : </label>
													<div class="col-sm-2">
														<input class="form-control numeric2point" type="text"
															id="balanceOfTax" name="balanceOfTax" readonly="">

													</div>
												</div>
											</div>
											<div class="row">
												<div class="col-sm-8"></div>
												<div class="form-group ">
													<label class="col-sm-2 control-label right"
														for="formGroupInputLarge">ยอดชำระจากใบแจ้งหนี้: <span
														style="color: red;">*</span></label>
													<div class="col-sm-2">
														<input class="form-control numeric2point" type="text"
															id="balanceOfTaxPrice" name="balanceOfTaxPrice">

													</div>
												</div>
											</div>
											<div class="row">
												<div class="col-sm-8"></div>
												<div class="form-group ">
													<label class="col-sm-2 control-label right"
														for="formGroupInputLarge">ยอดที่ต้องชำระ: <span
														style="color: red;">*</span></label>
													<div class="col-sm-2">
														<input class="form-control numeric2point" type="text"
															id="balanceSummary" name="balanceSummary">
														<p id="sBalanceSummary" style="color: red; display: none;">
															ยอดเงินของคุณ เกินยอดชำระจากใบแจ้งหนี้</p>
													</div>
												</div>
											</div>


										</div>

									</div>
								</div>
							</div>

						</div>
					</div>

				</div>
				<div class="row">
					<div class="col-md-6 col-sm-6">
						<div class="form-group">
							<div class="col-md-12 col-sm-12">
								<div class="panel">
									<div class="panel-heading">
										รายการหัก <select class="form-control"
											style="display: inline; width: 30%; padding-left: 20px">
											<option>ภาษีหัก ณ ที่จ่าย</option>
										</select>
									</div>
									<div class="panel-body">
										<input name="paymentTax.invoiceNo" id="invoiceNoTax"
											type="hidden">
										<div class="col-md-12 col-sm-12">
											<div class="row">
												<div class="form-group">
													<label class="col-sm-4 control-label right"
														for="formGroupInputLarge">ประเภทภาษีหัก ณ ที่จ่าย
														:</label>
													<div class="col-sm-6">
														<label> <input type="radio" name="radioDed"
															id="radioDedCD" value="01" checked> 69 ทวิ
														</label> <label> <input type="radio" name="radioDed"
															id="radioDedCC" value="02"> 3 เตรส
														</label> <label> <input type="radio" name="radioDed"
															id="radioDedCT" value="03"> 69 ตรี
														</label>
													</div>
												</div>
											</div>
											<div class="row">
												<div class="form-group ">
													<label class="col-sm-4 control-label right"
														for="formGroupInputLarge">เลขที่เอกสาร :</label>
													<div class="col-sm-6">
														<input class="form-control" type="text" id="docDed"
															name="paymentTax.docDed" placeholder="เลขที่เอกสาร">
														<p id="sdocDed" style="color: red; display: none;">คุณยังไม่ได้กรอก
															เลขที่เอกสาร</p>
													</div>
												</div>
											</div>
											<div class="row">
												<div class="form-group ">
													<label class="col-sm-4 control-label right"
														for="formGroupInputLarge">จำนวนเงิน :</label>
													<div class="col-sm-6">
														<input class="form-control numeric2point" type="text"
															id="moneyDed" name="paymentTax.moneyDed"
															placeholder="จำนวนเงิน">
														<p id="smoneyDed" style="color: red; display: none;">
															คุณยังไม่ได้กรอก จำนวนเงิน</p>
													</div>
												</div>
											</div>
											<div class="row">
												<div class="form-group">
													<div class="col-sm-10 right">
														<a onclick="addRow()" id="addRow" name="addRow"
															class="btn btn-warning"><span
															class="glyphicon glyphicon-plus">เพิ่มรายการภาษีหัก
																ณ ที่จ่าย</span></a>
														<button onclick="addRow()" id="addRowShow"
															disabled="disabled" name="addRowShow"
															class="btn btn-warning">
															<span class="glyphicon glyphicon-plus">เพิ่มรายการภาษีหัก
																ณ ที่จ่าย</span>
														</button>
														<p id="saddRow" style="color: red; display: none;">
															ลบรายการรับชำระออกก่อน</p>
													</div>

												</div>
											</div>
											<div class="row">
												<div class="form-group">
													<div class="col-sm-12">
														<table class="table table-striped" id="deductibleTable">
															<thead>
																<tr>
																	<th>#</th>
																	<th>เลขที่ใบแจ้งค่าใช้บริการ</th>
																	<th>เลขที่เอกสาร</th>
																	<th>ประเภทหัก ณ ที่จ่าย</th>
																	<th>จำนวนเงิน</th>
																</tr>
															</thead>
															<tbody>

															</tbody>
														</table>
													</div>
												</div>
											</div>
											<div class="row">
												<div class="form-group">
													<div class="col-sm-12 right">
														<button type="button" class="btn btn-primary"
															id="btnAddprice" onclick="addDataTableDed()">
															<span class="glyphicon glyphicon-plus">เพิ่มรายการหัก</span>
														</button>
														<button onclick="addRow()" id="addRowShow1"
															disabled="disabled" name="addRowShow1"
															class="btn btn-primary">
															<span class="glyphicon glyphicon-plus">เพิ่มรายการหัก</span>
														</button>
														<p id="saddRow1" style="color: red; display: none;">
															ลบรายการรับชำระออกก่อน</p>
													</div>
												</div>
											</div>


										</div>
									</div>
								</div>
							</div>


						</div>
					</div>
					<div class="col-md-6 col-sm-6">
						<div class="form-group">
							<div class="col-md-12 col-sm-12">
								<div class="panel">
									<div class="panel-heading">
										วิธีการรับชำระ <select class="form-control" id="typePayment"
											name="typePayment" onchange="findTypePayment()"
											style="display: inline; width: 30%; padding-left: 20px">
											<option value="money">เงินสด</option>
											<option value="credit">บัตรเครดิต</option>
											<option value="check">เช็ค</option>
										</select>
									</div>

									<div class="panel-body">
										<div class="col-md-12 col-sm-12">
											<div id="money">

												<div class="row">
													<div class="form-group">
														<label class="col-sm-8 control-label right"
															for="formGroupInputLarge">จำนวนเงิน :</label>
														<div class="col-sm-4">
															<input class="form-control numeric2point" type="text"
																placeholder="จำนวนเงิน" id="moneyTran"
																name="paymentTranPrice.moneyTran">
														</div>

													</div>
												</div>
											</div>

											<div id="credit">
												<div class="row">
													<div class="form-group">
														<label class="col-sm-2 control-label right"
															for="formGroupInputLarge">ประเภทของบัตรเครดิต:</label>
														<div class="col-sm-4">
															<select class="form-control" id="creditType"
																name="paymentTranPrice.creditType">
																<option value="">กรุณาเลือก</option>
																<option value="visa">VISA</option>
																<option value="masterCard">MASTER-CARD</option>
															</select>
														</div>
														<label class="col-sm-2 control-label right"
															for="formGroupInputLarge">เลขที่บัตร:</label>
														<div class="col-sm-4">
															<input class="form-control" type="text" id="creditNo"
																maxlength="16" name="paymentTranPrice.creditNo"
																placeholder="เลขที่บัตร">
														</div>
													</div>
												</div>
												<div class="row">
													<div class="form-group">
														<label class="col-sm-2 control-label right"
															for="formGroupInputLarge">ธนาคารเจ้าของเครื่อง
															(EDC):</label>
														<div class="col-sm-4">
															<select class="form-control" id="edcType"
																name="paymentTranPrice.edcType">
																<option value="">กรุณาเลือก</option>
																<%
																	for (int i = 0; i < masterBankCode.size(); i++) {
																%>
																<option id="nameBank"
																	value="<%=masterBankCode.get(i).getKeyCode()%>"><%=masterBankCode.get(i).getValue()%></option>
																<%
																	}
																%>
															</select>
														</div>
														<label class="col-sm-2 control-label right"
															for="formGroupInputLarge">จำนวนเงิน :</label>
														<div class="col-sm-4">
															<input class="form-control numeric2point" type="text"
																id="creditPrice" name="paymentTranPrice.creditPrice"
																placeholder="จำนวนเงิน">
														</div>
													</div>
												</div>
												<div class="row">
													<div class="form-group">
														<div class="col-sm-12 right">
															<button type="button" class="btn btn-warning"
																onclick="addDataTablecreditTranPrice()">
																<span class="glyphicon glyphicon-plus">เพิ่มรายการบัตรเครดิต</span>
															</button>
														</div>
													</div>
												</div>
												<div class="row">
													<div class="form-group">
														<div class="col-sm-12">
															<table class="table" id="creditTable">
																<thead>
																	<tr>
																		<th>#</th>
																		<th>ประเภทบัตรเครดิต</th>
																		<th>เลขที่บัตร</th>
																		<th>EDC</th>
																		<th>จำนวนเงิน</th>
																	</tr>
																</thead>
																<tbody>
																</tbody>
															</table>
														</div>
													</div>
												</div>
											</div>

											<div id="check">
												<div class="row">
													<div class="form-group">
														<label class="col-sm-2 control-label right"
															for="formGroupInputLarge">รหัสธนาคาร:</label>
														<div class="col-sm-4">
															<select class="form-control" id="bankNo"
																name="paymentTranPrice.bankNo" onchange="findBank()">
																<option value="">กรุณาเลือก</option>
																<%
																	for (int i = 0; i < masterBankCode.size(); i++) {
																%>
																<option value="<%=masterBankCode.get(i).getKeyCode()%>"><%=masterBankCode.get(i).getKeyCode()%></option>
																<%
																	}
																%>
															</select>
														</div>
														<label class="col-sm-2 control-label right"
															for="formGroupInputLarge">เลขที่เช็ค:</label>
														<div class="col-sm-4">
															<input class="form-control" type="text" id="checkNo"
																maxlength="7" name="paymentTranPrice.checkNo"
																placeholder="เลขที่เช็ค">
														</div>
													</div>
												</div>
												<div class="row">
													<div class="form-group">
														<label class="col-sm-2 control-label right"
															for="formGroupInputLarge">ชื่อธนาคาร :</label>
														<div class="col-sm-4">
															<select class="form-control" id="bankName"
																name="paymentTranPrice.bankName" onchange="findBankNo()">
																<option value="">กรุณาเลือก</option>
																<%
																	for (int i = 0; i < masterBankCode.size(); i++) {
																%>
																<option id="nameBank"
																	value="<%=masterBankCode.get(i).getKeyCode()%>"><%=masterBankCode.get(i).getValue()%></option>
																<%
																	}
																%>
															</select>


														</div>
														<label class="col-sm-2 control-label right"
															for="formGroupInputLarge">วันที่หน้าเช็ค :</label>
														<div class="col-sm-4">
															<input class="form-control" type="date" id="dateCheck"
																name="paymentTranPrice.dateCheck">
														</div>
													</div>
												</div>
												<div class="row">
													<div class="form-group">
														<label class="col-sm-2 control-label right"
															for="formGroupInputLarge">สาขา :</label>
														<div class="col-sm-4">
															<input class="form-control" type="text" id="branchCheck"
																name="paymentTranPrice.branchCheck" placeholder="สาขา">
														</div>
														<label class="col-sm-2 control-label right"
															for="formGroupInputLarge">จำนวนเงิน :</label>
														<div class="col-sm-4">
															<input class="form-control numeric2point" type="text"
																id="moneyCheck" name="paymentTranPrice.moneyCheck"
																placeholder="จำนวนเงิน">
														</div>
													</div>
												</div>
												<div class="row">
													<div class="form-group">
														<div class="col-sm-12 right">
															<button type="button" class="btn btn-warning"
																onclick="addDataTableCheck()">
																<span class="glyphicon glyphicon-plus">เพิ่มรายการเช็ค</span>
															</button>
														</div>
													</div>
												</div>
												<div class="row">
													<div class="form-group">
														<div class="col-sm-12">
															<table class="table" id="checkTable">
																<thead>
																	<tr>
																		<th>#</th>
																		<th>รหัสธนาคาร</th>
																		<th>ชื่อธนาคาร</th>
																		<th>สาขา</th>
																		<th>เลขที่เช็ค</th>
																		<th>วันที่หน้าเช็ค</th>
																		<th>จำนวนเงิน</th>
																	</tr>
																</thead>
																<tbody>
																</tbody>
															</table>
														</div>
													</div>
												</div>
											</div>
											<div class="col-md-12 col-sm-12">
												<div class="row">
													<div class="form-group">
														<div class="col-sm-12 right">
															<button type="button" class="btn btn-primary"
																onclick="sumTranPrice()">
																<span class="glyphicon glyphicon-plus">เพิ่มวิธีการรับชำระ</span>
															</button>
														</div>
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>

						</div>
					</div>
				</div>
				<!-- panel รายการหัก -->
				<div class="row">
					<div class="col-md-6 col-sm-6">
						<div class="form-group">
							<div class="col-md-12 col-sm-12">
								<div class="panel">
									<div class="panel-heading" style="background-color: #ee7600;">สรุปรายการหัก</div>
									<div class="panel-body">
										<div class="col-md-12 col-sm-12">
											<div style="display: none">
												<table id="sumDeductibleTable">
													<thead>
														<tr></tr>
													</thead>
													<tbody></tbody>
												</table>
											</div>
											<table class="table" id="showDeductibleTable">
												<thead>
													<tr align="center">
														<th>#</th>
														<th>รายการหัก</th>
														<th>จำนวนเงิน</th>
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
					</div>
					<div class="col-md-6 col-sm-6">
						<div class="form-group">
							<div class="col-md-12 col-sm-12">
								<div class="panel">
									<div class="panel-heading" style="background-color: #ee7600;">สรุปวิธีการชำระเงิน</div>
									<div class="panel-body">
										<div class="col-md-12 col-sm-12">
											<div style="display: none">
												<table id="sumTotalPriceTable">
													<thead>
														<tr></tr>
													</thead>
													<tbody></tbody>
												</table>
											</div>
											<table class="table" id="showTotalPriceTable">

												<thead>
													<tr>
														<th>#</th>
														<th>วิธีการชำระเงิน</th>
														<th>จำนวนเงิน</th>
														<th></th>
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
					</div>
					<!-- Summary  -->
					<div class="col-md-12 col-sm-12">
						<div class="form-group">
							<div class="col-md-12 col-sm-12">
								<div class="panel">
									<div class="panel-heading" style="background-color: #ee7600;">สรุปยอดเงินที่ต้องชำระ</div>
									<div class="panel-body">
										<div class="col-md-12 col-sm-12">
											<div class="row">

												<div class="form-group ">
													<label class="col-sm-1 control-label right"
														for="formGroupInputLarge">เพิ่มเติม :</label>
													<div class="col-sm-3">
														<input class="form-control" type="text" id="remark"
															name="remark" maxlength="100">
													</div>
												</div>
											</div>
											<div class="row">
												<div class="col-sm-7"></div>
												<div class="form-group ">
													<label class="col-sm-2 control-label right"
														for="formGroupInputLarge">ยอดเงินที่ต้องชำระก่อนภาษีมูลค่าเพิ่ม
														:</label>
													<div class="col-sm-3">
														<input class="form-control" type="hidden"
															id="balanceBeforeTaxs" readonly="">
													</div>
													<div class="col-sm-3">
														<input class="form-control numeric2point" type="text"
															id="balanceBeforeTaxsShow" readonly="">
													</div>
												</div>
											</div>
											<div class="row">
												<div class="col-sm-7"></div>
												<div class="form-group ">
													<label class="col-sm-2 control-label right"
														for="formGroupInputLarge">ภาษีมูลค่าเพิ่ม :</label>
													<div class="col-sm-3">
														<input class="form-control" type="hidden" id="vats"
															readonly=""> <input
															class="form-control numeric2point" type="text"
															id="vatsShow" readonly="">
													</div>
												</div>
											</div>
											<div class="row">
												<div class="col-sm-7"></div>
												<div class="form-group ">
													<label class="col-sm-2 control-label right"
														for="formGroupInputLarge">ยอดเงินที่ต้องชำระรวมภาษีมูลค่าเพิ่ม
														:</label>
													<div class="col-sm-3">
														<input class="form-control" type="hidden"
															id="balanceOfTaxs" readonly=""> <input
															class="form-control numeric2point" type="text"
															id="balanceOfTaxsShow" readonly="">
													</div>
												</div>
											</div>
											<div class="row">
												<div class="col-sm-7"></div>
												<div class="form-group ">
													<label class="col-sm-2 control-label right"
														for="formGroupInputLarge">ภาษีหัก ณ ที่จ่าย :</label>
													<div class="col-sm-3">
														<input class="form-control" type="text" id="summaryTax"
															name="summaryTax" readonly="">
													</div>
												</div>
											</div>

											<div class="row">

												<div class="form-group ">
													<div class="col-sm-7"></div>
													<div class="col-sm-1" align="right">
														<input type="radio" id="radioButton" readonly=""
															name="radioButton"> รับภาระภาษีเต็มจำนวน
													</div>
													<div class="col-sm-1" align="right">
														<input type="radio" id="radioButtons" readonly=""
															name="radioButton"> รับภาระภาษีบางส่วน
													</div>
													<div class="col-sm-3">
														<input class="form-control numeric2point" type="text"
															id="taxOnly" name="taxOnly" onclick="taxDiscount()" onchange="taxDiscount()" >
													</div>
												</div>
											</div>
											<div class="row">
												<div class="col-sm-7"></div>
												<div class="form-group ">
													<label class="col-sm-2 control-label right"
														for="formGroupInputLarge">ยอดเงินที่ต้องชำระ :</label>
													<div class="col-sm-3">
														<input class="form-control" type="hidden"
															id="balanceSummarys" readonly=""> <input
															class="form-control numeric2point" type="text"
															id="balanceSummaryShow" readonly="">
													</div>
												</div>
											</div>
											<div class="row">
												<div class="col-sm-7"></div>
												<div class="form-group ">
													<label class="col-sm-2 control-label right"
														for="formGroupInputLarge">ยอดเงินรับมา :</label>
													<div class="col-sm-3">
														<input class="form-control" type="hidden" id="balanceSum"
															readonly=""> <input
															class="form-control numeric2point" type="text"
															id="balanceSumShow" readonly="">
													</div>
												</div>
											</div>
											<div class="row">
												<div class="col-sm-7"></div>
												<div class="form-group ">
													<label class="col-sm-2 control-label right"
														for="formGroupInputLarge">เงินทอน :</label>
													<div class="col-sm-3">
														<input class="form-control" type="text" name="change"
															id="change" readonly="">
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>

			</div>
			<div class="modal fade" role="dialog"
				aria-labelledby="mySmallModalLabel" aria-hidden="true" id="mi-modal">
				<div class="modal-dialog modal-sm" style="width: 450px">
					<div class="modal-content">
						<div class="modal-header">
							<h4 class="modal-title" id="myModalLabel">ยืนยันตัวตน</h4>
						</div>
						<div class="modal-body">

							<div class="row">
								<div class="form-group col-md-12">
									<label class="col-md-3 control-label">ชื่อเข้าระบบ</label>
									<div class="col-md-9">
										<input type="text" id="userName" name="userName"
											class="form-control">
									</div>
								</div>
								<div class="form-group col-md-12">
									<label class="col-md-3 control-label">รหัสเข้าระบบ</label>
									<div class="col-md-9">
										<input type="password" id="password" name="password"
											class="form-control">
									</div>
								</div>
							</div>
							<p id="messError" style="color: red;">
								กรุณาตรวจสอบความถูกต้อง</p>
						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-primary" id="modal-btn-si">ตกลง</button>
							<button type="button" class="btn btn-danger" id="modal-btn-no">ยกเลิก</button>
						</div>
					</div>
				</div>
			</div>
		</form>
	</div>

</body>
<jsp:include page="../layout/footer.jsp"></jsp:include>
</html>