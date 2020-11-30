package com.kalu.skin.deployer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import lib.kalu.skin.model.AttributeModel;
import lib.kalu.skin.setting.SkinSetting;
import lib.kalu.skin.base.BaseSkinParserXml;
import lib.kalu.skin.base.BaseSkinResource;
import com.kalu.skin.widget.CustomTitleView;

public class CustomViewTextColorResDeployer implements BaseSkinParserXml {
    @Override
    public void parserXml(@NonNull View view, @NonNull AttributeModel skinAttr, @NonNull BaseSkinResource resource) {

        if (!(view instanceof CustomTitleView)) {
            return;
        }
        CustomTitleView titleView = (CustomTitleView) view;
        Context applicationContext = view.getContext().getApplicationContext();

        if (SkinSetting.RES_TYPE_NAME_COLOR.equals(skinAttr.attrValueTypeName)) {
            titleView.setTextColor(resource.getColor(applicationContext, skinAttr.attrValueRefId));
        }
    }
}
