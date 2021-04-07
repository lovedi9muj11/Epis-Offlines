var tableInit;
var tableSelect;
var dataSelect;
var idRow;
var userFullName = '';
var customerAddress = '';
var clearing;
var valueIcon = "";
var IdSelected = "";
var chars = [];
var userApproved = "";

$(document).ready(function () {
    console.log("ready!");

	cancelPaymentTB = $('#cancelPaymentTB').DataTable({
		"filter" : false,
		"info" : false,
		"order": [[ 2, 'asc' ]],
		"columnDefs": [ {
			"searchable": false,
		} ]
	});
    $("#error").hide();
    $("#success").hide();
    $("#notClear").hide();
    $("#notClear2").hide();
    hidePanel()
    showPanel('1');
    removeCssLi();
    addCssLi('1');
    search();
    
    $('#cancelPaymentTB tbody').on('change', ':radio', function() {
    	 	$("#mi-modal").modal('show');
            idRow = parseInt($('input[name="select"]:checked').val());
            $(this).prop('checked', false);
    });
    
    function clickInvoiceDetail(){
        var tr = $(this).closest('tr');
        var row = tableInit.row(tr);

        if (row.child.isShown()) {
            // This row is already open - close it
            row.child.hide();
            tr.removeClass('shown');
        }
        else {
            // Open this row
            row.child(format(row.data())).show();
            tr.addClass('shown');
        }
    	
    };
    
    $('#cancelPaymentTB tbody').on('click', '#invoice', function () {
        var tr = $(this).closest('tr');
        var row = tableInit.row(tr);

        if (row.child.isShown()) {
            // This row is already open - close it
            row.child.hide();
            $('#'+valueIcon).addClass('glyphicon-plus').removeClass('glyphicon-minus');
            tr.removeClass('shown');
        }
        else {
        	console.log(row.data()[15]);
        	var url;
            if(row.data()[15] == "IBACSS"){
         	   url = ctx +"/getDetailBilling/"+IdSelected;
            }else{
         	   url = ctx +"/payOtherDetail/"+IdSelected;
            }
         	$.ajax({
   		        type: "GET",
   		        url: url,
   		        dataType: "json",
   		        async: false,
   		        contentType: "application/json; charset=utf-8",
   		        success: function (res) {
   		        	console.log(res);
   		         if(row.data()[15] == "IBACSS"){
  		        	 row.child(format2(res)).show();
  		           }else{
  		        	 row.child(format(res)).show();
  		           }
   		            $('#'+valueIcon).addClass('glyphicon-minus').removeClass('glyphicon-plus');
   		        }
         	});
        }
    });
    
    $('#selectCancelPaymentTB tbody').on('click', '#btn2', function () {
        var tr = $(this).closest('tr');
        var row = tableSelect.row(tr);

        if (row.child.isShown()) {
            // This row is already open - close it
            row.child.hide();
            $('#'+valueIcon).addClass('glyphicon-plus').removeClass('glyphicon-minus');
        }
        else {
        	console.log(row.data()[15]);
        	var url;
           if(row.data()[15] == "IBACSS"){
        	   url = ctx +"/getDetailBilling/"+IdSelected;
           }else{
        	   url = ctx +"/payOtherDetail/"+IdSelected;
           }
        	$.ajax({
  		        type: "GET",
  		        url: url,
  		        dataType: "json",
  		        async: false,
  		        contentType: "application/json; charset=utf-8",
  		        success: function (res) {
  		        	console.log(res);
  		          if(row.data()[15] == "IBACSS"){
  		        	 row.child(format2(res)).show();
  		           }else{
  		        	 row.child(format(bas)).show();
  		           }
  		        	
  		            $('#'+valueIcon).addClass('glyphicon-minus').removeClass('glyphicon-plus');
  		        }
        	});
           
        }
    });  

    var modalConfirm = function(callback){	  

    	  $("#modal-btn-si").on("click", function(){
    		  userApproved = $('#userName').val()
    		  callback(true);
    		  $("#mi-modal").modal('hide');
    	  });
    	  
    	  $("#modal-btn-no").on("click", function(){
    	    callback(false);
    	    $("#mi-modal").modal('hide');
    	  });
    	};
   	
    	modalConfirm(function(confirm){
    	    $("#error").hide();
    	    $("#success").hide();
    	    $("#notClear").hide();
    	    $("#notClear2").hide();
      	  if(confirm){
      		cancelPaymentTB.clear().draw();
      			var dataSend = { "userName": $('#userName').val(), "password": $('#password').val() };
      			$.ajax({
      		        type: "POST",
      		        url: ctx +"/cancelPayment/checkAuthentication",
      		        data: JSON.stringify(dataSend),
      		        dataType: "json",
      		        async: false,
      		        contentType: "application/json; charset=utf-8",
      		        success: function (res) {
      		        	console.log(res);
      		        	if(res){
      		        		$('#selectCancelPaymentTB').DataTable({
      		        			"filter" : false,
      		        			"info" : false,
      		        			"columnDefs": [ {
      		        				"searchable": false,
      		        				"orderable": false,
//      		        				"targets": [1,12]
      		        			} ]
      		        		});
      		        	var dataSend2 = { "manualId": idRow};
      		     			$.ajax({
      		     		        type: "POST",
      		     		        url: ctx +"/cancelPayment/findFromId",
      		     		        data: JSON.stringify(dataSend2),
      		     		        dataType: "json",
      		     		        async: false,
      		     		        contentType: "application/json; charset=utf-8",
      		     		        success: function (res) {
      		     		        	console.log(res)
      		     		        	if(res.length != 0){
      		     		        		if('C'==res[0].recordStatus) {
      		     		        			$("#notClear2").show();
      		     		        		}
      		     		        		
      		     		        		for (var i = 0; i < res.length; i++) {
      		     		        			createRow(res[i], i, "cancelPaymentTB", false);
          		     		            }
	      		  		     			showTableSelect();
	      		  		     			var $radios = $('input:radio[name=select]');
	      		  		     		 	$radios.filter('[value="'+idRow+'"]').prop('checked', true);
      		     		        	}else{
	      		     		       		$("#notClear").show();
	      		     		      	    hidePanel()
	      		     		      	    showPanel('1');
	      		     		      	    removeCssLi();
	      		     		      	    addCssLi('1');
	      		     		      	    search();
      		     		        	}
      		     		        	
      		     		        }
      		     			});
//      		     			$('#submitCancelPM').prop('disabled', true);
      		        	}else{
      		        		$("#error").show();
      		        	    hidePanel()
      		        	    showPanel('1');
      		        	    removeCssLi();
      		        	    addCssLi('1');
      		        	    search();
      		        	}
      		        	
      		        }
      			});
      			

      	  }
      	$('#userName').val('');
      	$('#password').val('');
      });
    	
    	$("#barCode").keypress(function(e){
            if ( e.which == 13 ) {
                var barcode = chars.join("");
                if(barcode.charAt(0) == "|") barcode = barcode.slice(1,barcode.length);
                if(barcode.charAt(0) == " ") barcode = barcode.slice(1,barcode.length);
                $("#barCode").val(barcode);
                chars = [];
                e.preventDefault();
                
                var setCode = barcode.split("\n");
                $.each(setCode, function(x){
                	if(x==2) {
                		$('#billNumber').val(setCode[x].substring(0, 9)); 
                	}
                });
            } else if ( e.which == 109 )  {
            } else {
                chars.push(String.fromCharCode(e.which));
            }
            search();
        });
    	
    	ddOther()
});


