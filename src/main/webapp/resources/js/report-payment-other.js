var dateFromGlobal = "";
var dateToGlobal = "";
var dateToGlobal2 = "";
var user;
var check = false;
$(document).ready(function (){
//	$("#sShowValidate").hide();
	var userName = $('#userName').val();
	console.log("======================= Start report payment ======================");
	reportPaymentTb = $('#reportPaymentTb').DataTable({
		"filter" : false,
		"info" : false,
		"columnDefs": [ {
			"searchable": false,
			"orderable": false,
//			"targets": [1,12]
		} ]
	});
	initCriteria();
	dropdownUser();
//	deopdownAccount();
	search();
//	user = ${pageContext.request.userPrincipal.name};

	
});
function initCriteria(){
	
	var now = new Date();
	var day = ("0" + now.getDate()).slice(-2);
	var month = ("0" + (now.getMonth() + 1)).slice(-2);
	var today = now.getFullYear()+"-"+(month)+"-"+(day);
	
	$('#dateFrom').val(today);
	$('#dateTo').val(today);
	$('#dateFromHour').val('00');
	$('#dateFromMinute').val('00');
	$('#dateToHour').val('23');
	$('#dateToMinute').val('59');
	$('#categoryPayment').val('');
	
};

function search(){
	check = false;
	$("#sShowValidate").hide();
	$("#error-end-date").addClass("hide");
	$("#error-end-date2").addClass("hide");
	reportPaymentTb.clear().draw();
	var dateFrom = $('#dateFrom').val() +" "+ $('#dateFromHour').val() +":"+ $('#dateFromMinute').val()+":00";
	var dateTo = $('#dateToHour').val() +":"+ $('#dateToMinute').val()+":00";  //$('#dateTo').val() +" "+ 
	dateFromGlobal = dateFrom;
	dateToGlobal = dateTo;
	dateToGlobal2 = $('#dateFrom').val()
	var data = '';
	var dataCritaria = {
			"dateFrom": dateFrom,
			"dateTo": dateTo,
			"dateTo2": dateToGlobal2,
//			"vatRate": $('#vat').val(),
//			"user":  $('#authorities').val(),
			"user":  $('#authorities').val()==''?$("#userLogin").val():$('#authorities').val(),
			"serviceType": $('#serviceType').val()
//			"accountId": $('#accountId').val()
		};
	if(validationSearchCriteria()){
		$.ajax({
	        type: "POST",
	        url: ctx +"/reportPaymentOther",
	        data: JSON.stringify(dataCritaria),
	        dataType: "json",
	        async: false,
	        contentType: "application/json; charset=utf-8",
	        success: function (res) {
		    	for (var i = 0; i < res.length; i++) {
		                createRow(res[i], i);
		                check = true;
		            }
	        }
		});
	}else{
//		$("#sShowValidate").show();
		$("#error-end-date").removeClass("hide");
		$("#error-end-date2").removeClass("hide");
	}

};

function clearCriteria(){
	initCriteria();
	search();
};

function printReport(){
	if(!check){
		swal("ยังไม่มีข้อมูลในการออกรายงาน")
	}else{
		$('#dateFromHidden').val(dateFromGlobal);
		$('#dateToHidden').val(dateToGlobal);
		$('#dateToHidden2').val(dateToGlobal2);
		$('#machinePaymentNameHidden').val($('#machinePaymentName').val());
		$('#authoritiesHidden').val($('#authorities').val()==''?$("#userLogin").val():$('#authorities').val());
		$("#paymentFrom").attr("action", "/Epis-Offlines/reportPaymentExcel").attr("target", "_blank").submit();
	}
	
};

function printReportPDF(){
	if(!check){
		swal("ยังไม่มีข้อมูลในการออกรายงาน")
	}else{
		$('#dateFromHidden').val(dateFromGlobal);
		$('#dateToHidden').val(dateToGlobal);
		$('#dateToHidden2').val(dateToGlobal2);
		$('#machinePaymentNameHidden').val($('#machinePaymentName').val());
		$('#authoritiesHidden').val($('#authorities').val()==''?$("#userLogin").val():$('#authorities').val());
		$("#paymentFrom").attr("action", "/Epis-Offlines/reportPaymentPDF").attr("target", "_blank").submit();
	}
	
};

