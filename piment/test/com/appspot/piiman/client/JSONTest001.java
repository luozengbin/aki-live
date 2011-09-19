package com.appspot.piiman.client;

import java.io.IOException;

import net.arnx.jsonic.JSON;
import net.arnx.jsonic.JSONException;

import com.appspot.piment.api.tqq.model.MessageResponse;
import com.appspot.piment.api.tqq.model.TimelineResponse;

public class JSONTest001 {

  /**
   * @param args
   * @throws IOException
   * @throws JSONException
   */
  public static void main(String[] args) throws JSONException, IOException {

//	 MessageResponse messageResponse =
//	 JSON.decode(JSONTest001.class.getClassLoader().getResourceAsStream("com/appspot/piiman/client/tqq_status.json"),
//	 MessageResponse.class);
//	 System.out.println(JSON.encode(messageResponse, true));

//	TimelineResponse timelineResponse = JSON.decode(JSONTest001.class.getClassLoader().getResourceAsStream("com/appspot/piiman/client/222.json"), TimelineResponse.class);
//	System.out.println(JSON.encode(timelineResponse, true));
	
	
//	TimelineResponse  timelineResponse = JSON.decode("{\"data\":null,\"errcode\":9,\"msg\":\"check sign error\",\"ret\":3}",TimelineResponse.class);
//	System.out.println(JSON.encode(timelineResponse, true));
	
	
	String aaa = "#囧司徒每日秀#08奥运期间中国系列报道\"寻龙记\"，御用记者Rob Riggle专访中国著名主持人，芮成钢[嘻嘻] 芮主播名言: Jon Stewart is nobody. We have 200 to 400 million viewers. So, don't you tell me how important you are.[奥特曼] Riggle立马拜倒在芮大师裤腿下 http://video.sina.com.cn/v/b/61213238-1788911247.html";
	
	System.out.println(aaa.length());
	
  }
}
