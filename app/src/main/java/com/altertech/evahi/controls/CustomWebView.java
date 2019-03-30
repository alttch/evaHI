package com.altertech.evahi.controls;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.webkit.HttpAuthHandler;
import android.webkit.WebBackForwardList;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class CustomWebView extends WebView {

	private static final String TAG = CustomWebView.class.getSimpleName();

	public static final String ABOUT_BLANK = "about:blank";

	public boolean isReadable = false;

	private boolean forceOriginalFormat = false;

	private WebViewClient webViewClient = null;

	public boolean readableBackState = false;

	public boolean loadingReadableBack = false;

	public boolean shouldClearHistory = false;

	public CustomWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		attrSet = attrs;
	}

	public boolean is_gone = true;

	public AttributeSet attrSet = null;

	public void setWebViewClient(WebViewClient client) {
		webViewClient = client;
		super.setWebViewClient(client);
	}

	public WebViewClient getWebViewClient() {
		return webViewClient;
	}

	public void setIsReadable(boolean isReadable) {
		this.isReadable = isReadable;
	}

	public void onWindowVisibilityChanged(int visibility) {
		super.onWindowVisibilityChanged(visibility);
		if (visibility == View.GONE) {
			try {
				WebView.class.getMethod("onPause").invoke(this);// stop flash
			} catch (Exception e) {
			}
			this.pauseTimers();
			this.is_gone = true;
		} else if (visibility == View.VISIBLE) {
			try {
				WebView.class.getMethod("onResume").invoke(this);// resume flash
			} catch (Exception e) {
			}
			this.resumeTimers();
			this.is_gone = false;
		}
	}

	public AttributeSet getAttributes() {
		return attrSet;
	}

	public void stopView() {
		try {
			WebView.class.getMethod("onPause").invoke(this);// stop flash
		} catch (Exception ignored) {
		}
		this.pauseTimers();
	}

	public void resumeView() {
		try {
			WebView.class.getMethod("onResume").invoke(this);// resume flash
		} catch (Exception ignored) {
		}
		this.resumeTimers();
	}

	@Override
	public WebBackForwardList saveState(Bundle outState) {
		outState.putBoolean("isReadable", isReadable);
		return super.saveState(outState);
	}

	@Override
	public WebBackForwardList restoreState(Bundle inState) {
		isReadable = inState.getBoolean("isReadable");
		return super.restoreState(inState);
	}

	/**
	 * Whether original format of the web page is required (no readability)
	 * 
	 * @return true if original format is required, false otherwise
	 */
	public boolean isOriginalRequired() {
		return forceOriginalFormat;
	}

	public void clearReadabilityState() {
		isReadable = false;
		forceOriginalFormat = false;

		readableBackState = false;
		loadingReadableBack = false;
	}

	public void forceOriginal() {
		isReadable = false;
		forceOriginalFormat = true;
	}

	public void clearBrowserState() {
		stopLoading();
		clearHistory();
		clearViewReliably();
		shouldClearHistory = true;

		clearReadabilityState();
	}

	private void clearViewReliably() {
		loadUrl(ABOUT_BLANK);
	}


	public String backPressAction() {
		WebBackForwardList history = copyBackForwardList();
		int index = -1;
		String url = null;

		while (canGoBackOrForward(index)) {
			if (!history.getItemAtIndex(history.getCurrentIndex() + index)
					.getUrl().equalsIgnoreCase(ABOUT_BLANK)) {
				goBackOrForward(index);
				url = history.getItemAtIndex(-index).getUrl();
				Log.e(TAG, "first non empty " + url);
				break;
			}
			index--;
		}
		return url;
	}
}
