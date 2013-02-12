<!DOCTYPE html>
<html>
	<head>

		<title> Search Page | MTKS </title>
		<link rel="stylesheet" type="text/css" href="css/style.css">
		<script language="JavaScript" type="text/javascript">

function checkinput ( form )
{
	var re = /^[a-zA-Z0-9 ]+$/i;
 
  if (form.searchbox.value == "") {
    alert( "Please enter a value.." );
    form.searchbox.focus();
    return false ;
  }
  
  if (!re.test(form.searchbox.value)) {
	    alert( "Please enter alpha-numeric value.." );
	    form.searchbox.focus();
	    return false ;
	  }

  return true ;
}

</script>

	</head>

	<body>
		
		<%@ include file="includes/header-div.jsp" %>

		<div id='container'>
			
			<form action="result.jsp" method="post" onsubmit="return checkinput(this);">

				<img id='logo_img' src="images/logo.png" alt="Logo">
				<h2>Multi-term Keyword Search</h2>
				<h3>in NoSQL Systems</h3>
				<input id='txtbox' name='searchbox' type='text' placeholder='Enter Search Term Here...'></input>
				<input id='s_button' type='submit' value='Go'></input>
			
			</form>

		</div>

		<%@ include file="includes/footer-div.jsp" %>
  
	</body>
</html>
