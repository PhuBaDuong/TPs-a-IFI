package ifi.phubaduong.p19;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.googlecode.objectify.ObjectifyService;

public class CardDeletion extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public CardDeletion() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Long cardId = Long.parseLong(req.getParameter("card_id"));
		ObjectifyService.ofy().delete().type(VisitingCard.class).id(cardId);
		resp.sendRedirect("card_list.jsp");
	}
}