function showTableSelect(){
    hidePanel();
    showPanel("2");
    removeCssLi();
    addCssLi('2');
};
//function showDetail(){
//    var tr = $(this).closest('tr');
//    var row = tableSelect.row(tr);
//
//    if (row.child.isShown()) {
//        // This row is already open - close it
//        row.child.hide();
//        tr.removeClass('shown');
//    }
//    else {
//        // Open this row
//        row.child(format(row.data())).show();
//        tr.addClass('shown');
//    }
//};

function search() {
	cancelPaymentTB.clear().draw();
	var data = '';
	var dataSend = { "accountNo": $('#accountNo').val(), "receiptNoManual": $('#receiptNumber').val(), "chkCancel": false };
	$.ajax({
        type: "POST",
        url: ctx +"/cancelPayment/find",
        data: JSON.stringify(dataSend),
        dataType: "json",
        async: false,
        contentType: "application/json; charset=utf-8",
        success: function (res) {
        	for (var i = 0; i < res.length; i++) {
                    createRow(res[i], (i+1), "cancelPaymentTB", true);
                }
        }
	})
	
};

function findppt1(value) {
	$.ajax({
        type: "GET",
        url: ctx +"/findByppt1/"+value,
//        data: JSON.stringify(dataSend),
        dataType: "json",
        async: false,
        contentType: "application/json; charset=utf-8",
        success: function (res) {
        	customerGroupName = res.value
        }
	})
	
};

