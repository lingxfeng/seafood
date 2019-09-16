$(document).ready(function (){
	if($("#provice").val()!=""){
		choice_pro($("#provice").val());
	}
})

function choice_pro(value) {
	   $("#city").empty();
	    $.ajax({
	        type: "post",
	        //url: "/popular.java?cmd=getCity",
	        url: "/systemRegion.java?cmd=getSystemRegionByParentSn",
	        data: "parentSn=" + value,
	        dataType: "json",
	        success: function(data) {
	            for (var i = 0; i < data.length; i++) {
	                $("#city").append("<option value='" + data[i].sn + "'>" + data[i].title + "</option>");
	            }
				judgeNext($("#city").val());
	        }
	    });
	}

function judgeNext(value){
	$.ajax({
        type: "post",
        //url: "/popular.java?cmd=getCity",
        url: "/distributionCore.java?cmd=judgeNext",
        data: "sn=" + value,
        dataType: "json",
        success: function(data) {
        	//console.dir(data)
        	if(data.status==1){
        		choice_city(value)
        		$("#area").show();
        	}else{
        		$("#area_id").val(value);
        		$("#area").hide();
        	}
        }
    });
}

function choice_city(value) {
	   $("#area").empty();
	    $.ajax({
	        type: "post",
	        //url: "/popular.java?cmd=getCity",
	        url: "/systemRegion.java?cmd=getSystemRegionByParentSn",
	        data: "parentSn=" + value,
	        dataType: "json",
	        success: function(data) {
	        	$("#area_id").val(value);
	            for (var i = 0; i < data.length; i++) {
	                $("#area").append("<option value='" + data[i].sn + "'>" + data[i].title + "</option>");
	            }
	            if($("#area").children().length>0){
	            	$("#area_id").val(data[0].sn);
	            }
	        }
	    });
	}

	function choice_area(value) {
		$("#area_id").val(value);
	}