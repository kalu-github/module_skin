package com.kalu.skin;

import com.kalu.skin.deployer.CustomViewTextColorResDeployer;
import com.kalu.skin.deployer.ViewBackgroundStyleParser;

import lib.kalu.skin.application.SkinApplication;
import lib.kalu.skin.attribute.SkinAttrParserManagerStyle;
import lib.kalu.skin.attribute.SkinAttrParserManagerXml;

/**
 * description: MyApplication
 * created by kalu on 2020-11-17
 */
public class MyApplication extends SkinApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        // 处理自定义的换肤属性, 增加自定义控件的自定义属性的换肤支持
        SkinAttrParserManagerXml.registerDeployer("titleTextColor", new CustomViewTextColorResDeployer());

        // 处理自定义的换肤属性, 增加xml里的style中指定的View background属性换肤
        SkinAttrParserManagerStyle.addStyleParser(new ViewBackgroundStyleParser());
    }

    @Override
    protected int onSkinStatusColor() {
        return R.color.statusBarColor;
    }
}
