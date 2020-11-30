package lib.kalu.skin.attribute;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import lib.kalu.skin.model.AttributeModel;
import lib.kalu.skin.setting.SkinSetting;

import java.util.HashMap;

/**
 * description: 解析xml中的换肤属性
 * created by kalu on 2020-11-10
 */
public class SkinAttrParserManager {

    private static final String TAG = SkinAttrParserManager.class.getSimpleName();

    public static HashMap<String, AttributeModel> parseSkinAttr(AttributeSet attrs, View view, String[] specifiedAttrList) {
        if (view == null) {
            return null;
        }
        //使用hashmap避免属性重复添加
        HashMap<String, AttributeModel> viewAttrs = new HashMap<>();
        Context context = view.getContext();

        //先处理style类型, 避免布局中定义的属性被style中定义的属性覆盖
        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            String attrName = attrs.getAttributeName(i);
            //处理控件中设置的style属性
            if ("style".equals(attrName)) {
                SkinAttrParserManagerStyle.parseStyle(view, attrs, viewAttrs, specifiedAttrList);
            }
        }

        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            String attrName = attrs.getAttributeName(i);
            String attrValue = attrs.getAttributeValue(i);
            if (SkinSetting.DEBUG) {
                Log.d(TAG, " parseSkinAttr attrName=" + attrName + " attrValue=" + attrValue + " view=" + view.getClass().getSimpleName());
            }

            if (!SkinAttrParserManagerXml.isSupportedAttr(attrName)) {
                continue;
            }
            //当前属性是否在xml中使用skin:attr="textColor|background"方式来指定，指定了，只替换指定属性，未指定替换全部
            if (!SkinSetting.isCurrentAttrSpecified(attrName, specifiedAttrList)) {
                continue;
            }

            //attrName=textColor attrValue=@2131492918 view=TextView
            if (!attrValue.startsWith("@")) {
                continue;
            }

            AttributeModel skinAttr = null;
            try {
                skinAttr = getSkinAttrFromId(context, attrName, attrValue);
            } catch (NumberFormatException ex) {
                Log.e(TAG, "parseSkinAttr() error happened", ex);
                skinAttr = getSkinAttrBySplit(context, attrName, attrValue);
            } catch (Resources.NotFoundException ex) {
                Log.e(TAG, "parseSkinAttr() error happened", ex);
            }

            if (skinAttr != null) {
                viewAttrs.put(skinAttr.attrName, skinAttr);
            }
        }
        return viewAttrs;
    }

    public static AttributeModel parseSkinAttr(Context context, String attrName, int resId) {
        if (context == null) {
            return null;
        }
        AttributeModel skinAttr = null;
        try {
            String attrValueName = context.getResources().getResourceEntryName(resId);
            String attrValueType = context.getResources().getResourceTypeName(resId);
            skinAttr = new AttributeModel(attrName, resId, attrValueName, attrValueType);
        } catch (Exception ex) {
            Log.e(TAG, " parseSkinAttr--- error happened ", ex);
        }
        return skinAttr;
    }

    private static AttributeModel getSkinAttrBySplit(Context context, String attrName, String attrValue) {
        try {
            int dividerIndex = attrValue.indexOf("/");
            String entryName = attrValue.substring(dividerIndex + 1, attrValue.length());
            String typeName = attrValue.substring(1, dividerIndex);
            int id = context.getResources().getIdentifier(entryName, typeName, context.getPackageName());
            return new AttributeModel(attrName, id, entryName, typeName);
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "getSkinAttrBySplit error happened", e);
        }
        return null;
    }

    private static AttributeModel getSkinAttrFromId(Context context, String attrName, String attrValue) {
        int id = Integer.parseInt(attrValue.substring(1));
        if (id == 0) {
            return null;
        }
        return parseSkinAttr(context, attrName, id);
    }
}
