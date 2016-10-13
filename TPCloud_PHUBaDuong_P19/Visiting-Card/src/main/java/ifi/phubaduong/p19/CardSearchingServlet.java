package ifi.phubaduong.p19;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;

public class CardSearchingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public CardSearchingServlet() {
		super();
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String firstName = req.getParameter("first_name");
		String lastName = req.getParameter("last_name");
		String telephone = req.getParameter("telephone");
		String email = req.getParameter("email");

		Query<VisitingCard> querry = ObjectifyService.ofy().load().type(VisitingCard.class).filter("userName",
				((Account) req.getSession().getAttribute("current_account")).getUserName());
		if (firstName != null && !firstName.equals("")) {
			querry = querry.filter("firstName", firstName);
		}
		if (lastName != null && !lastName.equals("")) {
			querry = querry.filter("lastName", lastName);
		}
		if (telephone != null && !telephone.equals("")) {
			querry = querry.filter("telNumber", telephone);
		}
		if (email != null && !email.equals("")) {
			querry = querry.filter("email", email);
		}

		req.getSession().setAttribute("first_name", firstName == null ? "" : firstName);
		req.getSession().setAttribute("last_name", lastName == null ? "" : lastName);
		req.getSession().setAttribute("telephone", telephone == null ? "" : telephone);
		req.getSession().setAttribute("email", email == null ? "" : email);

		List<VisitingCard> searchResults = querry.list();
		req.getSession().setAttribute("visiting_cards", searchResults);
		resp.sendRedirect("card_list_searched.jsp");
	}
}
