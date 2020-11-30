package lib.kalu.skin.pstyle;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import lib.kalu.skin.attribute.SkinAttrParserManager;
import lib.kalu.skin.model.AttributeModel;
import lib.kalu.skin.setting.SkinSetting;
import lib.kalu.skin.attribute.SkinAttrParserManagerXml;
import lib.kalu.skin.base.BaseSkinParserStyle;
import lib.kalu.skin.util.ReflectUtil;

import java.util.Map;

/**
 * description: 解析Xml中的style属性，使支持style中定义的TextView的textColor支持换肤
 * created by kalu on 2020-11-10
 */
public class SkinParserStyleTextColor implements BaseSkinParserStyle {

    private static int[] sTextViewStyleList;
    private static int sTextViewTextColorStyleIndex;

    @Override
    public void parseStyle(View view, AttributeSet attrs, Map<String, AttributeModel> viewAttrs, String[] specifiedAttrList) {
        if (!TextView.class.isAssignableFrom(view.getClass())) {
            return;
        }
        Context context = view.getContext();
        int[] textViewStyleable = getTextViewStyleableList();
        int textViewStyleableTextColor = getTextViewTextColorStyleableIndex();

        TypedArray a = context.obtainStyledAttributes(attrs, textViewStyleable, 0, 0);
        if (a != null) {
            int n = a.getIndexCount();
            for (int j = 0; j < n; j++) {
                int attr = a.getIndex(j);
                if (attr == textViewStyleableTextColor &&
                        SkinSetting.isCurrentAttrSpecified(SkinAttrParserManagerXml.TEXT_COLOR, specifiedAttrList)) {
                    int colorResId = a.getResourceId(attr, -1);
                    AttributeModel skinAttr = SkinAttrParserManager.parseSkinAttr(context, SkinAttrParserManagerXml.TEXT_COLOR, colorResId);
                    if (skinAttr != null) {
                        viewAttrs.put(skinAttr.attrName, skinAttr);
                    }
                }
            }
            a.recycle();
        }
    }

    private static int[] getTextViewStyleableList() {
        if (sTextViewStyleList == null || sTextViewStyleList.length == 0) {
            sTextViewStyleList = (int[]) ReflectUtil.getField("com.android.internal.R$styleable", "TextView");
        }
        return sTextViewStyleList;
    }

    private static int getTextViewTextColorStyleableIndex() {
        if (sTextViewTextColorStyleIndex == 0) {
            Object o = ReflectUtil.getField("com.android.internal.R$styleable", "TextView_textColor");
            if (o != null) {
                sTextViewTextColorStyleIndex = (int) o;
            }
        }
        return sTextViewTextColorStyleIndex;
    }
}
