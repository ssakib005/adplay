package com.ads;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import java.io.File;

public class AdPlay extends WebView implements View.OnTouchListener{

    String tag;
    Context context;
    private Button valid;
    boolean  isAdsFound = false, isTagAdded = false;

    public AdPlay(Context context) {
        super(context);
    }

    public AdPlay(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AdPlay(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressLint("NewApi")
    public AdPlay(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * Initialize Adplay Ads With Tag and Create Container
     * @param ctx
     * @param tag
     * @return
     */
    public AdPlay addTag(Context ctx, String tag) {
        tag = tag.replaceAll(System.getProperty("line.separator"), " ");
        this.context = ctx;
        this.tag = tag;
        Size size = getSize(tag);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            this.setScrollBarSize(0);
        }
        this.setHorizontalScrollBarEnabled(false);
        this.setVerticalScrollBarEnabled(false);
        this.setScrollContainer(false);
        getLayoutParams().height = size.getHeight();
        getLayoutParams().width = size.getWidth();
        requestLayout();
        return this;
    }

    /**
     * Get Ads Size From Tag
     * @param tag
     * @return
     */
    public Size getSize(String tag){
        try {
            String width = tag.split("data-ad-w")[1].split(" ")[0].split("=")[1]
                    .replace("\"", "").trim();
            String height = tag.split("data-ad-h")[1].split(" ")[0].split("=")[1]
                    .replace("\"", "").trim();
            int h = Integer.parseInt(height);
            int w = Integer.parseInt(width);

            h = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, h, getResources().getDisplayMetrics()) + 5;
            w = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, w, getResources().getDisplayMetrics())+5;

            return new Size(h, w);
        }catch (Exception ex){
            return null;
        }
    }

    /**
     * Load ads to adplay
     */
    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled", "AddJavascriptInterface"})
    public void load() {

        getSettings().setJavaScriptEnabled(true);
        setWebChromeClient(new WebChromeClient());
        addJavascriptInterface(this, "HTMLOUT");
        addJavascriptInterface(valid, "mraid.close");
        setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (!isAdsFound){
                    loadUrl("javascript:(function() { document.body.innerHTML += '" + tag + "';}())");
                }
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadUrl("javascript:window.HTMLOUT.processHTML(document.getElementsByTagName('html')[0].innerHTML);");
                    }
                }, 300);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.equals(getUrl())){
                    return false;
                }else{
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    context.startActivity(i);
                    return true;
                }
            }
        });
        System.out.println(BuildConfig.API_URL);
        loadUrl(BuildConfig.API_URL);
    }

    /**
     * Close Adplay Container
     * @param event
     * @return
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            @SuppressLint("ClickableViewAccessibility")
            HitTestResult hr = (this).getHitTestResult();
            if (hr.getType() == 0) {
                Log.i("WebView Click Event", "getExtra = " + hr.getExtra() + "\t\t Type=" + hr.getType());
                deleteCache(context);
                getLayoutParams().height = 0;
                getLayoutParams().width = 0;
                requestLayout();
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * Detect Ads Loaded or not
     * @param html
     */
    @JavascriptInterface
    public void processHTML(String html) {

        Size size = getSize(html);
        if (size != null){
            isAdsFound = true;
        }else{
            this.post(new Runnable() {
                @Override
                public void run() {
                    loadUrl("javascript:(function() { location.reload();}())");
                }
            });
        }
        Log.d("HTML_LOAD_DATA", "processHTML: " + html);
    }

    /**
     * Clear Cash From Webview
     * @param context
     */
    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) { e.printStackTrace();}
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            @SuppressLint("ClickableViewAccessibility")
            HitTestResult hr = ((WebView)v).getHitTestResult();
            if (hr.getType() == 0){
                Log.i("WebView Click Event", "getExtra = "+ hr.getExtra() + "\t\t Type=" + hr.getType());
                getLayoutParams().height = 0;
                getLayoutParams().width = 0;
                requestLayout();
            }
        }
        return false;
    }
}


