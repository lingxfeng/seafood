function setIsNoimg(id){
	var obj = jQuery("#"+id);
	var state = obj.val();
	if(state=="1"){
		obj.val("0");
		jQuery("#"+id+"Off").show();
		jQuery("#"+id+"On").hide();
	}else{
		obj.val("1");
		jQuery("#"+id+"Off").hide();
		jQuery("#"+id+"On").show();
	}
}
function setIsNo(id,isno){
	var obj = jQuery("#"+id);
	if(!isno){
		obj.val("0");
		jQuery("#"+id+"Off").show();
		jQuery("#"+id+"On").hide();
	}else{
		obj.val("1");
		jQuery("#"+id+"Off").hide();
		jQuery("#"+id+"On").show();
	}
}
function createDialogcur(dialog_id,dialog_title,dialog_height,dialog_width,dialog_top,dialog_contend){
    var dialog_left=300;
    jQuery("#"+dialog_id).remove();
    jQuery("body").append("<div id='"+dialog_id+"'><div class='white_content'> <a href='javascript:void(0);' dialog_uri='undefined' class='white_close' onclick='javascript:jQuery(\"#"+dialog_id+"\").remove();'></a><div class='white_box'><h1>"+dialog_title+"</h1><div class='content_load'></div></div></div><div class='black_overlay'></div></div>");
       if(dialog_top==undefined||dialog_top==""){
         dialog_top=jQuery(window).scrollTop()+(jQuery(window).height()-jQuery(document).outerHeight())/2-dialog_height/2;
       }else{
         dialog_top=parseInt(dialog_top)+jQuery(window).scrollTop();
       }
       var h=jQuery(document).height();
    jQuery('.black_overlay').css("height",h);
       var dialog_left=(jQuery(document).width()-dialog_width)/2;
    jQuery(".white_content").css("position","absolute").css("top",parseInt(dialog_top)+"px").css("left",parseInt(dialog_left)+"px");
    jQuery(".content_load").remove(); 
        jQuery("#"+dialog_id+" .white_content").css("width",dialog_width);
        jQuery("#"+dialog_id+" .white_box h1").after(dialog_contend);
        jQuery("#"+dialog_id).show();  
       jQuery("#"+dialog_id+" .white_box h1").css("cursor","move")
}