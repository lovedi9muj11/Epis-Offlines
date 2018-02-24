$(document).ready(function (){
	var userName = $('#userName').val();
	console.log("======================= Start report payment ======================");
	initCriteria();

	
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
	$('#vat').val('');
	$('#categoryPayment').val('');
};

function search(){
	histroryTB.clear().draw();
	var data = '';
	var dataSend = {
			"dateFrom": $('#billAccount').val(),
			"dateTo": $('#billAccount').val(),
			"accountId": $('#accountId').val(),
			"vat": $('#vat').val(),
			"machinePaymentName": $('#machinePaymentName').val(),
			"authorities": $('#authorities').val(),
			"categoryPayment": $('#categoryPayment').val(),
		};
	$.ajax({
        type: "POST",
        url: "/histroryPayment/find",
        data: JSON.stringify(dataSend),
        dataType: "json",
        async: false,
        contentType: "application/json; charset=utf-8",
        success: function (res) {
        	for (var i = 0; i < res.length; i++) {
                    createRow(res[i], i);
                }
        }
	})
};

function createRow(data, seq) {
	no = seq + 1
	paidDate = data.paidDateStr;
	createDate = data.createDateStr;
	invoiceNo = data.invoiceNo;
	branchCode = data.brancharea;
	createBy = data.createBy;
	receiptNoManual = data.receiptNoManual;
	period = data.period;
	amount = formatDouble(data.amount,2);
	source = data.source;
	paidAmount = formatDouble(data.paidAmount,2);
	vatAmount = formatDouble(data.vatAmount,2);
	recordStatus = data.recordStatus;

	if(data.remark == null || data.remark == ''){
		remark = "-"
	}else{
		remark = data.remark;
	}
	accountNo = data.accountNo;
	
    var t = $('#histroryPaymentTB').DataTable();
    var rowNode = t.row.add([no, paidDate, createDate,invoiceNo, branchCode, createBy, receiptNoManual, period, amount, source, paidAmount, vatAmount, recordStatus, remark, accountNo
    ]).draw(true).node();
    $(rowNode).find('td').eq(0).addClass('left');
    $(rowNode).find('td').eq(1).addClass('left');
    $(rowNode).find('td').eq(2).addClass('left');
    $(rowNode).find('td').eq(3).addClass('left');
    $(rowNode).find('td').eq(4).addClass('left');
    $(rowNode).find('td').eq(5).addClass('left');
    $(rowNode).find('td').eq(6).addClass('left');
    $(rowNode).find('td').eq(7).addClass('left');
    $(rowNode).find('td').eq(8).addClass('right');
    $(rowNode).find('td').eq(9).addClass('center');
    $(rowNode).find('td').eq(10).addClass('right');
    $(rowNode).find('td').eq(11).addClass('right');
    $(rowNode).find('td').eq(12).addClass('left');
    $(rowNode).find('td').eq(13).addClass('center');
};