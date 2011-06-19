package com.appspot.piment.client;

import com.appspot.piment.client.rpc.TQQAuthService;
import com.appspot.piment.client.rpc.TQQAuthServiceAsync;
import com.appspot.piment.client.rpc.TQQWeiboService;
import com.appspot.piment.client.rpc.TQQWeiboServiceAsync;
import com.appspot.piment.shared.StringUtils;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Piment implements EntryPoint {
  /**
   * Create a remote service proxy to talk to the server-side Auth service.
   */
  private final TQQAuthServiceAsync qqAuthService = GWT.create(TQQAuthService.class);

  private final TQQWeiboServiceAsync qqWeiboService = GWT.create(TQQWeiboService.class);

  TextBox msgBox = new TextBox();

  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {

    com.google.gwt.core.client.GWT.log("loading!!!");

    final Button sendButton = new Button("Add New User");

    sendButton.addClickHandler(new ClickHandler() {

      public void onClick(ClickEvent event) {

        qqAuthService.requestToken(new AsyncCallback<String>() {

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

    msgBox.setName("piment_msg_text");
    rootPanel.add(msgBox);
    msgBox.setSize("313px", "99px");

    Button btnNewButton = new Button("Send Test Message");
    btnNewButton.setSize("128px", "30px");

    btnNewButton.addClickHandler(new ClickHandler() {

      public void onClick(ClickEvent event) {

        qqWeiboService.sendMessage(msgBox.getText(), new AsyncCallback<Void>() {

          @Override
          public void onSuccess(Void result) {
            Window.alert("scussed!!!");
          }

          @Override
          public void onFailure(Throwable caught) {
            Window.alert("failed!!!");
          }
        });
      }
    });
    rootPanel.add(btnNewButton);

    Button ftechButton = new Button("FetchMessage");
    ftechButton.addClickHandler(new ClickHandler() {

      public void onClick(ClickEvent event) {

        qqWeiboService.fetchMessage(msgBox.getText(), new AsyncCallback<String>() {

          @Override
          public void onSuccess(String result) {
            msgBox.setText(result);
          }

          @Override
          public void onFailure(Throwable caught) {
            msgBox.setText(caught.getMessage());
          }
        });
      }
    });

    rootPanel.add(ftechButton);

    // AccessToken取得
    String oauth_token = Window.Location.getParameter("oauth_token");
    String oauth_verifier = Window.Location.getParameter("oauth_verifier");

    if (StringUtils.isNotBlank(oauth_token) && StringUtils.isNotBlank(oauth_verifier)) {

      qqAuthService.exchangeToken(oauth_token, oauth_verifier, new AsyncCallback<String>() {

        @Override
        public void onSuccess(String result) {
          Window.alert(result);

          Window.alert(Window.Location.getPath());

          Window.Location.replace(Window.Location.getPath());
        }

        @Override
        public void onFailure(Throwable caught) {
          // TODO 詳細な例外処理
          Window.alert(caught.getMessage());
        }
      });
    }
  }
}
