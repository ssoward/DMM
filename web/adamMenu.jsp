<ul id="nav">
<li><a href="./index.jsp">Home</a></li>
<%if(isAdmin){ %>
<li><a class="down" href="#">Admin<!--[if gte IE 7]><!--></a><!--<![endif]-->
	<!--[if lte IE 6]><table><tr><td><![endif]--><ul>
		<li><a href="./viewUser.jsp">Users</a></li>
		<li><a href="./newUser.jsp">New User</a></li>
          <li><a href="./editPages.jsp?page=1">Edit Home Page</a></li>
		<li class="last"><a href="./query.jsp">Query Browser</a></li>
	</ul><!--[if lte IE 6]></td></tr></table></a><![endif]-->
</li>
<%} %>
	
<li><a class="down" href="#">Teachers<!--[if gte IE 7]><!--></a><!--<![endif]-->
	<!--[if lte IE 6]><table><tr><td><![endif]--><ul>
		<li><a href="./createTeacher.jsp">Teachers</a></li>
     </ul><!--[if lte IE 6]></td></tr></table></a><![endif]-->
</li>
<li><a class="down" href="#">Instruments<!--[if gte IE 7]><!--></a><!--<![endif]-->
	<!--[if lte IE 6]><table><tr><td><![endif]--><ul>
		<li><a href="./createInstruments.jsp">Instruments</a></li>
     </ul><!--[if lte IE 6]></td></tr></table></a><![endif]-->
</li>
<li><a class="down" href="#">Inventory<!--[if gte IE 7]><!--></a><!--<![endif]-->
	<!--[if lte IE 6]><table><tr><td><![endif]--><ul>
		<li><a href="./instrumentInventory.jsp">Inventory</a></li>
     </ul><!--[if lte IE 6]></td></tr></table></a><![endif]-->
</li>
</ul>
<div id="content-main">
	<div class="margined_REMOVE">