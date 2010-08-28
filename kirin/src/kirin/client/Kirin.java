package kirin.client;

import java.util.List;

import kirin.client.model.AlbumModel;
import kirin.client.model.LoginInfo;
import kirin.client.model.PhotoModel;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Kirin implements EntryPoint {

	private LoginInfo loginInfo = null;

	private HorizontalPanel loginInfoPanel = new HorizontalPanel();

	public void onModuleLoad() {
		// Check login status using login service.
		LoginServiceAsync loginService = GWT.create(LoginService.class);
		loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
			public void onFailure(Throwable error) {
				// TODO render error div
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

	private void loadKirinData() {
		KirinServiceAsync kirinService = GWT.create(KirinService.class);
		kirinService.loadData(loginInfo, new AsyncCallback<List<AlbumModel>>() {
			public void onFailure(Throwable error) {
				// TODO render error div
			}

			public void onSuccess(List<AlbumModel> result) {
				aaa(result);
			}
		});
	}

	private void aaa(List<AlbumModel> kirinData) {
		
		final TextBox tx = new TextBox();
		
		// Create a tab panel
		DecoratedTabPanel tabPanel = new DecoratedTabPanel();
		tabPanel.setAnimationEnabled(true);
		for (AlbumModel album : kirinData) {
			// HTML topText = new HTML("更新日付：" + album.getUpdate());

			FlexTable o_layout = new FlexTable();
			o_layout.setCellSpacing(6);
			FlexCellFormatter o_cellFormatter = o_layout.getFlexCellFormatter();
			o_layout.setHTML(0, 0, album.getName());
			o_cellFormatter.setColSpan(0, 0, 5);

			int i = 1, j = 0;
			for (PhotoModel photoModel : album.getPhotos()) {
				FlexTable i_layout = new FlexTable();
				i_layout.setCellSpacing(6);
				FlexCellFormatter i_cellFormatter = i_layout.getFlexCellFormatter();
				i_layout.setHTML(0, 0, photoModel.getTitle());

				// Create a popup to show the full size image
				Image fullImage = new Image(photoModel.getURL());
				final PopupPanel imagePopup = new PopupPanel(true);
				imagePopup.setAnimationEnabled(true);
				imagePopup.setWidget(fullImage);
				
				final Element fullImageElement = fullImage.getElement();
				final int imageWidth = fullImageElement.getClientWidth();
				fullImage.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						imagePopup.hide();
					}
				});
				
				fullImage.addMouseDownHandler(new MouseDownHandler() {
					@Override
					public void onMouseDown(MouseDownEvent event) {
						int x_pos = event.getRelativeX(fullImageElement);
						
						if(x_pos <= (imageWidth/3)){
							tx.setValue("left");
						}else if(x_pos >= (imageWidth/3) * 2){
							tx.setValue("right");
						}
					}
				});
				
				fullImage.addMouseMoveHandler(new MouseMoveHandler() {
					public void onMouseMove(MouseMoveEvent event) {
						int x_pos = event.getRelativeX(fullImageElement);
						if(x_pos > (imageWidth/2)){
							//fullImageElement.removeClassName("leftCursor");
							fullImageElement.addClassName("rightCursor");
							
						}else{
							//fullImageElement.removeClassName("rightCursor");
							fullImageElement.addClassName("leftCursor");
						}
					}
				});

				Image thumbImage = new Image(photoModel.getThumbURL());

				thumbImage.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						imagePopup.center();
					}
				});

				i_cellFormatter.setColSpan(0, 0, 1);
				i_cellFormatter.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
				i_layout.setWidget(1, 0, thumbImage);

				DecoratorPanel decPanel = new DecoratorPanel();
				decPanel.setWidget(i_layout);

				if (j >= 5) {
					j = 0;
					i++;
				}
				o_layout.setWidget(i, j++, decPanel);
			}
			tabPanel.add(o_layout, album.getName());
		}
		tabPanel.selectTab(0);
		RootPanel.get("kirin_tabs").add(tx);
		RootPanel.get("kirin_tabs").add(tabPanel);
	}
}
