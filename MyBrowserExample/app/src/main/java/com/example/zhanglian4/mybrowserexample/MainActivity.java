package com.example.zhanglian4.mybrowserexample;

import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    ProgressBar myProgressBar;
    ImageView myImageView;
    WebView myWebView;
    LinearLayout myLinearLayout;
    SwipeRefreshLayout mySwipeLayout;
    String myCurrentUrl;
    Button mySearchButton;
    EditText myEditText;
    Button myBookmarkButton;
    private ListViewCustomAdapter listAdapter;
    List<Websites> fullList = new ArrayList<Websites>();
    List<Websites> historyList = new ArrayList<Websites>();
    List<String> addedWeb = new ArrayList<String>();
    List<String> historyDates = new ArrayList<String>();
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setInitialLayout();
    }

    protected void setInitialLayout() {
        setContentView(R.layout.activity_main);

        myProgressBar = findViewById(R.id.myProgressBar);
        myImageView = findViewById(R.id.myImageView);
        myWebView = findViewById(R.id.myWebView);
        myLinearLayout = findViewById(R.id.myLinearLayout);
        mySwipeLayout = findViewById(R.id.mySwipeLayout);
        mySearchButton = findViewById(R.id.mySearchButton);
        myEditText = findViewById(R.id.myEditText);
        myBookmarkButton = findViewById(R.id.myBookmarkButton);
        myProgressBar.setMax(100);
        myWebView.loadUrl("https://www.google.com");

        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setAppCacheEnabled(true);
        myWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        myWebView.getSettings().setDomStorageEnabled(true);

        myWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                myLinearLayout.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                myLinearLayout.setVisibility(View.GONE);
                mySwipeLayout.setRefreshing(false);

                super.onPageFinished(view, url);

                myCurrentUrl = url;
                historyList.add(new Websites(myWebView.getTitle().toString(), myCurrentUrl));
                Date t = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+13"));
                String historyDate = dateFormat.format(t);
                historyDates.add(historyDate);

                if(addedWeb.contains(myWebView.getTitle())){
                    myBookmarkButton.setBackgroundResource(R.drawable.ic_star_black_24dp);
                }else{
                    myBookmarkButton.setBackgroundResource(R.drawable.ic_star_border_black_24dp);
                }
            }
        });

        myWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                myProgressBar.setProgress(newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                getSupportActionBar().setTitle(title);
            }

            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                super.onReceivedIcon(view, icon);
                myImageView.setImageBitmap(icon);
            }
        });

        mySearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = myEditText.getText().toString();
                myWebView.loadUrl(url);
            }
        });

        myBookmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = myWebView.getUrl().toString();
                String website = myWebView.getTitle().toString();
                Websites web = new Websites(website, url);

                if (addedWeb.contains(website)) {
                    Toast.makeText(MainActivity.this, "This website is already in Bookmarks.", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(web.getWebsite(), web.getUrl());
                    fullList.add(web);
                    addedWeb.add(website);
                    myBookmarkButton.setBackgroundResource(R.drawable.ic_star_black_24dp);
                    Toast.makeText(MainActivity.this, "Added to Bookmarks.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        myWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String s, String s1, String s2, String s3, long l) {

                DownloadManager.Request myRequest = new DownloadManager.Request(Uri.parse(s));
                myRequest.allowScanningByMediaScanner();
                myRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                DownloadManager myManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                myManager.enqueue(myRequest);

                Toast.makeText(MainActivity.this, "Your file is downloading...", Toast.LENGTH_SHORT).show();
            }
        });

        mySwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                myWebView.reload();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_back:
                onBackPressed();
                break;

            case R.id.menu_forward:
                onForwardPressed();
                break;

            case R.id.menu_refresh:
                myWebView.reload();
                break;

            case R.id.menu_share:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Copied URL");
                shareIntent.putExtra(Intent.EXTRA_TEXT, myCurrentUrl);
                startActivity(Intent.createChooser(shareIntent, "Share URL ..."));
                break;

            case R.id.menu_history:
                setupHistoryScreen();
                break;

            case R.id.menu_bookmarks:
                setupBookmarkScreen();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void onForwardPressed() {
        if (myWebView.canGoForward()) {
            myWebView.goForward();
        } else {
            Toast.makeText(this, "Can't go further!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (myWebView.canGoBack()) {
            myWebView.goBack();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Confirm");
            builder.setMessage("Do you want to quit this app?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    finish();
                }
            });
            builder.setNegativeButton("No", null);
            builder.show();

        }
    }

    private void setupBookmarkScreen() {

        for (int i = 0; i <= 20; i++) {
            fullList.add(new Websites("hello", "world"));
        }

        setContentView(R.layout.bookmarks);
        list = (ListView) findViewById(R.id.myBookmarksListView);
        listAdapter = new ListViewCustomAdapter(this, fullList);
        list.setAdapter(listAdapter);

        getSupportActionBar().setTitle("Bookmarks");

        Button btn = findViewById(R.id.buttonBack);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setInitialLayout();
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setInitialLayout();
                myWebView.loadUrl(fullList.get(i).getUrl());
            }
        });
    }

    private void setupHistoryScreen() {

        setContentView(R.layout.history);

        getSupportActionBar().setTitle("History");

        Button btn = findViewById(R.id.buttonBack2);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setInitialLayout();
            }
        });

        ScrollView myHistoyScrollView = findViewById(R.id.myHistoryScrollView);
        TextView myHistoryTextView = findViewById(R.id.myHistoryTextView);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < historyList.size(); i++) {
            sb.append("- " +historyDates.get(i)+"   "+ historyList.get(i).getWebsite() + " : " + "\n" + "    " + historyList.get(i).getUrl() + "\n" + "\n");

            myHistoryTextView.setAutoLinkMask(Linkify.ALL);
            myHistoryTextView.setMovementMethod(LinkMovementMethod.getInstance());

            myHistoryTextView.setText(sb.toString());

        }
    }
}


