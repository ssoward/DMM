<%@page import="com.soward.util.ProductsLocationCountUtil"%>
<%
//  ProductsLocationCountUtil.updateCounts();


%>

<html>
<head>
<title>jQuery Hello World</title>
 
<script type="text/javascript" src="js/jquery-1.4.2.js"></script>
 
</head>
 
<script type="text/javascript">
$(document).ready(function() {
       $("#getWeatherReport").click(function(){
         $cityName = document.getElementById("cityName").value;
         $.post("WeatherServlet", {cityName:$cityName}, function(xml) {
        $("#weatherReport").html(
          $("report", xml).text()
        );          
         });
     });
 });
</script>
<form name="form1" type="get" method="post">
    Enter City :
    <input type="text" name="cityName" id="cityName" size="30" />
    <input type="button" name="getWeatherReport" id="getWeatherReport"
        value="Get Weather" />
</form>
<div id="weatherReport" class="outputTextArea">
</div>

<script type="text/javascript">
 
$(document).ready(function(){
 $("#flag").html("Hello World !! (display due to jQuery)");
});
 
</script>
<body>
<font color=red> 
Hello World !! (display due to HTML)
</font>
<font color=blue>
<div id="flag">
</div>
</font>
</body>
</html>
 
