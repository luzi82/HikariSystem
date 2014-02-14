package com.luzi82.hikari.client.android.demo;

import java.util.concurrent.Executor;
import java.util.concurrent.Future;

import org.apache.http.Header;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.luzi82.concurrent.FutureCallback;
import com.luzi82.concurrent.GuriFuture;
import com.luzi82.hikari.client.HsHttpClient;

public class HttpClient implements HsHttpClient {

	AsyncHttpClient asyncHttpClient;
	Executor executor;

	public HttpClient(Executor executor) {
		asyncHttpClient = new AsyncHttpClient();
		this.executor = executor;
	}

	class SendRequestFuture extends GuriFuture<String> {

		final String url;
		final String request;

		public SendRequestFuture(String url, String request,
				FutureCallback<String> callback) {
			super(callback, HttpClient.this.executor);
			this.url = url;
			this.request = request;
			System.err.println("AUEFR1Zp url: " + url);
			System.err.println("fdXtwJSl request: " + request);
		}

		public void start() {
			new Step0().start();
		}

		RequestHandle requestHandle;

		public class Step0 extends Step {

			@Override
			public void _run() throws Exception {
				RequestParams requestParams = new RequestParams();
				requestParams.put("arg", request);
				requestHandle = asyncHttpClient.post(url, requestParams,
						new AsyncHttpResponseHandler() {
							@Override
							public void onFailure(int arg0, Header[] arg1,
									byte[] arg2, Throwable arg3) {
								// System.err.println("T34u0hBG onFailure");
								if (arg3 instanceof Exception) {
									Exception exception = (Exception) arg3;
									SendRequestFuture.this.failed(exception);
								} else {
									arg3.printStackTrace();
									SendRequestFuture.this
											.failed(new Exception(arg3));
								}
							}

							@Override
							public void onSuccess(int arg0, Header[] arg1,
									byte[] arg2) {
								// System.err.println("c9VXiR9u onSuccess");
								String ret = null;
								try {
									if (arg0 != 200) {
										throw new StatusCodeException(arg0);
									}
									completed(new String(arg2, "utf-8"));
								} catch (Exception e) {
									SendRequestFuture.this.failed(e);
								}
								completed(ret);
							}
						});
			}

			@Override
			public boolean cancel(boolean mayInterruptIfRunning) {
				synchronized (SendRequestFuture.this) {
					if (requestHandle != null) {
						return requestHandle.cancel(mayInterruptIfRunning);
					}
				}
				return super.cancel(mayInterruptIfRunning);
			}

		}

	}

	public static class StatusCodeException extends Exception {
		private static final long serialVersionUID = -4080044588622387992L;
		public int code;

		public StatusCodeException(int code) {
			super("code=" + code);
			this.code = code;
		}
	}

	@Override
	public Future<String> sendRequest(String url, String request,
			FutureCallback<String> callback) {
		SendRequestFuture ret = new SendRequestFuture(url, request, callback);
		ret.start();
		return ret;
	}

}
