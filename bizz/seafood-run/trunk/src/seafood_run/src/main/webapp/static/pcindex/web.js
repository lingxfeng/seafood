// JavaScript Document

jQuery(document).ready(function() {
    /*加载*/
    $.MyCommon.PageLoading({ sleep: 300 });
    //导航PAD&PHONE
    $(".menu-activator").click(function(){
        $(this).parent().find(".menu-overlay").toggleClass("active",300);
        $(this).toggleClass("active");
    });
    //图片延迟加载
    jQuery("img").lazyload({effect: "show"});

    //apply_list
    jQuery(".apply_list li:odd").css("border-right","none");

   /* $(window).resize();
    //图片加载
    $(".img-load").each(function () {
        if($(window).width()<768)
        {
            $(this).attr({ src: $(this).data("phone-src")})
        }
        else
        {
            $(this).attr({ src: $(this).data("src") })
        }

    });*/ 

    /*置顶置底*/
	jQuery('.back_top').click(function(){
		jQuery('html,body').animate({scrollTop: '0'}, 800);
   });

  
	   

});


