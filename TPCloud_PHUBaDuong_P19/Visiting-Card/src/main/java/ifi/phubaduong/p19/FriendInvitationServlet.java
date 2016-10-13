package ifi.phubaduong.p19;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.googlecode.objectify.ObjectifyService;

public class FriendInvitationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public FriendInvitationServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		MakingFriend newMakingFriend = new MakingFriend(
				req.getParameter("user_name"), req.getParameter("friend_name"),
				"0");
		newMakingFriend.setCreatedDate(new Date());
		ObjectifyService.ofy().save().entity(newMakingFriend).now();
		resp.sendRedirect("friend_search.jsp");
	}

}
