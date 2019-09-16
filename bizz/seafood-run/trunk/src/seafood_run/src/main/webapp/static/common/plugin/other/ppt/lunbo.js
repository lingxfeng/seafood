var pptFun = function(id,leftId,rightId){
	var carouselTop=document.getElementById(id);
				var carouselLeft1=document.getElementById(leftId);
				var carouselRight1=document.getElementById(rightId);
				var lis=carouselTop.getElementsByTagName('li');
				var spans=carouselTop.getElementsByTagName('span');
				carouselTop.onmouseover=function(){
					carouselLeft1.style.display='';	
					carouselRight1.style.display='';
					clearInterval(dodo);
				}
				carouselTop.onmouseout=function(){
					carouselLeft1.style.display='none';	
					carouselRight1.style.display='none';
					dodo=setInterval(opacityRight,5000);
				}
				for(var i=0;i<spans.length;i++){
					spans[i].onmouseover=function(){
						var oldnum,newnum;
						this.setAttribute('val','1');
						for(var j=0;j<spans.length;j++)
						{
						/*if(spans[j].className=='current')
							{
								oldnum=j;
							}*/
							
							if(spans[j].getAttribute('val')=='1')
							{
								newnum=j;
								this.removeAttribute('val');
							}else{
								oldnum=j;
							}

						}
						if(oldnum==newnum)
						{
							return;
						}
						opacity(oldnum,newnum,0);
					}
				}
				
				carouselLeft1.onclick=function(){
					var oldnum,newnum;
					for(var i=0;i<spans.length;i++){
						if(spans[i].className=='current'){
							oldnum=i;
							break;
						}
					}
					if(oldnum==0){
						newnum=spans.length-1;
					}
					else{
						newnum=oldnum-1;
					}
					opacity(oldnum,newnum,0);
				}
				
				carouselRight1.onclick=opacityRight;
				function opacityRight(){
					var oldnum,newnum;
					for(var i=0;i<spans.length;i++){
						if(spans[i].className=='current'){
							oldnum=i;
							break;
						}
					}	
					if(oldnum==spans.length-1){
						newnum=0;
					}
					else{
						newnum=oldnum+1;
					}
					opacity(oldnum,newnum,100);
				}
				
				function opacity(oldnum,newnum,num){
					spans[oldnum].className='';
				//	console.log('oldnum  '+oldnum);
				//	console.log('newnum  '+newnum);
					spans[newnum].className='current';	
					num+=4;
					if(num<=1000){
						lis[oldnum].style.opacity=1-num/1000;
						lis[oldnum].style.filter='alpha(opacity='+(100-num/10)+')';
						lis[newnum].style.opacity=num/1000;
						lis[newnum].style.filter='alpha(opacity='+num/10+')';
						return setTimeout(function(){opacity(oldnum,newnum,num);},5);
					}
				}
				
				var dodo=setInterval(opacityRight,5000);
				
      }


