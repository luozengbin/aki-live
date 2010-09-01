package gfriends.server;

import gfriends.server.model.Greeting;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetBadgeTextService extends javax.servlet.http.HttpServlet {

  private static final long serialVersionUID = 4183788899241319346L;

  public void doPost(HttpServletRequest resuest, HttpServletResponse response) throws IOException {
    doGet(resuest, response);
  }

  public void doGet(HttpServletRequest resuest, HttpServletResponse response) throws IOException {

    response.setContentType("text/javascript");

    String lastCheckout = resuest.getParameter("lastCheckout");

    Date lastCheckoutDt = null;

    if (lastCheckout != null && lastCheckout.trim().length() > 0) {
      Calendar calendar = Calendar.getInstance();
      calendar.setTimeInMillis(Long.valueOf(lastCheckout));
      lastCheckoutDt = calendar.getTime();
    } else {
      lastCheckoutDt = new Date(0);
    }
    
    PersistenceManager pm = null;
    try {
      pm = PMF.get().getPersistenceManager();

      String query = "select from " + Greeting.class.getName() + " where date > :lastCheckoutDt order by date desc";

      @SuppressWarnings("unchecked")
      List<Greeting> greetings = (List<Greeting>) pm.newQuery(query).execute(lastCheckoutDt);

      if (greetings.size() > 0) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"size\":").append(greetings.size()).append(", \"newLastCheckout\":").append(greetings.get(0).getDate().getTime()).append("}");

        response.getOutputStream().print(sb.toString());
      } else {
        response.getOutputStream().print("{\"size\":0, \"newLastCheckout\":" + lastCheckout + "}");
      }

    } finally {
      pm.close();
    }

  }
}
