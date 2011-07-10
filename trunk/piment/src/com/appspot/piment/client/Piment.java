package com.appspot.piment.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Anchor;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Piment implements EntryPoint {

  private final VLayout layoutMain = new VLayout();

  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {

	Anchor addSinaUser = new Anchor("添加新浪微博帐号", "javascript:void(0)");
	addSinaUser.addClickHandler(new com.google.gwt.event.dom.client.ClickHandler() {

	  @Override
	  public void onClick(ClickEvent event) {
		popup("/user/auth/requestToken?weibo_source=Sina", null, 850, 650);
	  }
	});

	Anchor addTqqUser = new Anchor("添加腾讯微博帐号", "javascript:void(0)");
	addTqqUser.addClickHandler(new com.google.gwt.event.dom.client.ClickHandler() {

	  @Override
	  public void onClick(ClickEvent event) {
		popup("/user/auth/requestToken?weibo_source=Tqq", null, 850, 650);
	  }
	});

	layoutMain.addMember(addSinaUser);
	layoutMain.addMember(addTqqUser);

	layoutMain.draw();
  }

  public native void showMessage(String msg) /*-{
		alert(msg);
  }-*/;

  public static native void popup(String url, String windownName, int width, int height) /*-{
		window
				.open(
						url,
						windownName,
						"width="
								+ width
								+ ",height="
								+ height
								+ ",menubar=0,scrollbars=1, resizable=1,status=1,titlebar=0,toolbar=0,location=1");
  }-*/;
}
