package kirin.client;

import java.util.List;

import kirin.client.model.AlbumModel;
import kirin.client.model.LoginInfo;
import kirin.client.model.PhotoModel;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
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
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Kirin implements EntryPoint {

	private LoginInfo loginInfo = null;

	private HorizontalPanel loginInfoPanel = new HorizontalPanel();

	private List<AlbumModel> albumList = null;

	private AlbumModel currentAlbum = null;

	private KirinServiceAsync kirinService = GWT.create(KirinService.class);

	LoginServiceAsync loginService = GWT.create(LoginService.class);

	public void onModuleLoad() {
		// Check login status using login service.
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
		kirinService.loadAlbum(loginInfo, new AsyncCallback<List<AlbumModel>>() {
			public void onFailure(Throwable error) {
				// TODO render error div
			}

			public void onSuccess(List<AlbumModel> result) {
				albumList = result;
				initTabs();
			}
		});
	}

	private void initTabs() {

		// Create a tab panel
		final DecoratedTabPanel tabPanel = new DecoratedTabPanel();
		tabPanel.setAnimationEnabled(true);

		final FlexTable o_layout = new FlexTable();
		o_layout.setCellSpacing(6);
		FlexCellFormatter o_cellFormatter = o_layout.getFlexCellFormatter();
		// o_layout.setHTML(0, 0, "更新日付：" + album.getUpdate());
		o_cellFormatter.setColSpan(0, 0, 5);

		tabPanel.add(o_layout, loginInfo.getNickname());

		final ListBox dropBox = new ListBox(false);

		for (AlbumModel album : albumList) {
			dropBox.addItem(album.getName());
		}

		o_layout.setWidget(0, 0, dropBox);

		dropBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				initPhotoLayout(dropBox.getSelectedIndex(), o_layout);
			}
		});

		tabPanel.selectTab(0);

		dropBox.setItemSelected(0, true);
		
		initPhotoLayout(0, o_layout);

		dropBox.setSelectedIndex(0);

		RootPanel.get("kirin_tabs").add(tabPanel);
	}

	private void initPhotoLayout(int selectedIdx, final FlexTable o_layout) {
		for (int i = 1; i < o_layout.getRowCount(); i++) {
			o_layout.removeRow(i);
		}

		currentAlbum = albumList.get(selectedIdx);

		if (currentAlbum.getPhotos() == null || currentAlbum.getPhotos().size() == 0) {

			kirinService.loadPhoto(loginInfo, currentAlbum.getAlbumid(), new AsyncCallback<List<PhotoModel>>() {
				@Override
				public void onSuccess(List<PhotoModel> result) {

					int i = 1, j = 0;
					for (PhotoModel photoModel : result) {
						FlexTable i_layout = new FlexTable();
						i_layout.setCellSpacing(6);
						FlexCellFormatter i_cellFormatter = i_layout.getFlexCellFormatter();
						i_layout.setHTML(0, 0, photoModel.getTitle());

						// Create a popup to show the full size image
						final Image fullImage = new Image(photoModel.getURL());
						final PopupPanel imagePopup = new PopupPanel(true);
						imagePopup.setAnimationEnabled(true);
						imagePopup.setWidget(fullImage);

						final Element fullImageElement = fullImage.getElement();

						fullImage.addClickHandler(new ClickHandler() {
							public void onClick(ClickEvent event) {
								imagePopup.hide();
							}
						});

						fullImage.addMouseDownHandler(new MouseDownHandler() {
							@Override
							public void onMouseDown(MouseDownEvent event) {
								int x_pos = event.getRelativeX(fullImageElement);
								int imageWidth = fullImageElement.getClientWidth();
								if (x_pos <= (imageWidth / 3)) {
									// TODO change to next image
								} else if (x_pos >= (imageWidth / 3) * 2) {
									// TODO change to pre image
								}
							}
						});

						fullImage.addMouseMoveHandler(new MouseMoveHandler() {
							public void onMouseMove(MouseMoveEvent event) {
								int x_pos = event.getRelativeX(fullImageElement);
								int imageWidth = fullImageElement.getClientWidth();
								if (x_pos > (imageWidth / 2)) {
									fullImageElement.getStyle().setCursor(com.google.gwt.dom.client.Style.Cursor.MOVE);
								} else {
									fullImageElement.getStyle().setCursor(com.google.gwt.dom.client.Style.Cursor.E_RESIZE);
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
				}

				@Override
				public void onFailure(Throwable caught) {
					// TODO render error div
				}
			});
		}
	}
}
