package lib.kalu.skin.pxml;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ProgressBar;

import lib.kalu.skin.model.AttributeModel;
import lib.kalu.skin.setting.SkinSetting;
import lib.kalu.skin.base.BaseSkinParserXml;
import lib.kalu.skin.base.BaseSkinResource;

public class SkinParserXmlProgressBarIndeterminateDrawable implements BaseSkinParserXml {
    @Override
    public void parserXml(@NonNull View view, @NonNull AttributeModel skinAttr, @NonNull BaseSkinResource resource) {
        if (!(view instanceof ProgressBar)) {
            return;
        }

        ProgressBar pb = (ProgressBar) view;
        Drawable drawable = null;
        Context applicationContext = view.getContext().getApplicationContext();

        if (SkinSetting.RES_TYPE_NAME_COLOR.equals(skinAttr.attrValueTypeName)) {
            drawable = new ColorDrawable(resource.getColor(applicationContext, skinAttr.attrValueRefId));
        } else if (SkinSetting.RES_TYPE_NAME_DRAWABLE.equals(skinAttr.attrValueTypeName)) {
            drawable = resource.getDrawable(applicationContext, skinAttr.attrValueRefId);
        }
        if (drawable != null) {
            pb.setIndeterminateDrawable(drawable);
        }
    }
}
