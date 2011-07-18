package com.appspot.piment.api.tqq.model;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

public class MessageData implements java.io.Serializable {

  private static final long serialVersionUID = 487710671362457204L;

  private String id;
  private String city_code;
  private int count;
  private String country_code;
  private String from;
  private int geo;
  private String head;
  private String[] image;
  private boolean isvip;
  private String location;
  private int mcount;
  private Music music;
  private String name;
  private String nick;
  private String origtext;
  private String province_code;
  private int self;
  private MessageData source;
  private int status;
  private String text;
  private Date timestamp;
  private Date timestamp_now;
  private String type;
  private Video video;
  private Map<String, String> user;

  public String getId() {
	return id;
  }

  public void setId(String id) {
	this.id = id;
  }

  public String getCity_code() {
	return city_code;
  }

  public void setCity_code(String city_code) {
	this.city_code = city_code;
  }

  public int getCount() {
	return count;
  }

  public void setCount(int count) {
	this.count = count;
  }

  public String getCountry_code() {
	return country_code;
  }

  public void setCountry_code(String country_code) {
	this.country_code = country_code;
  }

  public String getFrom() {
	return from;
  }

  public void setFrom(String from) {
	this.from = from;
  }

  public int getGeo() {
	return geo;
  }

  public void setGeo(int geo) {
	this.geo = geo;
  }

  public String getHead() {
	return head;
  }

  public void setHead(String head) {
	this.head = head;
  }

  public String[] getImage() {
	return image;
  }

  public void setImage(String[] image) {
	this.image = image;
  }

  public boolean isIsvip() {
	return isvip;
  }

  public void setIsvip(boolean isvip) {
	this.isvip = isvip;
  }

  public String getLocation() {
	return location;
  }

  public void setLocation(String location) {
	this.location = location;
  }

  public int getMcount() {
	return mcount;
  }

  public void setMcount(int mcount) {
	this.mcount = mcount;
  }

  public Music getMusic() {
	return music;
  }

  public void setMusic(Music music) {
	this.music = music;
  }

  public String getName() {
	return name;
  }

  public void setName(String name) {
	this.name = name;
  }

  public String getNick() {
	return nick;
  }

  public void setNick(String nick) {
	this.nick = nick;
  }

  public String getOrigtext() {
	return origtext;
  }

  public void setOrigtext(String origtext) {
	this.origtext = origtext;
  }

  public String getProvince_code() {
	return province_code;
  }

  public void setProvince_code(String province_code) {
	this.province_code = province_code;
  }

  public int getSelf() {
	return self;
  }

  public void setSelf(int self) {
	this.self = self;
  }

  public MessageData getSource() {
	return source;
  }

  public void setSource(MessageData source) {
	this.source = source;
  }

  public int getStatus() {
	return status;
  }

  public void setStatus(int status) {
	this.status = status;
  }

  public String getText() {
	return text;
  }

  public void setText(String text) {
	this.text = text;
  }

  public Date getTimestamp() {
	return timestamp;
  }

  public void setTimestamp(Date timestamp) {
	this.timestamp = timestamp;
  }

  public Date getTimestamp_now() {
	return timestamp_now;
  }

  public void setTimestamp_now(Date timestamp_now) {
	this.timestamp_now = timestamp_now;
  }

  public String getType() {
	return type;
  }

  public void setType(String type) {
	this.type = type;
  }

  public Video getVideo() {
	return video;
  }

  public void setVideo(Video video) {
	this.video = video;
  }

  public Map<String, String> getUser() {
	return user;
  }

  public void setUser(Map<String, String> user) {
	this.user = user;
  }

  @Override
  public String toString() {
	return "MessageData [id=" + id + ", city_code=" + city_code + ", count=" + count + ", country_code=" + country_code + ", from=" + from + ", geo=" + geo + ", head=" + head + ", image="
	    + Arrays.toString(image) + ", isvip=" + isvip + ", location=" + location + ", mcount=" + mcount + ", music=" + music + ", name=" + name + ", nick=" + nick + ", origtext=" + origtext
	    + ", province_code=" + province_code + ", self=" + self + ", source=" + source + ", status=" + status + ", text=" + text + ", timestamp=" + timestamp + ", timestamp_now=" + timestamp_now
	    + ", type=" + type + ", video=" + video + ", user=" + user + "]";
  }

}
