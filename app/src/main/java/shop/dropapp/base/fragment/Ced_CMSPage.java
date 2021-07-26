package shop.dropapp.base.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import shop.dropapp.R;

public class Ced_CMSPage extends Activity {

    private WebView webView;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view);
        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(getIntent().getStringExtra("cms"));

    }

}
