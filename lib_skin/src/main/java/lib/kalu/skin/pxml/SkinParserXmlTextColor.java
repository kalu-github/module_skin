package lib.kalu.skin.pxml;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import lib.kalu.skin.base.BaseSkinParserXml;
import lib.kalu.skin.base.BaseSkinResource;
import lib.kalu.skin.model.AttributeModel;
import lib.kalu.skin.setting.SkinSetting;

public class SkinParserXmlTextColor implements BaseSkinParserXml {

    @Override
    public void parserXml(@NonNull View view, @NonNull AttributeModel skinAttr, @NonNull BaseSkinResource resource) {

        if (!(view instanceof TextView) || !SkinSetting.RES_TYPE_NAME_COLOR.equals(skinAttr.attrValueTypeName))
            return;

        TextView textView = (TextView) view;
        Context applicationContext = view.getContext().getApplicationContext();
        textView.setTextColor(resource.getColorStateList(applicationContext, skinAttr.attrValueRefId));
    }
}