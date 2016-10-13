package ifi.phubaduong.p19;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Result;

public class VisitingCardCreationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public VisitingCardCreationServlet() {
		super();
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		VisitingCard newVisitingCard = new VisitingCard();
		Account currentAccount = (Account) req.getSession().getAttribute("current_account");
		if (currentAccount == null) {
			resp.getWriter().print("Vous pouvez faire login avant de faire creer une carte de visite!");
			return;
		}
		newVisitingCard.setUserName(currentAccount.getUserName());
		newVisitingCard.setFirstName(req.getParameter("first_name"));
		newVisitingCard.setLastName(req.getParameter("last_name"));
		newVisitingCard.setTelNumber(req.getParameter("telephone"));
		newVisitingCard.setEmail(req.getParameter("email"));
		newVisitingCard.setOrganisation(req.getParameter("organisation"));
		newVisitingCard.setPosition(req.getParameter("position"));
		newVisitingCard.setCreatedDate(new Date());
		
		Result<Key<VisitingCard>> result = ObjectifyService.ofy().save().entity(newVisitingCard);
		result.now();
//		resp.getWriter().print(newVisitingCard.getId());
//		resp.getWriter().print(newVisitingCard.getUserName());
		resp.sendRedirect("../home.jsp");
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
