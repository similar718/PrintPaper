package com.inphase.okhttputils.callback;

import java.io.IOException;

import okhttp3.Response;

import org.json.JSONException;
import org.json.JSONObject;

import com.inphase.okhttputils.model.Consts;
import com.inphase.okhttputils.model.CustomException;

public abstract class StringCallback extends AbsCallback<String> {

	@Override
	public String parseNetworkResponse(Response response) throws IOException,
			CustomException {
		String result = response.body().string();
		if (result.indexOf("status") > -1) {
			try {
				JSONObject obj = new JSONObject(result);
				if (obj.getInt("status") == 400) {
					throw new CustomException(obj.getInt("status"),
							obj.getString("detail"), obj.getInt("msgflag"));
				}
				if (obj.getInt("status") == 500) {
					throw new CustomException(Consts.CODE_TOKENINVALID_ERROR,
							obj.getString("detail"), obj.getInt("msgflag"));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
}
