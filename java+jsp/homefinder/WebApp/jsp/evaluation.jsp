<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html lang="en">
<head>
  <meta charset="utf-8">
  <title>Home Finder</title>
  <meta name="description" content="Home Finder">
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1"> 
  <meta http-equiv="content-type" content="text/html; charset=UTF-8"/> 
  <link rel="stylesheet" href="<%=request.getContextPath()%>/css/bootstrap.css">
  <link rel="stylesheet" href="<%=request.getContextPath()%>/css/font-awesome.min.css">
  <link rel="stylesheet" href="<%=request.getContextPath()%>/css/plugin.css">
  <link rel="stylesheet" href="<%=request.getContextPath()%>/css/style_evalute.css">
  <!--[if lt IE 9]>
    <script src="js/ie/respond.min.js"></script>
    <script src="js/ie/html5.js"></script>
  <![endif]-->
  <link href="http://code.google.com/apis/maps/documentation/javascript/examples/default.css" rel="stylesheet" type="text/css" /> 
  <script type="text/javascript" src="http://maps.google.com/maps/api/js?key=AIzaSyBJq8j_bjzrmK-zsOIuItyfbU-pm4BXYaU"></script> 
  <script type="text/javascript"> 
  	var geocoder; 
  	var map;
    function codeAddress() {
    	
		var total_address = document.getElementById("total_address").value;
    	geocoder = new google.maps.Geocoder();
    	var myOptions = { 
      		zoom:15, 
     		mapTypeId: google.maps.MapTypeId.ROADMAP 
    	};
    	map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);
    	geocoder.geocode( { 'address': total_address, 'region': 'us'}, function(results, status) { 
        	if (status == google.maps.GeocoderStatus.OK) { 
        		console.log(results[0].geometry.location) 
        		map.setCenter(results[0].geometry.location); 
        		this.marker = new google.maps.Marker({ 
        			title:total_address, 
            		map: map,  
            		position: results[0].geometry.location 
        		}); 
            	var infowindow = new google.maps.InfoWindow({ 
                	content: '<strong>'+total_address+'</strong><br/>'+'Latitude: '+results[0].geometry.location.lat()+'<br/>Longitude: '+results[0].geometry.location.lng() 
            	}); 
            	infowindow.open(map,marker); 
      		} else { 
        		alert("Geocode was not successful for the following reason: " + status); 
      		} 
    	}); 
  	}
</script>

</head>
<body>
  
  <section id="content">
    <section class="main padder">
      <div class="clearfix">
        <h4>Evaluation Result</h4>
      </div>
        
      <div class="row">
        <div class="col-lg-8">
          <section class="panel">
            <header class="panel-heading">
              Location
            </header>
            <div>
                <div id="map_canvas" style="height:500px;width:100%"></div>
			</div>
          </section>
        </div>
        <div class="col-lg-4">
          <section class="panel">
            <header class="panel-heading">
              <input id="back" name="back" type="button" value="Back" onclick = "back()">
            </header>
            <div class="pull-in text-center">
              <h4>Evaluation Score:</h4>
              <div id="total_score" class="h4 m-t-mini"><strong>0</strong></div>
              <div class="line"></div>
              <div class="inline">
                <div id="canvas-holder">
                	<canvas id="canvas-radar" width="350" height="200"/>
		      	</div>
              </div>
              <div class="line"></div>
              
              <div><strong>Housing: <s:property value='housing'/></strong><br>
                   <s:property value='housingMessage'/>
              </div>
              
              <div class="line"></div>
              <div><strong>Mobility: <s:property value='mobility'/></strong><br>
                   <s:property value='mobilityMessage'/>
              </div>
              
              <div class="line"></div>
              <div><strong>Safety: <s:property value='safety'/></strong><br>
                   <s:property value='safetyMessage'/>
              </div>
              
              <div class="line"></div>
              <div><strong>Health & Special Services: <s:property value='health'/></strong><br>
                   <s:property value='healthMessage'/>
              </div>
              
              <div class="line"></div>
              <div><strong>Disability Community: <s:property value='community'/></strong><br>
                   <s:property value='communityMessage'/>
              </div>
              
            </div>
          </section>
        </div>
      </div>
    </section>
  </section>
  
   <input id="total_address" name="total_address" type="hidden" value="<s:property value='total_address'/>">
   <input id="latitude" name="latitude" type="hidden" value="<s:property value='latitude'/>">
   <input id="longitude" name="longitude" type="hidden" value="<s:property value='longitude'/>">
   <input id="housing" name="housing" type="hidden" value="<s:property value='housing'/>">
   <input id="mobility" name="mobility" type="hidden" value="<s:property value='mobility'/>">
   <input id="safety" name="safety" type="hidden" value="<s:property value='safety'/>">
   <input id="health" name="health" type="hidden" value="<s:property value='health'/>">
   <input id="community" name="community" type="hidden" value="<s:property value='community'/>">
   <input id="total" name="total" type="hidden" value="<s:property value='total'/>">
  <!-- footer -->
  <footer id="footer">
    <div class="text-center padder clearfix">
      <p>
        <small></small><br><br>
      </p>
    </div>
  </footer>
  <script src="<%=request.getContextPath()%>/js/Chart.js"></script>
  <script>
    var radarChartData;
    var housing;
    var mobility;
	var safety;
	var health;
	var community;
	var total;
	
	function initRadar() {
	    housing = document.getElementById("housing").value;
	    mobility = document.getElementById("mobility").value;
	    safety = document.getElementById("safety").value;
	    health = document.getElementById("health").value;
	    community = document.getElementById("community").value;
	    total = document.getElementById("total").value;
	    document.getElementById('total_score').innerHTML='<strong>'+total+'</strong>';
	    radarChartData = {
		labels: ["Housing", "Mobility", "Safety", "Health & Special Services", "Disability Community"],
		datasets: [
			{
				label: "Score",
				fillColor: "rgba(151,187,205,0.2)",
				strokeColor: "rgba(151,187,205,1)",
				pointColor: "rgba(151,187,205,1)",
				pointStrokeColor: "#fff",
				pointHighlightFill: "#fff",
				pointHighlightStroke: "rgba(151,187,205,1)",
				data: [housing,mobility,safety,health,community]
			}
		]
	    };
	    
	    window.myRadar = new Chart(document.getElementById("canvas-radar").getContext("2d")).Radar(radarChartData, {
			responsive: true
		});
	    
  	}
	
  	window.onload = function(){

		codeAddress();
		initRadar();
	}
  	
  	function back(){
    	window.location.href='<%=request.getContextPath()%>/evaluation/index.action';
    }
  </script>
  
</body>
</html>