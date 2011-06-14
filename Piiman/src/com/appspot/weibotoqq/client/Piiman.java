package com.appspot.weibotoqq.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Piiman implements EntryPoint {
  /**
   * Create a remote service proxy to talk to the server-side Auth service.
   */
  private final AuthServiceAsync authService = GWT.create(AuthService.class);

  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {

    final Button sendButton = new Button("Add New User");

    sendButton.addClickHandler(new ClickHandler() {

      public void onClick(ClickEvent event) {

        authService.requestToken(new AsyncCallback<String>() {

          @Override
          public void onSuccess(String requestTokenUrl) {

            Window.alert(requestTokenUrl);

            Window.Location.replace(requestTokenUrl);
          }

          @Override
          public void onFailure(Throwable caught) {
            // TODO 詳細な例外処理
            Window.alert(caught.getMessage());
          }
        });
      }
    });

    // Add the nameField and sendButton to the RootPanel
    // Use RootPanel.get() to get the entire body element
    RootPanel rootPanel = RootPanel.get("main_content");
    rootPanel.add(sendButton);

    String oauth_token = Window.Location.getParameter("oauth_token");
    String oauth_verifier = Window.Location.getParameter("oauth_verifier");

    if (isNotBlank(oauth_token) && isNotBlank(oauth_verifier)) {

      authService.exchangeToken(oauth_token, oauth_verifier, new AsyncCallback<String>() {

        @Override
        public void onSuccess(String result) {
          Window.alert(result);
        }

        @Override
        public void onFailure(Throwable caught) {
          // TODO 詳細な例外処理
          Window.alert(caught.getMessage());
        }
      });
    }
  }

  private boolean isNotBlank(String str) {

    return (str != null && str.trim().length() > 0) ? true : false;

  }
}
