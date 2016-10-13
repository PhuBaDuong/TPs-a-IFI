package ifi.phubaduong.p19;

import com.googlecode.objectify.ObjectifyService;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		List<Account> accountFounds = ObjectifyService.ofy().load().type(Account.class)
				.filter("userName", req.getParameter("user_name")).list();
		if (accountFounds == null || accountFounds.isEmpty()) {
			resp.getWriter().println("Le nom du compte n'existe pas!");
		} else {
			if (!accountFounds.get(0).getPassword().equals(req.getParameter("password"))) {
				resp.getWriter().println("Le mots de passe est faux!");
				resp.getWriter().println(accountFounds.get(0).getPassword());
			} else {
				HttpSession session = req.getSession();
				session.setAttribute("current_account", accountFounds.get(0));
				session.setMaxInactiveInterval(600);
				resp.sendRedirect("../home.jsp");
			}
		}
	}
}
