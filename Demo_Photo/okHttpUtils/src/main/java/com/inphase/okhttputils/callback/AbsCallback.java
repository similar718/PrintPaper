package com.inphase.okhttputils.callback;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import com.inphase.okhttputils.L;
import com.inphase.okhttputils.request.BaseRequest;

import android.support.annotation.Nullable;
import okhttp3.Request;
import okhttp3.Response;

public abstract class AbsCallback<T> {
    /** 请求网络开始前，UI线程 */
    public void onBefore(BaseRequest request) {
    }

    /** 请求网络结束后，UI线程 */
    public void onAfter(@Nullable T t, Request request, Response response, @Nullable Exception e) {
    }

    /**
     * Post执行上传过程中的进度回调，get请求不回调，UI线程
     *
     * @param currentSize  当前上传的字节数
     * @param totalSize    总共需要上传的字节数
     * @param progress     当前上传的进度
     * @param networkSpeed 当前上传的速度   字节/秒
     */
    public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
    }

    /**
     * 执行下载过程中的进度回调，UI线程
     *
     * @param currentSize  当前下载的字节数
     * @param totalSize    总共需要下载的字节数
     * @param progress     当前下载的进度
     * @param networkSpeed 当前下载的速度   字节/秒
     */
    public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
    }

    /** 拿到响应后，将数据转换成需要的格式，子线程中执行，可以是耗时操作 */
    public abstract T parseNetworkResponse(Response response) throws Exception;

    /** 对返回数据进行操作的回调， UI线程 */
    public abstract void onSuccess(T t);
    
    /**
     * 请求成功，结果失败
     * @param msg 错误信息
     */
    public abstract void onError(ErroBean msg);
    
    /**
     * token失效
     */
    public abstract void onTokenInvalid();

    /** 请求失败，响应错误，数据解析错误等，都会回调该方法， UI线程 */
    public void onError(Request request, @Nullable Response response, @Nullable Exception e) {
        if (e != null){
//        	e.printStackTrace();
        	if(e instanceof ConnectException){
        		onError(new ErroBean(0, "网络连接异常！", 0));
        	}else if(e instanceof UnknownHostException){
        		onError(new ErroBean(0, "当前无网络连接，请检查您的网络!", 0));
        	}else if(e instanceof SocketException){
        		onError(new ErroBean(0, "网络连接异常！", 0));
        	}else {
        		onError(new ErroBean(0, e.getMessage(), 0));
			}
        } 
        else if (response != null) {
        	L.e("服务器内部错误，或者找不到页面等");
        	onError(new ErroBean(0, "服务器内部错误，或者找不到页面等", 0));
        }
    }

    public static final AbsCallback CALLBACK_DEFAULT = new AbsCallback() {

        @Override
        public Response parseNetworkResponse(Response response) throws Exception {
            return response;
        }

        @Override
        public void onSuccess(Object response) {
        }

		@Override
		public void onError(ErroBean msg) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTokenInvalid() {
			// TODO Auto-generated method stub
			
		}
    };
}