package com.inphase.okhttputils.request;
import okhttp3.Request;
import okhttp3.RequestBody;

public class DeleteRequest extends BaseRequest<DeleteRequest> {

    public DeleteRequest(String url) {
        super(url);
    }

    @Override
    public RequestBody generateRequestBody() {
        return null;
    }

    @Override
    public Request generateRequest(RequestBody requestBody) {
        Request.Builder requestBuilder = new Request.Builder();
        appendHeaders(requestBuilder);
        url = createUrlFromParams(url, params.urlParamsMap);
        return requestBuilder.delete(requestBody).url(url).tag(tag).build();
    }
}
