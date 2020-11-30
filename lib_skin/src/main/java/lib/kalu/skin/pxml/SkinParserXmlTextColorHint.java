package lib.kalu.skin.pxml;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import lib.kalu.skin.model.AttributeModel;
import lib.kalu.skin.setting.SkinSetting;
import lib.kalu.skin.base.BaseSkinParserXml;
import lib.kalu.skin.base.BaseSkinResource;

/**
 * description: 文字提示颜色属性的换肤支持（android:textColorHint）
 * created by kalu on 2020-11-10
 */
public class SkinParserXmlTextColorHint implements BaseSkinParserXml {
    @Override
    public void parserXml(@NonNull View view, @NonNull AttributeModel skinAttr, @NonNull BaseSkinResource resource) {
        if (!(view instanceof TextView)) {
            return;
        }

        TextView tv = (TextView) view;
        Context applicationContext = view.getContext().getApplicationContext();

        if (SkinSetting.RES_TYPE_NAME_COLOR.equals(skinAttr.attrValueTypeName)) {
            ColorStateList textHintColor = resource.getColorStateList(applicationContext, skinAttr.attrValueRefId);
            tv.setHintTextColor(textHintColor);
        }
    }
}
