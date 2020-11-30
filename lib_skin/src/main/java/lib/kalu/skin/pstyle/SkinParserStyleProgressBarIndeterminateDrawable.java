package lib.kalu.skin.pstyle;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;

import lib.kalu.skin.attribute.SkinAttrParserManager;
import lib.kalu.skin.attribute.SkinAttrParserManagerXml;
import lib.kalu.skin.model.AttributeModel;
import lib.kalu.skin.setting.SkinSetting;
import lib.kalu.skin.base.BaseSkinParserStyle;
import lib.kalu.skin.util.ReflectUtil;

import java.util.Map;

/**
 * description: 解析Xml中的style属性，使支持style中定义的ProgressBar的indeterminateDrawable属性支持换肤
 * created by kalu on 2020-11-10
 */
public class SkinParserStyleProgressBarIndeterminateDrawable implements BaseSkinParserStyle {

    private static int[] sProgressBarStyleList;
    private static int sProgressBarIndeterminateDrawableIndex;

    @Override
    public void parseStyle(View view, AttributeSet attrs, Map<String, AttributeModel> viewAttrs, String[] specifiedAttrList) {
        if (!ProgressBar.class.isAssignableFrom(view.getClass())) {
            return;
        }
        Context context = view.getContext();
        int[] progressBarStyleList = getProgressBarStyleable();
        int progressBarIndeterminateDrawableIndex = getProgressBarIndeterminateDrawableIndex();

        final TypedArray a = context.obtainStyledAttributes(attrs, progressBarStyleList, 0, 0);

        if (a != null) {
            int n = a.getIndexCount();
            for (int j = 0; j < n; j++) {
                int attr = a.getIndex(j);
                if (attr == progressBarIndeterminateDrawableIndex &&
                        SkinSetting.isCurrentAttrSpecified(SkinAttrParserManagerXml.PROGRESSBAR_INDETERMINATE_DRAWABLE, specifiedAttrList)) {
                    int drawableResId = a.getResourceId(attr, -1);
                    AttributeModel skinAttr = SkinAttrParserManager.parseSkinAttr(context, SkinAttrParserManagerXml.PROGRESSBAR_INDETERMINATE_DRAWABLE, drawableResId);
                    if (skinAttr != null) {
                        viewAttrs.put(skinAttr.attrName, skinAttr);
                    }
                }
            }
            a.recycle();
        }
    }

    private static int[] getProgressBarStyleable() {
        if (sProgressBarStyleList == null || sProgressBarStyleList.length == 0) {
            sProgressBarStyleList = (int[]) ReflectUtil.getField("com.android.internal.R$styleable", "ProgressBar");
        }
        return sProgressBarStyleList;
    }

    private static int getProgressBarIndeterminateDrawableIndex() {
        if (sProgressBarIndeterminateDrawableIndex == 0) {
            Object o = ReflectUtil.getField("com.android.internal.R$styleable", "ProgressBar_indeterminateDrawable");
            if (o != null) {
                sProgressBarIndeterminateDrawableIndex = (int) o;
            }
        }
        return sProgressBarIndeterminateDrawableIndex;
    }
}
