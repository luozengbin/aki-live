package com.appspot.piment.client;

import com.appspot.piment.client.rpc.SinaAuthService;
import com.appspot.piment.client.rpc.SinaAuthServiceAsync;
import com.appspot.piment.client.rpc.TQQAuthService;
import com.appspot.piment.client.rpc.TQQAuthServiceAsync;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.IFrameElement;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HTML;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.CloseClientEvent;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Piment implements EntryPoint {
  /**
   * Create a remote service proxy to talk to the server-side Auth service.
   */
  private final TQQAuthServiceAsync qqAuthService = GWT.create(TQQAuthService.class);

  private final SinaAuthServiceAsync sinaAuthService = GWT.create(SinaAuthService.class);

  private final Frame frame = new Frame();

  private enum ActionMode {
	Sina, Tqq
  }

  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {

	IButton btnAddSinaUser = new IButton("添加新浪微博帐号");
	btnAddSinaUser.setShowRollOver(true);
	btnAddSinaUser.setShowDown(true);
	btnAddSinaUser.addClickHandler(new AddUserClickHandler(ActionMode.Sina));

	IButton btnAddTqqUser = new IButton("添加腾讯微博帐号");
	btnAddTqqUser.setShowRollOver(true);
	btnAddTqqUser.setShowDown(true);
	btnAddTqqUser.addClickHandler(new AddUserClickHandler(ActionMode.Tqq));

	VLayout layoutMain = new VLayout();
	layoutMain.addMember(btnAddSinaUser);
	layoutMain.addMember(btnAddTqqUser);

	layoutMain.draw();
  }

  class AddUserClickHandler implements ClickHandler {

	private ActionMode mode;

	public AddUserClickHandler(ActionMode mode) {
	  super();
	  this.mode = mode;
	}

	public void onClick(ClickEvent event) {

	  final Window winModal = new Window();
	  winModal.setWidth(800);
	  winModal.setHeight(600);
	  winModal.setTitle("帐号授权");
	  winModal.setShowMinimizeButton(false);
	  winModal.setIsModal(true);
	  winModal.setShowModalMask(true);
	  winModal.centerInPage();
	  winModal.addCloseClickHandler(new CloseClickHandler() {
		public void onCloseClick(CloseClientEvent event) {
		  winModal.destroy();
		}
	  });

	  final HTMLPane londingContent = new HTMLPane();
	  londingContent.setContents("<h3>正在处理中，请稍等片刻。</h3>");

	  final HTMLPane htmlPane = new HTMLPane();
	  htmlPane.setShowEdges(true);
	  // htmlPane.setContentsURL("http://www.google.com/");
	  // htmlPane.setContentsType(ContentsType.PAGE);
	  htmlPane.setWidth100();
	  htmlPane.setHeight100();

	  // winModal.addItem(londingContent);
	  // winModal.addItem(htmlPane);
	  winModal.show();

	  AsyncCallback<String> requestTokenCallback = new AsyncCallback<String>() {
		@Override
		public void onSuccess(String requestTokenUrl) {

		  // /htmlPane.setContentsURL(requestTokenUrl);

		  /* FANGFA1 */
		  RequestBuilder rb = new RequestBuilder(RequestBuilder.GET, requestTokenUrl);

		  rb.setCallback(new RequestCallback() {

			@Override
			public void onResponseReceived(Request request, Response response) {
			  htmlPane.setContents(response.getText());
			  winModal.addItem(htmlPane);
			  winModal.draw();
			}

			@Override
			public void onError(Request request, Throwable exception) {
			  // TODO Auto-generated method stub

			}
		  });

		  try {
			rb.send();
		  } catch (RequestException e) {
			GWT.log("error " + e);
		  }

		  /* FANGFA2 */
		  // frame.setUrl(requestTokenUrl);
		  //
		  // frame.addLoadHandler(new LoadHandler() {
		  //
		  // @Override
		  // public void onLoad(LoadEvent event) {
		  // }
		  // });
		  //
		  // frame.setWidth("100%");
		  // frame.setHeight("100%");
		  // // setScrolling
		  // IFrameElement frameElement = IFrameElement.as(frame.getElement());
		  // frameElement.setScrolling("no");
		  // frameElement.setName("_self");
		  // londingContent.destroy();
		  // winModal.addItem(frame);
		  //
		  // Document frameDocument = getIFrameDocument(frameElement);
		  // if (frameDocument != null) {
		  // showMessage("aaa");
		  // }
		}

		@Override
		public void onFailure(Throwable caught) {
		  HTML errorContent = new HTML("<h3><font color='red'>" + caught.getMessage() + "</font></h3>");
		  winModal.addItem(errorContent);
		}
	  };

	  switch (mode) {
	  case Sina:
		sinaAuthService.requestToken(requestTokenCallback);
		break;
	  case Tqq:
		qqAuthService.requestToken(requestTokenCallback);
		break;
	  default:
		break;
	  }
	}
  }

  private native Document getIFrameDocument(IFrameElement iframe)/*-{
		return iframe.contentDocument;
  }-*/;

  public native void showMessage(String msg) /*-{
		alert(msg);
  }-*/;

  public static native void popup(String url, String windowname, String width, String height) /*-{
		var features = "location=no, menubar=no, status=yes, scrollbars=yes, resizable=yes, toolbar=no";
		if (width) {
			if (window.screen.width > width)
				features += ", left=" + (window.screen.width - width) / 2;
			else
				width = window.screen.width;
			features += ", width=" + width;
		}
		if (height) {
			if (window.screen.height > height)
				features += ", top=" + (window.screen.height - height) / 2;
			else
				height = window.screen.height;
			features += ", height=" + height;
		}
		window.open(url, windowname, features);
  }-*/;
}
