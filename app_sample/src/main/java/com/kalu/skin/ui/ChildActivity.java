package com.kalu.skin.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kalu.skin.R;

import lib.kalu.skin.SkinManager;
import lib.kalu.skin.util.AssetUtil;

public class ChildActivity extends Activity {

    private TextView mTextView;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child);

        mTextView = findViewById(R.id.my_textView);
        mImageView = findViewById(R.id.my_imageView);

        SkinManager.getManager().setImageDrawable(getApplicationContext(), mImageView, R.drawable.my_image);
        SkinManager.getManager().setTextViewColor(getApplicationContext(), mTextView, R.color.myTextColor);
    }

    //相应按钮点击事件
    public void clickButton(View v) {
        switch (v.getId()) {
            case R.id.change_skin_button:
                changeSkin();
                break;
            case R.id.recovery_skin_button:
                loadDefault();
                break;
        }
    }

    private void changeSkin() {
        String copyAssetFile = AssetUtil.copyAssetFile(getApplicationContext(), "skins/app_sample_skin-debug.apk", "skin2021");
        SkinManager.getManager().loadSkin(getApplicationContext(), copyAssetFile);
    }

    private void loadDefault() {
        SkinManager.getManager().loadDefault(getApplicationContext());
    }
}