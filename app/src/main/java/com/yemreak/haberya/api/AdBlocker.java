package com.yemreak.haberya.api;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebResourceResponse;

import androidx.annotation.WorkerThread;

import com.yemreak.haberya.R;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import okhttp3.HttpUrl;
import okio.BufferedSource;
import okio.Okio;

/**
 * @see <a href="https://android.yemreak.com/yazilarim/webview-uzerindeki-reklamlari-engelleme">WebView Üzerindeki Reklamları Engelleme ~ YEmreAk</a>
 */
public class AdBlocker {
	public static final String TAG = AdBlocker.class.getName();

	private static final String AD_HOSTS_FILE = "ads.txt";
	private static final Set<String> AD_HOSTS = new HashSet<>();

	public static void init(Context context) {
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				try {
					Log.d(TAG, "doInBackground: Reklam filtresi yükleniyor");
					loadFromAssets(context);
				} catch (IOException e) {
					// noop
					e.printStackTrace();
				}
				return null;
			}
		}.execute();
	}

	@WorkerThread
	private static void loadFromAssets(Context context) throws IOException {
		InputStream stream = context.getResources().openRawResource(R.raw.ads);
		BufferedSource buffer = Okio.buffer(Okio.source(stream));
		String line;
		while ((line = buffer.readUtf8Line()) != null) {
			AD_HOSTS.add(line);
		}
		buffer.close();
		stream.close();
	}

	public static boolean isAd(String url) {
		HttpUrl httpUrl = HttpUrl.parse(url);
		return isAdHost(httpUrl != null ? httpUrl.host() : "");
	}

	private static boolean isAdHost(String host) {
		if (TextUtils.isEmpty(host)) {
			return false;
		}
		int index = host.indexOf(".");
		return index >= 0 && (AD_HOSTS.contains(host) ||
				index + 1 < host.length() && isAdHost(host.substring(index + 1)));
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static WebResourceResponse createEmptyResource() {
		return new WebResourceResponse("text/plain", "utf-8", new ByteArrayInputStream("".getBytes()));
	}
}
