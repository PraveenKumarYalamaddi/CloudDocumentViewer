package com.doc.viewer;

import com.google.gson.Gson;

public class DocViewerUtil {
	
	
	private final static String BASE_URL = "https://console.cloud-elements.com/elements/api-v2/";
	private final static String DOC_URL ="hubs/documents/";
	private final static String FILE_URL="files?path=/";
	private final static String FOLDER_CONTENTS="folders/contents?path=/";
	private final static String USER = "User ";
	private final static String USER_TOKEN = "Tpr8Uru0ZqcUNcXXXBAOo+otIxgnfKojxP1lidtcims=,";
	private final static String ORGANIZATION = "Organization ";
	private final static String ORGANIZATION_TOKEN = "df903b7f988fc81e5fa1d8abf2fb1c8e,";
	private final static String ELEMENT = "Element ";
	private final static String ELEMENT_TOKEN = "b6yhLd/wrclBadPVFI+HWqkHDVvp6B/cTJ9AbU+f7uc=";
	private static final String FILE = "file";
	private static final String FOLDER = "folder_contents";
	private static final String AUTH = "auth";
	
	public static String endPoint(String key) {
		switch (key) {
		case FILE:
			return BASE_URL+DOC_URL+FILE_URL;
		case FOLDER:
			return BASE_URL+DOC_URL+FOLDER_CONTENTS;
		case AUTH:
			return USER+USER_TOKEN+ORGANIZATION+ORGANIZATION_TOKEN+ELEMENT+ELEMENT_TOKEN;
		default:
			return BASE_URL;
		}
		
	}
	
	public static String toJson(Object jsonObj) {
		try {
			Gson gson = new Gson();
			return gson.toJson(jsonObj);
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}
	
	public static <T> T fromJson(String json, Class<T> type) {
		try {
			Gson gson = new Gson();
			return gson.fromJson(json, type);
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}
}
