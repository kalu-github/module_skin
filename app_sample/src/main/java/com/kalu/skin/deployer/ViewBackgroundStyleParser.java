package com.kalu.skin.deployer;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import lib.kalu.skin.attribute.SkinAttrParserManagerXml;
import lib.kalu.skin.model.AttributeModel;
import lib.kalu.skin.setting.SkinSetting;
import lib.kalu.skin.attribute.SkinAttrParserManager;
import lib.kalu.skin.base.BaseSkinParserStyle;
import lib.kalu.skin.util.ReflectUtil;

import java.util.Map;

/**
 * description: 解析Xml中的style属性，使支持style中定义的View的background支持换肤
 * created by kalu on 2020-11-10
 */
public class ViewBackgroundStyleParser implements BaseSkinParserStyle {

    private static int[] sViewStyleList;
    private static int sViewBackgroundStyleIndex;

    @Override
    public void parseStyle(View view, AttributeSet attrs, Map<String, AttributeModel> viewAttrs, String[] specifiedAttrList) {
        Context context = view.getContext();
        int[] viewStyleable = getTextViewStyleableList();
        int viewStyleableBackground = getTextViewTextColorStyleableIndex();

        TypedArray a = context.obtainStyledAttributes(attrs, viewStyleable, 0, 0);
        if (a != null) {
            int n = a.getIndexCount();
            for (int j = 0; j < n; j++) {
                int attr = a.getIndex(j);
                if (attr == viewStyleableBackground &&
                        SkinSetting.isCurrentAttrSpecified(SkinAttrParserManagerXml.BACKGROUND, specifiedAttrList)) {
                    int drawableResId = a.getResourceId(attr, -1);
                    AttributeModel skinAttr = SkinAttrParserManager.parseSkinAttr(context, SkinAttrParserManagerXml.BACKGROUND, drawableResId);
                    if (skinAttr != null) {
                        viewAttrs.put(skinAttr.attrName, skinAttr);
                    }
                }
            }
            a.recycle();
        }
    }

    private static int[] getTextViewStyleableList() {
        if (sViewStyleList == null || sViewStyleList.length == 0) {
            sViewStyleList = (int[]) ReflectUtil.getField("com.android.internal.R$styleable", "View");
        }
        return sViewStyleList;
    }

    private static int getTextViewTextColorStyleableIndex() {
        if (sViewBackgroundStyleIndex == 0) {
            Object o = ReflectUtil.getField("com.android.internal.R$styleable", "View_background");
            if (o != null) {
                sViewBackgroundStyleIndex = (int) o;
            }
        }
        return sViewBackgroundStyleIndex;
    }
}
