package com.library.core.bean;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class PicUrl implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String picUrl;
	private String thumbUrl;
	private String filePath;
	
	public PicUrl(JSONObject jo) throws JSONException{
		if(jo.has("pic"))
			picUrl = jo.getString("pic");
		if(jo.has("thumb_pic"))
			thumbUrl = jo.getString("thumb_pic");
	}
	public PicUrl(){
		 
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getThumbUrl() {
		return thumbUrl;
	}

	public void setThumbUrl(String thumbUrl) {
		this.thumbUrl = thumbUrl;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
}
