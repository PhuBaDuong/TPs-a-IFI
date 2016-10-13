package ifi.phubaduong.p19;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.googlecode.objectify.ObjectifyService;

public class FriendAcceptionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public FriendAcceptionServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Long id = Long.parseLong(req.getParameter("making_friend_id"));
		MakingFriend makingFriend = ObjectifyService.ofy().load().type(MakingFriend.class).id(id).now();
		makingFriend.setStatus("1");
		ObjectifyService.ofy().save().entity(makingFriend);
		resp.sendRedirect("list_friend_sending_request.jsp");
	}

}
