<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<h1>Creer un carte de visite</h1>
<body>
	<%
		if (request.getSession().getAttribute("current_account") == null) {
			response.sendRedirect("../home.jsp");
		}
	%>
	<form action="opening_visiting_card" method="post">
		<table>
			<tr>
				<td>Nom</td>
				<td><input width="100px" type="text" name="first_name"></td>
			</tr>
			<tr>
				<td>Prenom</td>
				<td><input width="100px" type="text" name="last_name"></td>
			</tr>
			<tr>
				<td>Telephone</td>
				<td><input width="100px" type="text" name="telephone"></td>
			</tr>
			<tr>
				<td>Email</td>
				<td><input width="100px" type="text" name="email"></td>
			</tr>
			<tr>
				<td>Organisation</td>
				<td><input width="100px" type="text" name="organisation"></td>
			</tr>
			<tr>
				<td>Position</td>
				<td><input width="100px" type="text" name="position"></td>
			</tr>
		</table>
		<input type="submit" value="Creer">
	</form>
</body>
</html>