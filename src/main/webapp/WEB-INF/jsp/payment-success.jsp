<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="../layout/header.jsp"></jsp:include>
<jsp:include page="../layout/menu.jsp"></jsp:include>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>Epis Offline</title>

<link href="${contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
<link href="${contextPath}/resources/css/payment.css" rel="stylesheet">

<script src="${contextPath}/resources/lib/jquery-3.3.1.min.js"></script>
<script src="${contextPath}/resources/js/bootstrap.min.js"></script>
<script src="${contextPath}/resources/js/payment-success.js"></script>
<script src="${contextPath}/resources/js/utils.js" type="text/javascript"></script>
<script src="${contextPath}/resources/lib/autoNumeric-1.7.4.js"></script>

<%=request.getAttribute("paymentResultReq")%>
</head>
<body>
	<header class="header_page"></header>

	<form id="paymentFroms" method="post" class="form-horizontal"
		role="form">
		<div id="page-content-wrapper">
			<nav class="navbar navbar-default">
				<div class="container-fluid">
					<ul class="nav navbar-nav">
						<li><a href="#">ผลการรับชำระเงิน </a></li>
						<li class="active"><a href="#">ผลการรับชำระ </a></li>
					</ul>
				</div>
			</nav>
			<input name="documentNo" id="documentNo"
				value="${paymentResultReq.documentNo}" type="hidden">
			<div class="row">

				<div class="col-md-12 col-sm-12">
					<div class="form-group" align="right">
						<div class="col-md-12 col-sm-12">
							<button name="btnSubmit" id="btnSubmit" type="button"
								class="btn btn-info btn-lg" onclick="submti()">
								<span class="glyphicon glyphicon-file"> พิมพ์ใบเสร็จ</span>
							</button>
							<button name="submitFormPayment" type="button"
								class="btn btn-danger btn-lg" onclick="backPayment()">
								<span class="glyphicon glyphicon-chevron-left">
									กลับไปยังหน้าชำระ</span>
							</button>
						</div>
					</div>
				</div>
			</div>
			<!-- panel รายการหัก -->
			<div class="row">

				<div class="col-md-12 col-sm-12">
					<div class="form-group">
						<div class="col-md-12 col-sm-12">
							<div class="panel">
								<div class="panel-heading" style="background-color: #ee7600;">ผลการรับชำระเงิน</div>
								<div class="panel-body" align="center">
									<table class="table" id="showResultTable" align="center">
										<thead align="center">
											<tr>
												<th></th>
												<th style='text-align: center;'>#</th>
												<th style='text-align: center;'>เลขที่ลูกค้า</th>
												<th style='text-align: center;'>ชื่อลูกค้า</th>
												<th style='text-align: center;'>เลขที่ใบเสร็จรับเงิน/ใบกำกับภาษี</th>
												<th style='text-align: center;'>จำนวนเงินที่ชำระ</th>
												<th style='text-align: center;'>สถานะการรับชำระ</th>
											</tr>
										</thead>
										<tbody align="center">
											<tr align="center">
												<td id="plus"><a
													onclick='openTable(${paymentResultReq.manualId})'><span
														class="glyphicon glyphicon-plus"></span></a></td>
												<td id="remove"><a onclick="removeTable()"><span
														class="glyphicon glyphicon-minus"></span></a></td>
												<td align="center">1</td>
												<td align="center">${paymentResultReq.custNo}</td>
												<td align="center">${paymentResultReq.custName}</td>
												<td align="center">${paymentResultReq.documentNo}</td>
												<td align="center">${paymentResultReq.balanceSummaryStr}</td>
												<td align="center">บันทึกรายการเรียบร้อย</td>
											</tr>
										</tbody>
									</table>
									<table class="table " id="showResultTableRQ">
										<thead align="center">
											<tr align="center">
												<th style='text-align: center;'>เลขที่ใบแจ้งค่าใช้บริการ</th>
												<th style='text-align: center;'>วันที่จัดทำใบแจ้งค่าใช้บริการ</th>
												<th style='text-align: center;'>วันที่ครบกำหนด</th>
												<th style='text-align: center;'>ยอดก่อนภาษี</th>
												<th style='text-align: center;'>ภาษีมูลค่าเพิ่ม</th>
												<th style='text-align: center;'>ยอดเงินรวมภาษี</th>
												<th style='text-align: center;'>ส่วนลดหลังการขาย</th>
												<th style='text-align: center;'>ยอดชำระ</th>
												<th style='text-align: center;'>ภาษีหัก ณ ที่จ่าย</th>
												<!-- 												<th style='text-align: center;'>จำนวนเงินคงค้าง</th> -->
												<th style='text-align: center;'>รอบการใช้งาน</th>
											</tr>
										</thead>
										<tbody>
											<!-- 											<tr align="center"> -->
											<%-- 												<td align="center">${paymentResultReq.invoiceNo}</td> --%>
											<%-- 												<td align="center">${paymentResultReq.invoiceDateRS}</td> --%>
											<%-- 												<td align="center">${paymentResultReq.dateLineRS}</td> --%>
											<%-- 												<td align="center">${paymentResultReq.beforeVatStr}</td> --%>
											<%-- 												<td align="center">${paymentResultReq.vatStr}</td> --%>
											<%-- 												<td align="center">${paymentResultReq.balanceOfvatStr}</td> --%>
											<%-- 												<td align="center">${paymentResultReq.discountStr}</td> --%>
											<%-- 												<td align="center">${paymentResultReq.balanceSummaryStr}</td> --%>
											<%-- 												<td align="center">${paymentResultReq.deductionStr}</td> --%>
											<%-- 											<td align="center">${ paymentResultReq.balancePriceStr}</td> --%>
											<%-- 												<td align="center">${paymentResultReq.period}</td> --%>
											<!-- 											</tr> -->
										</tbody>
									</table>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</form>
</body>
<jsp:include page="../layout/footer.jsp"></jsp:include>
</html>
