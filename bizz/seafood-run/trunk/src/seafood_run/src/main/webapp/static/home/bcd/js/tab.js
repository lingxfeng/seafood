


function doClick(o){
	 o.className="nav_current";
	 var j;
	 var id;
	 var e;
	 for(var i=1;i<=4;i++){ //这里3 需要你修改 你多少条分类 就是多少
	   id ="nav"+i;	   
	   j = document.getElementById(id);
	   e = document.getElementById("sub"+i);
	   if(id != o.id){
	   	 j.className="nav_link";
	   	 e.style.display = "none";
	   }else{
			e.style.display = "block";
	   }
	 }
	 }
