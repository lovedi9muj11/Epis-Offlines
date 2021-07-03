var dateFromGlobal = "";
var dateToGlobal = "";
var dateToGlobal2 = "";
var user;
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
//	$('#vat').val('');
	$('#categoryPayment').val('');
//	$("#authorities").val('');
//	$("#accountId").val('');
	
};

function search(){
	$("#sShowValidate").hide();
//	$("#error-end-date").addClass("hide");
//	$("#error-end-date2").addClass("hide");
	reportPaymentTb.clear().draw();
	var dateFrom = $('#dateFrom').val() +" "+$('#dateFromHour').val() +":" + $('#dateFromMinute').val()+":00";
	var dateTo = $('#dateToHour').val() +":" + $('#dateToMinute').val()+":00"; //$('#dateTo').val() +" "+
	dateFromGlobal = dateFrom;
	dateToGlobal = dateTo;
	dateToGlobal2 = $('#dateFrom').val()
	var data = '';
	var dataCritaria = {
			"dateFrom": dateFrom,
			"dateTo": dateTo,
			"dateTo2": dateToGlobal2,
//			"vatRate": $('#vat').val(),
			"user":  $('#authorities').val()==''?$("#userLogin").val():$('#authorities').val(),
			"serviceType": $('#serviceType').val()
//			"accountId": $('#accountId').val()
		};
	if(validationSearchCriteria()){
		$.ajax({
	        type: "POST",
	        url: ctx +"/reportTranfer",
	        data: JSON.stringify(dataCritaria),
	        dataType: "json",
	        async: false,
	        contentType: "application/json; charset=utf-8",
	        success: function (res) {
		    	for (var i = 0; i < res.length; i++) {
		                createRow(res[i], i);
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
	$('#dateFromHidden').val(dateFromGlobal);
	$('#dateToHidden').val(dateToGlobal);
	$('#dateToHidden2').val(dateToGlobal2);
	$('#machinePaymentNameHidden').val($('#machinePaymentName').val());
//	$('#accountIdHidden').val($('#accountId').val());
	$('#authoritiesHidden').val($('#authorities').val()==''?$("#userLogin").val():$('#authorities').val());
	$("#paymentFrom").attr("action", "/Epis-Offlines/reportPaymentExcel").attr("target", "_blank").submit();
};

function printReportPDF(){
	$('#dateFromHidden').val(dateFromGlobal);
	$('#dateToHidden').val(dateToGlobal);
	$('#dateToHidden2').val(dateToGlobal2);
	$('#machinePaymentNameHidden').val($('#machinePaymentName').val());
//	$('#accountIdHidden').val($('#accountId').val());
	$('#authoritiesHidden').val($('#authorities').val()==''?$("#userLogin").val():$('#authorities').val());
	$("#paymentFrom").attr("action", "/Epis-Offlines/reportPaymentPDF").attr("target", "_blank").submit();
};
function formatDate(date) {
	  var hours = date.getHours();
	  var minutes = date.getMinutes();
	   
	  minutes = minutes < 10 ? '0'+minutes : minutes;
	  var day = date.getDate() < 10 ? '0'+date.getDate() : date.getDate();
	  var month = (date.getMonth()+1) < 10 ? '0'+(date.getMonth()+1) : (date.getMonth()+1);
	  
	  var strTime = hours + ':' + minutes ;
	  return   day + "/" +month+ "/" + date.getFullYear();
	}
function formatDateTime(date) {
	  var hours = date.getHours();
	  var minutes = date.getMinutes();
	   
	  minutes = minutes < 10 ? '0'+minutes : minutes;
	  var day = date.getDate() < 10 ? '0'+date.getDate() : date.getDate();
	  var month = (date.getMonth()+1) < 10 ? '0'+(date.getMonth()+1) : (date.getMonth()+1);
	  var strTime = hours + ':' + minutes ;
	  return   day + "/" +month+ "/" + date.getFullYear() + "  " + strTime;
	}
function createRow(data, seq) {
	manualId = seq+1;
	startDate =  formatDateTime(new Date(data.startDate));
	endDate =  formatDate(new Date(data.endDate));
	successTask =  data.successTask;
	status = data.status=='PAYMENT'?ORDER_PAYMENT:ORDER_CANCEL
	if(data.errorTask > 0){
		remake =data.errorTask +'<a name="errorTask" id="errorTask" onclick="dialogRemake('+data.id+')"><span name="icon" id="icon" class="fa fa-envelope"></a>';
	
	}else{
		remake = "";
	}
	errorTask =  data.errorTask;
	system =  data.system;
	
    var t = $('#reportPaymentTb').DataTable();
    var rowNode = t.row.add([manualId,startDate,endDate,successTask+" "+status,remake,system]).draw(true).node();
    $(rowNode).find('td').eq(0).addClass('center');
    $(rowNode).find('td').eq(1).addClass('left');
    $(rowNode).find('td').eq(2).addClass('left');
    $(rowNode).find('td').eq(3).addClass('center');
    $(rowNode).find('td').eq(4).addClass('center');
    $(rowNode).find('td').eq(5).addClass('center');
//    $(rowNode).find('td').eq(6).addClass('left');
};
function createRow2(data, seq) {
	manualId = seq+1;
	if(data.invoiceNo != null){invoiceNo =  data.invoiceNo;}else{invoiceNo= "-"}
	
	receiptNoManual =  data.receiptNoManual;

    var t = $('#reportRecriptTb').DataTable();
    var rowNode = t.row.add([manualId,invoiceNo,receiptNoManual]).draw(true).node();
    $(rowNode).find('td').eq(0).addClass('center');
    $(rowNode).find('td').eq(1).addClass('center');
    $(rowNode).find('td').eq(2).addClass('center');
//    $(rowNode).find('td').eq(6).addClass('left');
};

function dropdownUser(){
	var userLogin = $("#userLogin").val();
	var supervisor = false;
	var dataSend = { "username": "" };

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
//	var dataSend = {"manualId": value.source};
	$('#reportRecriptTb').DataTable().clear()
	$.ajax({
        type: "POST",
        url: ctx +"/findByRecriptList",
        data: JSON.stringify(value),
        dataType: "json",
        async: false,
        contentType: "application/json; charset=utf-8",
        success: function (res) {
        	for (var i = 0; i < res.length; i++) {
                createRow2(res[i], i);
            }
        }
	})
};

function dialogCancelRemake(value){
	$("#remake_dialog").modal('show');
	var dataSend = {"manualId": value};
	$.ajax({
        type: "POST",
        url: ctx +"/histroryPayment/findInvoiceByManualIdCancel",
        data: JSON.stringify(dataSend),
        dataType: "json",
        async: false,
        contentType: "application/json; charset=utf-8",
        success: function (res) {
        	$("#remake").text(res.cancelReason);
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