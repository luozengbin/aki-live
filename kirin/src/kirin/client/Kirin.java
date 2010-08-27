package kirin.client;


import kirin.client.model.LoginInfo;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Kirin implements EntryPoint {

	private LoginInfo loginInfo = null;
	
	private String kirinData = null;
	
	private HorizontalPanel loginInfoPanel = new HorizontalPanel();

	public void onModuleLoad() {
		// Check login status using login service.
		LoginServiceAsync loginService = GWT.create(LoginService.class);
		loginService.login(GWT.getHostPageBaseURL(),
				new AsyncCallback<LoginInfo>() {
					public void onFailure(Throwable error) {
					}

					public void onSuccess(LoginInfo result) {
						loginInfo = result;
						if (loginInfo.isLoggedIn()) {
							loadLogout();
							loadKirinData();
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

	public void loadKirinData() {

		// Create a tab panel
		DecoratedTabPanel tabPanel = new DecoratedTabPanel();
		tabPanel.setWidth("400px");
		tabPanel.setAnimationEnabled(true);

		KirinServiceAsync kirinService = GWT.create(KirinService.class);
		
		kirinService.loadData(loginInfo, new AsyncCallback<String>() {
					public void onFailure(Throwable error) {
						kirinData = error.getMessage();
					}
					public void onSuccess(String result) {
						kirinData = result;
					}
				});
		
		// Add a home tab
		String[] tabTitles = { "Hello", "GWT Logo", "More Info" };
		HTML homeText = new HTML(kirinData);
		tabPanel.add(homeText, tabTitles[0]);

		// Add a tab with an image
		VerticalPanel vPanel = new VerticalPanel();
		vPanel.add(new HTML("image logo"));
		tabPanel.add(vPanel, tabTitles[1]);

		// Add a tab
		HTML moreInfo = new HTML("Tabs are highly customizable using CSS.");
		tabPanel.add(moreInfo, tabTitles[2]);

		// Return the content
		tabPanel.selectTab(0);
		// tabPanel.ensureDebugId("cwTabPanel");

		RootPanel.get("kirin_tabs").add(tabPanel);
	}
}
