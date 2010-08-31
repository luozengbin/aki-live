package gfriends.server;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetBadgeTextService extends javax.servlet.http.HttpServlet {

	private static final long serialVersionUID = 4183788899241319346L;

	public void doGet(HttpServletRequest resuest, HttpServletResponse response) {

		String param = resuest.getParameter("lastCheckout");

		if (param != null && param.trim().length() > 0) {

			long lastCheckout = Long.parseLong(param);
			
			
		}

	}
}