function clearCriteria(){
	$('#accountNo').val('');
	$('#receiptNumber').val('');
	$('#barCode').val('');
	search();
};
function chaengIcon(value){
	valueIcon= 'icon'+value;
	IdSelected = value;
}

let customerGroupName = '';
function createRow(data, seq, table,check) {
	radioSelect =  '<input type="radio" name="select" value="'+data.manualId+'"> <input type="hidden"  value="'+data.serviceType+'"><input type="hidden" name="clearing" id="clearing" value="'+data.clearing+'">'
	invoice =  '<a name="invoice" id="invoice" onclick="chaengIcon('+data.manualId+')"><span name="icon'+data.manualId+'" id="icon'+data.manualId+'" class="glyphicon glyphicon-plus"></a>'
//	invoice =  ''
	no = seq;
	receiptNoManual = data.receiptNoManual;
	createDate = data.createDateStr
	accountNo = data.accountNo;
	customer = data.customerName;
//	serviceName = data.serviceName;
	serviceName = '';
	findppt1(data.customerGroup)
	userGroup = customerGroupName;
	
//	amount = formatDouble(data.amountOther,2);
	amount = formatDouble(data.amount,2);
	branchCode = data.brancharea;
	createBy = data.createBy;
	if(data.recordStatus == 'A'){
		recordStatus = 'ปกติ';
	}else if(data.recordStatus == 'C'){
		recordStatus = 'ยกเลิก';
	}
	customerAddress = data.customerAddress;
	userFullName = data.customerName;
	reason = '-';
	
	tableInit = $('#'+table).DataTable();
	var rowNode = tableInit.row.add([invoice, radioSelect, no, receiptNoManual, createDate, accountNo, customer, userGroup, amount, branchCode, createBy, recordStatus, reason]).draw(true).node();
    $(rowNode).find('td').eq(0).addClass('center');
    $(rowNode).find('td').eq(1).addClass('center');
    $(rowNode).find('td').eq(2).addClass('center');
    $(rowNode).find('td').eq(3).addClass('center');
    $(rowNode).find('td').eq(4).addClass('center');
    $(rowNode).find('td').eq(5).addClass('center');
    $(rowNode).find('td').eq(6).addClass('center');
    $(rowNode).find('td').eq(7).addClass('center');
    $(rowNode).find('td').eq(8).addClass('center');
    $(rowNode).find('td').eq(9).addClass('center');
    $(rowNode).find('td').eq(10).addClass('center');
    $(rowNode).find('td').eq(11).addClass('center');
    $(rowNode).find('td').eq(12).addClass('center');
    $(rowNode).find('td').eq(13).addClass('center');

};

function createRowSelect(data, seq, table) {
	customerAddress = data.customerAddress;
	userFullName = data.customerName;
	
	no = seq+1;
	receiptNoManual = data.receiptNoManual;
	createDate =data.createDateStr;
	dateMake = data.createDateStr;
	accountNo = data.accountNo;
	customer = data.customerName;
	payType = data.paymentMethod;
	amount = formatDouble(data.amount,2);
	branchCode = data.brancharea;
	createBy = data.createBy;
	recordStatus = data.recordStatus;
	vatAmount = formatDouble(data.vatAmount,2);
	sumTotal =  data.amount + data.vatAmount;
	tableSelect = $('#'+table).DataTable();
    var rowNode = tableSelect.row.add([no, receiptNoManual, createDate, dateMake, accountNo, customer, payType, amount, branchCode, createBy, recordStatus, vatAmount, sumTotal]).draw(true).node();
    $(rowNode).find('td').eq(0).addClass('center');
    $(rowNode).find('td').eq(1).addClass('center');

};



