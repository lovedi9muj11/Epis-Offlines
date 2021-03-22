$(document).ready(function() {
	
	$('.groupType').select2({
//	    width: '30%'
	});
	
	$.ajax({
	    type: 'GET',
	    url: ctx +"/masterData/masterDataBatch"
	}).then(function (data) {
		for(var i=0; i<data.length; i++) {
			var element = data[i];
			$('#groupType').append('<option value="' + element.value+ '">' + element.name + '</option>');
		}
	});
	
	setValue();
	
	$('#groupType').change(function () {
		var groupTypeVal = $('#groupType').val();
		$('#month').empty();
		$('#date').empty();
		$('#hour').empty();
		$('#minute').empty();
		radioHour = document.getElementById("radioHour");
    	radioHour.checked = true;
    	radioMin = document.getElementById("radioMin");
    	radioMin.checked = true;
		if(''==groupTypeVal) {
			setValue();
		}else{
			findGroupTypeByKeyCode(groupTypeVal);
			
		}
		
	});
	
	$('#month').change(function () {
		var pass = true;
		var monthVal = $('#month').val();
		var yearNow = $('#yearNow').val();
		
		var firstDay = new Date(yearNow, monthVal, 1);
		var lastDay = new Date(yearNow, monthVal, 0);
		
		if('*' == monthVal)lastDay = 28; //ช่วยคิดหน่อย
		
		if('*'!=monthVal) {
			$('#date').empty();
			$('#hour').empty();
			$('#minute').empty();
			
//			if(2==monthVal) {
//				pass = isDate("1/02/"+yearNow);
//			}
			
			setDate(lastDay.getDate());
			setHour();
			setMinute();
			
			if('L'==monthVal)document.getElementById("date").disabled = true;
			else document.getElementById("date").disabled = false;
		}else {
			document.getElementById("date").disabled = false;
			
			$('#date').empty();
			setDate(lastDay);
		}
		
	});
	
	$('#saveBtn').click(function() {
		if(''==$("#groupType").val())return swal(PLS_SELECT+' '+PLS_SELECT_ALL_TYPE)
		var radioHour = document.getElementsByName("radioHour");
		var radioMin = document.getElementsByName("radioMin");
		var radioResult = "";
		for (var x = 0; x < radioHour.length; x++) {
			if (radioHour[x].checked) {
				radioResult = radioHour[x].value;
			}
		}
		var radioMinResult = "";
		for (var x = 0; x < radioMin.length; x++) {
			if (radioMin[x].checked) {
				radioMinResult = radioMin[x].value;
			}
		}
		var masterDataBean = {
			"month":$("#month").val() ,
			"day":$("#date").val() ,
			"hour":$("#hour").val() ,
			"flagH":radioResult ,
			"flagM":radioMinResult ,
			"minute":$("#minute").val() ,
			"orderBatch":$("#groupType").val()
		}
		
		$.ajax({
	        type: "POST",
	        url: ctx +"/insertBatch",
	        data: JSON.stringify(masterDataBean),
	        dataType: "json",
	        async: false,
	        contentType: "application/json; charset=utf-8",
	        success: function (res) {
	        	swal('Insert : '+res.responseText)
	        },
	        error : function (res) {
	        	swal('Insert : '+res.responseText)
	        }
		});
		
	})
	
});

var monthLists = "JAN,FEB,MAR,APR,MAY,JUN,JUL,AUG,SEP,OCT,NOV,DEC";
function setMonth(months) {
	let monthLists2 = monthLists.split(',')
	console.log(monthLists2.length)
	$('#month').append('<option value="*">' + PLS_SELECT_ALL_MONTH + '</option>');
	$('#month').append('<option value="L">' + PLS_SELECT_ALL_MONTH_LAST_DAY + '</option>');
	for(var i=0; i<months.length; i++) {
		$('#month').append('<option value="'+(months[i].key)+'">' + (months[i].value) + '</option>');
	}
	if('' != $('#monthdd').val()) {
		$('#month').val($('#monthdd').val());
	}
}

function setDate(days) {
	$('#date').append('<option value="*">' + PLS_SELECT_ALL_DAY + '</option>');
	for(var i=0; i<days; i++) {
		$('#date').append('<option value="'+(i+1)+'">' + (i+1) + '</option>');
	}
}

function setHour() {
	if('1'==hourValue) {
		for(var i=0; i<25; i++) {
			$('#hour').append('<option value="'+(i)+'">' + (i) + '</option>');
		}
	}else {
		$('#hour').append('<option value="*">' + PLS_SELECT_ALL_HOUR + '</option>');
		for(var i=2; i<25; i++) {
			$('#hour').append('<option value="'+(i)+'">' + (i) + '</option>');
		}
	}
}

function setMinute() {
//	$('#minute').append('<option value="*">' + PLS_SELECT_ALL_MINUTE + '</option>');
	if('1'==minValue) {
		for(var i=0; i<60; i++) {
			$('#minute').append('<option value="'+(i)+'">' + (i) + '</option>');
		}
	}else {
		$('#minute').append('<option value="*">' + PLS_SELECT_ALL_MINUTE + '</option>');
		for(var i=2; i<60; i++) {
			$('#minute').append('<option value="'+(i)+'">' + (i) + '</option>');
		}
	}
	
}

