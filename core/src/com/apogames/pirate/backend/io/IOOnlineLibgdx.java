package com.apogames.pirate.backend.io;

import com.apogames.pirate.Constants;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.net.HttpParametersUtils;
import com.badlogic.gdx.net.HttpRequestBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Io online libgdx.
 */
public class IOOnlineLibgdx {

	public IOOnlineLibgdx() {
	}

	public String save(final String email, final String solution) {
		try {
			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put("email", String.valueOf(email));
			parameters.put("solution", String.valueOf(solution));
			HttpRequest request = new HttpRequest(HttpMethods.POST);
			request.setUrl(Constants.USERLEVELS_SAVEPHP);
			request.setHeader("Content-Type", "application/x-www-form-urlencoded");
			request.setContent(HttpParametersUtils.convertHttpParameters(parameters));

			Gdx.net.sendHttpRequest(request, new HttpResponseListener() {
				@Override
				public void handleHttpResponse(HttpResponse httpResponse) {
					Constants.WAIT_FOR_ANSWER = httpResponse.getResultAsString();
				}

				@Override
				public void failed(Throwable t) {
					Constants.WAIT_FOR_ANSWER = "failed: "+t.getMessage();
					Gdx.app.log("Failed ", t.getMessage());
				}

				@Override
				public void cancelled() {
					Gdx.app.log("Cancelled", "Save cancelled");
				}
			});
		} catch (Exception me) {
			System.err.println("Exception: " + me);
			Constants.WAIT_FOR_ANSWER = "Exception: "+me.getMessage();
		}
		return "";
	}

	public boolean load() {
		try {
			HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
			HttpRequest httpRequest = requestBuilder.newRequest().method(HttpMethods.GET).url(Constants.USERLEVELS_GETPHP).build();

			Gdx.net.sendHttpRequest(httpRequest, new HttpResponseListener() {
				@Override
				public void handleHttpResponse(HttpResponse httpResponse) {
					String resultAsString = httpResponse.getResultAsString();
					String[] split = resultAsString.split("\n", -1);
					if ((split != null) && (split.length > 2)) {
						Constants.YEAR = Integer.valueOf(split[0]);
						Constants.MONTH = Integer.valueOf(split[1]);
						Constants.DAY = Integer.valueOf(split[2]);

						Constants.IS_DATE_SET = true;
					}
				}

				@Override
				public void failed(Throwable t) {
					Gdx.app.log("Failed ", t.getMessage());
				}

				@Override
				public void cancelled() {
					Gdx.app.log("Cancelled", "Load cancelled");
				}
			});

			return true;
		} catch (Exception me) {
			System.err.println("Exception: " + me);
		}
		return false;
	}

}