function format(d) {
//	sprintStr =  d[9].split(",");
//	invoice = parseFloat(sprintStr[0]+sprintStr[1]) - parseFloat(d[13])
	var txt="";
	var i = 0;
	for(i=0; i < d.length; i++){
		txt = txt+ '<tr>'+
//			        '<th style="text-align: left;">'+'ServiceName :'+d[i].serviceName+'</th>'+
//			        '<th style="text-align: right;">'+d[i].quantity +'</th>'+
//			        '<th style="text-align: right;">'+d[i].discountStr+'</th>'+
//			        '<th style="text-align: right;">'+d[i].vatStr+'</th>'+
//			        '<th style="text-align: right;">'+d[i].amountStr+'</th>'+
			        '<th style="text-align: left;">'+d[i].amountType+'</th>'+
			        '<th style="text-align: left;">'+d[i].serviceName+'</th>'+
			        '<th style="text-align: right;">'+d[i].quantity+'</th>'+
			        '<th style="text-align: right;">'+d[i].beforeVatStr+'</th>'+
			        '<th style="text-align: right;">'+d[i].vatStr+'</th>'+
			        '<th style="text-align: right;">'+d[i].discountStr+'</th>'+
			        '<th style="text-align: right;">'+d[i].discountspacalStr+'</th>'+
			        '<th style="text-align: right;">'+d[i].amountStr+'</th>'+
			    '</tr>'
	}

    return '<table class="table table-bordered" cellspacing="0" width="100%">'+
			    '<thead>'+
			    '<tr>'+
//			        '<th style="text-align: left;">รายการ</th>'+
//			        '<th style="text-align: right;">จำนวน</th>'+
//			        '<th style="text-align: right;">ส่วนลด</th>'+
//			        '<th style="text-align: right;">ภาษีมูลค่าเพิ่ม</th>'+
//			        '<th style="text-align: right;">จำนวนเงิน</th>'+
				    '<th style="text-align: center;">ประเภทรายได้</th>'+
			        '<th style="text-align: center;">ชื่อบริการ</th>'+
			        '<th style="text-align: right;">จำนวนรายการ</th>'+
			        '<th style="text-align: right;">จำนวนเงินต่อหน่วย</th>'+
			        '<th style="text-align: right;">ภาษีมูลค่าเพิ่ม</th>'+
			        '<th style="text-align: right;">ส่วนลด</th>'+
			        '<th style="text-align: right;">ส่วนลดพิเศษ</th>'+
			        '<th style="text-align: right;">ยอดเงินรวม</th>'+
			    '</tr>'+
			'</thead>'+
			'<tbody>'+
			txt +
			'</tbody>'
		'</table>';
};

function format2(d) {
//	sprintStr =  d[9].split(",");
//	invoice = parseFloat(sprintStr[0]+sprintStr[1]) - parseFloat(d[13])
    return '<table class="table table-bordered" cellspacing="0" width="100%">'+
			    '<thead>'+
			    '<tr>'+
			        '<th style="text-align: left;">รายการ</th>'+
			        '<th style="text-align: right;">จำนวน</th>'+
			        '<th style="text-align: right;">ส่วนลด</th>'+
			        '<th style="text-align: right;">ภาษีมูลค่าเพิ่ม</th>'+
			        '<th style="text-align: right;">จำนวนเงิน</th>'+
			    '</tr>'+
			'</thead>'+
			'<tbody>'+
			    '<tr>'+
			        '<th style="text-align: left;">'+'invoiceNo :'+d.invoiceNo+'</th>'+
			        '<th style="text-align: right;">'+d.balanceSummaryStr +'</th>'+
			        '<th style="text-align: right;">'+d.discountStr+'</th>'+
			        '<th style="text-align: right;">'+d.vatStr+'</th>'+
			        '<th style="text-align: right;">'+d.balanceOfvatStr+'</th>'+
			    '</tr>'+
			'</tbody>'
		'</table>';
};

//action Controller
function submitCancelPayment(){
	var dataSet = {
			"manualId" : idRow,
			"statusCancelPayment":$('#problemCancel').val(),
			"addressNewCancelPayment": $('#address').val(),
			"customerName": $('#fullName').val(),
			"chkPaymentType": "OTHER",
			"userApproved" : userApproved
	};
	$.ajax({
	        type: "POST",
	        url: ctx +"/cancelPayment/updateStatus",
	        data: JSON.stringify(dataSet),
	        dataType: "json",
	        async: false,
	        contentType: "application/json; charset=utf-8",
	        success: function (res) {
	        	var result = res.receiptNoManual != '';
	        	if(result){
	        		console.log(res.receiptNoManual)
	        		$('#documentNo').val(res.receiptNoManual);
	        		$('#chkPaymentType').val(res.chkPaymentType);
	        		$("#success").show();
	        	    hidePanel()
	        	    showPanel('1');
	        	    removeCssLi();
	        	    addCssLi('1');
	        		search();
	        	}else{
	        		$("#error").show();
	        	}
	        }
		});
};


