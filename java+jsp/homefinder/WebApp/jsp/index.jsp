<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<title>Home Finder</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
<link href='http://fonts.googleapis.com/css?family=Open+Sans:400,600' rel='stylesheet' type='text/css'>
<link href='http://fonts.googleapis.com/css?family=Rokkitt' rel='stylesheet' type='text/css'>
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
 <script type="text/javascript"  src="<%=request.getContextPath()%>/js/menu.js"></script>
<!-- start top_js_button -->
<script type="text/javascript" src="<%=request.getContextPath()%>/js/move-top.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/easing.js"></script>
   <script type="text/javascript">
		jQuery(document).ready(function($) {
			$(".scroll").click(function(event){		
				event.preventDefault();
				$('html,body').animate({scrollTop:$(this.hash).offset().top},1200);
			});
		});
	</script>
	 	<!---strat-slider---->	
	    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/slider.css" />
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/modernizr.custom.28468.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.cslider.js"></script>
			<script type="text/javascript">
				$(function() {
				
					$('#da-slider').cslider({
						autoplay	: true,
						bgincrement	: 450
					});
				
				});
			</script>
		<!---//strat-slider---->
		
		
<!---strat-submit---->
<script type="text/javascript" src="http://maps.google.com/maps/api/js?key=AIzaSyBJq8j_bjzrmK-zsOIuItyfbU-pm4BXYaU"></script>
<script type="text/javascript">
	function evalAndRedir(){
	    var address;
	    //communityPopulation("44","88");
        if(document.getElementById("address").value=="Please type address"){
        	address = "";
        }else{
            address = document.getElementById("address").value;
        }
        var city;
        if(document.getElementById("city").value=="0"){
            alert("Please choose a city!");
            return false;
        }else{
            city = document.getElementById("city").value; 
        }
			     
	    var zipcode;
        if(document.getElementById("zipcode").value=="Please type zipcode"){
        	zipcode = "";
        }else{
            zipcode = document.getElementById("zipcode").value;
        }
        var total_address = address+" "+city+" "+zipcode;
        document.getElementById("total_address").value = total_address;
        var geocoder = new google.maps.Geocoder();
        
    	geocoder.geocode( { 'address': total_address, 'region': 'us'}, function(results, status) {
        	if (status == google.maps.GeocoderStatus.OK) {
        		//console.log(results[0].geometry.location)
        		var latitude = results[0].geometry.location.lat();
        		var longitude = results[0].geometry.location.lng();
        		document.getElementById("latitude").value = latitude;
        		
        		document.getElementById("longitude").value = longitude;
        		// get community population
        		communityPopulation(latitude,longitude);
             	
      		} else { 
        		alert("Geocode was not successful for the following reason: " + status); 
      		} 
    	});
    	
        
                 
        //window.open("evaluate.html?address=" + address+"&city="+city+"&zipcode="+zipcode);
        //location.href='evaluate.html?address='+document.forms[0].elements[0].value;
}
</script>
<!---//strat-submit---->
		
<script src="<%=request.getContextPath()%>/js/citysdk.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/citysdk.census.js" text="text/javascript"></script>
<script type="text/javascript">
     function communityPopulation(latitude,longitude){	 
    	 // census sdk
    	 var sdk = new CitySDK();
         var censusModule = sdk.modules.census;
         censusModule.enable("7822a3caa2a5c795812333c4c96cb0ef11452557");
    	 var request = {
                 "lat": latitude,
                 "lng": longitude,
                 "level": "blockGroup",
                 "variables": [
                     "B01003_001E",
                     "B18101_001E"
                 ]
          };
    	  censusModule.APIRequest(request, function(response) {
    		  console.log(response);
             
             if(response.data[0].B01003_001E!=null){
            	 document.getElementById("total_population").value = response.data[0].B01003_001E;
             }
             if(response.data[0].B18101_001E!=null){
            	 document.getElementById("disabled_population").value = response.data[0].B18101_001E;
             }
             submitform();
         });
       }
     
     
     function submitform(){
    	 var userForm = document.getElementById("userForm");
      	 userForm.action = "<%=request.getContextPath()%>/evaluation/evaluation.action";
      	 userForm.submit();
     }
</script>
		
</head>

