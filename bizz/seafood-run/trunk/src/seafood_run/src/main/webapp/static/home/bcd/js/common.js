$(function(){
	//单独选择某一个
	$("input[name='check_item']").click(function(){
			var index=$("input[name='check_item']").index(this);
			$("input[name='check_item']").eq(index).toggleClass("checked");//伪复选
	});	
	//全选
	$("#check_all,#box_all").click(function(){
     //$("input[name='check_item']").attr("checked",$(this).attr("checked"));
	 //$("input[name='check_item'],#check_all,#box_all").toggleClass("checked");
	 if($("#box_all").attr("checked")==false || $("#box_all").attr("checked")==undefined){
		 $("[name='check_item']").attr("checked",true);//全选
		 $("input[name='check_item']").addClass("checked");
		 $("#box_all").addClass("checked");
		 $("#box_all").attr("checked",true);
		 var allAmt = 0;
		 var i=1;
			$('.cartlist').each(function(){
				allAmt+=($("#cId"+i).val())*($("#pri"+i).val());
				i++;
			});
			$('#allAmt').text(allAmt.toFixed(2));
	 }else{
		 $("[name='check_item']").attr("checked",false);
		 $("input[name='check_item']").removeClass("checked");
		 $("#box_all").removeClass("checked");
		 $("#box_all").attr("checked",false);
		 $('#allAmt').text(0);
	 }
	});

});