package lib.kalu.skin.pxml;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ListView;

import lib.kalu.skin.model.AttributeModel;
import lib.kalu.skin.setting.SkinSetting;
import lib.kalu.skin.base.BaseSkinParserXml;
import lib.kalu.skin.base.BaseSkinResource;

public class SkinParserXmlListViewDivider implements BaseSkinParserXml {

    @Override
    public void parserXml(@NonNull View view, @NonNull AttributeModel skinAttr, @NonNull BaseSkinResource resource) {
        if (!(view instanceof ListView)) {
            return;
        }
        ListView listView = (ListView) view;
        Context applicationContext = view.getContext().getApplicationContext();

        if (SkinSetting.RES_TYPE_NAME_COLOR.equals(skinAttr.attrValueTypeName)) {
            int color = resource.getColor(applicationContext, skinAttr.attrValueRefId);
            listView.setDivider(new ColorDrawable(color));
        } else if (SkinSetting.RES_TYPE_NAME_DRAWABLE.equals(skinAttr.attrValueTypeName)) {
            listView.setDivider(resource.getDrawable(applicationContext, skinAttr.attrValueRefId));
        }
    }
}
