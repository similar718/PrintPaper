package com.inphase.okhttputils.callback;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.inphase.okhttputils.L;
import com.inphase.okhttputils.model.Consts;
import com.inphase.okhttputils.model.CustomException;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Response;

public abstract class BeanCallBack<T> extends AbsCallback<T> {

	@Override
	public T parseNetworkResponse(Response response) throws Exception {
		Type type = this.getClass().getGenericSuperclass();
		if (type instanceof ParameterizedType) {
			// 如果用户写了泛型，就会进入这里，否者不会执行
			ParameterizedType parameterizedType = (ParameterizedType) type;
			Type beanType = parameterizedType.getActualTypeArguments()[0];
			String result = response.body().string();
			L.d(result);
			if (result.indexOf("status") > -1) {
				try {
					JSONObject obj = new JSONObject(result);
					ErroBean bean = new ErroBean(obj.getInt("status"),
							obj.getString("detail"), obj.getInt("msgflag"));
					if (obj.getInt("status") == 400) {
						throw new CustomException(bean);
					}
					if (obj.getInt("status") == 404) {
						throw new CustomException(bean);
					}
					if (obj.getInt("status") == 500) {
						throw new CustomException(
								Consts.CODE_TOKENINVALID_ERROR,
								obj.getString("detail"), obj.getInt("msgflag"));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			if (beanType == String.class) {
				// 如果是String类型，直接返回字符串
				return (T) result;
			} else {
				// 如果是 Bean List Map ，则解析完后返回
				JSONObject obj = new JSONObject(result);
				if (result.indexOf("status") > -1) {
					if (obj.getInt("status") == 200) {
						if (obj.has("detail")) {
							return new Gson().fromJson(obj.getString("detail"),
									beanType);
						}
					}
				}
				return new Gson().fromJson(result, beanType);
			}
		} else {
			// 如果没有写泛型，直接返回Response对象
			return (T) response;
		}
	}
}
