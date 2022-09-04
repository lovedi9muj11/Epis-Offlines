let mapGLObj
let productCode
let segmentCode
let productName
let segmentName
let custOtherList
let chkOther = false
let chkOtherTax1 = false
let userGroupGBs
let userGroupGBOthers
let masterSegmentsGBs
let masterProductsGBs
let keyCode
let serviceNames
let serviceTypes
let isShPS = false;

$(document).ready(function() {
	
	
			$('#inputServiceDepartment').select2();
			$('#inputServiceType').select2();
			$('#inputServiceName').select2();
	
			$("#inputServiceAmount").on( "click",  function() {
				this.select();
			});
			$("#moneyDed1").on( "click",  function() {
				this.select();
			});
			$("#inputServiceDiscount").on( "click",  function() {
				this.select();
			});
			$("#inputSpecialDiscount").on( "click",  function() {
				this.select();
			});
			$("#moneyTran").on( "click",  function() {
				this.select();
			});
			$("#moneyDed").on( "click",  function() {
				this.select();
			});
			$("#creditPrice").on( "click",  function() {
				this.select();
			});
			$("#moneyCheck").on( "click",  function() {
				this.select();
				
			});
			
			// set mapgl for pay
			document.getElementById("findOther").disabled = true;
			set4gl()
			$("#serror").hide();
			$("#perror").hide();
			
			$('.groupType').select2({
			    width: '50%'
			});
			
			findUserGroup()
			findServiceType()
			findServiceName()
			
			
			custOtherList = $('#custOtherList').DataTable({
				"filter" : false,
				"info" : false,
				"paging": false,
				"columnDefs": [{
					"searchable": false,
					"orderable": false,
					"targets": 0
				}]
			});
			
//			$("#moneyTran").on( "change",  function() {
//				if($("#moneyTran").val() == ""){
//				var table = document.getElementById("sumtableBillingList");
//				replaseIndexV4(table);
//				}
//			});
			$("#moneyTran").on( "change",  function() {
			var balance =  FormatMoneyShowToNumber($("#balanceSummarys").val());
			var inPrice = FormatMoneyShowToNumber($("#balanceSummary").val());

			if($("#moneyTran").val() == ""){
				$("#moneyTran").val(balance.toFixed(2).toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g,"$1,"));
			}				
		});
		
		$("#creditPrice").on( "change",  function() {
			
			var balance =  FormatMoneyShowToNumber($("#balanceSummarys").val());
			var inPrice = FormatMoneyShowToNumber($("#balanceSummary").val());

			if($("#creditPrice").val() == ""){
				$("#creditPrice").val(balance.toFixed(2).toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g,"$1,"));
			}			
		});
		$("#moneyCheck").on( "change",  function() {
			var balance =  FormatMoneyShowToNumber($("#balanceSummarys").val());
			var inPrice = FormatMoneyShowToNumber($("#balanceSummary").val());

			if($("#moneyCheck").val() == ""){
				$("#moneyCheck").val(balance.toFixed(2).toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g,"$1,"));
			}			
		});
		
		
			$.ajax({
			    type: 'GET',
			    url: ctx +"/getvatRate"
			}).then(function (data) {
				for(var i=0; i<data.length; i++) {
					var element = data[i];
					if(element.text != 'Non-VAT'){
						$('#vatrate').append('<option value="' + element.text+ '">' + element.text +' %'+ '</option>');
					}else{
						$('#vatrate').append('<option value="' + element.text+ '">' + element.text + '</option>');
					}
					
				}
			});
			
			
			$("#inputServiceDiscount").on( "change",  function() {
				var inputServiceDiscount = $("#inputServiceDiscount").val();
				
				
				if(inputServiceDiscount == ""){
					inputServiceDiscount = "0";
				}
				$("#inputServiceDiscount").val(FormatMoneyShowToNumber(inputServiceDiscount).toFixed(2).toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g,"$1,"));
			});
			
			$("#inputSpecialDiscount").on( "change",  function() {
				var inputSpecialDiscount = $("#inputSpecialDiscount").val();
				
				
				if(inputSpecialDiscount == ""){
					inputSpecialDiscount = "0";
				}
				$("#inputSpecialDiscount").val(FormatMoneyShowToNumber(inputSpecialDiscount).toFixed(2).toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g,"$1,"));
			});
			
			$("#inputServiceAmount").on( "change",  function() {
				var inputServiceAmount = $("#inputServiceAmount").val();
				
				
				if(inputServiceAmount == ""){
					inputServiceAmount = "0";
				}
				$("#inputServiceAmount").val(FormatMoneyShowToNumber(inputServiceAmount).toFixed(2).toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g,"$1,"));
			});
			$("#moneyDed").on( "change",  function() {
				var moneyDed = $("#moneyDed").val();
				
				
				if(moneyDed == ""){
					moneyDed = "0";
				}
				$("#moneyDed").val(FormatMoneyShowToNumber(moneyDed).toFixed(2).toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g,"$1,"));
			});
			
			$("#moneyDed1").on( "change",  function() {
				var moneyDed1 = $("#moneyDed1").val();
				
				
				if(moneyDed1 == ""){
					moneyDed1 = "0";
				}
				$("#moneyDed1").val(FormatMoneyShowToNumber(moneyDed1).toFixed(2).toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g,"$1,"));
			});
			
			$("#inputServiceMoreData").on( "change",  function() {
				var inputServiceMoreData = $("#inputServiceMoreData").val();
				
				
				if(inputServiceMoreData == ""){
					inputServiceMoreData = "1";
				}
				$("#inputServiceMoreData").val(inputServiceMoreData);
			});
			
			findTypePayment();
			findBank();
			findBankNo();
			summaryTax();
			hideShowdat();
			hideDetailPayment();
			disBtn();
			vatAmount();
//			document.getElementById("radioButton").disabled = true;
			document.getElementById("radioButton1").disabled = true;
			document.getElementById("radioButton2").disabled = true;
			document.getElementById("radioButton3").disabled = true;