<body>
<!-- start header -->
<div class="header_bg">
<div class="wrap">
	<div id="content">
      <header id="topnav">
        <nav>
        		 <ul>
					<li class="active"><a href="#home" class="scroll">Evaluate</a></li>
					<li><a href="#about" class="scroll">Search</a></li>
					<div class="clear"> </div>
				</ul>
        </nav>
         <h1><a href="index.html"><img src="<%=request.getContextPath()%>/images/logo.png"></a></h1>
        <a href="#" id="navbtn">Nav Menu</a>
        <div class="clear"> </div>
      </header><!-- @end #topnav -->
    </div>
</div>
</div>
<!-- start slider -->
<div class="slider_bg" id="home">
<div class="wrap">
	<div class="slider">
				<!---start-da-slider----->
				<div id="da-slider" class="da-slider">
				<div class="da-slide">
					<h2>Descriptions</h2>
					<p>Descriptions bala bala bala bala bala bala bala bala bala bala bala bala bala bala bala bala bala bala bala bala</p>
					<a href="#evaluate" class="da-link">Evaluate</a>
				</div>
				<div class="da-slide">
					<h2>Description2</h2>
					<p>Descriptions bala bala bala bala bala bala bala bala bala bala bala bala bala bala bala bala bala bala bala bala</p>
					<a href="#evaluate" class="da-link">Evaluate</a>
				</div>
				<div class="da-slide">
					<h2>Description3</h2>
					<p>Descriptions bala bala bala bala bala bala bala bala bala bala bala bala bala bala bala bala bala bala bala bala</p>
					<a href="#evaluate" class="da-link">Evaluate</a>
				</div>
				<div class="da-slide">
					<h2>Description4</h2>
					<p>Descriptions bala bala bala bala bala bala bala bala bala bala bala bala bala bala bala bala bala bala bala bala</p>
					<a href="#evaluate" class="da-link">Evaluate</a>
				</div>			
			</div>
 			<!---//End-da-slider----->
 	</div>
</div>
</div>


<!---------evaluation-------------->
<div class="contact" id="evaluate">
	<h3 class="heading">Input Address</h3>
			<p class="desc">Type your address or zipcode.</p>
	<div class="contact-form">
		<div class="wrap">
			 	 <div class="content">
		 		 </div>
		 		        <s:form id="userForm" method="post" action="">
					    	<div>
						    	<input id="address" name="address" type="text" class="textbox" value="Please type address" onfocus="this.value = '';" onblur="if (this.value == '') {this.value = 'Please type address';}">
						    	<select id="city" name="city">
						    	    <option value ="0">City</option>
						    	    <s:iterator value="cities" status="true">
                                    	<option value="<s:property value="place"/>">
                        				<s:property value="place" /></option>
				  				    </s:iterator>
                                </select>  
						    	<div class="clear"> </div>
						    </div>
						    <div>
		 	 	            	<h2 class="heading1_mid">or</h2>
		 		            </div>
						    <div>
						    	<input id="zipcode" name="zipcode" type="text" class="textbox" value="Please type zipcode" onfocus="this.value = '';" onblur="if (this.value == '') {this.value = 'Please type zipcode';}">
						    	 <div class="clear"> </div>
						    </div>
						    <div>
						   		<span><input type="button" class="" value="Evaluate" onclick = "evalAndRedir()"></span>
						    </div>
						    <input id="total_address" name="total_address" type="hidden" value="">
						    <input id="total_population" name="total_population" type="hidden" value="0">
						    <input id="disabled_population" name="disabled_population" type="hidden" value="0">
						    <input id="latitude" name="latitude" type="hidden" value="">
						    <input id="longitude" name="longitude" type="hidden" value="">
					    </s:form>
				    </div>
  				<div class="clear"> </div>		
			  </div>
</div>
</div>
<!--------end_evaluation----------->
<div class="footer">
		<div class="footer_top">
			<div class="wrap">
		<div class="social-icons">
			 
		</div>
		<div class="footer_nav">
			
		</div>
		<div class="clear"> </div>
		</div>
		<!-- scroll_top_btn -->
	    <script type="text/javascript">
			$(document).ready(function() {
			
				var defaults = {
		  			containerID: 'toTop', // fading element id
					containerHoverID: 'toTopHover', // fading element hover id
					scrollSpeed: 1200,
					easingType: 'linear' 
		 		};
				
				
				$().UItoTop({ easingType: 'easeOutQuart' });
				
			});
		</script>
		 <a href="#" id="toTop" style="display: block;"><span id="toTopHover" style="opacity: 1;"></span></a>
		<!--end scroll_top_btn -->
</div>
</body>

</html>