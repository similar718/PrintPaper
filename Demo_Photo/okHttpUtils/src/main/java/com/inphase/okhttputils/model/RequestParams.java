package com.inphase.okhttputils.model;

import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class RequestParams {

	/** 普通的键值对参数 */
	public ConcurrentHashMap<String, String> urlParamsMap;

	/** 文件的键值对参数 */
	public ConcurrentHashMap<String, FileWrapper> fileParamsMap;

	private void init() {
		urlParamsMap = new ConcurrentHashMap<>();
		fileParamsMap = new ConcurrentHashMap<>();
	}

	public RequestParams() {
		init();
	}

	public RequestParams(String key, String value) {
		init();
		put(key, value);
	}

	public RequestParams(String key, File file) {
		init();
		put(key, file);
	}

	public void put(RequestParams params) {
		if (params != null) {
			if (params.urlParamsMap != null && !params.urlParamsMap.isEmpty())
				urlParamsMap.putAll(params.urlParamsMap);
			if (params.fileParamsMap != null && !params.fileParamsMap.isEmpty())
				fileParamsMap.putAll(params.fileParamsMap);
		}
	}

	public void put(String key, String value) {
		if (key != null && value != null) {
			urlParamsMap.put(key, value);
		}
	}

	public void put(String key, File file) {
		put(key, file, file.getName());
	}

	public void put(String key, File file, String fileName) {
		put(key, file, fileName, guessMimeType(fileName));
	}

	public void put(String key, File file, String fileName,
			MediaType contentType) {
		if (key != null) {
			fileParamsMap
					.put(key, new FileWrapper(file, fileName, contentType));
		}
	}

	public Map<String, String> getMap() {
		Map<String, String> map = new HashMap<String, String>();
		for (Map.Entry<String, String> e : urlParamsMap.entrySet()) {
			map.put(e.getKey(), e.getValue());
		}
		return map ;
	}

	public void clear() {
		urlParamsMap.clear();
		fileParamsMap.clear();
	}

	private MediaType guessMimeType(String path) {
		FileNameMap fileNameMap = URLConnection.getFileNameMap();
		String contentType = fileNameMap.getContentTypeFor(path);
		if (contentType == null) {
			contentType = "application/octet-stream";
		}
		return MediaType.parse(contentType);
	}

	/**
	 * 文件类型的包装类
	 */
	public static class FileWrapper {
		public File file;
		public String fileName;
		public MediaType contentType;
		public long fileSize;

		public FileWrapper(File file, String fileName, MediaType contentType) {
			this.file = file;
			this.fileName = fileName;
			this.contentType = contentType;
			this.fileSize = file.length();
		}

		public String getFileName() {
			if (fileName != null) {
				return fileName;
			} else {
				return "nofilename";
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		for (ConcurrentHashMap.Entry<String, String> entry : urlParamsMap
				.entrySet()) {
			if (result.length() > 0)
				result.append("&");
			result.append(entry.getKey()).append("=").append(entry.getValue());
		}
		for (ConcurrentHashMap.Entry<String, FileWrapper> entry : fileParamsMap
				.entrySet()) {
			if (result.length() > 0)
				result.append("&");
			result.append(entry.getKey()).append("=").append(entry.getValue());
		}
		return result.toString();
	}
}