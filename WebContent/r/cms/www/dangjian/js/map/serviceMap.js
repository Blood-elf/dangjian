var map;
//复杂的自定义覆盖物
function ComplexCustomOverlay(point){
  this._point = point;
}

function ComplexOverlay(point){
	  this._point = point;
	}
$(function(){
	    console.log('111111111111111111111'+new Date());
		//创建百度地图
		map = new BMap.Map("map",{});    // 创建Map实例 120.3054559,31.57003745
		var point=new BMap.Point(120.422423,31.511137);
		map.centerAndZoom(point, 13);  // 初始化地图,设置中心点坐标和地图级别
		map.addControl(new BMap.MapTypeControl());   //添加地图类型控件
		map.enableScrollWheelZoom(true);
		
		
	    ComplexCustomOverlay.prototype = new BMap.Overlay();
	    ComplexCustomOverlay.prototype.initialize = function(maps){
	      this._map = maps;
	      var div = this._div = document.createElement("div");
	      div.style.position = "absolute";
	      div.style.zIndex = BMap.Overlay.getZIndex(this._point.lat);//聚合功能?
	      div.style.height = "45px";
	      div.style.width="45px";

	      var arrow = this._arrow = document.createElement("img");
	      arrow.src = "../r/cms/www/dangjian/images/map-flag-1_05.png";
	      arrow.style.width = "41px";
	      arrow.style.height = "65px";
	      arrow.style.top = "22px";
	      arrow.style.left = "10px";
	      div.appendChild(arrow);
	     

	      map.getPanes().labelPane.appendChild(div);//getPanes(),返回值:MapPane,返回地图覆盖物容器列表  labelPane呢???
	      
	      return div;
	      
	    }
	    
	    ComplexCustomOverlay.prototype.draw = function(){
	      var maps = this._map;
	      var pixel = maps.pointToOverlayPixel(this._point);
	      this._div.style.left = pixel.x - parseInt(this._arrow.style.left) + "px";
	      this._div.style.top  = pixel.y - 30 + "px";
	    }
	    ComplexCustomOverlay.prototype.addEventListener = function(event,fun){
	    	this._div['on'+event] = fun;
	    }
	        
	    //ssdfsd
	    ComplexOverlay.prototype = new BMap.Overlay();
	    ComplexOverlay.prototype.initialize = function(maps){
	      this._map = maps;
	      var div = this._div = document.createElement("div");
	      div.style.position = "absolute";
	      div.style.zIndex = BMap.Overlay.getZIndex(this._point.lat);//聚合功能?
	      div.style.height = "45px";
	      div.style.width="45px";

	      var arrow = this._arrow = document.createElement("img");
	      arrow.src = "../r/cms/www/dangjian/images/map-flag-1_03.png";
	      arrow.style.width = "41px";
	      arrow.style.height = "65px";
	      arrow.style.top = "22px";
	      arrow.style.left = "10px";
	      div.appendChild(arrow);
	     

	      map.getPanes().labelPane.appendChild(div);//getPanes(),返回值:MapPane,返回地图覆盖物容器列表  labelPane呢???
	      
	      return div;
	      
	    }
	    
	    ComplexOverlay.prototype.draw = function(){
	      var maps = this._map;
	      var pixel = maps.pointToOverlayPixel(this._point);
	      this._div.style.left = pixel.x - parseInt(this._arrow.style.left) + "px";
	      this._div.style.top  = pixel.y - 30 + "px";
	    }
	    ComplexOverlay.prototype.addEventListener = function(event,fun){
	    	this._div['on'+event] = fun;
	    }
	    //sdfsdf
		
		
		$('.showModelClose').click(function(){
			$('.overlay').hide();
			$('.mapShowModel').hide();
			$('.transfer').addClass('active');
			$('.transfer').siblings().removeClass('active');
			$('#tab1').css('display', 'block');
			$('#tab2').css('display','none');
		});
		
		$('.refresh').click(function(){
			getServiceOrganizationInfo();
		});
		
		$('.showSearch').click(function(){
			
//			
			if($('.sear').css('display')=='none'){
				$('.sear').fadeIn();
			}else{
				$('.sear').fadeOut();
				$("input[name='search']").val("");
			}
		});
		
		$('.searching').click(function(){
			var value=$("input[name='search']").val();
			console.log(value);
			getServiceOrganizationInfo(value);
		});
		
		//tab
		$(".tab_content").hide();
		$("ul.tabs li:first").addClass("active").show();
		$(".tab_content:first").show();
		//On Click Event
		$("ul.tabs li").click(function() {
		    $("ul.tabs li").removeClass("active");
		    $(this).addClass("active");
		    $(".tab_content").hide();
		    var activeTab = $(this).find("a").attr("href");
		    $(activeTab).fadeIn();
		    return false;
		});
		getServiceOrganizationInfo();
		console.log('22222222222222222222222'+new Date());
})

function getServiceOrganizationInfo(value){
	var data={};
	var markers = [];
	data.value=value;
	$.ajax({
		url:"/jeeadmin/jeecms/front/web_list.do",
		data:data,
		async:true,
		success:function(result){
			debugger;
			if (document.createElement('canvas').getContext) {
				map.clearOverlays();
				var data=result.pagination;
				for(var i=0;i<data.length;i++){
					var point=new BMap.Point(data[i].transferLongitude,data[i].transferLatitude);
					addMarker(point,data[i],"f");
					if(!data[i].isSame){
						var point=new BMap.Point(data[i].serviceLongitude,data[i].serviceLatitude);
						addMarker(point,data[i],"s");
					}
/*					if(i==0&&value!=null){
						map.centerAndZoom(point, 14);  // 初始化地图,设置中心点坐标和地图级别
					}else{
						map.centerAndZoom(point, 12);
					}*/
				}
			}
		}

	});
}

//编写自定义函数,创建标注
function addMarker(point,perm,check){
 var marker;
 if(check=='f'){
	 marker = new ComplexCustomOverlay(point);
 }else{
	 marker = new ComplexOverlay(point);
 }
  map.addOverlay(marker);
  marker.addEventListener("click",function(){
	  createModel(perm);
  });
}

function createModel(data){
		$('#organizationName').text(data.organizationName);
		if(data.isSame){
			$('#serviceAddress').text(data.transferAddress);
		}else{
			$('#serviceAddress').text(data.serviceAddress);
		}
		$('#transferAddress').text(data.transferAddress);
		$('#transferScope').text(data.transferScope);
		$('#transferContactInfo').text(data.transferContactInfo);
		
		$('#serviceScope').text(data.serviceScope);
		$('#serviceContactInfo').text(data.serviceContactInfo);
		$('#transferTime').text(data.transferStartdate);
		$('#serviceTime').text(data.serviceStartdate);
		$('.overlay').show();
		$('.mapShowModel').show();
}