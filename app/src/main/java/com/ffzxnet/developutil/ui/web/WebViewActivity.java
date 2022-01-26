package com.ffzxnet.developutil.ui.web;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.ffzxnet.developutil.R;
import com.ffzxnet.developutil.base.ui.BaseActivity;
import com.ffzxnet.developutil.constans.MyConstans;
import com.ffzxnet.developutil.utils.ui.ToastUtil;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import butterknife.BindView;

public class WebViewActivity extends BaseActivity {
    @BindView(R.id.toolbar_title_name)
    TextView toolbarTitleName;
    @BindView(R.id.web_view)
    WebView webView;

    @Override
    public int getContentViewByBase(Bundle savedInstanceState) {
        return R.layout.activity_web;
    }

    @Override
    public void createdViewByBase(Bundle savedInstanceState) {
        String url = getBundle().getString(MyConstans.KEY_DATA, "");
        if (TextUtils.isEmpty(url)) {
            ToastUtil.showToastLong("网址失效，请换个网址");
            finish();
        } else {
            initToolBar("", "正在加载网页");
            initWebView(url);
        }
    }

    private void initWebView(String url) {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.clearCache(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setAllowFileAccess(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setDomStorageEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setAppCachePath(getApplication().getCacheDir().getAbsolutePath());


        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest webResourceRequest) {
                toolbarTitleName.setText("加载网页中...");
                webView.loadUrl(webResourceRequest.getUrl().toString());
                return true;
            }

            @Override
            public void onPageFinished(WebView webView, String s) {
                super.onPageFinished(webView, s);
                toolbarTitleName.setText(webView.getTitle());
            }
        });


        webView.loadUrl(url);
    }

    @Override
    protected void onClickTitleBack() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            goBackByQuick();
        }
    }
}
