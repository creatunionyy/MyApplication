package com.library.core.base;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import com.library.core.parse.JsonParse;

public class HttpPageParam implements XHttpPagination, Serializable {

	private static final long serialVersionUID = 1L;

	private String last;
	private String offset;
	private boolean hasmore;

	public HttpPageParam(JSONObject jo) throws JSONException {
		JsonParse.parse(jo, this);
	}

	public int getTotalCount() {
		return 0;
	}

	public int getCurrentCount() {
		return 0;
	}

	
	public boolean isHasmore() {
		return hasmore;
	}

	public void setHasmore(boolean hasmore) {
		this.hasmore = hasmore;
	}

	@Override
	public boolean hasMore() {
		return hasmore;
	}

	public String getLast() {
		return last;
	}

	public void setLast(String last) {
		this.last = last;
	}

	public String getOffset() {
		return offset;
	}

	public void setOffset(String offset) {
		this.offset = offset;
	}
	
	
}
