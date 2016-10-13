package ifi.phubaduong.p19;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.googlecode.objectify.ObjectifyService;

public class AccountRegistrationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Account newAccount = new Account();
		newAccount.setFirstName(req.getParameter("first_name"));
		newAccount.setLastName(req.getParameter("last_name"));
		newAccount.setUserName(req.getParameter("user_name"));
		newAccount.setPassword(req.getParameter("password"));
		newAccount.setEmail(req.getParameter("email"));
		newAccount.setCreatedDate(new Date());
		ObjectifyService.ofy().save().entity(newAccount).now();
		resp.sendRedirect("../home.jsp");
	}
}
