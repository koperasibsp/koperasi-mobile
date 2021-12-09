package id.co.bspguard.android.bravo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

public class OpenBrowser extends AppCompatActivity {

    String uri;
    ProgressBar proBar;
    WebView webView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_browser);
        webView = (WebView) findViewById(R.id.webview);
        proBar = (ProgressBar) findViewById(R.id.proBar);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new myWebClient());
        Intent intent = getIntent();
        uri = intent.getStringExtra("uri");
        //Toast.makeText(OpenBrowser.this, uri, Toast.LENGTH_LONG).show();
        proBar.setVisibility(View.VISIBLE);
        webView.loadUrl(uri);
    }

    public void onBackPressed () {
        AlertDialog.Builder aBuilder = new AlertDialog.Builder(OpenBrowser.this);
        aBuilder.setTitle(Html.fromHtml("<small>Transaction Exit</small>"));
        aBuilder.setMessage(Html.fromHtml("<small>Anda yakin akan keluar dari transaksi?</small>"))
                .setCancelable(false)
                .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(OpenBrowser.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = aBuilder.create();
        alertDialog.show();
    }

    public class myWebClient extends WebViewClient {
        @Override
        public void onPageFinished (WebView view, String url) {
            super.onPageFinished(view, url);
            proBar.setVisibility(View.GONE);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return super.shouldOverrideUrlLoading(view, url);
        }
    }
}
