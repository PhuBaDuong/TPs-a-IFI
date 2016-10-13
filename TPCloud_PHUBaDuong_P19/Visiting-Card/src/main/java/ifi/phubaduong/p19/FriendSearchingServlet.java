package ifi.phubaduong.p19;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;

public class FriendSearchingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public FriendSearchingServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String firstName = req.getParameter("first_name");
		String lastName = req.getParameter("last_name");
		String userName = req.getParameter("user_name");
		String email = req.getParameter("email");

		Query<Account> querry = ObjectifyService.ofy().load().type(Account.class);
		if (firstName != null && !firstName.equals("")) {
			querry = querry.filter("firstName", firstName);
		}
		if (lastName != null && !lastName.equals("")) {
			querry = querry.filter("lastName", lastName);
		}
		if (userName != null && !userName.equals("")) {
			querry = querry.filter("userName", userName);
		}
		if (email != null && !email.equals("")) {
			querry = querry.filter("email", email);
		}

		req.getSession().setAttribute("first_name", firstName == null ? "" : firstName);
		req.getSession().setAttribute("last_name", lastName == null ? "" : lastName);
		req.getSession().setAttribute("user_name", userName == null ? "" : userName);
		req.getSession().setAttribute("email", email == null ? "" : email);

		List<Account> searchResults = querry.list();
		req.getSession().setAttribute("searched_friends", searchResults);
		req.getSession().setAttribute("isSearching", "true");
		resp.sendRedirect("friend_search.jsp");

	}
}