function setValue() {
	$('#month').append('<option value="'+'">' + PLS_SELECT + '</option>');
	
	$('#date').append('<option value="'+'">' + PLS_SELECT + '</option>');
	
	$('#hour').append('<option value="'+'">' + PLS_SELECT + '</option>');
	
	$('#minute').append('<option value="'+'">' + PLS_SELECT + '</option>');
}

var hourValue;
var minValue;
function findGroupTypeByKeyCode(groupKey) {
	var masterDataBean = { "group":groupKey};
	var lastDay;
	$.ajax({
	        type: "POST",
	        url: ctx +"/findGroupTypeByKeyCode",
	        data: JSON.stringify(masterDataBean),
	        dataType: "json",
	        async: false,
	        contentType: "application/json; charset=utf-8",
	        success: function (res) {
	        	$('#yearNow').val(res.yearNow);
	        	$('#monthdd').val(res.month);
	        	$('#daydd').val(res.day);
	        	$('#hourdd').val(res.hour);
	        	
	        	hourValue = res.flagH
	        	minValue = res.flagM
	        	if(res.flagH=='1') {
	        		radioHour = document.getElementById("radioHour");
		        	radioHour.checked = true;
	        	}else {
	        		radioHour = document.getElementById("radioHour2");
		        	radioHour.checked = true;
	        	}
	        	
	        	if(res.flagM=='1') {
	        		radioMin = document.getElementById("radioMin");
	        		radioMin.checked = true;
	        	}else {
	        		radioMin = document.getElementById("radioMin2");
	        		radioMin.checked = true;
	        	}
	        	
	        	var str = res.minute;
	        	var n = str.search("/");
	        	if((-1)!=n) {
	        		var ans = str.split('/')
	        		$('#mindd').val(ans[1]);
	        	}else {
	        		$('#mindd').val(res.minute);
	        	}
	        	
	        	setMonth(res.dropDownMonths);
	        	
	        	if('*' == res.month){
	        		lastDay = 28; //ช่วยคิดหน่อย
	        	}else{
	        		lastDay = res.monthNow;
	        	}
	        	
	        	if('L'==res.month)document.getElementById("date").disabled = true;
	        	else document.getElementById("date").disabled = false;
				
	        	setDate(lastDay);
				$('#date').val($('#daydd').val());
				setHour();
				$('#hour').val($('#hourdd').val());
				setMinute();
				$('#minute').val($('#mindd').val());
	        }
  });
	
//	$.ajax({
//	    type: 'POST',
//	    url: ctx +"/findGroupTypeByKeyCode",
//	    data: JSON.stringify(masterDataBean),
//        dataType: "json"
//	}).then(function (data) {
//		for(var i=0; i<data.length; i++) {
//			var element = data[i];
//			$('#groupType').append('<option value="' + element.value+ '">' + element.name + '</option>');
//		}
//	});
}

//parse Format
function toFormat(txtDate) {
	var currVal = txtDate; 
	if(currVal == '') 
	return "";

	//Declare Regex 
	var rxDatePattern = /^(\d{1,2})(\/|-)(\d{1,2})(\/|-)(\d{4})$/; 
	var dtArray = currVal.match(rxDatePattern); // is format OK? 

	if (dtArray == null) 
		return "";

	//Checks for mm/dd/yyyy format. 
	dtMonth = dtArray[3]; 
	dtDay= dtArray[1]; 
	dtYear = dtArray[5]; 

	if (dtMonth < 1 || dtMonth > 12) 
		return ""; 
	else if (dtDay < 1 || dtDay> 31) 
		return ""; 
	else if ((dtMonth==4 || dtMonth==6 || dtMonth==9 || dtMonth==11) && dtDay ==31) 
		return ""; 
	else if (dtMonth == 2) { 
		var isleap = (dtYear % 4 == 0 && (dtYear % 100 != 0 || dtYear % 400 == 0)); 
		if (dtDay> 29 || (dtDay ==29 && !isleap)) 
			return "";
	} 
	
	return dtYear+dtMonth+dtDay; 
}

function isDate(txtDate) {
	var currVal = txtDate; 
	if(currVal == '') 
	return true; 

	//Declare Regex 
	var rxDatePattern = /^(\d{1,2})(\/|-)(\d{1,2})(\/|-)(\d{4})$/;
	var dtArray = currVal.match(rxDatePattern); // is format OK?

	if (dtArray == null) 
	return false; 

	//Checks for mm/dd/yyyy format. 
	dtMonth = dtArray[3]; 
	dtDay= dtArray[1]; 
	dtYear = dtArray[5]; 

	if (dtMonth < 1 || dtMonth > 12) 
	return false; 
	else if (dtDay < 1 || dtDay> 31) 
	return false; 
	else if ((dtMonth==4 || dtMonth==6 || dtMonth==9 || dtMonth==11) && dtDay ==31) 
	return false; 
	else if (dtMonth == 2) { 
		var isleap = (dtYear % 4 == 0 && (dtYear % 100 != 0 || dtYear % 400 == 0)); 
		if (dtDay> 29 || (dtDay ==29 && !isleap)) 
		return false; 
	}
	
	return true; 
}

function batchHMode(value) {
	hourValue = value
	minValue = value
	
	$('#hour').empty();
	$('#minute').empty();
	setHour()
	if(value=='1') {
		document.getElementById("minute").disabled = false;
		$('#radioMin').prop('checked',true);
		setMinute()
	} else if(value=='2') {
		$("#minute").val('0')
		document.getElementById("minute").disabled = true;
//		$('#radioMin2').prop('checked',true);
	}
	
}
