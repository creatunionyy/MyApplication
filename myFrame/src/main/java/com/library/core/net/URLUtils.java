package com.library.core.net;

public class URLUtils {

	static UrlController urlController = UrlController.getInstance(false);

	public static final String URL = urlController.getUrl();

	public static final String IP = urlController.getIp();

	public static final int PORT = urlController.getPort();
	
	public static final String PLATE_VIEWPOINT = URL + "/account/app/GetNotesByPlate";

}
