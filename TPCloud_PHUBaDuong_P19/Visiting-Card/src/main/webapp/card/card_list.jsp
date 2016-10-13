<%@page import="java.util.ArrayList"%>
<%@page import="com.googlecode.objectify.ObjectifyService"%>
<%@page import="ifi.phubaduong.p19.Account"%>
<%@page import="ifi.phubaduong.p19.VisitingCard"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>La liste des cartes</title>
<style>
.data_table, th, td {
	border: 1px solid black;
}
</style>
</head>
<h1>La liste des cartes</h1>
<body>
	<%
		if (request.getSession().getAttribute("current_account") == null) {
			response.sendRedirect("../home.jsp");
		} else {

			Account currentAccount = (Account) request.getSession().getAttribute("current_account");
			List<VisitingCard> visitingCards = ObjectifyService.ofy().load().type(VisitingCard.class)
					.filter("userName", currentAccount.getUserName()).list();
			if (visitingCards == null || visitingCards.isEmpty()) {
	%>
	<p>Vous n'avez aucune de carte de visite!</p>
	<%
		} else {
	%>
	<form method="get" action="card_searching">
		<table>
			<tr>
				<td>Nom <input type="text" name="first_name"></td>
				<td>Prenom<input type="text" name="last_name"></td>
				<td>Telephone<input type="text" name="telephone"></td>
				<td>Email<input type="text" name="email"></td>
			</tr>
		</table>
		<input type="submit" value="Search">
	</form>

	<table class="data_table">
		<th>Id</th>
		<th>Nom</th>
		<th>Prenom</th>
		<th>Nom du compte</th>
		<th>Email</th>
		<th>Telephone</th>
		<th>Organisation</th>
		<th>Position</th>
		<th>Date</th>
		<%
			for (VisitingCard visitingCard : visitingCards) {
		%>
		<tr>
			<td><%=visitingCard.getId()%></td>
			<td><%=visitingCard.getFirstName()%></td>
			<td><%=visitingCard.getLastName()%></td>
			<td><%=visitingCard.getUserName()%></td>
			<td><%=visitingCard.getEmail()%></td>
			<td><%=visitingCard.getTelNumber()%></td>
			<td><%=visitingCard.getOrganisation()%></td>
			<td><%=visitingCard.getPosition()%></td>
			<td><%=visitingCard.getCreatedDate()%></td>
			<td><a href="card_deletion?card_id=<%=visitingCard.getId()%>">Delete</a></td>
		</tr>
		<%
			}
		%>

	</table>
	<%
		}
		}
	%>
</body>
</html>