package com.gq.common.utils;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gq.common.request.RequestEntity;

/**
 * 将JSON字符串转换为具体的对象
 * 
 * @author zhi
 *
 */
public class GsonUtils {

	private static GsonBuilder builder = new GsonBuilder();
	private static Gson gson = builder.registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory()).create();

	public static <T> T fromJson(String json, Class<T> cla) {
		if (isNotEmpty(json)) {
			T t = gson.fromJson(json.trim(), cla);
			return t;
		}
		return null;
	}

	public static <T> T fromJson(String json, Type typeOfT) {
		if (isNotEmpty(json)) {
			return gson.fromJson(json, typeOfT);
		}
		return null;
	}

	public static String toJson(Object obj) {
		if (null != obj) {
			return gson.toJson(obj);
		}
		return null;
	}

	
	public static RequestEntity fromRequestJson(String json) {
		if (isNotEmpty(json)) {
			RequestEntity entity = gson.fromJson(json.trim(), RequestEntity.class);
			return entity;
		}
		return null;
	}


	private static boolean isNotEmpty(String json) {
		return !StringUtils.isNullOrEmpty(json);
	}

}