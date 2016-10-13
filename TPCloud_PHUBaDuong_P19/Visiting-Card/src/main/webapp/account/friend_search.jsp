<%@page import="com.googlecode.objectify.cmd.Query"%>
<%@page import="ifi.phubaduong.p19.MakingFriend"%>
<%@page import="com.googlecode.objectify.ObjectifyService"%>
<%@page import="ifi.phubaduong.p19.Account"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Chercher les utilisateurs</title>
<style>
.data_table, th, td {
	border: 1px solid black;
}
</style>
</head>
<body>
	<%
		if (request.getSession().getAttribute("current_account") == null) {
			response.sendRedirect("../home.jsp");
		} else {
	%>
	<h1>Chercher les utilisateurs</h1>
	<form method="get" action="friend_searching">
		<table>
			<tr>
				<%
					if (request.getSession().getAttribute("isSearching") != null
								&& ((String) request.getSession().getAttribute("isSearching")).equals("true")) {
				%>
				<td>Nom :<input type="text" name="first_name"
					value="<%=request.getSession().getAttribute("first_name")%>"></td>
				<td>Prenom :<input type="text" name="last_name"
					value="<%=request.getSession().getAttribute("last_name")%>"></td>
				<td>Nom du compte :<input type="text" name="user_name"
					value="<%=request.getSession().getAttribute("user_name")%>"></td>
				<td>Email :<input type="text" name="email"
					value="<%=request.getSession().getAttribute("email")%>"></td>

				<%
					} else {
				%>
				<td>Nom :<input type="text" name="first_name">
				</td>
				<td>Prenom :<input type="text" name="last_name">
				</td>
				<td>Nom du compte :<input type="text" name="user_name">
				</td>
				<td>Email :<input type="text" name="email">
				</td>
				<%
					}
				%>
			</tr>
		</table>
		<input type="submit" value="Search Friend">
	</form>
	<%
		List<Account> searchedFriends = (List<Account>) request.getSession().getAttribute("searched_friends");
			if (request.getSession().getAttribute("isSearching") == null
					|| !((String) request.getSession().getAttribute("isSearching")).equals("true")) {
				searchedFriends = ObjectifyService.ofy().load().type(Account.class).list();
				request.getSession().removeAttribute("isSearching");
			}
			if (searchedFriends == null || searchedFriends.isEmpty()) {
				{
	%>
	<p>Il n'y a pas de account!</p>
	<%
		}
			} else {
	%>
	<table class="data_table">
		<th>Nom</th>
		<th>Prenom</th>
		<th>Nom du compte</th>
		<th>Email</th>
		<th>Date</th>

		<%
			Query<MakingFriend> querry = ObjectifyService.ofy().load().type(MakingFriend.class).filter(
							"userName", ((Account) request.getSession().getAttribute("current_account")).getUserName());
					for (Account account : searchedFriends) {
		%>
		<tr>
			<td><%=account.getFirstName()%></td>
			<td><%=account.getLastName()%></td>
			<td><%=account.getUserName()%></td>
			<td><%=account.getEmail()%></td>
			<td><%=account.getCreatedDate()%></td>
			<%
				if (querry.filter("friendName", account.getUserName()).list() == null
									|| querry.filter("friendName", account.getUserName()).list().isEmpty()) {
			%>
			<td><a
				href="friend_invitation?user_name=<%=((Account) request.getSession().getAttribute("current_account")).getUserName()%>&friend_name=<%=account.getUserName()%>">
					Ajouter ami(e) </a></td>
			<%
				} else {
								if (!querry.filter("friendName", account.getUserName()).filter("status", "0").list()
										.isEmpty()) {
			%>
			<td><a href="#">Requete envoyee</a></td>
			<%
				} else {
			%>
			<td><a href="#">Ami(e)s</a></td>
			<td><a
				href="../card/friend_cards.jsp?user_name=<%=querry.filter("friendName", account.getUserName()).filter("status", "1")
										.list().get(0).getFriendName()%>">Voir
					ses cartes de visites </a></td>
			<%
				}
							}
			%>
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