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
		if(''==groupTypeVal) {
			$('#month').empty();
			$('#date').empty();
			$('#hour').empty();
			$('#minute').empty();
			setValue();
		}else{
			$('#month').empty();
			$('#date').empty();
			$('#hour').empty();
			$('#minute').empty();
			findGroupTypeByKeyCode(groupTypeVal);
			
		}
		
	});
	
	$('#month').change(function () {
		var pass = true;
		var monthVal = $('#month').val();
		var yearNow = $('#yearNow').val();
		
		var firstDay = new Date(yearNow, monthVal, 1);
		var lastDay = new Date(yearNow, monthVal, 0);
		
//		alert(firstDay.getDate())
//		alert(lastDay.getDate())
		
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
		}
		
	});
	
	$('#saveBtn').click(function() {
		if(''==$("#groupType").val())return swal(PLS_SELECT+' '+PLS_SELECT_ALL_TYPE)
//		if(''==$("#month").val())return swal(PLS_SELECT+' '+PLS_SELECT_MONTH)
//		if(''==$("#date").val())return swal('date')
//		if(''==$("#hour").val())return swal('hour')
//		if(''==$("#minute").val())return swal('minute')
		
		var masterDataBean = {
			"month":$("#month").val() ,
			"day":$("#date").val() ,
			"hour":$("#hour").val() ,
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
//	        	alert(res.responseText)
	        },
	        error : function (res) {
	        	swal('Insert : '+res.responseText)
//	        	alert(res.responseText)
	        }
		});
		
	})
	
});

function setMonth(months) {
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
	$('#hour').append('<option value="*">' + PLS_SELECT_ALL_HOUR + '</option>');
	for(var i=0; i<24; i++) {
		$('#hour').append('<option value="'+(i+1)+'">' + (i+1) + '</option>');
	}
}

function setMinute() {
	$('#minute').append('<option value="*">' + PLS_SELECT_ALL_MINUTE + '</option>');
	for(var i=0; i<60; i++) {
		$('#minute').append('<option value="'+(i+1)+'">' + (i+1) + '</option>');
	}
}

function setValue() {
	$('#month').append('<option value="'+'">' + PLS_SELECT + '</option>');
	
	$('#date').append('<option value="'+'">' + PLS_SELECT + '</option>');
	
	$('#hour').append('<option value="'+'">' + PLS_SELECT + '</option>');
	
	$('#minute').append('<option value="'+'">' + PLS_SELECT + '</option>');
}

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
	        	$('#mindd').val(res.minute);
	        	
	        	setMonth(res.dropDownMonths);
	        	
	        	if('*' == res.month){
	        		lastDay = 28; //ช่วยคิดหน่อย
	        	}else{
	        		lastDay = new Date(res.yearNow, res.month, 0);
	        	}
	        	
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
