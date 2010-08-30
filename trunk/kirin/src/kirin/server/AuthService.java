package kirin.server;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gdata.client.http.AuthSubUtil;

public class AuthService extends javax.servlet.http.HttpServlet {

	private static final long serialVersionUID = -3881623433384087916L;

	public void doGet(HttpServletRequest resuest, HttpServletResponse response) {

		try {

			HttpSession session = resuest.getSession();

			if (session.getAttribute("sessionToken") == null) {

				boolean isReply = false;

				if (resuest.getQueryString() != null) {

					String onetimeUseToken = AuthSubUtil.getTokenFromReply(resuest.getQueryString());

					if (onetimeUseToken != null && onetimeUseToken.trim().length() > 0) {

						isReply = true;
						String sessionToken = AuthSubUtil.exchangeForSessionToken(onetimeUseToken, null);

						session.setAttribute("sessionToken", sessionToken);
						
						response.sendRedirect("/Kirin.html");
					}
				}

				if (!isReply) {
					String loginUrl = AuthSubUtil.getRequestUrl(resuest.getRequestURL().toString(), "http://picasaweb.google.com/data/ http://www.google.com/m8/feeds/", false, true);
					response.sendRedirect(loginUrl);
				}

			} else {
				response.sendRedirect("/Kirin.html");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