//			document.getElementById("radioButtons").disabled = true;
			$("#change").val(parseFloat(0).toFixed(2));
			$("#balanceSumShow").val(parseFloat(0).toFixed(2));
			$("#balanceSummarys").val(parseFloat(0).toFixed(2));
			$("#beforeSaleShow").val(
					parseFloat(0).toFixed(2).replace(
							/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));

			$("#moneyTran").val(
					parseFloat(0).toFixed(2).replace(
							/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
			$("#creditPrice").val(
					parseFloat(0).toFixed(2).replace(
							/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
			$("#moneyCheck").val(
					parseFloat(0).toFixed(2).replace(
							/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
			$("#balanceBeforeTax").val(
					parseFloat(0).toFixed(2).replace(
							/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
			$("#vat").val(
					parseFloat(0).toFixed(2).replace(
							/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
			$("#vatsShow").val(
					parseFloat(0).toFixed(2).replace(
							/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
			$("#vats").val(parseFloat(0).toFixed(2));
			$("#balanceSummaryShow").val(
					parseFloat(0).toFixed(2).replace(
							/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
			$("#balanceOfTaxsShow").val(
					parseFloat(0).toFixed(2).replace(
							/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
			$("#balanceOfTaxs").val(parseFloat(0).toFixed(2));
			$("#balanceBeforeTaxsShow").val(
					parseFloat(0).toFixed(2).replace(
							/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
			$("#sale").val(
					parseFloat(0).toFixed(2).replace(
							/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
			$("#salespacial").val(
					parseFloat(0).toFixed(2).replace(
							/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
			$("#inputServiceDiscount").val(
					parseFloat(0).toFixed(2).replace(
							/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
			$("#inputSpecialDiscount").val(
									parseFloat(0).toFixed(2).replace(
											/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
			$("#inputServiceAmount").val(parseFloat(0).toFixed(2).replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));								
			$("#moneyDed").val(parseFloat(0).toFixed(2).replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
			$("#moneyDed1").val(parseFloat(0).toFixed(2).replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
			$("#summaryTax").val(parseFloat(0).toFixed(2).replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
			
//			document.getElementById("inputSpecialDiscount").readOnly = true;
			$("#inputSpecialDiscount").prop('disabled', true);
			$('#rbSpecialDiscount').click(function() {
				  $("#mi-modal").modal('show');
				  $("#modal-btn-si").on("click", function(){
					  var dataSend = { "userName": $('#userName').val(), "password": $('#password').val() };
					  $.ajax({
		      		        type: "POST",
		      		        url: ctx +"/cancelPayment/checkAuthentication",
		      		        data: JSON.stringify(dataSend),
		      		        dataType: "json",
		      		        async: false,
		      		        contentType: "application/json; charset=utf-8",
		      		        success: function (res) {
		      		        	if(res){
//		      		        		document.getElementById("inputSpecialDiscount").readOnly = false;	
		      		        		$("#inputSpecialDiscount").prop('disabled', false);
		      		        	}else{
		      		        		$("#mi-modal").modal('hide');
		      		        		$("#mi-modal-notauthen").modal('show');
		      		        	
		      		        	}
		      		        	
		      		        }
					  });
					    
					    $("#mi-modal").modal('hide');
					    
					    
					  });
					  
					  $("#modal-btn-no").on("click", function(){
					    $("#mi-modal").modal('hide');
					    
					    
					 });
					 $("#modal-btn-ok").on("click", function(){
					    $("#mi-modal-notauthen").modal('hide');
					    
					    
					 });
					 $('#userName').val("");
					 $('#password').val("");
			    });
			$('#inputServiceAmount').change(function() {
				$('#sinputServiceAmount').hide();
			});
			$('#inputServiceMoreData').change(function() {
				$('#sinputServiceMoreData').hide();
			});
			$('#userGroup').change(function() {
				$('#suserGroup').hide();
			});
			
			
			$(document).ready(function() {
			    $('.js-example-basic-single').select2();
			});
			
			$("#creditPrice").on( "keyup",  function() {
				var balance =  FormatMoneyShowToNumber($("#balanceSummarys").val());
				var inPrice = FormatMoneyShowToNumber($("#balanceSummary").val());

				if($("#creditPrice").val() == ""){
					$("#creditPrice").val(balance.toFixed(2).toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g,"$1,"));
				}
			});	  
			$("#moneyCheck").on( "keyup",  function() {
				var balance =  FormatMoneyShowToNumber($("#balanceSummarys").val());
				var inPrice = FormatMoneyShowToNumber($("#balanceSummary").val());

				if($("#moneyCheck").val() == ""){
					$("#moneyCheck").val(balance.toFixed(2).toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g,"$1,"));
				}
			});		 
			
		});

function disBtn() {
	var table = document.getElementById("showTotalPriceTable");
	var rowLength = table.rows.length;

	if (rowLength > 1) {

		$('button#submitFormPayment').prop('disabled', false);
	} else {
		$('button#submitFormPayment').prop('disabled', true);
	}
}

function vatAmount() {
	var result = $("#balanceSum").val();
	if ($("#balanceSum").val() !== '') {
		result = parseFloat($("#balanceSum").val().replaceAll(",", ""));
	}
	var vaq = $('#vatrate').val();
	if(vaq == 'notVat'){
		vaq = '0';
	}
	var vatRQ = parseFloat(parseFloat(vaq).toFixed(2).replace(",", ""));
	var beforeVat = parseFloat(0);
	var vat = parseFloat(0);
	var summary = parseFloat(0);
	var summaryT = parseFloat(0);
	var vatCo = parseFloat(107);
	var vatRq = parseFloat(0);

	summaryT = parseFloat(result * parseFloat(vatRQ));
	vat = parseFloat(summaryT / vatCo);

	beforeVat = parseFloat(result - vat);
	summary = parseFloat(beforeVat + vat);

//	$("#balanceOfTaxs").val(summary.toFixed(2));
	$("#beforeSale").val(beforeVat.toFixed(2));
//
//	$("#balanceOfTaxsShow").val(
//			summary.toFixed(2).toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g,
//					"$1,"));

	$("#balanceBeforeTaxs").val(beforeVat.toFixed(2));
//	$("#vats").val(vat.toFixed(2));

//	$("#balanceBeforeTaxsShow").val(
//			beforeVat.toFixed(2).toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g,
//					"$1,"));
//	$("#vatsShow").val(
//			vat.toFixed(2).toString()
//					.replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
};

function hideShowdat() {
	$("#sCustName").hide();
	$("#sCustNo").hide();
	$("#staxId").hide();
	$("#staxId2").hide();
	$("#scustAddress").hide();
	$("#scustBrach").hide();
	$("#suserGroup").hide();
	$("#sdebtCollection").hide();
	$("#sinvoiceNo").hide();
	$("#sserviceNo").hide();
	$("#sstartupDate").hide();
	$("#sendDate").hide();
	$("#sdeadlines").hide();
	$("#sinvoiceDate").hide();
	$("#addRowShow").hide();
	$("#buttonAddBillingListDis").hide();
	$("#addDataTableDedDis").hide();

}

function hideDetailPayment() {
	$("#sinputServiceType").hide();
	$("#sinputServiceDepartment").hide();
	$("#sinputServiceName").hide();
	$("#sinputServiceMoreData").hide();
	$("#sinputServiceAmount").hide();
	$("#moneyDedTxt").hide();

	$("#creditTypeTxt").hide();
	$("#edcTypeTxt").hide();
	$("#creditNoTxt").hide();
	$("#creditPriceTxt").hide();
	$("#creditPriceTxt").hide();

	$("#bankNoTxt").hide();
	$("#bankNameTxt").hide();
	$("#bankNameTxt").hide();
	$("#checkNoTxt").hide();
	$("#checkNoTxtless").hide();
	$("#dateCheckTxt").hide();
	$("#moneyCheckTxt").hide();

	$("#moneyTranTxt").hide();
	$("#sCustNotxt").hide();

	//
	$("#sshowProductSegment").hide();
}

function submitForm() {
	let pass = true;
	var balanceOfTaxs = parseFloat($("#balanceOfTaxs").val().replaceAll(",", ""));
	var summaryTax = parseFloat($("#summaryTax").val().replaceAll(",", ""));
	var balance = balanceOfTaxs + summaryTax ;
	
	if ($("#balanceSum").val() < balance) {
	 swal("ยอดเงินรับมาไม่ถูกต้อง")
	}else{
	
	hideShowdat();
	var radioButtons = document.getElementsByName("radioDed");
	var radioResult = "";
	// var invoiceNo = $("#invoiceNo").val();
	var deductible = [];
	var resultDeductible = [];
	var totalPrice = [];
	var resultTotalPrice = [];
	var resultTblSale = [];
	
	// get radio
	for (var x = 0; x < radioButtons.length; x++) {
		if (radioButtons[x].checked) {
			radioResult = radioButtons[x].value;
		}
	}
	
	// ภาษี หัก ณ ที่จ่าย
	var table = document.getElementById("sumDeductibleTable");
	var rowLength = table.rows.length;

	for (var i = 1; i < rowLength; i++) {
		deductible = [];
		var oCells = table.rows.item(i).cells;
		for (var fs = 0; fs < oCells.length; fs++) {

			deductible.push(oCells[fs].innerHTML);
		}
		resultDeductible.push(deductible);

	}
	
	//new logical
	tblbills = []
	billLists.forEach( element => {
		bill = {
			"inputServiceType" : element.serviceType,
			"inputServiceName" : element.serviceName,
			"inputServiceName2" : element.serviceName,
			"inputServiceCode" : element.serviceCode,
			"inputServiceMoreData" : element.serviceMoreData,
			"inputServiceAmount" : FormatMoneyShowToNumber(element.serviceAmount),
			"inputServiceDiscount" : FormatMoneyShowToNumber(element.serviceDiscount),
			"inputSpecialDiscount" : FormatMoneyShowToNumber(element.specialDiscount),
			"vatSale" : '-' == element.chkVatamount?0:FormatMoneyShowToNumber(element.chkVatamount),
			"summaryinvoice" : FormatMoneyShowToNumber(element.amountTotal.toString())
		}
		tblbills.push(bill);
	})
	
	// ตารางขาย
	var tblSale = document.getElementById("sumtableBillingList");
	var roeLeng = tblSale.rows.length;
	var servicenameEdt= 0;
	for (var v = 1; v < roeLeng; v++) {
		slae = [];
		var oCellss = tblSale.rows.item(v).cells;
		for (var fv = 0; fv < oCellss.length; fv++) {
			if (oCellss[fv].lastChild.value) {
				if(oCellss[fv].children.length > 1){
					slae.push(oCellss[fv].children[0].value);
					slae.push(oCellss[fv].children[1].value);
					tblbills[servicenameEdt++].inputServiceName = oCellss[fv].children[0].value;
				}
			
			}
			
		}

	}

	// ตาราง สรุป ยอดเงิน
	var tableTotalPrice = document.getElementById("sumTotalPriceTable");
	var rowLengths = tableTotalPrice.rows.length;

	for (var y = 1; y < rowLengths; y++) {
		totalPrice = [];
		var oCells = tableTotalPrice.rows.item(y).cells;
		for (var fs = 0; fs < oCells.length; fs++) {

			totalPrice.push(oCells[fs].innerHTML);
		}
		resultTotalPrice.push(totalPrice);

	}
	// list ขาย
	var listpaymentSale = [];
	var listpaymentSaleRQ = [];
	var vatSaleChk;
	
	for (var h = 0; h < resultTblSale.length; h++) {
		
		if('-' == resultTblSale[h][8]) {
			vatSaleChk = 0;
		}else{
			vatSaleChk = resultTblSale[h][8].replace(",", "");
		}
		
		listpaymentSale = []
		listpaymentSale = {
			"inputServiceType" : resultTblSale[h][1],
			"inputServiceName" : resultTblSale[h][2],
			"inputServiceCode" : resultTblSale[h][3],
			"inputServiceMoreData" : resultTblSale[h][4],
			"inputServiceAmount" : FormatMoneyShowToNumber(resultTblSale[h][5]),
			 "inputServiceDiscount" : FormatMoneyShowToNumber(resultTblSale[h][6]),
			 "inputSpecialDiscount" : FormatMoneyShowToNumber(resultTblSale[h][7]),
			"vatSale" : vatSaleChk,
			
			"summaryinvoice" : FormatMoneyShowToNumber(resultTblSale[h][10])
		}
		listpaymentSaleRQ.push(listpaymentSale);
	}

	// list ภาษี หัก ณ ที่จ่าย
	var listpaymentTaxQ = [];
	var listpaymentTaxRQ = [];

	for (var a = 0; a < resultDeductible.length; a++) {
		listpaymentTaxQ = []
		listpaymentTaxQ = {
			"custNo" : resultDeductible[a][1],
			"docDed" : resultDeductible[a][2],
			"radioDed" : resultDeductible[a][5],
			"moneyDed" : FormatMoneyShowToNumber(resultDeductible[a][4])*-1
		}
		listpaymentTaxRQ.push(listpaymentTaxQ);
	}

	// list TranPrice

	var listpaymentTranPriceQ = [];
	var listpaymentTranPriceRQ = [];

	for (var b = 0; b < resultTotalPrice.length; b++) {
		listpaymentTranPriceQ = [];
		if (resultTotalPrice[b][1] == "CC") {
			listpaymentTranPriceQ = {
				"typePayment" : resultTotalPrice[b][1],
				"moneyTran" : FormatMoneyShowToNumber(resultTotalPrice[b][2])
			}
		} else if (resultTotalPrice[b][1] == "CR") {
			listpaymentTranPriceQ = {
				"typePayment" : resultTotalPrice[b][1],
				"creditType" : resultTotalPrice[b][2],
				"bankName" : resultTotalPrice[b][4],
				"creditNo" : resultTotalPrice[b][3],
				"edcType" : resultTotalPrice[b][6],
				"edcCode" : resultTotalPrice[b][6],
				"creditPrice" : FormatMoneyShowToNumber(resultTotalPrice[b][5])
			}
		} else if (resultTotalPrice[b][1] == "CH") {
			listpaymentTranPriceQ = {
				"typePayment" : resultTotalPrice[b][1],
				"bankNo" : resultTotalPrice[b][2],
				"bankName" : resultTotalPrice[b][3],
				"branchCheck" : resultTotalPrice[b][4],
				"checkNo" : resultTotalPrice[b][5],
				"dateCheck" : resultTotalPrice[b][6],
				"moneyCheck" : FormatMoneyShowToNumber(resultTotalPrice[b][7])
			}
		}

		listpaymentTranPriceRQ.push(listpaymentTranPriceQ);
	}

//	if ($("#custNo").val() == "") {
//		$("#sCustNo").show();
//		return $("#custNo").focus();
//	}
//	if ($("#custName").val() == "") {
//		$("#sCustName").show();
//		return $("#custName").focus();
//	}
	if ($("#userGroup").val() == "") {
		$("#suserGroup").show();
		return $("#userGroup").focus();
	}
	
	if (chkOther) {
		if($("#taxId").val() == '') {
			$('#staxId').show();
			pass = false;
		}else if($("#taxId").val().length < 13) {
			$('#staxId2').show();
			pass = false;
		}
		if(!pass)return $("#taxId").focus();
	}else {
		if($("#custNo").val() == '') {
			$('#sCustNo').show();
			return $("#custNo").focus();
		}
	}
	
	if(chkOtherTax1) {
		if($('#custName').val() == '' || $('#custAddress').val() == '') {
			if($('#custName').val() == '') {
				$('#sCustName').show();
				$('#scustAddress').hide();
				return $("#custName").focus();
			}else if($('#custAddress').val() == '') {
				$('#scustAddress').show();
				$('#sCustName').hide();
				return $("#custAddress").focus();
			}
		}else{
			$('#sCustName').hide();
			$('#scustAddress').hide();
		}
	}
//	if ($("#custBrach").val() == "") {
//		$("#scustBrach").show();
//		return $("#custBrach").focus();
//	}

	
//	if ($("#custAddress").val() == "") {
//		$("#scustAddress").show();
//		return $("#custAddress").focus();
//	}
	
	var vatRate;

		vatRate = $("#vatrate").val();

	var dataSend = {
		"custName" : $("#custName").val(),
		"custNo" : $("#custNo").val(),
		"taxId" : $("#taxId").val(),
		"haveDocNo" : $("#docDed").val(),
		"custAddress" : $("#custAddress").val(),
		"custBrach" : $("#custBrach").val(),
		"userGroup" : $("#userGroup").val(),
		"userName" : $("#userName1").val(),
		"vatrate" : vatRate,
		"balanceBeforeTax" : parseFloat($("#balanceBeforeTax").val().replace(
				",", "")),
		"vat" : parseFloat($("#vat").val().replace(",", "")),
		"balanceSummary" : parseFloat($("#balanceSummary").val().replace(",",
				"")),
		"balanceBeforeTaxs" : parseFloat($("#balanceBeforeTaxs").val().replace(
				",", "")),
		"vats" : parseFloat($("#vats").val().replace(",", "")),
		"balanceOfTaxs" : parseFloat($("#balanceOfTaxs").val().replace(",", "")),
		"balanceSummarys" : parseFloat($("#balanceSummarys").val().replace(",",
				"")),
		"balanceSum" : parseFloat($("#balanceSum").val().replace(",", "")),
		"remark" : $("#remark").val(),
		"summaryTax" : parseFloat(($("#summaryTax").val().replace(",", ""))),
		"change" : parseFloat(($("#change").val().replace(",", ""))),
		"sale" : $("#sale").val(),
		"salespacial": $("#salespacial").val(),
//		"paymentBill" : listpaymentSaleRQ,
		"paymentBill" : tblbills,
		"paymentTax" : listpaymentTaxRQ,
		"paymentTranPrice" : listpaymentTranPriceRQ,
		"inputServiceDepartment":$("#inputServiceDepartment").val(),
		"productCode":productCode,
		"segmentCode":segmentCode,
		"productName":productName,
		"segmentName":segmentName,
		"incomeEdit":$("#incomeEdit").val(),
		"keyCode" : keyCode,
		"taxOnly" : $('#taxOnly').is(":checked"),
	}
	document.getElementById("submitFormPayment").disabled = true;

	$.ajax({
		type : "POST",
		url : ctx +"/paymenOthertService",
		data : JSON.stringify(dataSend),
		dataType : "json",
		async : false,
		contentType : "application/json; charset=utf-8",
		success : function(res) {
			if (res > 0) {
				window.location.href = "payOtherSuccess?idUser=" + res;
			}
		}
	})
	
	}//else

};
function findTypePayment() {
	var result = document.getElementById("typePayment").value;
	var credit = document.getElementById("credit");
	var check = document.getElementById("check");
	var money = document.getElementById("money");
	if (result == 'credit') {
		credit.style.display = "block";
		check.style.display = "none";
		money.style.display = "none";
	} else if (result == 'money') {
		credit.style.display = "none";
		check.style.display = "none";
		money.style.display = "block";
	} else if (result == 'check') {
		credit.style.display = "none";
		check.style.display = "block";
		money.style.display = "none";
	}
}

//function findBank() {
//	var bankNo = document.getElementById("bankNo").value;
//	var bankName = document.getElementById("bankName").value;
//	if (bankNo == "001") {
//		$('#bankName').val("ktb");
//	} else if (bankNo == "002") {
//		$('#bankName').val("scb");
//	} else if (bankNo == "003") {
//		$('#bankName').val("kbk");
//	}
//}
//
//function findBankNo() {
//	var bankNo = document.getElementById("bankNo").value;
//	var bankName = document.getElementById("bankName").value;
//	if (bankName == "ktb") {
//		$('#bankNo').val("001");
//	} else if (bankName == "scb") {
//		$('#bankNo').val("002");
//	} else if (bankName == "kbk") {
//		$('#bankNo').val("003");
//	}
//}
function findBank() {
	var bankNo = document.getElementById("bankNo").value;
		$('#bankName').val(bankNo);
		$('#bankNo').val(bankNo);
}

function findBankNo() {
	var bankName = document.getElementById("bankName").value;
		$('#bankNo').val(bankName);
		$('#bankName').val(bankName);

}
var current = "";
var billLists = [];
function buttonAddBillingList() {
	current = "addbill";
	hideDetailPayment();
//	if ($("#inputServiceType").val() == "") {
//		$("#sinputServiceType").show();
//		return $("#inputServiceType").focus();
//	}
	if ($("#inputServiceDepartment").val() == "") {
		$("#sinputServiceDepartment").show();
		return $("#inputServiceDepartment").focus();
	}
	
	if ($("#inputServiceName").val() == "") {
		$("#sinputServiceName").show();
		return $("#inputServiceName").focus();
	}
	if ($("#inputServiceMoreData").val() == "") {
		$("#sinputServiceMoreData").show();
		return $("#inputServiceMoreData").focus();
	}
	if ($("#inputServiceAmount").val() <= 0.00 ) {
		$("#sinputServiceAmount").show();
		return $("#inputServiceAmount").focus();
	}

	//
	if(isShPS) {
		if(!productCode || !segmentCode) {
			$("#sshowProductSegment").show();
			return
		}
	}
	
	var inputServiceTypeName = ''
	if($('#inputServiceType').val() != '') {
		inputServiceTypeName = $("#inputServiceType option:selected").text();
	}

	var table = document.getElementById("sumtableBillingList").rows.length;
	var inputServiceType = $("#inputServiceType").val(); // หน่วยรับได้
	var inputServiceDepartment = $("#inputServiceDepartment").val();
	var inputServiceCode = $("#inputServiceName").val();
	var inputServiceName = $("#inputServiceName option:selected").text();
	var inputServiceMoreData = $("#inputServiceMoreData").val();
	var inputServiceAmount = $("#inputServiceAmount").val();
	var inputServiceDiscount = $("#inputServiceDiscount").val();
	var inputSpecialDiscount = $("#inputSpecialDiscount").val();
	var vatRate = $('#vatrate').val();
	if(vatRate == 'Non-VAT'){
		vatRate = '0';
	}
	var moneyDed1 = $("#moneyDed1").val();
	var radiovat = document.getElementsByName("radiovat");

	 if(inputServiceDiscount == ""){ 
		 inputServiceDiscount = parseFloat(0).toFixed(2); } 
	 if(inputSpecialDiscount == ""){
		inputSpecialDiscount = parseFloat(0).toFixed(2); }
	 
		var radioResult = "";
		for (var x = 0; x < radiovat.length; x++) {
			if (radiovat[x].checked) {
				radioResult = radiovat[x].value;
			}
		}
		var specialDiscount = FormatMoneyShowToNumber(inputSpecialDiscount);
		vatRate = parseFloat(vatRate.replace(",", ""));

		var serviceAmount = FormatMoneyShowToNumber(inputServiceAmount);
		var serviceMoreData = FormatMoneyShowToNumber(inputServiceMoreData);
		var amountTotal;
		var vatamount;
		if(radioResult == "beforvat"){
			var amountDiscount = disDiscount(serviceAmount);
			var amountBeforVat = disVat(amountDiscount,radioResult);
			 amountTotal = calurateVatRate(amountBeforVat, vatRate);
			vatamount = amountTotal- amountDiscount;
			
		}else{
			var amountBeforVat = disVat(serviceAmount ,radioResult);
			var amountDiscount = disDiscount(amountBeforVat);
			 amountTotal = calurateVatRate(amountDiscount, vatRate);
			 vatamount = amountTotal- amountDiscount;
			 
			 serviceAmount = amountBeforVat;
			 
		}
		
		var chkVatamount;
		if("Non-VAT" == $("#vatrate option:selected").val()) {
			chkVatamount = '-';
		}else{
			if(vatamount < 0 ){
				vatamount = vatamount*(-1);
				chkVatamount = vatamount.toFixed(2).toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,");
			}else{
				chkVatamount = vatamount.toFixed(2).toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,");
			}
		}
		
		var count = parseFloat(0);
		count = parseFloat(count + parseFloat(table));

		let billList = {
				"serviceTypeName" : inputServiceTypeName,
				"count" : count,
				"serviceType" : inputServiceType,
				"serviceName" : inputServiceName,
				"serviceCode" : inputServiceCode,
				"serviceMoreData" : serviceMoreData,
				"serviceAmount" : serviceAmount.toFixed(2).toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"),
				"serviceDiscount" : inputServiceDiscount,
				"specialDiscount" : specialDiscount.toFixed(2).toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"),
				"chkVatamount" : chkVatamount,
				"moneyDed1" : moneyDed1,
				"amountTotal" : amountTotal
		}
		
		billLists.push(billList);
		
		var stype =inputServiceTypeName.split("-")
		var sname =inputServiceName.split("-")
		
		if(stype.length > 1){
			stype = stype[1]
		}else{
			stype = inputServiceTypeName
		}
		var len = sname.length;
		var sn = 1;
		var text="";
		if(len > 1){
			for(; sn < len; sn++ ){
				if(sn === 1)text += sname[sn];
				if(sn > 1)text += '-'+sname[sn];
			}
			sname = text;
		}else{
			sname = inputServiceName
		}
		
		
	var markup = "<tr><td>"
		+ count
		+ "</td><td>"
		+ stype
		+ "<input class='form-control' type='hidden'	name='inputServiceType'value='"
		+ inputServiceType
		+ "' />"
		+ "</td><td>"
		+ "<input class='form-control' type='text'	name='serviceNametxt'value='"
		+ sname
		+ "' />"
		+ "<input class='form-control' type='hidden'	name='serviceCodetxt'value='"
		+ inputServiceCode
		+ "' />"
		+ "</td><td>"
		+ serviceMoreData
		+ "</td><td>"
		+ serviceAmount.toFixed(2).toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,")
		+ "</td><td>"
		+ inputServiceDiscount
		+ "</td><td>" 
		+ specialDiscount.toFixed(2).toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,")
		+"</td><td>"
		+ chkVatamount
		+ "</td><td>"
		+ moneyDed1
		+"</td><td>"
		+ amountTotal.toFixed(2).toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,")
		+ "</td><td id='delete'><a  onclick='deleteTableSale("
		+ count
		+ ")'><span class='glyphicon glyphicon-trash'></span></a></td></tr>";
$("#sumtableBillingList").find('tbody').append(markup);

	

	var moneyCC = $("#moneyTran").val();
	var balanceBeforeTaxRQ = $("#balanceBeforeTax").val();
	var vatRQ = $("#vat").val();
	var money = parseFloat(moneyCC.replace(",", ""));
	$("#inputSpecialDiscount").val(parseFloat(0).toFixed(2));
	$("#inputServiceDiscount").val(parseFloat(0).toFixed(2));
	$("#inputSpecialDiscount").prop('disabled', true);
	//money = parseFloat(money + amount); ไม่แน่ใจว่า ตรงนี้ทำอะไร
	vatRQ = parseFloat(vat + vatRQ);
	balanceBeforeTaxRQ = parseFloat(amountBeforVat + balanceBeforeTaxRQ);

//	$("#moneyCheck").val(
//			money.toFixed(2).toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g,
//					"$1,"));
//	$("#creditPrice").val(
//			money.toFixed(2).toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g,
//					"$1,"));

	$("#balanceBeforeTax").val(balanceBeforeTaxRQ.toFixed(2));
	$("#vat").val(vatRQ.toFixed(2));


	var table = document.getElementById("sumtableBillingList");
	var re = replaseIndexV4(table);
	$("#moneyDed1").val(parseFloat(0).toFixed(2).replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
	document.getElementById('vatrate').disabled = true;

}
function calurateVatRate (amountDiscount ,vatRate){

	return amountDiscount +( amountDiscount *vatRate/100  );
	
}
function deleteTableSale(count) {
	current ="delbill";
	
	let resObjs =  billLists.filter(function(Obj) {
		return Obj.count != count;
	});
	
	
	
	billLists = resObjs
	var x = 1
	tblbills = []
	billLists.forEach( element => {
		bill = {
				"amountTotal": element.amountTotal,
				"chkVatamount": element.chkVatamount,
				"count": x,
				"moneyDed1": element.serviceAmount,
				"serviceAmount": element.serviceAmount,
				"serviceCode": element.serviceCode,
				"serviceDiscount": element.serviceDiscount,
				"serviceMoreData": element.serviceMoreData,
				"serviceName": element.serviceName,
				"serviceType": element.serviceType,
				"serviceTypeName": element.serviceTypeName,
				"specialDiscount": element.specialDiscount
		}
		x++
		tblbills.push(bill);
	})
	
	billLists = tblbills;
	
	
	var balanceSummary = $("#balanceSummary").val();
	var balanceBeforeTaxRQ = $("#balanceBeforeTax").val();
	var vatRQ = $("#vat").val();

	var vatRate = $('#vatrate').val();
	if(vatRate == 'notVat'){
		vatRate = '0';
	}
	var table = document.getElementById("sumtableBillingList");
	if (table.rows.length > 0) {
		for (var i = 1; i <= table.rows.length; i++) {
			if (count == i) {
				var oCells = table.rows.item(i).cells;
				var tbMoney = parseFloat(oCells[7].innerHTML.replace(",", ""));
				var amounts = parseFloat(tbMoney);
				var vat = parseFloat((amounts * parseFloat(vatRate))
						/ parseFloat(107));
				var beforeVat = parseFloat(amounts - vat);

				var reMoney = parseFloat(parseFloat(balanceSummary) - tbMoney);
				var vatSv = parseFloat(parseFloat(vatRQ) - vat);
				var beforeSv = parseFloat(parseFloat(balanceBeforeTaxRQ)
						- beforeVat);

				$("#moneyTran").val(reMoney.toFixed(2));
// $("#balanceSummarys").val(reMoney.toFixed(2));
				$("#balanceSummary").val(reMoney.toFixed(2));
				$("#balanceBeforeTax").val(beforeSv.toFixed(2));
				$("#balanceSummaryShow").val(
						reMoney.toFixed(2).toString().replace(
								/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
				$("#vat").val(vatSv.toFixed(2));
				table.deleteRow(parseFloat(count));
			}
		}
	}
	
	if(1 == table.rows.length) {
		document.getElementById('vatrate').disabled = false;
	}
	
	replaseIndexV4(table);
}

function addRow() {
	hideDetailPayment();
	var table = document.getElementById("deductibleTable").rows.length;
	var radioButtons = document.getElementsByName("radioDed");
	var radioResult = "";
	var radioResultValue = "";
	var invoiceNo = $("#invoiceNo").val();
	for (var x = 0; x < radioButtons.length; x++) {
		if (radioButtons[x].checked) {
			radioResult = radioButtons[x].value;

			if(radioResult == "01"){
				radioResult = "69 ทวิ";
				radioResultValue = "69BIS";
			}else if(radioResult =="02"){
				radioResult = "3 เตรส";
				radioResultValue = "3TREDECIM";
			}else if(radioResult == "03"){
				radioResult = "69 ตรี";
				radioResultValue = "69TRE";
			}
		}
	}
	var docDed = $("#docDed").val();
	var dmoney = $("#moneyDed").val();
	var custNo = $("#custNo").val();
	/*
	 * if(invoiceNo == ""){ alert(" กรุณากรอกใหม่ !"); return
	 * $("#invoiceNo").focus(); } if(docDed == ""){ alert("กรุณากรอกเลขที่เอกสาร
	 * กรุณากรอกใหม่ !"); return $("#docDed").focus(); }
	 */
//	if (custNo == "") {
//		$("#sCustNo").show();
//		return $("#custNo").focus();
//	}
	if (dmoney == "") {
		$("#moneyDedTxt").show();
		return $("#moneyDed").focus();
	}

	var moneyDed = dmoney;
	var count = 1;

	for (count; count < table; count++) {
		count + table;
	}

	if (parseFloat(moneyDed) < 0) {
//		alert("กรุณากรอกจำนวนเงิน  กรุณากรอกใหม่ !");
		swal("จำนวนเงินเกิน กรุณากรอกใหม่ !")
		return $("#moneyDed").focus();
	}
	if (moneyDed == "") {
//		alert("กรุณากรอกจำนวนเงิน  กรุณากรอกใหม่ !");
		swal("จำนวนเงินเกิน กรุณากรอกใหม่ !")
		return $("#moneyDed").focus();
	}
	var markup = "<tr><td>"
			+ tdAutoNumber()
			+ "</td><td>"
			+ custNo
			+ "</td><td>"
			+ docDed
			+ "</td><td>"
			+ radioResult
			+ "</td><td>"
			+ "-"+moneyDed.replace(/(\d)(?=(\d\d\d)+(?!\d))/g,"$1,")
			+ "<td style='display: none'>"+ radioResultValue + "</td>"
			+ "</td><td><a onclick='myDeleteFunction("
			+ tdAutoNumber()
			+ ")'><span class='glyphicon glyphicon-trash'></span></a></td></tr>";

	$("#deductibleTable").find('tbody').append(markup);
	$("#sCustNo").hide();
};

function myDeleteFunction(count) {
	var table = document.getElementById("deductibleTable");
	if (table.rows.length > 0) {
		for (var i = 1; i <= table.rows.length; i++) {
			if (count == i) {
				table.deleteRow(parseFloat(count));
			}
		}
	}
	// tdAutoNumber();
	replaseIndexV1(table);

}
function tdAutoNumber() {
	var table = document.getElementById("deductibleTable");
	var txt = "";
	var i;
	for (i = 0; i < table.rows.length; i++) {
		txt = table.rows.length;
	}
	return txt;
}
function myDeleteDed(count) {
	current = "delwt";
	
	var bas = $("#balanceSummarys").val();
	var balance = parseFloat(bas.replace(",", ""));
	var tableDed = document.getElementById("showDeductibleTable");
	var table = document.getElementById("sumDeductibleTable");
	var erq = $("#balanceSummary").val();
	var result = parseFloat(erq.replace(",", ""));
	var st = $("#summaryTax").val();
	var summaryTax = parseFloat(st.replaceAll(",", ""));
	var summaryTa = parseFloat(0);

	if (table.rows.length > 0) {
		for (var i = 1; i < tableDed.rows.length; i++) {

			if (count == i) {
				
				var oCells = table.rows.item(i).cells;
				sumded -= parseInt(currentList[count-1])
				var total = parseFloat(oCells[3].innerHTML.replace(",", ""));
				var balances = parseFloat(parseFloat(balance)
						+ parseFloat(total ));
				
				if (balances < result) {
					// balance = result;
					$("#change").val(
							parseFloat(0).toFixed(2).toString().replace(
									/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
				}

// $("#balanceSummarys").val(balance.toFixed(2));
				// $("#balanceSummaryShow").val(balance.toFixed(2));

				// vatAmount();
				for( var i = 0; i < currentList.length; i++){ 
			        if (i == count-1) { 
			        	currentList.splice(i, 1); 
			        }
			    }
				
				tableDed.deleteRow(count);
				table.deleteRow(count);
				removeTax();
			}
		}
	}
	replaseIndexV2(table);
	replaseIndexV3(tableDed);

}
var sumded = 0;
var currentList = [];

function addDataTableDed() {
	current = "addwt";
	var xx = $("#moneyDed").val().replaceAll(",", "")
	sumded += parseInt(xx);
	currentList.push(parseInt(xx));
	
	var oTable = document.getElementById('deductibleTable');
	var tableDed = document.getElementById("showDeductibleTable");
	var result = [];
	// var deq = $("#deduction").val();
	// var deduction = parseFloat(deq.replace(",", ""));
	var rowLength = oTable.rows.length;
	var number = parseFloat(tableDed.rows.length - parseFloat(1));
	var bas = $("#balanceSummarys").val();
	var balance = parseFloat(bas.replace(",", ""));
	var basu = $("#balanceSummarys").val();
	var branSum = parseFloat(basu.replace(",", ""));
	var sq = $("#summaryTax").val();
	var summaTax = parseFloat(sq);
	for (var i = parseFloat(1); i < rowLength; i++) {
		var oCells = oTable.rows.item(i).cells;
		result = [];
		for (var fs = 0; fs < oCells.length; fs++) {
			result.push(oCells[fs].innerHTML);
		}
		if (branSum == "") {
			branSum = parseFloat(0);
		}
		if (summaTax == "") {
			summaTax = parseFloat(0);
		}
		var plus = parseFloat(summaTax) + parseFloat(result[4]);
		if (plus > parseFloat(branSum)) {
			swal("จำนวนเงินเกิน กรุณากรอกใหม่ !")
			//alert("จำนวนเงินเกิน กรุณากรอกใหม่ !");
			return;
		}
		var prict = result[4]
		var numberRun = number + i;
		var markup = "<tr><td>"
				+ numberRun
				+ "</td><td>"
				+ result[3]
				+ "</td><td>"
				+ prict
				+ "</td><td><a onclick='myDeleteDed("
				+ numberRun
				+ ")'><span class='glyphicon glyphicon-trash'></span></a></td></tr>";
		$("#showDeductibleTable").find('tbody').append(markup);
		var markup1 = "<tr><td>" + numberRun + "</td><td>" + result[1]
				+ "</td><td>" + result[2] + "</td><td>" + result[3]
				+ "</td><td>" + result[4] + "</td><td>" + result[5] + "</td></tr>";
		$("#sumDeductibleTable").find('tbody').append(markup1);
		var prict1 = prict
		balance = parseFloat(balance) - parseFloat(prict1 );

	}
	for (var i = document.getElementById("deductibleTable").rows.length; i > 1; i--) {
		document.getElementById("deductibleTable").deleteRow(i - 1);
	}
	$("#balanceSummarys").val(parseFloat(balance *-1).toFixed(2).replace(",", ""));
	
	
	 summaryTax();
	// vatAmount();
	replaseIndexV3(tableDed);
}
// เงินสด
function addDataTableMoneyTranPrice() {
	hideDetailPayment();
	var table = document.getElementById("showTotalPriceTable").rows.length;
	var number = parseFloat(table - parseFloat(1));
	var count = parseInt(1);
	var numberRun = count + number;

	var moneyss = $("#moneyTran").val();
	if (moneyss == "") {
		moneyss = parseFloat(0).toFixed(2);
	}
	var money = FormatMoneyShowToNumber(moneyss);

	if (money == "") {
		$("#moneyTranTxt").show();
		return $("#moneyTran").focus();
	}
	if (money < 0) {
		$("#moneyTranTxt").show();
		return $("#moneyTran").focus();
	}
	var nameMode = "CC";
	var nameMode1 = "เงินสด";

	var sumx = $("#summaryTax").val();
	if (sumx == "") {
		sumx = parseFloat(0).toFixed(2);
	}
	var summaryTax = parseFloat(sumx.replaceAll(",", ""));

	var ba2 = $("#balanceSum").val();
	if (ba2 == "") {
		ba2 = parseFloat(0).toFixed(2);
	}
	var balanceS = parseFloat(ba2.replaceAll(",", ""));
	if (balanceS == "") {
		balanceS = parseFloat(0);
	}
	var bag2 = $("#balanceSummarys").val();
	if (bag2 == "") {
		bag2 = parseFloat(0).toFixed(2);
	}
	var branSum = parseFloat(bag2.replaceAll(",", ""));

	var markup = "<tr><td>"
			+ numberRun
			+ "</td><td>"
			+ nameMode1
			+ "</td><td>"
			+ money.toFixed(2).toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g,
					"$1,")
			+ "</td><td><a onclick='myDeleteSumCreditTranPrice("
			+ numberRun
			+ ")'><span class='glyphicon glyphicon-trash'></span></a></td></tr>";
	$("#showTotalPriceTable").find('tbody').append(markup);

	var markup1 = "<tr><td>" + numberRun + "</td><td>" + nameMode + "</td><td>"
			+ money + "</td></tr>";
	$("#sumTotalPriceTable").find('tbody').append(markup1);

	$("#moneyTran").val("");
	
	var beq = $("#balanceSummarys").val();
	var balan = parseFloat(beq.replaceAll(",", ""));
	var ceq = $("#balanceSummarys").val();
	var changeRQ = parseFloat(ceq.replaceAll(",", ""));

	changeRQ = parseFloat(money) - parseFloat(balan);
	balan = parseFloat(balan) - parseFloat(money);
	if (balan < 0) {
		balan = parseFloat(0);
	}
	if (changeRQ < 0) {
		changeRQ = parseFloat(0);
	}

	var sop = $("#balanceSummary").val();
	var sumPrice = parseFloat(sop.replaceAll(",", ""));

	$("#balanceSummarys").val(balan.toFixed(2));
//	$("#moneyTran").val(balan.toFixed(2));
	$("#creditPrice").val(balan.toFixed(2));
	$("#moneyCheck").val(balan.toFixed(2));
	balanceS = parseFloat(balanceS + money + (summaryTax));

	if (parseFloat(sumPrice) < parseFloat(balanceS)) {
		// sumPrice = parseFloat(sumPrice) + parseFloat(money)
		balanceS = balanceS - (summaryTax);
		$("#balanceSum").val(parseFloat(balanceS).toFixed(2).replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
		$("#balanceSumShow").val(
				parseFloat(balanceS).toFixed(2).replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
	} else {
		$("#balanceSum").val(parseFloat(money).toFixed(2).replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
		$("#balanceSumShow").val(
				parseFloat(money).toFixed(2).replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
	}
	
	

	vatAmount();
	disBtn();
	changeMoney(changeRQ);
}

// add ข้อมูลลง เครดิต
function addDataSumCreditTranPrice() {
	var table = document.getElementById("showTotalPriceTable").rows.length;
	var oTable = document.getElementById('creditTable');
	var result = [];
	var nameMode = "CR";
	var nameMode1 = "บัตรเครดิต";
	var rowLength = oTable.rows.length;
	var count = parseInt(0);
	var number = parseFloat(table - parseFloat(1));
	var bas2 = $("#balanceSum").val();
	
	if (bas2 == "") {
		bas2 = parseFloat(0).toFixed(2);
	}
	var balanceS = parseFloat(bas2.replace(",", ""));

	if (balanceS == "") {
		balanceS = parseFloat(0);
	}
	for (i = 1; i < rowLength; i++) {
		var oCells = oTable.rows.item(i).cells;
		result = [];
		for (var fs = 0; fs < oCells.length; fs++) {
			result.push(oCells[fs].innerHTML);
		}
		var ba23 = $("#balanceSummarys").val();
		var branSum = parseFloat(ba23.replace(",", ""));
		var ba24 = $("#balanceSummaryShow").val();
		var branSum1 = parseFloat(ba24.replace(",", ""));
		
		var bard = $("#balanceSum").val();
		var brana = parseFloat(bard.replace(",", ""));

		var stc = $("#summaryTax").val();
		var summaTax = parseFloat(stc.replace(",", ""));

		if (branSum == "") {
			branSum = parseFloat(0);
		}
		if (brana == "") {
			brana = parseFloat(0);
		}
		var plus = parseFloat(result[4].toString().replace(",", ""));

		if (plus > parseFloat(branSum)) {
//			alert("จำนวนเงินเกิน กรุณากรอกใหม่ !");
			swal("จำนวนเงินเกิน กรุณากรอกใหม่ !")
			return;
		}

		var numberRun = number + i;
		var markup = "<tr><td>"
		+ numberRun
		+ "</td><td>"+ nameMode1+ "</td><td>"+ result[4].replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,")	+ "</td><td><a onclick='myDeleteSumCreditTranPrice("+ numberRun	+ ")'><span class='glyphicon glyphicon-trash'></span></a></td></tr>";
$("#showTotalPriceTable").find('tbody').append(markup);
var markup1 = "<tr><td>" + numberRun + "</td><td>" + nameMode+ "</td><td>" + result[1] + "</td><td>" + result[2] + "</td><td>" + result[3]	+ "</td><td>" + result[4]+ "</td><td>"+ result[5]+ "</td></tr>";
$("#sumTotalPriceTable").find('tbody').append(markup1);


		var ba3a = $("#balanceSummaryShow").val();
		var balan = parseFloat(ba3a.replace(",", ""));
		var baGet = $("#balanceSumShow").val();
		var balanget = parseFloat(baGet.replace(",", ""));
		var price = result[4].replace(",", "")
		
		

		balan = (parseFloat(balan) - parseFloat(balanget) ) - price;
		$("#balanceSummarys").val(balan.toFixed(2));
		$("#moneyTran").val(balan.toFixed(2));
		$("#creditPrice").val(balan.toFixed(2));
		$("#moneyCheck").val(balan.toFixed(2));
		balanceS = parseFloat(balanceS) + parseFloat(price);
		$("#balanceSum").val(parseFloat(balanceS).toFixed(2));
		$("#balanceSumShow").val(
				parseFloat(balanceS).toFixed(2).replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
		
		
	}
	for (var i = document.getElementById("creditTable").rows.length; i > 1; i--) {
		document.getElementById("creditTable").deleteRow(i - 1);
	}
	vatAmount();
	disBtn();
	
	
}
function addDataSumCheckTranPrice() {
	var table = document.getElementById("showTotalPriceTable").rows.length;
	var oTable = document.getElementById('checkTable');
	var result = [];
	var nameMode = "CH";
	var nameMode1 = "เช็ค";
	var rowLength = oTable.rows.length;
	var count = parseInt(0);
	var number = parseFloat(table - parseFloat(1));
	
	var ba3d = $("#balanceSum").val();
	
	if (ba3d == "") {
		ba3d = parseFloat(0).toFixed(2);
	}

	var balanceS = parseFloat(ba3d.replace(",", ""));
	if (balanceS == "") {
		balanceS = parseFloat(0);
	}
	for (i = 1; i < rowLength; i++) {
		var oCells = oTable.rows.item(i).cells;
		result = [];
		for (var fs = 0; fs < oCells.length; fs++) {
			result.push(oCells[fs].innerHTML);
		}
		var banSu = $("#balanceSummarys").val();
		var branSum = parseFloat(banSu.replace(",", ""));
		var ssq = $("#balanceSum").val();
		var brana = parseFloat(ssq.replace(",", ""));
		var sumTax = $("#summaryTax").val();
		var summaTax = parseFloat(sumTax.replace(",", ""));

		if (branSum == "") {
			branSum = parseFloat(0);
		}
		if (brana == "") {
			brana = parseFloat(0);
		}
		var plus = parseFloat(result[6].toString().replace(",", ""));
		if (plus > parseFloat(branSum)) {
			//alert("จำนวนเงินเกิน กรุณากรอกใหม่ !");
			swal("จำนวนเงินเกิน กรุณากรอกใหม่ !")
			return;
		}
		var numberRun = number + i;
		var markup = "<tr><td>"
				+ numberRun
				+ "</td><td>"
				+ nameMode1
				+ "</td><td>"
				+ result[6].replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,")
				+ "</td><td><a onclick='myDeleteSumCreditTranPrice("
				+ numberRun
				+ ")'><span class='glyphicon glyphicon-trash'></span></a></td></tr>";
		$("#showTotalPriceTable").find('tbody').append(markup);
		var markup1 = "<tr><td>" + numberRun + "</td><td>" + nameMode
				+ "</td><td>" + result[1] + "</td><td>" + result[2]
				+ "</td><td>" + result[3] + "</td><td>" + result[4]
				+ "</td><td>" + result[5] + "</td><td>" + result[6]
				+ "</td></tr>";
		$("#sumTotalPriceTable").find('tbody').append(markup1);
		var ba3a = $("#balanceSummaryShow").val();
		var balan = parseFloat(ba3a.replace(",", ""));
		var baGet = $("#balanceSumShow").val();
		var balanget = parseFloat(baGet.replace(",", ""));

		var price = result[6].replace(",", "")
		balan = (parseFloat(balan) - parseFloat(balanget)) - parseFloat(price);
		$("#balanceSummarys").val(balan.toFixed(2));
		$("#moneyTran").val(balan.toFixed(2));
		$("#creditPrice").val(balan.toFixed(2));
		$("#moneyCheck").val(balan.toFixed(2));
		balanceS = parseFloat(parseFloat(balanceS) + parseFloat(price));
		$("#balanceSum").val(parseFloat(balanceS).toFixed(2));
		$("#balanceSumShow").val(
				parseFloat(balanceS).toFixed(2).replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
		
	}
	for (var i = document.getElementById("checkTable").rows.length; i > 1; i--) {
		document.getElementById("checkTable").deleteRow(i - 1);
	}
	vatAmount();
	disBtn();
	

}

function addDataTableCheck() {
	hideDetailPayment()
	var summaryTax = $("#summaryTax").val();
	var table = document.getElementById("checkTable").rows.length;
	var bankNo = document.getElementById("bankNo").value;
	var bankName = document.getElementById("bankName").value;
	var checkNo = $("#checkNo").val();
	var branchCheck = $("#branchCheck").val();
	var moneyCa = $("#moneyCheck").val();
	var dateCheck = $("#dateCheck").val();
	var date = dateCheck.split("-");
	var dateChek = date[2] + "/" + date[1] + "/" + date[0]

	if (moneyCa == "") {
		moneyCa = parseFloat(0).toFixed(2);
	}
	var moneyCheck = parseFloat(moneyCa.replaceAll(",", ""));
	var count = parseInt(1);
	for (count; count < table; count++) {
		count
	}
	if (bankNo == "") {
		$("#bankNoTxt").show();
		return $("#bankNo").focus();
	}
	if (bankName == "") {
		$("#bankNameTxt").show();
		return $("#bankName").focus();
	}
	if (parseFloat(moneyCheck) < parseFloat(0)) {
		$("#bankNameTxt").show();
		return $("#bankName").focus();
	}
	if (checkNo.length == "" ) {
		$("#checkNoTxt").show();
		return $("#checkNo").focus();
	}
	if (checkNo.length < 7) {
		$("#checkNoTxtless").show();
		return $("#checkNo").focus();
	}
	if (dateCheck == "") {
		$("#dateCheckTxt").show();
		return $("#dateCheck").focus();
	}

	

	if (parseFloat(moneyCheck) < parseFloat(0)) {
		$("#moneyCheckTxt").show();
		return $("#moneyCheck").focus();
	}
	if (moneyCheck == "") {
		$("#moneyCheckTxt").show();
		return $("#moneyCheck").focus();
	}

	var bankName =document.getElementById('bankName').options[document.getElementById('bankName').selectedIndex].text;
	var markup = "<tr><td>"
			+ count
			+ "</td><td>"
			+ bankNo
			+ "</td><td>"
			+ bankName
			+ "</td><td>"
			+ branchCheck
			+ "</td><td>"
			+ checkNo
			+ "</td><td>"
			+ dateChek
			+ "</td><td>"
			+ moneyCheck.toFixed(2).toString().replace(
					/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,")
			+ "</td><td><a onclick='myDeleteCheckTranPrice("
			+ count
			+ ")'><span class='glyphicon glyphicon-trash'></span></a></td></tr>";
	$("#checkTable").find('tbody').append(markup);

}

function addDataTablecreditTranPrice() {
	hideDetailPayment();
	var table = document.getElementById("creditTable").rows.length;
	var creditType = document.getElementById("creditType").value;
	var creditTypeText = $("#creditType option:selected").text();
	var edcType = document.getElementById("edcType");
	var selectedText = edcType.options[edcType.selectedIndex].text;
	var creditNo = $("#creditNo").val();
	var crepi = $("#creditPrice").val();
	if (crepi == "") {
		crepi = parseFloat(0).toFixed(2);
	}
	var creditPrice = parseFloat(crepi.replaceAll(",", ""));
	var nameMode = "บัตรเครดิต";
	var sumTax = $("#summaryTax").val();
	var summaryTax = parseFloat(sumTax.replaceAll(",", ""));
	// var moneyT = parseFloat(creditPrice - parseFloat(summaryTax));
	if (edcType == "001") {
		edcType = "ธนาคารกรุงไทย";
	} else if (edcType == "002") {
		edcType = "ธนาคารไทยพานิชย์";
	} else if (edcType == "003") {
		edcType = "ธนาคารกสิกรไทย";
	}

	var count = parseInt(1);
	for (count; count < table; count++) {
		count
	}

	if (creditType == "") {
		$("#creditTypeTxt").show();
		return $("#creditType").focus();
	}
	if (edcType == "") {
		$("#edcTypeTxt").show();
		return $("#edcType").focus();
	}

	if (creditNo.length == "" || creditNo.length < 16) {
		$("#creditNoTxt").show();
		return $("#creditNo").focus();
	}

	if (parseFloat(creditPrice) < parseFloat(0)) {
		$("#creditPriceTxt").show();
		return $("#creditPrice").focus();
	}
	if (creditPrice == "") {
		$("#creditPriceTxt").show();
		return $("#creditPrice").focus();
	}

	var markup = "<tr><td>"
			+ count
			+ "</td><td>"
			+ creditTypeText
			+ "</td><td>"
			+ creditNo
			+ "</td><td>"
			+ selectedText
			+ "</td><td>"
			+ creditPrice.toFixed(2).toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,")
			+ "</td><td style='display: none'>"
			+ edcType.value 
			+ "</td><td><a onclick='myDeletecreditTranPrice("
			+ count
			+ ")'><span class='glyphicon glyphicon-trash'></span></a></td></tr>";
	$("#creditTable").find('tbody').append(markup);

//	$("#creditNo").val("");
//	$("#creditPrice").val("");
//	$("#edcType").val("");
//	$("#creditType").val("");
}

function sumTranPrice() {
	var result = document.getElementById("typePayment").value;
	if ($("#moneyTran").val() == "" || $("#moneyTran").val() == "0.00" ) {
		$("#moneyTranTxt").show();
		return $("#moneyTran").focus();
	}
	$('addRow').attr("disabled", "true");
	$("#addRow").hide();
	$("#addRowShow").show();
	
	$("#delete").hide();

	$('addDataTableDedShow').attr("disabled", "true");
	$("#addDataTableDedShow").hide();
	$("#addDataTableDedDis").show();

	$('buttonAddBillingListDis').attr("disabled", "true");
	$("#buttonAddBillingList").hide();
	$("#buttonAddBillingListDis").show();

	if (result == 'credit') {
		addDataSumCreditTranPrice();
	} else if (result == 'money') {
		addDataTableMoneyTranPrice();
	} else if (result == 'check') {
		addDataSumCheckTranPrice();
	}

	var tablesumTotals = document.getElementById("showTotalPriceTable");
	replaseIndex(tablesumTotals);
	
//	$("#balanceSum").val(s.toFixed(2));
//	$("#balanceSumShow").val(
//			s.toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
}
function myDeletecreditTranPrice(count) {
	var tablesumTotal = document.getElementById("creditTable");
	if (tablesumTotal.rows.length > 0) {
		tablesumTotal.deleteRow(count);

	}
}
function myDeleteSumCreditTranPrice(numberRun) {
	var tablesumTotals = document.getElementById("showTotalPriceTable");
	var tablesumTotal = document.getElementById("sumTotalPriceTable");

	if (numberRun == "1") {

		$("#addRow").show();
		$("#addRowShow").hide();
		$("#delete").show();

		$("#buttonAddBillingList").show();
		$("#buttonAddBillingListDis").hide();

		$("#addDataTableDedShow").show();
		$("#addDataTableDedDis").hide();
	}

	var summaryTa = parseFloat(0);
	var banol = $("#balanceSummarys").val();
	var balance = parseFloat(banol.replace(",", ""));
	var sump = $("#balanceSummary").val();
	var sumPrice = parseFloat(sump.replace(",", ""));
	var bansum = $("#balanceSum").val();
	var balanceSum = parseFloat(bansum.replace(",", ""));
	var sumTax = $("#summaryTax").val();
	var summaryTax = parseFloat(sumTax.replace(",", ""));
	var res = parseFloat(0);
	var chen = $("#change").val();
	if (tablesumTotals.rows.length > 0) {
		for (var i = 1; i < tablesumTotals.rows.length; i++) {
			if (numberRun == i) {
				var oCells = tablesumTotals.rows.item(i).cells;
				var total = oCells[2].innerHTML.replace(",", "");
				balance = parseFloat(parseFloat(balance) + parseFloat(total));
				balanceSum = parseFloat(parseFloat(balanceSum)- parseFloat(total))
				if (parseFloat(balanceSum) < 0) {
					balanceSum = parseFloat(0);
				}

				balance = parseFloat(balance) - parseFloat(summaryTax);
				res = parseFloat(balance) + parseFloat(chen);
				 if (parseFloat(res) >= parseFloat(sumPrice)) {
				 $("#change").val(parseFloat(0).toFixed(2));
				 }
				
				 if (parseFloat(sumPrice) < parseFloat(balance)) {
				 balance = parseFloat(sumPrice);
				 $("#change").val(parseFloat(0).toFixed(2));
				 }
				
				 if (parseFloat(sumPrice) >= parseFloat(res)) {
				 var totalChange = parseFloat(balanceSum)-
				 parseFloat(sumPrice);
				 if(totalChange > 0){
				 $("#change").val(parseFloat(totalChange).toFixed(2));
				 }
								
				 }
				$("#moneyCheck").val(
						balance.toFixed(2).toString().replace(
								/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
				$("#creditPrice").val(
						balance.toFixed(2).toString().replace(
								/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
				$("#moneyTran").val(
						balance.toFixed(2).toString().replace(
								/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
				$("#balanceSummarys").val(
						balance.toFixed(2).toString().replace(
								/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));

				
				vatAmount();

				tablesumTotals.deleteRow(numberRun);
				tablesumTotal.deleteRow(numberRun);

			}
		}

	}
	
	replaseIndex(tablesumTotals);

	
}

function myDeleteCheckTranPrice(count) {
	var tablesumTotal = document.getElementById("checkTable");
	if (tablesumTotal.rows.length > 0) {
		tablesumTotal.deleteRow(count);
	}
}

function summaryTax() {
	var table = document.getElementById("showDeductibleTable");
	var rowLength = table.rows.length;
	var summary = parseFloat(0);

	for (var i = 1; i < rowLength; i++) {
		var oCells = table.rows.item(i).cells;
		var total = parseFloat(oCells[2].innerHTML);
		summary += total;

	}
	if (rowLength <= 0) {
		summary = parseFloat(0);
	}
	if (summary < 0) {
		summary = parseFloat(0);
	}

	$("#summaryTax").val(summary.toFixed(2)*-1);
	vatAmount();
}

function removeTax() {
	var table = document.getElementById("showDeductibleTable");
	var rowLength = table.rows.length;
	var summary = parseFloat(0);
	for (var i = 1; i < rowLength; i++) {
		var oCells = table.rows.item(i).cells;
		var total = parseFloat(oCells[2].innerHTML);
		summaryTa = total;
		summary += total;
	}
	if (rowLength <= 0) {
		summary = parseFloat(0);
	}
	if (summary < 0) {
		summary = parseFloat(0);
	}
	$("#summaryTax").val(summary.toFixed(2));

}

function replaseIndex(str) {
	var suminputmon = 0;

	rows = str.getElementsByTagName('tr');
	if (rows.length > 1) {
		var i, j, cells, customerId;
		for (i = 0, j = rows.length; i < j; ++i) {
			cells = rows[i].getElementsByTagName('td');
			if (!cells.length) {
				continue;
			}
			cells[0].innerHTML = i;
			if (cells[2].innerHTML) {

				suminputmon = suminputmon
						+ FormatMoneyShowToNumber(cells[2].innerHTML);
			}
			cells[3].innerHTML = "<a onclick='myDeleteSumCreditTranPrice(" + i
					+ ")'><span class='glyphicon glyphicon-trash'></span></a>";
		}
		$("#balanceSum").val(suminputmon.toFixed(2));
//		$("#balanceSummarys").val(suminputmon.toFixed(2));
		$("#balanceSumShow").val(
				suminputmon.toFixed(2).toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
	}else{
		
		var defualt =  FormatMoneyShowToNumber($("#balanceOfTaxsShow").val());
		$("#balanceSum").val(suminputmon.toFixed(2));
		$("#balanceSumShow").val(
				suminputmon.toFixed(2).toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
		$("#balanceSummarys").val(defualt.toFixed(2));
		$("#balanceSummaryShow").val(
				defualt.toFixed(2).toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));

	}
	
	totalSum();
};
function replaseIndexV1(str) {

	rows = str.getElementsByTagName('tr')
	if (rows.length > 1) {
		var i, j, cells, customerId;
		for (i = 0, j = rows.length; i < j; ++i) {
			cells = rows[i].getElementsByTagName('td');
			if (!cells.length) {
				continue;
			}
			cells[0].innerHTML = i;
			cells[5].innerHTML = "<a onclick='myDeleteFunction(" + i
					+ ")'><span class='glyphicon glyphicon-trash'></span></a>";
		}
	}
	totalSum();
}
function replaseIndexV2(str) {

	rows = str.getElementsByTagName('tr')
	if (rows.length > 1) {
		var i, j, cells, customerId;
		for (i = 0, j = rows.length; i < j; ++i) {
			cells = rows[i].getElementsByTagName('td');
			if (!cells.length) {
				continue;
			}
			cells[0].innerHTML = i;
			cells[4].innerHTML = "<a onclick='myDeleteFunction(" + i
					+ ")'><span class='glyphicon glyphicon-trash'></span></a>";
		}
	}
	totalSum();
}
function replaseIndexV3(str) {
	var sumdect = 0;
	rows = str.getElementsByTagName('tr')
	if (rows.length > 1) {
		var i, j, cells, customerId;
		for (i = 0, j = rows.length; i < j; ++i) {
			cells = rows[i].getElementsByTagName('td');
			if (!cells.length) {
				continue;
			}
			cells[0].innerHTML = i;
			cells[3].innerHTML = "<a onclick='myDeleteDed(" + i
					+ ")'><span class='glyphicon glyphicon-trash'></span></a>";
			sumdect = sumdect + FormatMoneyShowToNumber(cells[2].innerHTML);
		}
	}
	var sumtableBillingList = document.getElementById("sumtableBillingList");
	replaseIndexV4(sumtableBillingList);
	$("#summaryTax").val(
			sumdect.toFixed(2).toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g,
					"$1,"));
	var balanceOfTaxs = $("#balanceOfTaxs").val();
	if (balanceOfTaxs > 0) {
		var sumtotal = balanceOfTaxs - (sumdect*-1);
		$("#balanceSummarys").val(sumtotal);
		$("#balanceSummaryShow").val(
				sumtotal.toFixed(2).toString().replace(
						/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
		$("#creditPrice").val(
				sumtotal.toFixed(2).toString().replace(
						/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
		$("#moneyCheck").val(
				sumtotal.toFixed(2).toString().replace(
						/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
//		 var sumtableBillingList =
//		 document.getElementById("sumtableBillingList");
//		 replaseIndexV4(sumtableBillingList);
	} else {
		$("#balanceSummarys").val(0.00);
		var sumtableBillingList = document
				.getElementById("sumtableBillingList");
		replaseIndexV4(sumtableBillingList);
	}
	 totalSum();

}
function replaseIndexV4(str) {
	var sumInputmon = 0;
	var beforeSaleShow = 0;
	var beforeSaleShow1 = 0;
	var vat = 0;
	var spacial = 0;
	var sale =0;
	var summaryTax =0;
	var sumWt;
	rows = str.getElementsByTagName('tr')
	if (rows.length > 1) {
		var i, j, cells, customerId;
		for (i = 0, j = rows.length; i < j; ++i) {
			cells = rows[i].getElementsByTagName('td');
			if (!cells.length) {
				continue;
			}
			sumInputmon = sumInputmon
					+ FormatMoneyShowToNumber(cells[9].innerHTML);
			beforeSaleShow = beforeSaleShow
					+ (FormatMoneyShowToNumber(cells[4].innerHTML)*FormatMoneyShowToNumber(cells[3].innerHTML)) ;
			if(cells[7].innerHTML != "-"){
				vat = vat + FormatMoneyShowToNumber(cells[7].innerHTML);
			}
		
			cells[0].innerHTML = i;
			cells[10].innerHTML = "<a onclick='deleteTableSale(" + i
					+ ")'><span class='glyphicon glyphicon-trash'></span></a>";
			spacial = spacial + FormatMoneyShowToNumber(cells[6].innerHTML) ;
			sale = sale + FormatMoneyShowToNumber(cells[5].innerHTML);
			summaryTax= (summaryTax + FormatMoneyShowToNumber(cells[8].innerHTML))*FormatMoneyShowToNumber(cells[3].innerHTML);
			beforeSaleShow1 = beforeSaleShow1+((FormatMoneyShowToNumber(cells[4].innerHTML)*FormatMoneyShowToNumber(cells[3].innerHTML))-FormatMoneyShowToNumber(cells[5].innerHTML)-FormatMoneyShowToNumber(cells[6].innerHTML)) ;
		}
	}
//	$("#moneyDed").val(summaryTax);
	$("#moneyTran").val(
			sumInputmon.toFixed(2).toString().replace(
					/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
	$("#creditPrice").val(
			sumInputmon.toFixed(2).toString().replace(
					/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
	$("#moneyCheck").val(
			sumInputmon.toFixed(2).toString().replace(
					/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
	$("#balanceSummarys").val(sumInputmon.toFixed(2));
	$("#balanceSummary").val(sumInputmon.toFixed(2));

	$("#balanceSummaryShow").val(
			sumInputmon.toFixed(2).toString().replace(
					/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
	$("#beforeSaleShow").val(
			beforeSaleShow.toFixed(2).toString().replace(
					/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
	$("#balanceBeforeTaxsShow").val(
			beforeSaleShow1.toFixed(2).toString().replace(
					/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
	$("#vats").val(vat);
	$("#vatsShow").val(
			vat.toFixed(2).toString()
					.replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));

	$("#balanceOfTaxs").val(sumInputmon);
	$("#balanceOfTaxsShow").val(
			sumInputmon.toFixed(2).toString().replace(
					/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
	$("#sale").val(
			sale.toFixed(2).toString().replace(
					/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
	$("#salespacial").val(
			spacial.toFixed(2).toString().replace(
					/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
	var sumtx = parseInt(summaryTax) - parseInt(sumded);
	
if(current == "addbill"){
	$("#moneyDed").val(summaryTax.toFixed(2).toString().replaceAll(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
	//$("#moneyDed").val(summaryTax.toFixed(2).toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
}else if(current == "addwt"){
	$("#moneyDed").val(sumtx.toFixed(2).toString().replaceAll(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
}
else if(current == "delbill"){
	$("#moneyDed").val(sumtx.toFixed(2).toString().replaceAll(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
}
else if(current == "delwt"){
	$("#moneyDed").val(sumtx.toFixed(2).toString().replaceAll(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
}

	// totalSum();
	return sumInputmon;
}
function totalSum() {
	var sumtotal = FormatMoneyShowToNumber($("#balanceOfTaxs").val());
	var income = FormatMoneyShowToNumber($("#balanceSumShow").val());
	var summaryTax = FormatMoneyShowToNumber($("#summaryTax").val());
	var total =  (sumtotal+summaryTax );
	if(income > 0){
		var result = total - (income)
		if (result > 0) {
//			$("#balanceSummaryShow").val(result.toFixed(2).toString()
//					.replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
//			$("#balanceSummarys").val(result.toFixed(2).toString()
//			.replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
			var  a = 0;
			$("#moneyTran").val(result.toFixed(2).toString()
					.replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
			$("#change").val(a.toFixed(2).toString()
					.replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
		} else {
			var notnevite = result * (-1);
			var  a = 0;
//			$("#balanceSummaryShow").val(total.toFixed(2).toString()
//					.replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
//			$("#balanceSummarys").val(total.toFixed(2).toString()
//			.replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
			$("#change").val(	notnevite.toFixed(2).toString()
					.replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
		}
	}else{
		$("#balanceSummaryShow").val(total.toFixed(2).toString()
				.replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
		$("#balanceSummarys").val(total.toFixed(2).toString()
				.replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
		$("#moneyTran").val(total.toFixed(2).toString()
				.replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
		$("#creditPrice").val(total.toFixed(2).toString()
				.replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
		$("#moneyCheck").val(total.toFixed(2).toString()
				.replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
		
		
	}
	
	
}

function calVat() {
	var userGroup = $("#userGroup").val();
	var vatRate = $('#vatrate').val();
	if(vatRate == 'notVat'){
		vatRate = '0';
	}
	var inputServiceMoreData = $("#inputServiceMoreData").val();
	var inputServiceAmount = $("#inputServiceAmount").val();
	var inputServiceDiscount = $("#inputServiceDiscount").val();
	var inputSpecialDiscount = $("#inputSpecialDiscount").val();
	var radiovat = document.getElementsByName("radiovat");
	var radioResult = "";
	for (var x = 0; x < radiovat.length; x++) {
		if (radiovat[x].checked) {
			radioResult = radiovat[x].value;
		}
	}
	
	if ($("#userGroup").val() == "") {
		$("#suserGroup").show();
		return $("#userGroup").focus();
	}
	var serviceMoreData = parseFloat(inputServiceMoreData); // จำนวนรายการ
	// ส่วนลด
	// สว่นลดพิเศษ
	var specialDiscount = parseFloat(inputSpecialDiscount.replace(",", ""));
	
	var serviceAmount = FormatMoneyShowToNumber(inputServiceAmount); // จำนวนต่อหน่วย
	if(serviceAmount == 0 || serviceAmount == ""){
		$("#sinputServiceAmount").show();
		if(serviceMoreData == 0 || serviceMoreData == ""){
			$("#sinputServiceMoreData").show();
			return false;
		}
		return false;
	}
	
	var amountDiscount = disDiscount(serviceAmount);
	var amountBeforVat = disVat(amountDiscount,radioResult);
	calWT(amountBeforVat);
	
	
	
}

function calWT(amount){
	var userGroup = $("#userGroup").val();
	var wt;
	
	if(chkOther) {
		if(userGroup=='13') userGroup='1'
		else if(userGroup=='14') userGroup='2'
		else if(userGroup=='15') userGroup='3'
	}
	
	$.ajax({
	    type: 'GET',
	    url: ctx +"/getWT/"+userGroup
	}).then(function (data) {
			wt = amount*parseInt(data.value)/100;
			$("#moneyDed1").val(parseFloat(wt).toFixed(2).toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g,"$1,"));
	});
	
	
}
function disVat(serviceAmount,amountType){
	var vatRate = $('#vatrate').val();
	if(vatRate == 'Non-VAT'){
		vatRate = '0';
	}
	vatRate = parseFloat(vatRate.replace(",", ""));
	if(amountType == "beforvat"){
		amountBeforVat = serviceAmount;
	}else{
		amountBeforVat = serviceAmount - (serviceAmount * (vatRate/(100+vatRate)));
	}
	return amountBeforVat;
}

function disDiscount(amountBeforVat){
	var inputServiceDiscount = $("#inputServiceDiscount").val();
	var inputSpecialDiscount = $("#inputSpecialDiscount").val();
	var serviceMoreData = $("#inputServiceMoreData").val();
	var inputServiceMoreData = parseFloat(serviceMoreData.replace(",", ""));
	var specialDiscount = parseFloat(inputSpecialDiscount.replace(",", ""));
	var serviceDiscount = parseFloat(inputServiceDiscount.replace(",", ""));
	var amount = (amountBeforVat*serviceMoreData) - specialDiscount - serviceDiscount;
	return amount;
	
}

function autoSelect(){
	var event = $("#userGroup").val();
	let keyCheck = '';
	
	if(userGroupGBs) {
		let resObjs =  userGroupGBs.filter(function(Obj) {
			return Obj.property1 == event;
		});
		
		keyCheck = resObjs[0].keyCode
		
		if(chkOther) {
			let objs =  userGroupGBOthers.filter(function(Obj) {
				return Obj.keyCode == event;
			});
			if('3TRE_AVERAGE_CHARGE'==objs[0].property5) { keyCheck = '1' }
			else if('69BIS_AVERAGE_CHARGE'==objs[0].property5) { keyCheck = '2' }
		}
	}
	
	if(keyCheck == "1"){
		// 69 ทวิ
		radiobtn = document.getElementById("radioDedCC");
		radiobtn.checked = true;
	}else if(keyCheck == "2"){
		radiobtns = document.getElementById("radioDedCD");
		radiobtns.checked = true;
	}else if(keyCheck == "3"){
		radiobtns = document.getElementById("radioDedCC");
		radiobtns.checked = true;
	}else{
		radiobtns = document.getElementById("radioDedCT");
		radiobtns.checked = true;
	}
	
	if(userGroupGBs) {
		let resObjs =  userGroupGBs.filter(function(Obj) {
			return Obj.property1 == event;
		});
		
		if(resObjs)keyCode = resObjs[0].keyCode
	}
}
	function autoSelectVat(){
	
	var event = $("#vatrate").val();
	if(event == "Non-VAT"){
		 document.getElementById("aftervat").disabled = true;
		 document.getElementById("taxOnly").disabled = true;
		 document.getElementById("taxOnly").checked = false;
	}else{
		document.getElementById("aftervat").disabled = false;
		document.getElementById("taxOnly").disabled = false;
	}
}
	
	function set4gl() {
		
		$.ajax({
		    type: 'GET',
		    url: ctx +"/other/setGL",
		    dataType: "json",
	        async: true,
	        contentType: "application/json; charset=utf-8",
		}).then(function (res) {
			mapGLObj = res.mapGLBean
			masterSegmentsGBs = res.masterSegments
			masterProductsGBs = res.masterProducts
//			findSegmentNProduct(res.masterSegments, res.masterProducts)
		});
		
	}
	
	function checkMapGL(code1, code2) {
		let resObjs =  mapGLObj.filter(function(Obj) {
			return Obj.serviceCode == code1 ;
//			&& Obj.revenueTypeCode == code2
		});

		if(0 < resObjs.length) {
			if('2' == resObjs[0].glCode.substring(0, 1) && 'Y' == resObjs[0].erpInterfaceFlag) {
				showProductSegment()
				$("#shPS").show()
				isShPS = true;
				return true
//			}else if('5' == resObjs[0].glCode.substring(0, 1) || '2' == resObjs[0].glCode.substring(0, 1) && 'N' == resObjs[0].erpInterfaceFlag) {
			}else if('N' == resObjs[0].erpInterfaceFlag) {
//				swal(WORD_5)
				$("#shPS").hide()
				$("#error").show()
				return true
			}else {
				$("#error").hide()
				$("#shPS").hide()
				return true
			}
		}
		
		return false
	}
	
	function showProductSegment() {
		findSegmentNProduct(masterSegmentsGBs, masterProductsGBs)
		
		if(productCode)$("#productDD").val(productCode)
		if(segmentCode)$("#segmentDD").val(segmentCode)
		
		$("#map-gl-other").modal('show');
		$("#error").hide()
	}
	

	
	
	function modalConfirmReason(callback){
		if(callback){
			if(validateOtherDropdown()) {
				productCode = $("#productDD").val()
				segmentCode = $("#segmentDD").val()
				productName = $("#productDD option:selected").text()
				segmentName = $("#segmentDD option:selected").text()
				$("#map-gl-other").modal('hide');
			}
		}else{
			$("#map-gl-other").modal('hide');
		}
	}
	
	function findSegmentNProduct(masterSegments, masterProducts) {
		
		$('#segmentDD').empty();
		$('#productDD').empty();
		$('#segmentDD').append('<option value="">' + PLS_SELECT + '</option>');
		$('#productDD').append('<option value="">' + PLS_SELECT + '</option>');
		
		for(var i=0; i<masterSegments.length; i++) {
			$('#segmentDD').append('<option value="'+(masterSegments[i].value)+'">' + (masterSegments[i].text) + '</option>');
		}
		
		for(var i=0; i<masterProducts.length; i++) {
			$('#productDD').append('<option value="'+(masterProducts[i].value)+'">' + (masterProducts[i].text) + '</option>');
		}
		
	}
	
	function findUserGroup() {
		$.ajax({
		    type: 'GET',
		    url: ctx +"/other/find/usergroup",
		    dataType: "json",
	        async: true,
	        contentType: "application/json; charset=utf-8",
		}).then(function (res) {
			userGroupGBs = res.usergroup
			userGroupGBOthers = res.usergroupOther
			setUserGroup(userGroupGBs)
		});
	}
	
	function setUserGroup(userGroups) {
		
		if(userGroups) {
			$('#userGroup').empty();
			$('#userGroup').append('<option value="">' + PLS_SELECT + '</option>');
			if(chkOther) {
				for(var i=0; i<userGroups.length; i++) {
					if(userGroups[i].keyCode == '1' || userGroups[i].keyCode == '2' || userGroups[i].keyCode == '3') {
						$('#userGroup').append('<option value="'+(userGroups[i].property1)+'">' + (userGroups[i].property3) + '</option>');
//						$('#userGroup').append('<option value="'+(userGroups[i].keyCode)+'">' + (userGroups[i].property3) + '</option>');
					}
				}
			}else {
				for(var i=0; i<userGroups.length; i++) {
					$('#userGroup').append('<option value="'+(userGroups[i].property1)+'">' + (userGroups[i].property3) + '</option>');
//					$('#userGroup').append('<option value="'+(userGroups[i].keyCode)+'">' + (userGroups[i].property3) + '</option>');
				}
			}
		}
		
	}
	
	function setUserGroupOther() {
		
		if(userGroupGBOthers) {
			$('#userGroup').empty();
			$('#userGroup').append('<option value="">' + PLS_SELECT + '</option>');
			if(chkOther) {
				for(var i=0; i<userGroupGBOthers.length; i++) {
					$('#userGroup').append('<option value="'+(userGroupGBOthers[i].keyCode)+'">' + (userGroupGBOthers[i].value) + '</option>');
				}
			}else {
				for(var i=0; i<userGroups.length; i++) {
					$('#userGroup').append('<option value="'+(userGroups[i].keyCode)+'">' + (userGroups[i].property3) + '</option>');
				}
			}
		}
		
	}
	
	function validateOtherDropdown() {
		if(''!=$("#productDD").val() && ''!=$("#segmentDD").val()) {
			$("#serror").hide();
			$("#perror").hide();
			return true
		}else{
			if(''==$("#segmentDD").val() && ''==$("#productDD").val()) {
				$("#serror").show();
				$("#perror").show();
				return false
			}else if(''==$("#segmentDD").val()) {
				$("#perror").hide();
				$("#serror").show();
				return false
			}else if(''==$("#productDD").val()) {
				$("#serror").hide();
				$("#perror").show();
				return false
			}
		}
		
	}
	
	function findByTaxId() {
		var taxId = $("#taxId").val();
		
		$.ajax({
		    type: 'GET',
		    url: ctx +"/other/findtax"+taxId,
		    dataType: "json",
	        async: true,
	        contentType: "application/json; charset=utf-8",
		}).then(function (res) {
			mapGLObj = res.mapGLBean
		});
	}
	
	function findOtherCustomer() {
		$("#other-customer").modal('show');
	}
	
	let responseGB
	function modalConfirmOtherCust(status) {
		if(status) {
			custOtherList.clear().draw();
			dataSend = {
					"taxId" : $("#taxOtherId").val(),
					"name" : $("#nameOtherId").val(),
			}
			
			$.ajax({
				type : "POST",
				url : ctx +"/other/findOtherCustomer",
				data : JSON.stringify(dataSend),
				dataType : "json",
				async : false,
				contentType : "application/json; charset=utf-8",
				success : function(res) {
					if (res) {
						responseGB = res
						for (var i = 0; i < res.length; i++) {
							createRowCustOtherList(res[i], i);
		                }
					}
				}
			})
			
		}else {
			$("#other-customer").modal('hide');
			custOtherList.clear().draw();
			$("#taxOtherId").val('')
			$("#nameOtherId").val('')
		}
	}
	
	function createRowCustOtherList(data, seq) {
	    colSeq = (seq + 1);
	    colCur2 = data.name;
	    colCur3 = data.taxId;
	    colCur4 = '<button type="button" class="btn btn-success" onclick="pickData('+data.taxId+')"><span name="icon" id="icon" class="fa fa-plus">เลือก</buttona>';
	
	    var t = $('#custOtherList').DataTable();
	    var rowNode = t.row.add([ colSeq, colCur2, colCur3, colCur4 ]).draw(true).node();
	    $(rowNode).find('td').eq(0).addClass('center');
	    $(rowNode).find('td').eq(1).addClass('center');
	    $(rowNode).find('td').eq(2).addClass('center');
	    $(rowNode).find('td').eq(3).addClass('center');
	}
	
	function pickData(data) {
		let resObjs =  responseGB.filter(function(Obj) {
			return Obj.taxId == data;
		});
		if(resObjs) {
			document.getElementById("chkOther").checked = true;
			fnChkOther()
			
			let resObj = resObjs[0]
			document.getElementById("custNo").disabled = true
			document.getElementById("taxId").disabled = true
			
			$('#custName').val(resObj.name)
			$('#taxId').val(resObj.taxId)
			$('#userGroup').val(resObj.serviceCode)
			autoSelect()
			$('#custBrach').val(resObj.branch)
			$('#custAddress').val(resObj.address)
			
			$("#other-customer").modal('hide');
			custOtherList.clear().draw();
			$("#taxOtherId").val('')
			$("#nameOtherId").val('')
			
		}
	}
	
	function clearOtherCustomer() {
		$('#custName').val('')
		$('#taxId').val('')
		$('#userGroup').val('')
		$('#custBrach').val('')
		$('#custAddress').val('')
		
		document.getElementById("custNo").disabled = false
		document.getElementById("taxId").disabled = false
	}
	
	function fnTaxOnly() {
		chkOtherTax1 = !chkOtherTax1
	}
	
	function fnChkOther() {
		chkOther = document.getElementById("chkOther").checked
		
		if(chkOther) {
			document.getElementById("custNo").disabled = true
			document.getElementById("findOther").disabled = false;
			setUserGroupOther()
		}else {
			document.getElementById("custNo").disabled = false
			document.getElementById("findOther").disabled = true;
			setUserGroup(userGroupGBs)
		}
	}
	
	function segmentSelect() {
		let smCode = $("#segmentDD").val()
		let pdCode = $("#productDD").val()
		
		if('' === smCode) {
			findSegmentNProduct(masterSegmentsGBs, masterProductsGBs)
		}else {
			let resObjs =  masterProductsGBs.filter(function(Obj) {
				return Obj.value.substring(0, 5) == smCode;
			});
			
			if(resObjs.length > 0) {
				$('#productDD').empty();
				$('#productDD').append('<option value="">' + PLS_SELECT + '</option>');
				
				for(var i=0; i<resObjs.length; i++) {
					$('#productDD').append('<option value="'+(resObjs[i].value)+'">' + (resObjs[i].text) + '</option>');
				}
				
				$("#productDD").val(pdCode)
			}
		}
	}
	
	function productSelect() {
		let smCode = $("#segmentDD").val()
		let pdCode = $("#productDD").val()
		
		if('' === pdCode) {
			findSegmentNProduct(masterSegmentsGBs, masterProductsGBs)
		}else {
			let resObjs =  masterSegmentsGBs.filter(function(Obj) {
				return Obj.value == pdCode.substring(0, 5);
			});
			
			if(resObjs.length > 0) {
				$('#segmentDD').empty();
				$('#segmentDD').append('<option value="">' + PLS_SELECT + '</option>');
				
				for(var i=0; i<resObjs.length; i++) {
					smCode = resObjs[i].value
					$('#segmentDD').append('<option value="'+(resObjs[i].value)+'">' + (resObjs[i].text) + '</option>');
				}
				$("#segmentDD").val(smCode)
			}
		}
	}
	
	function findServiceType() {
		$.ajax({
		    type: 'GET',
		    url: ctx +"/other/find/servicetype",
		    dataType: "json",
	        async: true,
	        contentType: "application/json; charset=utf-8",
		}).then(function (res) {
			serviceTypes = res
			setServiceType(res)
		});
	}
	
	function setServiceType(servicetypeList) {
		
		if(servicetypeList) {
			$('#inputServiceType').empty();
			$('#inputServiceType').append('<option value="">' + PLS_SELECT + '</option>');
				for(var i=0; i<servicetypeList.length; i++) {
					if(servicetypeList[i].revenueTypeName.toUpperCase() != 'NULL'){
						$('#inputServiceType').append('<option value="'+(servicetypeList[i].revenueTypeCode)+'">' + (servicetypeList[i].revenueTypeCode)+ '-' +(servicetypeList[i].revenueTypeName) + '</option>');
					}
				}
		}
		
	}
	
	function findServiceName() {
		$.ajax({
		    type: 'GET',
		    url: ctx +"/other/find/servicename",
		    dataType: "json",
	        async: true,
	        contentType: "application/json; charset=utf-8",
		}).then(function (res) {
			serviceNames = res
			setServiceName(res)
		});
	}
	
	function setServiceName(servicenameList) {
		
		if(servicenameList) {
			$('#inputServiceName').empty();
			$('#inputServiceName').append('<option value="">' + PLS_SELECT + '</option>');
				for(var i=0; i<servicenameList.length; i++) {
					$('#inputServiceName').append('<option value="'+(servicenameList[i].serviceCode)+'">' + (servicenameList[i].glCode)+ '-' +(servicenameList[i].serviceName) + '</option>');
								
				}
		}
		
	}
	
function findServiceTypeServiceName(servicetypeList, servicenameList) {
		
		$('#inputServiceType').empty();
		$('#inputServiceName').empty();
		$('#inputServiceType').append('<option value="">' + PLS_SELECT + '</option>');
		$('#inputServiceName').append('<option value="">' + PLS_SELECT + '</option>');
		
		for(var i=0; i<servicetypeList.length; i++) {
			if(servicetypeList[i].revenueTypeName.toUpperCase() != 'NULL'){
				$('#inputServiceType').append('<option value="'+(servicetypeList[i].revenueTypeCode)+'">' + (servicetypeList[i].revenueTypeCode)+ '-' +(servicetypeList[i].revenueTypeName) + '</option>');
			}
		}
		
		for(var i=0; i<servicenameList.length; i++) {
			$('#inputServiceName').append('<option value="'+(servicenameList[i].serviceCode)+'">' + (servicenameList[i].glCode)+ '-' +(servicenameList[i].serviceName) + '</option>');
		}
		
	}
	
	
	function mapServiceType() {
		let svTypes = $("#inputServiceType").val()
		let svNames = $("#inputServiceName").val()
		if('' === svTypes) {
			findServiceTypeServiceName(serviceTypes,serviceNames)
			$("#inputServiceName").val('')
		}else {
			let resObjs =  serviceNames.filter(function(Obj) {
				return Obj.revenueTypeCode == svTypes;
			});
			
			if(resObjs.length > 0) {
				
					$('#inputServiceName').empty();
					$('#inputServiceName').append('<option value="">' + PLS_SELECT + '</option>');
					
					for(var i=0; i<resObjs.length; i++) {
						
							$('#inputServiceName').append('<option value="'+(resObjs[i].serviceCode)+'"  >' + (resObjs[i].glCode)+ '-' +(resObjs[i].serviceName) + '</option>');
						
					}
					if(resObjs.length == 1){
						svNames = resObjs[0].serviceCode;
					}
					
				$("#inputServiceName ").val(svNames)
			}
		}
	}
	
	function mapServiceName() {
		let svType = ""
		let svName = $("#inputServiceName").val()
//		let svNameText = $("#inputServiceName option:selected").text()
		
		isShPS = false;
		checkMapGL(svName, svType)
		
		if('' === svName) {
			findServiceTypeServiceName(serviceTypes,serviceNames)
			$("#inputServiceType").val('')
		}else {
			let sCode = serviceNames.filter(function(Obj) {
				return Obj.serviceCode == svName;
			});
			
//			let segMentCode = sCode[0].segMentCode;  why
			let revenueTypeCode = sCode[0].revenueTypeCode;
			
			let resObjs =  serviceTypes.filter(function(Obj) {
//				return Obj.productCode.substring(0, 5) == segMentCode;  why
				return Obj.revenueTypeCode == revenueTypeCode;
//				return Obj.glCode == svName;
			});
			
			$('#inputServiceType').empty();
			$('#inputServiceType').append('<option value="">' + PLS_SELECT + '</option>');
			
			if(resObjs.length > 0) {
				var svt = '';
				
				for(var i=0; i<resObjs.length; i++) {
					if(serviceTypes[i].revenueTypeName.toUpperCase() != 'NULL'){
						svType = resObjs[i].revenueTypeCode
						$('#inputServiceType').append('<option value="'+(resObjs[i].revenueTypeCode)+'">' + (resObjs[i].revenueTypeCode)+ '-' +(resObjs[i].revenueTypeName) + '</option>');
					}
				}
			}
			$("#inputServiceType").val(svType)
		}
	}
	
//	function checkTaxIdNumber() {
//		var taxId = $('#taxId').val()
//		
//		var res = isNaN(taxId);
//		if(res) {
//			$('#taxId').val('')
//			return
//		}
//	}
	
	
