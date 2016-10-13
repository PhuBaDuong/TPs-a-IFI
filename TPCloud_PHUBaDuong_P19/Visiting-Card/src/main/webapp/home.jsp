<%@page import="com.googlecode.objectify.ObjectifyService"%>
<%@page import="ifi.phubaduong.p19.MakingFriend"%>
<%@page import="ifi.phubaduong.p19.Account"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="com.google.appengine.api.users.User"%>
<%@ page import="com.google.appengine.api.users.UserService"%>
<%@ page import="com.google.appengine.api.users.UserServiceFactory"%>
<%@ page import="java.util.List"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html>
<head>
</head>

<body>
	<h1>Welcome to Visiting Card Management!</h1>
	<table>
		<%
			Account sessionUser = (Account) request.getSession().getAttribute("current_account");
			if (sessionUser != null) {
				List<MakingFriend> friendDemands = ObjectifyService.ofy().load().type(MakingFriend.class)
						.filter("friendName", sessionUser.getUserName()).filter("status", "0").list();
				if (friendDemands != null && !friendDemands.isEmpty()) {
					request.getSession().setAttribute("friends_sending_request", friendDemands);
		%>
		<tr>
			<td><a href="account/list_friend_sending_request.jsp">Vous
					avez <%=friendDemands.size()%> demands d'amis
			</a></td>
		</tr>
		<%
			}
		%>
		<tr>
			<td><a href="card/opening_card_form.jsp">Creer un carte de
					visite</a></td>
		</tr>
		<tr>
			<td><a href="card/card_list.jsp">La list de cartes crees</a></td>
		</tr>
		<tr>
			<td><a href="account/friend_search.jsp">Chercher des amis</a></td>
		</tr>
		<tr>
			<td><a href="account/logout">Logout</a></td>
		</tr>
		<%
			} else {
		%>
		<tr>
			<td><a href="account/login_form.jsp">Login</a></td>
		</tr>
		<tr>
			<td><a href="account/registration_form.jsp">Sign up</a></td>
		</tr>
		<%
			}
		%>
	</table>
</body>
</html>
