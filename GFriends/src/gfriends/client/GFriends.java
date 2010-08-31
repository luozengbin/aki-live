package gfriends.client;

import java.util.List;

import gfriends.client.model.GreetingItem;
import gfriends.client.model.LoginInfo;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class GFriends implements EntryPoint {

	private LoginInfo loginInfo = null;

	private HorizontalPanel loginInfoPanel = new HorizontalPanel();

	LoginServiceAsync loginService = GWT.create(LoginService.class);

	GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

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
					loadGFriendsData();
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

	private void loadGFriendsData() {

		final RichTextArea area = new RichTextArea();
		area.ensureDebugId("cwRichText-area");
		area.setSize("40em", "6em");

		// Add the components to a panel
		Grid t_grid = new Grid(2, 1);
		t_grid.setStyleName("cw-RichText");
		t_grid.setWidget(0, 0, area);

		Button sendButton = new Button("SendMessage");
		t_grid.setWidget(1, 0, sendButton);
		DecoratorPanel t_decPanel = new DecoratorPanel();
		t_decPanel.setWidget(t_grid);

		final FlexTable flexTable = new FlexTable();
		
		flexTable.addStyleName("cw-FlexTable");
		flexTable.setWidth("800px");
		flexTable.setCellSpacing(5);
		flexTable.setCellPadding(3);

		DecoratorPanel b_decPanel = new DecoratorPanel();
		b_decPanel.setWidget(flexTable);

		sendButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				if (area.getText() == null || area.getText().trim().length() == 0) {
					Window.alert("no message need to send!!!");
				} else {
					greetingService.pushMessage(area.getText(), new AsyncCallback<List<GreetingItem>>() {

						@Override
						public void onFailure(Throwable caught) {
							// TODO render error div
						}

						@Override
						public void onSuccess(List<GreetingItem> result) {
							flexTable.clear(true);

							GreetingItem greetingItem = null;
							for (int i = 0; i < result.size(); i++) {
								greetingItem = result.get(i);

								String innerHtml = "<p><b>"
										+ (greetingItem.getNickName() == null ? "An anonymous person " : greetingItem.getNickName())
										+ " wrote: </b>(" + greetingItem.getDataTime() + ")</p>" + "<blockquote>" + greetingItem.getContent() + "</blockquote>";
								
								flexTable.setHTML(i, 0, innerHtml);
							}
						}
					});
				}
			}
		});

		VerticalPanel vPanel = new VerticalPanel();
		vPanel.setSpacing(5);

		vPanel.add(t_decPanel);
		vPanel.add(b_decPanel);

		RootPanel.get("gfriends_content").add(vPanel);
	}

}
