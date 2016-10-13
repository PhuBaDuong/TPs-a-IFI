<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Create new account</title>
</head>
<body>
	<h1>Creer un nouveau compte</h1>
	<form action="register" method="post">
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
				<td>Email</td>
				<td><input width="100px" type="text" name="email"></td>
			</tr>
			<tr>
				<td>Nom du compte</td>
				<td><input width="100px" type="text" name="user_name"></td>
			</tr>
			<tr>
				<td>Mots de passe</td>
				<td><input width="100px" type="password" name="password"></td>
			</tr>
		</table>
		<input type="submit" value="Register">
	</form>
</body>
</html>