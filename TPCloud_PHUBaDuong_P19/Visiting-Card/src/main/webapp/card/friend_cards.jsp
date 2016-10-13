<%@page import="com.googlecode.objectify.ObjectifyService"%>
<%@page import="ifi.phubaduong.p19.VisitingCard"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Les cartes d'ami(e)</title>
<style>
.data_table, th, td {
	border: 1px solid black;
}
</style>
</head>
<body>
	<%
		if (request.getSession().getAttribute("current_account") == null
				|| request.getParameter("user_name") == null) {
			response.sendRedirect("../home.jsp");
		} else {
	%>
	<h1>
		Les cartes de visite de ami(e)
		<%=request.getParameter("user_name")%></h1>

	<%
		List<VisitingCard> visitingCards = ObjectifyService.ofy().load().type(VisitingCard.class)
					.filter("userName", request.getParameter("user_name")).list();
			if (visitingCards != null && !visitingCards.isEmpty()) {
	%>
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