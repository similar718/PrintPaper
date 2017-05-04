package com.inphase.okhttputils.request;


import java.util.Map;

import com.inphase.okhttputils.model.RequestParams;

import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

public class PatchRequest extends BaseRequest<PatchRequest> {

    public PatchRequest(String url) {
        super(url);
    }

    @Override
    public RequestBody generateRequestBody() {
        if (params.fileParamsMap.isEmpty()) {
            //表单提交，没有文件
            FormBody.Builder bodyBuilder = new FormBody.Builder();
            for (String key : params.urlParamsMap.keySet()) {
                bodyBuilder.add(key, params.urlParamsMap.get(key));
            }
            return bodyBuilder.build();
        } else {
            //表单提交，有文件
            MultipartBody.Builder multipartBodybuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            //拼接键值对
            if (!params.urlParamsMap.isEmpty()) {
                for (Map.Entry<String, String> entry : params.urlParamsMap.entrySet()) {
                    multipartBodybuilder.addFormDataPart(entry.getKey(), entry.getValue());
                }
            }
            //拼接文件
            for (Map.Entry<String, RequestParams.FileWrapper> entry : params.fileParamsMap.entrySet()) {
                RequestBody fileBody = RequestBody.create(entry.getValue().contentType, entry.getValue().file);
                multipartBodybuilder.addFormDataPart(entry.getKey(), entry.getValue().fileName, fileBody);
            }
            return multipartBodybuilder.build();
        }
    }

    @Override
    public Request generateRequest(RequestBody requestBody) {
        Request.Builder requestBuilder = new Request.Builder();
        appendHeaders(requestBuilder);
        return requestBuilder.patch(requestBody).url(url).tag(tag).build();
    }
}
