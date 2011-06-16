package com.appspot.piment.jobs;

import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.appspot.piment.Constants;

public class Job001 extends HttpServlet {

  private static final long serialVersionUID = 1288120185298127312L;
  
  private static final Logger log = Logger.getLogger(Constants.FQCN + Job001.class.getName());

  public void doGet(HttpServletRequest req, HttpServletResponse resp){
    
    log.info("-- job001 start --");
    
    for (int i = 0; i < 10; i++) {
      log.info("job001 processing ... " + i + "%");  
    }
    
    log.info("-- job001 end --");
    
  }
}
