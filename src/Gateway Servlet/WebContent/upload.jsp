<!DOCTYPE html>
<html>
	<head>

		<title> Upload Page | MTKS </title>
		<link rel="stylesheet" type="text/css" href="css/style.css">
		<script type="text/javascript">

function checkinput ( form )
{
	var re = /^[a-zA-Z0-9 ]+$/i;
 
  if (form.titlebox.value == "") {
    alert( "Please enter a value in Title.." );
    form.searchbox.focus();
    return false ;
  }
  
  if (!re.test(form.titlebox.value)) {
	    alert( "Please enter alpha-numeric value in Title.." );
	    form.searchbox.focus();
	    return false ;
	  }
  
  if (form.textbox.value == "") {
	    alert( "Please enter a value in text box.." );
	    form.searchbox.focus();
	    return false ;
	  }
	  
	  if (!re.test(form.textbox.value)) {
		    alert( "Please enter alpha-numeric value in text box.." );
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
			
			<form action="#" method="post" onsubmit="return checkinput(this);">

				<input id='u_button' type='submit' value='Go'></input>
				<input id='titlebox' name='titlebox' type='text' placeholder='Enter Title Here...'></input>
				<textarea placeholder='Enter Text Here...' name='textbox'></textarea>
			
			</form>

		</div>

		<%@ include file="includes/footer-div.jsp" %>
  
	</body>
</html>
