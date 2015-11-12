package com.creatunion.runner;

import java.util.HashMap;
import java.util.Map;

import com.library.core.event.Event;
import com.library.core.net.bean.HttpClientBean;
import com.library.core.net.http.HttpRequestRunner;

public class TestRunner extends HttpRequestRunner {

	@Override
	public void onEventRun(Event event) throws Exception {
		
		Map<String, String> params = new HashMap<String, String>();
	//	params.put(key, new File(path))
		HttpClientBean info = new HttpClientBean(/*URLUtils.LOGIN*/"", null);
		
		String result  = doPost(info).getRetData();
		event.addReturnParam(result);
		event.setSuccess(true);
	}

}
