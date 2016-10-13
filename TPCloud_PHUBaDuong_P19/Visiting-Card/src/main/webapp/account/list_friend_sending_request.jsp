<%@page import="ifi.phubaduong.p19.Account"%>
<%@page import="com.googlecode.objectify.ObjectifyService"%>
<%@page import="ifi.phubaduong.p19.MakingFriend"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<style>
.data_table, th, td {
	border: 1px solid black;
}
</style>
</head>

<body>
	<h1>Liste des personnes vous ont demandé d'être d'ami</h1>
	<%
		Account currentAccount = (Account) request.getSession().getAttribute("current_account");
		if (currentAccount == null) {
			response.sendRedirect("../home.jsp");
		} else {
			List<MakingFriend> demandingUserNames = ObjectifyService.ofy().load().type(MakingFriend.class)
					.filter("userName", currentAccount.getUserName()).filter("status", "0").list();
			//List<MakingFriend> demandingUserNames = ObjectifyService.ofy().load().type(MakingFriend.class).list();
			if (demandingUserNames == null || demandingUserNames.isEmpty()) {
	%>
	<h2>Il n'y a personne</h2>
	<%
		} else {
	%>
	<table class="data_table">
		<th>Nom du compte</th>
		<%
			for (MakingFriend makingFriend : demandingUserNames) {
						//ObjectifyService.ofy().delete().type(MakingFriend.class).id(makingFriend.getId());
		%>
		<tr>
			<td><%=makingFriend.getFriendName()%></td>
			<td><a
				href="accepter_la_demande?making_friend_id=<%=makingFriend.getId()%>">Accepter</td>
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