function createRow(data, seq) {
	manualId = seq+1;
	var serviceName = "";
	if(data.serviceType == "IBACSS"){
		serviceType = 'รับชำระค่าใช้บริการ';
		serviceName = data.invoiceNo;
	}else if(data.serviceType == "OTHER"){
		serviceType = 'รับชำระค่าใช้บริการอื่น ๆ';
		serviceName = data.serviceName;
	}
	receiptNo = data.receiptNoManual;
	accountSubNo = data.accountSubNo;
	customerName = data.customerName;
	department = data.department;
//	department = data.departmentName;
	invoiceNo = data.invoiceNo;
	createBy = data.paymentMethod;
	if(data.refNo) {noRefer = data.refNo;} else {noRefer = '-'}
	beforVat = formatDouble(data.beforVatOther,2);
	vatAmount =  formatDouble(data.vatAmountOther,2);
	amount =  formatDouble(data.amountOther,2);
	if (data.status == 'C'){
		statusStr = 'ยกเลิก';
//		remake = '<a name="invoice" id="invoice" onclick="dialogRemake('+data.manualId+')"><span name="icon" id="icon" class="fa fa-envelope"></a>';
		if(data.remake != ""){
			remake ='<a name="invoice" id="invoice" onclick="dialogRemake('+data.manualId+')"><span name="icon" id="icon" class="fa fa-envelope"></a>';
		}else {
			remake = '-';
		}
	}else{
		if(data.remake != ""){
			remake ='<a name="invoice" id="invoice" onclick="dialogRemake('+data.manualId+')"><span name="icon" id="icon" class="fa fa-envelope"></a>';
		}else {
			remake = '-';
		}
		statusStr = 'ปกติ';
	}
	
	
	
	
    var t = $('#reportPaymentTb').DataTable();
    var rowNode = t.row.add([manualId, serviceType, receiptNo, accountSubNo, customerName, department, serviceName, createBy ,noRefer , beforVat, vatAmount, amount, statusStr,remake
    ]).draw(true).node();
    $(rowNode).find('td').eq(0).addClass('center');
    $(rowNode).find('td').eq(1).addClass('left');
    $(rowNode).find('td').eq(2).addClass('left');
    $(rowNode).find('td').eq(3).addClass('left');
    $(rowNode).find('td').eq(4).addClass('left');
    $(rowNode).find('td').eq(5).addClass('left');
//    $(rowNode).find('td').eq(6).addClass('left');
    $(rowNode).find('td').eq().addClass('left');
    $(rowNode).find('td').eq(7).addClass('center');
    $(rowNode).find('td').eq(8).addClass('center');
    $(rowNode).find('td').eq(9).addClass('right');
    $(rowNode).find('td').eq(10).addClass('right');
    $(rowNode).find('td').eq(11).addClass('right');
    $(rowNode).find('td').eq(12).addClass('center');
};

function dropdownUser(){
	var dataSend = { "username": "" };
	var userLogin = $("#userLogin").val();
	var supervisor = false;

	$.ajax({
        type: "POST",
        url: ctx +"/userManageMent/search",
        data: JSON.stringify(dataSend),
        dataType: "json",
        async: false,
        contentType: "application/json; charset=utf-8",
        success: function (response) {
        	var res = response.userBeans
	        for(var a = 0, value = res.length; value>a ; a++){
	        	if(userLogin == res[a].userName){
	        		if(res[a].roleCode == 'SUP '){
	        			generateDropDown(res);
	        		}else{
	        		  var $el = $("#authorities");
	        	      $el.empty();
	        	      $el.append($("<option>").attr('value',userLogin).text(userLogin));
	        	      $el.prop( "disabled", true );
	        		}
	        		break;
	        	}
	        }
	        $('#machinePaymentName').val(response.centerServiceName)
        }
	});
	
};
function deopdownAccount(){
	$.ajax({
        type: "POST",
        url: ctx +"/findGL_AccountMaster",
        dataType: "json",
        async: false,
        contentType: "application/json; charset=utf-8",
        success: function (res) {
        	var $el = $("#accountId");
            $el.empty();
            $el.append($("<option></option>").attr("value", '').text('ทั้งหมด'));
	        for(var a = 0, s = res.length; s>a ; a++){
	        	$el.append($("<option>").attr('value',res[a].glCode).text(res[a].glCode));
	        }
        }
	});
	
}
function generateDropDown(value){
	var $el = $("#authorities");
    $el.empty();
    $el.append($("<option></option>").attr("value", '').text('ทั้งหมด'));
    for(var a = 0, s = value.length; s>a ; a++){
    	if(value[a].roleCode == 'USER '){
    		$el.append($("<option>").attr('value',value[a].userName).text(value[a].userName));
    	}
    }
};

function dialogRemake(value){
	$("#remake_dialog").modal('show');
	var dataSend = {"manualId": value};
	$.ajax({
        type: "POST",
        url: ctx +"/histroryPayment/findInvoiceByManualId",
        data: JSON.stringify(dataSend),
        dataType: "json",
        async: false,
        contentType: "application/json; charset=utf-8",
        success: function (res) {
        	$("#remake").text(res.remark);
        }
	})
};

function closeDialog(){
	$("#remake_dialog").modal('hide');
}

function validationSearchCriteria(){
	var returnResult = true;
	var dateFrom = $('#dateFrom').val();
	var dateFromHour = $('#dateFromHour').val();
	var dateFromMinute = $('#dateFromMinute').val()
	var dateTo = $('#dateTo').val();
	var dateToHour = $('#dateToHour').val();
	var dateToMinute = $('#dateToMinute').val();
	var dateFromFormate = dateFrom+" "+ dateFromHour+":"+ dateFromMinute;
	var dateToFormate = dateTo+" "+dateToHour+":"+dateToMinute;
	 if ((Date.parse(dateFromFormate) > Date.parse(dateToFormate))) {
		 returnResult = false;
	 }
	 return returnResult;
	
}