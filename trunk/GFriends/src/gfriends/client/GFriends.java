package gfriends.client;

import gfriends.client.model.ContactItem;
import gfriends.client.model.GreetingItem;
import gfriends.client.model.LoginInfo;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DecoratedStackPanel;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.HorizontalSplitPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.VerticalSplitPanel;

public class GFriends implements EntryPoint {

  private static final int REFRESH_INTERVAL = 1000 * 5; // ms

  private static final int SCROLL_TITLE_INTERVAL = 300;

  private LoginInfo loginInfo = null;

  private HorizontalPanel loginInfoPanel = new HorizontalPanel();

  private List<ContactItem> contactItemList = new ArrayList<ContactItem>();

  private LoginServiceAsync loginService = GWT.create(LoginService.class);

  private GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

  private ContactsServiceAsync contactsService = GWT.create(ContactsService.class);

  private MessageAsyncCallback autoMessageAsyncCallback = null;

  private Timer scrollTitleTimmer = null;

  private int newMsgNum = 0;

  @Override
  public void onModuleLoad() {
    loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
      public void onFailure(Throwable error) {
        // TODO render error div
      }

      public void onSuccess(LoginInfo result) {
        loginInfo = result;
        if (loginInfo.isLoggedIn()) {
          loadLogout();
          if (loginInfo.isRegisted()) {
            loadConcasts();
          } else {
            // render register_content

            RootPanel.get("register_content").add(new HTML("<p><b>Please Fill Auto Register Form And Confirm The Response!</b></p>"));

            final TextBox nicknameTx = new TextBox();
            final TextBox emailTx = new TextBox();
            final Button registerBt = new Button("Register");

            // Create a table to layout the form options
            FlexTable layout = new FlexTable();
            layout.setCellSpacing(6);
            FlexCellFormatter cellFormatter = layout.getFlexCellFormatter();

            // Add a title to the form
            layout.setHTML(0, 0, "Auto Register");
            cellFormatter.setColSpan(0, 0, 2);
            cellFormatter.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);

            // Add some standard form options
            layout.setHTML(1, 0, "NickName");
            layout.setWidget(1, 1, nicknameTx);
            layout.setHTML(2, 0, "Email");
            layout.setWidget(2, 1, emailTx);

            layout.setHTML(2, 0, "Email");
            layout.setWidget(2, 1, emailTx);

            layout.setWidget(3, 0, registerBt);

            // Wrap the content in a DecoratorPanel
            final DecoratorPanel decPanel = new DecoratorPanel();
            decPanel.setWidget(layout);

            RootPanel.get("register_content").add(decPanel);

            final HTML hintMsg = new HTML();
            RootPanel.get("register_content").add(hintMsg);

            registerBt.addClickHandler(new ClickHandler() {
              @Override
              public void onClick(ClickEvent event) {

                if (nicknameTx.getValue() != null && nicknameTx.getValue().trim().length() > 0 && emailTx.getValue() != null
                    && emailTx.getValue().trim().length() > 0) {
                  
                  contactsService.register(nicknameTx.getValue(), emailTx.getValue(), new AsyncCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean result) {
                      // Window.Location.reload();
                      if (result) {
                        decPanel.setVisible(false);
                        hintMsg.setHTML("<p><b><font color=\"blue\">Register Done, But It's NOT FINISHED!!! <br/> Until you get a confirm mail from this site.</font></b></p>");
                      } else {
                        decPanel.setVisible(true);
                        hintMsg.setHTML("<p><b><font color=\"red\">The email is duplicated. check again please.</font></b></p>");
                      }
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                    }
                  });
                } else {
                  hintMsg.setHTML("<p><font color=\"red\">Input with errors.</font></p>");
                }
              }
            });
          }
        } else {
          loadLogin();
        }
      }
    });

  }

  private void loadLogin() {
    // Assemble login panel.
    Anchor loginLink = new Anchor("Sign In");
    loginLink.setHref(loginInfo.getLoginUrl());
    loginInfoPanel.add(new Label("Please sign in to your Google Account."));
    loginInfoPanel.add(loginLink);

    Button bt = new Button("removeAll");
    bt.addClickHandler(new ClickHandler() {

      @Override
      public void onClick(ClickEvent event) {
        contactsService.removeAllContact(new AsyncCallback<Void>() {

          @Override
          public void onSuccess(Void result) {
            Window.alert("done!!!");
          }

          @Override
          public void onFailure(Throwable caught) {
            // TODO Auto-generated method stub

          }
        });
      }
    });
    //
    // final TextBox nicknameTx = new TextBox();
    // final TextBox emailTx = new TextBox();
    // Button bt1 = new Button("register");
    // bt1.addClickHandler(new ClickHandler() {
    // @Override
    // public void onClick(ClickEvent event) {
    // contactsService.register(nicknameTx.getValue(), emailTx.getValue(), new
    // AsyncCallback<Void>() {
    // @Override
    // public void onSuccess(Void result) {
    // }
    //
    // @Override
    // public void onFailure(Throwable caught) {
    // }
    // });
    // }
    // });
    //
    // RootPanel.get("authInfo").add(nicknameTx);
    // RootPanel.get("authInfo").add(emailTx);
    // RootPanel.get("authInfo").add(bt1);
    // RootPanel.get("authInfo").add(bt);
    RootPanel.get("authInfo").add(loginInfoPanel);
  }

  private void loadLogout() {
    // Assemble logout panel.
    Anchor logoutLink = new Anchor("Sign Out");
    logoutLink.setHref(loginInfo.getLogoutUrl());
    loginInfoPanel.add(new Label(loginInfo.getEmailAddress()));
    loginInfoPanel.add(logoutLink);
    RootPanel.get("authInfo").add(loginInfoPanel);
  }

  private void loadConcasts() {
    // load contacts info
    contactsService.loadContacts(new AsyncCallback<List<ContactItem>>() {
      @Override
      public void onFailure(Throwable caught) {
      }

      @Override
      public void onSuccess(List<ContactItem> result) {
        contactItemList = result;
        loadGFriendsData();
      }
    });
  }

  private void loadGFriendsData() {

    // -----------------------left panel-------------------------
    Images images = (Images) GWT.create(Images.class);
    // Create a new stack panel
    DecoratedStackPanel stackPanel = new DecoratedStackPanel();
    stackPanel.setWidth("200px");
    stackPanel.setHeight("100%");
    String contactsHeader = getHeaderString("Contacts", images.contactsgroup());
    stackPanel.add(createContactsItem(images), contactsHeader, true);

    // Add a list of filters
    String filtersHeader = getHeaderString("Filters", images.filtersgroup());
    stackPanel.add(createFiltersItem(), filtersHeader, true);

    stackPanel.ensureDebugId("cwStackPanel");

    // -----------------------right panel-------------------------

    final FlexTable topFlexTable = new FlexTable();
    FlexCellFormatter cellFormatter = topFlexTable.getFlexCellFormatter();
    topFlexTable.setCellSpacing(3);
    topFlexTable.setCellPadding(3);
    cellFormatter.setColSpan(0, 0, 2);

    final RichTextArea area = new RichTextArea();
    area.ensureDebugId("cwRichText-area");
    area.setSize("750px", "100px");
    topFlexTable.setWidget(0, 0, area);

    /*
     * Image reloadImage = new Image("/images/refresh.png");
     * reloadImage.setWidth("24px"); reloadImage.setHeight("24px");
     * reloadImage.addStyleName("img_button"); topFlexTable.setWidget(1, 0,
     * reloadImage);
     */

    Button sendButton = new Button("SendMessage");
    cellFormatter.setHorizontalAlignment(1, 1, HasHorizontalAlignment.ALIGN_RIGHT);
    topFlexTable.setWidget(1, 1, sendButton);

    final FlexTable flexTable = new FlexTable();
    flexTable.addStyleName("cw-FlexTable");
    flexTable.setWidth("100%");
    flexTable.setCellSpacing(5);
    flexTable.setCellPadding(3);
    VerticalSplitPanel vSplit = new VerticalSplitPanel();
    vSplit.ensureDebugId("cwVerticalSplitPanel");
    vSplit.setSize("100%", "100%");
    vSplit.setSplitPosition("155px");
    vSplit.setTopWidget(topFlexTable);
    vSplit.setBottomWidget(flexTable);

    final MessageAsyncCallback messageAsyncCallback = new MessageAsyncCallback();
    messageAsyncCallback.setFlexTable(flexTable);
    greetingService.loadGreeting(messageAsyncCallback);

    sendButton.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {

        if (area.getText() == null || area.getText().trim().length() == 0) {
          Window.alert("no message need to send!!!");
        } else {
          greetingService.pushMessage(area.getText(), messageAsyncCallback);
          area.setText("");
        }
      }
    });

    /*
     * reloadImage.addClickHandler(new ClickHandler() {
     * 
     * @Override public void onClick(ClickEvent event) {
     * greetingService.loadGreeting(messageAsyncCallback); } });
     */

    // -------------------main split panel-----------------------
    HorizontalSplitPanel hSplit = new HorizontalSplitPanel();
    hSplit.ensureDebugId("cwHorizontalSplitPanel");

    hSplit.setSize("1000px", "700px");
    hSplit.setSplitPosition("20%");
    hSplit.setRightWidget(vSplit);
    hSplit.setLeftWidget(stackPanel);

    // Wrap the split panel in a decorator panel
    DecoratorPanel decPanel = new DecoratorPanel();
    decPanel.setWidget(hSplit);

    RootPanel.get("gfriends_content").add(decPanel);

    // -------------------auto reload---------------------------
    autoMessageAsyncCallback = new MessageAsyncCallback();
    autoMessageAsyncCallback.setFlexTable(flexTable);
    autoMessageAsyncCallback.setAutoRefresh(true);
    Timer refreshTimer = new Timer() {
      @Override
      public void run() {
        greetingService.loadGreeting(autoMessageAsyncCallback);
      }
    };
    refreshTimer.schedule(1000 * 10);
    refreshTimer.scheduleRepeating(REFRESH_INTERVAL);
  }

  class MessageAsyncCallback implements AsyncCallback<List<GreetingItem>> {

    private FlexTable flexTable;

    private boolean autoRefresh = false;

    @Override
    public void onFailure(Throwable caught) {
      // TODO render error div
    }

    @Override
    public void onSuccess(List<GreetingItem> result) {

      newMsgNum = 0;
      this.flexTable.removeAllRows();

      GreetingItem greetingItem = null;

      if (result.size() == 0) {
        this.flexTable.setHTML(0, 0, "<b><font color=\"red\">no messages!!!</font></b>");

      } else {

        if (result.size() > 0) {

          long lastTimestamp = 0;
          if (isLocalStorageSupported()) {
            if (isAutoRefresh()) {
              lastTimestamp = Long.valueOf(getAutoLastGreetingTime());
            } else {
              lastTimestamp = Long.valueOf(getLastGreetingTime());
              setAutoLastGreetingTime(String.valueOf(lastTimestamp));
            }
            setLastGreetingTime(Long.toString(result.get(0).getTimestamp()));
          }

          FlexCellFormatter cellFormatter = flexTable.getFlexCellFormatter();

          int skipCount = 0;
          for (int i = 0; i < result.size(); i++) {

            greetingItem = result.get(i);

            ContactItem currentContact = null;
            for (ContactItem contactItem : contactItemList) {
              if (contactItem.getEmail().equals(greetingItem.getEmail())) {
                currentContact = contactItem;
                break;
              }
            }

            if (currentContact.isEnable()) {
              cellFormatter.addStyleName(i - skipCount, 0, currentContact.getStyle() + "_bkcolor");
              StringBuilder sb = new StringBuilder();

              if (isLocalStorageSupported() && greetingItem.getTimestamp() > lastTimestamp) {
                sb.append("<img src='/images/new_icon.jpg' width='33px' Height='19px'/><br/>");
                newMsgNum++;
              }
              sb.append("[" + DateTimeFormat.getMediumDateTimeFormat().format(greetingItem.getDataTime()) + "]&nbsp;&nbsp;&nbsp;").append("<b>")
                  .append(currentContact.getNickName()).append(" wrote: </b>").append("<br/>").append("<blockquote><pre>")
                  .append(greetingItem.getContent()).append("</pre></blockquote>");
              this.flexTable.setHTML(i - skipCount, 0, sb.toString());

            } else {
              skipCount++;
            }
          }
        }
      }

      // // update title
      // if (newMsgNum > 0) {
      //
      // if (scrollTitleTimmer == null) {
      // scrollTitleTimmer = new Timer() {
      // @Override
      // public void run() {
      // String msg = String.valueOf(newMsgNum) + "通新メッセージが届きました！";
      // scrolleTitle(msg, " " + msg);
      // }
      // };
      // }
      //
      // scrollTitleTimmer.scheduleRepeating(SCROLL_TITLE_INTERVAL);
      //
      // } else {
      // if (scrollTitleTimmer != null) {
      // scrollTitleTimmer.cancel();
      // }
      // }

    }

    public void setFlexTable(FlexTable flexTable) {
      this.flexTable = flexTable;
    }

    public FlexTable getFlexTable() {
      return flexTable;
    }

    public void setAutoRefresh(boolean autoRefresh) {
      this.autoRefresh = autoRefresh;
    }

    public boolean isAutoRefresh() {
      return autoRefresh;
    }
  }

  public interface Images extends Tree.Resources {
    ImageResource contactsgroup();

    ImageResource defaultContact();

    ImageResource filtersgroup();

    @Source("noimage.png")
    ImageResource treeLeaf();
  }

  private VerticalPanel createFiltersItem() {
    VerticalPanel filtersPanel = new VerticalPanel();
    filtersPanel.setSpacing(4);

    ClickHandler handler = new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        CheckBox cb = (CheckBox) event.getSource();
        for (ContactItem contactItem : contactItemList) {
          if (contactItem.getEmail().equals(cb.getFormValue())) {
            contactItem.setEnable(cb.getValue());
            greetingService.loadGreeting(autoMessageAsyncCallback);
            break;
          }
        }
      }
    };

    for (ContactItem contactItem : this.contactItemList) {
      CheckBox cb = new CheckBox(contactItem.getNickName());
      cb.setFormValue(contactItem.getEmail());
      contactItem.setEnable(true);
      cb.setValue(contactItem.isEnable());
      cb.addClickHandler(handler);
      filtersPanel.add(cb);
    }
    return filtersPanel;
  }

  private FlexTable createContactsItem(Images images) {

    // Create a popup to show the contact info when a contact is clicked
    HorizontalPanel contactPopupContainer = new HorizontalPanel();
    contactPopupContainer.setSpacing(5);
    contactPopupContainer.add(new Image(images.defaultContact()));
    final HTML contactInfo = new HTML();
    contactPopupContainer.add(contactInfo);
    final PopupPanel contactPopup = new PopupPanel(true, false);
    contactPopup.setWidget(contactPopupContainer);

    final FlexTable flexTable = new FlexTable();
    FlexCellFormatter cellFormatter = flexTable.getFlexCellFormatter();
    flexTable.setWidth("100%");
    flexTable.setCellSpacing(2);
    flexTable.setCellPadding(2);

    for (int i = 0; i < this.contactItemList.size(); i++) {
      ContactItem contactItem = this.contactItemList.get(i);
      final String contactName = contactItem.getNickName();
      final String contactEmail = contactItem.getEmail();
      final HTML contactLink = new HTML("<a href=\"javascript:undefined;\">" + contactName + "</a>");
      cellFormatter.addStyleName(i, 0, contactItem.getStyle() + "_bkcolor");
      flexTable.setWidget(i, 0, contactLink);

      // Open the contact info popup when the user clicks a contact
      contactLink.addClickHandler(new ClickHandler() {
        public void onClick(ClickEvent event) {
          // Set the info about the contact
          contactInfo.setHTML(contactName + "<br><i>" + contactEmail + "</i>");
          // Show the popup of contact info
          int left = contactLink.getAbsoluteLeft() + 14;
          int top = contactLink.getAbsoluteTop() + 14;
          contactPopup.setPopupPosition(left, top);
          contactPopup.show();
        }
      });
    }

    return flexTable;
  }

  private String getHeaderString(String text, ImageResource image) {
    // Add the image and text to a horizontal panel
    HorizontalPanel hPanel = new HorizontalPanel();
    hPanel.setSpacing(0);
    hPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
    hPanel.add(new Image(image));
    HTML headerText = new HTML(text);
    headerText.setStyleName("cw-StackPanelHeader");
    hPanel.add(headerText);

    return hPanel.getElement().getString();
  }

  // ------------------- native code ----------------------------//
  public static native boolean isLocalStorageSupported() /*-{
    if ($wnd.localStorage) {
    return true;
    }else{
    return false;
    }
  }-*/;

  public static native String getLastGreetingTime() /*-{
    if($wnd.localStorage.lastGreetingTime){
    return $wnd.localStorage.lastGreetingTime;
    }else{
    return "0";
    }
  }-*/;

  public static native void setLastGreetingTime(String lastGreetingTime) /*-{
    $wnd.localStorage.lastGreetingTime = lastGreetingTime;
  }-*/;

  public static native String getAutoLastGreetingTime() /*-{
                                                        if($wnd.localStorage.autoLastGreetingTime){
                                                        return $wnd.localStorage.autoLastGreetingTime;
                                                        }else{
                                                        return "0";
                                                        }
                                                        }-*/;

  public static native void setAutoLastGreetingTime(String autoLastGreetingTime) /*-{
                                                                                 $wnd.localStorage.autoLastGreetingTime = autoLastGreetingTime;
                                                                                 }-*/;

  public static native void scrolleTitle(String msg, String msgud) /*-{
                                                                   if (msgud.length < msg.length) msgud += " - " + msg;
                                                                   msgud = msgud.substring(1, msgud.length);
                                                                   document.title = msgud.substring(0, msg.length);
                                                                   }-*/;
}
