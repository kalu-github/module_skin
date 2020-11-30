package lib.kalu.skin.pxml;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.View;

import lib.kalu.skin.model.AttributeModel;
import lib.kalu.skin.setting.SkinSetting;
import lib.kalu.skin.base.BaseSkinParserXml;
import lib.kalu.skin.base.BaseSkinResource;

public class SkinParserXmlBackground implements BaseSkinParserXml {
    @Override
    public void parserXml(@NonNull View view, @NonNull AttributeModel skinAttr, @NonNull BaseSkinResource resource) {

        Context applicationContext = view.getContext().getApplicationContext();

        if(SkinSetting.RES_TYPE_NAME_COLOR.equals(skinAttr.attrValueTypeName)){
            view.setBackgroundColor(resource.getColor(applicationContext, skinAttr.attrValueRefId));
        }else if(SkinSetting.RES_TYPE_NAME_DRAWABLE.equals(skinAttr.attrValueTypeName)){
            Drawable bg = resource.getDrawable(applicationContext, skinAttr.attrValueRefId);
            view.setBackground(bg);
        }
    }
}
