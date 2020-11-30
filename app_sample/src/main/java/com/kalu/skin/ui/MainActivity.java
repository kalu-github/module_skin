package com.kalu.skin.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.kalu.skin.R;

import lib.kalu.skin.SkinManager;
import lib.kalu.skin.util.AssetUtil;

public class MainActivity extends Activity {

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        int identifierLayout = SkinManager.getManager().getIdentifierLayout("activity_main", R.layout.activity_main);
//        String resourceName = SkinManager.getManager().getResourceName(identifierLayout);
////
////        ViewGroup parent = (ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content).getParent();
////        parent.removeAllViews();
////
//        setContentView(identifierLayout);
        setContentView(R.layout.activity_main);

        ImageView imageView = findViewById(R.id.logo);
        SkinManager.getManager().setImageDrawable(getApplicationContext(), imageView, R.drawable.my_image);
        TextView textView = findViewById(R.id.text);
        SkinManager.getManager().setTextViewColor(getApplicationContext(), textView, R.color.myTextColor);

        WebView webView = findViewById(R.id.webview);
        if (null != webView) {
            webView.getSettings().setJavaScriptEnabled(true);
            webView.addJavascriptInterface(this, "android");

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("http://10.128.207.128:4200/#/");
            stringBuilder.append("home/index?channelNo=8&sessionId=");
            stringBuilder.append("123456");

            String url = stringBuilder.toString();
            webView.loadUrl("http://172.17.23.146:8001/index.html");
        }
    }

    @JavascriptInterface
    public void check(@Nullable String string) {
        Log.e("tag", "check => string = " + string);

        if (TextUtils.isEmpty(string))
            return;

        if ("1".equals(string)) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    changeSkin();
                }
            });

        } else if ("2".equals(string)) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loadDefault();
                }
            });
        }
    }

    //相应按钮点击事件
    public void clickButton(View v) {
        switch (v.getId()) {
            case R.id.change_skin_button:

                changeSkin();

//                // int main = SkinManager.getManager().getIdentifierLayout("activity_main");
//                View view = SkinManager.getManager().getContentView(getApplicationContext(), "activity_main");
//
//                ViewGroup parent = (ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content).getParent();
//                parent.removeAllViews();
//                parent.addView(view);

                WebView webView = findViewById(R.id.webview);
                webView.loadUrl("javascript:update(\"1\")");
                break;
            case R.id.recovery_skin_button:
                loadDefault();
                WebView webView2 = findViewById(R.id.webview);
                webView2.loadUrl("javascript:update(\"2\")");
                break;
            case R.id.jump_skin_button:
                Intent intent = new Intent(getApplicationContext(), ChildActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void changeSkin() {
        String copyAssetFile = AssetUtil.copyAssetFile(getApplicationContext(), "skins/app_sample_skin-debug.apk", "skin2021");
        SkinManager.getManager().loadSkin(getApplicationContext(), copyAssetFile);

        String pluginSkinPath = SkinManager.getManager().getPluginSkinPath();
        Log.e("test", "changeSkin => pluginSkinPath = " + pluginSkinPath);
    }

    private void loadDefault() {
        SkinManager.getManager().loadDefault(getApplicationContext());

        String pluginSkinPath = SkinManager.getManager().getPluginSkinPath();
        Log.e("test", "loadSkinDefault => pluginSkinPath = " + pluginSkinPath);
    }
}