function addCssLi(select) {
    $("#li" + select).css("color", "red")
};
function removeCssLi(){
	 $("#li1" ).css("color", "black");
	 $("#li2" ).css("color", "black");
	 $("#li3" ).css("color", "black");
	 $("#li4" ).css("color", "black");
};

function showPanel(select) {
    $("#panel" + select).show();
};

function hidePanel(){
    $("#panel1").hide();
    $("#panel2").hide();
    $("#panel3").hide();
    $("#panel4").hide();
};

function showReasonCancel(){
	if($('#problemCancel').val() != ''){
		if($('#problemCancel').val() == '001'){
			 $("#fullName").val(userFullName);
			 $("#address").val(customerAddress);
			$("#reason-cancel").modal('show');
		}else{
			
			swal({
				  title: "คุณต้องการยกเลิกรายการหรือไม่",
				  text: "",
				  icon: "warning",
				  buttons: true,
				  successMode: true,
				})
				.then((willDelete) => {
				  if (willDelete) {
//				    swal("yes xxx", {
//				      icon: "success",
//				    });
					  
					$("#reason-cancel").modal('hide');
					submitCancelPayment();
				  } else {
//				    swal("xxx");
				  }
				})
				
//			var r = confirm("คุณต้องการยกเลิกรายการหรือไม่ ");
//			if(r){
//				$("#reason-cancel").modal('hide');
//				submitCancelPayment();
//			}
			
		}
	}else{
		swal("โปรดระบุตัวเลือก");
//		alert("โปรดระบุตัวเลือก");
	}
};

function modalConfirmReason(callback){
	if(callback){
		var r = confirm("คุณต้องการยกเลิกรายการหรือไม่ ");
		if(r){
			submitCancelPayment();
			if($('#chkPaymentType').val() === "IBACSS") {
				rePrint();
			}else{
				rePrintOther();
			}
			
			$("#reason-cancel").modal('hide');
		}
	}else{
		$("#reason-cancel").modal('hide');
	}

};
function converDateToString(value){
	var d = new Date(value)
    month = '' + (d.getMonth() + 1),
    day = '' + d.getDate(),
    year = d.getFullYear(),
    hours = d.getHours(),
    minutes = d.getMinutes(),
    seconds = d.getSeconds();

	if (month.length < 2) month = '0' + month;
	if (day.length < 2) day = '0' + day;
	
	if (hours.length < 2) hours = '0' + hours;
	if (minutes.length < 2) minutes = '0' + minutes;
	if (seconds.length < 2) seconds = '0' + seconds;

return [day, month, year].join('/')+" "+ [hours,minutes,seconds].join(':');
};

function rePrint() {
//	$("#cancelForm").attr("action", "/previewPaymentEpisOffline.pdf").attr("target", "_blank").submit();
	
	window.open(ctx +"/previewPaymentEpisOffline/"+$('#documentNo').val()+".pdf",  'top=0,left=0,menubar=no,status=yes,scrollbars=yes,resizable=yes,width=1500,height=700');
}

function rePrintOther() {
//	$("#cancelForm").attr("action", "/previewPaymentEpisOfflineOther.pdf").attr("target", "_blank").submit();
	
	window.open(ctx +"/previewPaymentEpisOfflineOther/"+$('#documentNo').val()+".pdf",  'top=0,left=0,menubar=no,status=yes,scrollbars=yes,resizable=yes,width=1500,height=700');
}

function searchReceiptNoById(id) {
	var dataSend = { "manualId": id };
	$.ajax({
        type: "POST",
        url: ctx +"/searchReceiptNoById",
        data: JSON.stringify(dataSend),
        dataType: "json",
        async: false,
        contentType: "application/json; charset=utf-8",
        success: function (res) {
        	console.log(res.documentNo)
        	$('#documentNo').val(res.documentNo);
        }
	})
}

function ddOther() {
//	var dataSend = { "manualId": id };
	$.ajax({
        type: "GET",
        url: ctx +"/dropdownOther",
//        data: JSON.stringify(dataSend),
        dataType: "json",
        async: false,
        contentType: "application/json; charset=utf-8",
        success: function (res) {
        	$('#problemCancel').append('<option value="">' + PLS_SELECT + '</option>')
        	
        	if(undefined!=res) {
        		for(var i=0; i<res.length; i++) {
        			$('#problemCancel').append('<option value="' + res[i].key + '">' + res[i].value + '</option>')
        		}
        	}
        	
        }
	})
}