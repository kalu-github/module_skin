package lib.kalu.skin.pxml;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import lib.kalu.skin.model.AttributeModel;
import lib.kalu.skin.setting.SkinSetting;
import lib.kalu.skin.base.BaseSkinParserXml;
import lib.kalu.skin.base.BaseSkinResource;
import lib.kalu.skin.util.ReflectUtil;

public class SkinParserXmlActivityStatusBarColor implements BaseSkinParserXml {
    @Override
    public void parserXml(@NonNull View view, @NonNull AttributeModel skinAttr, @NonNull BaseSkinResource resource) {
        //the view is the window's DecorView
        Window window;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            //API23以上，DecorView独立成一个类，并持有mWindow对象
            window = (Window) ReflectUtil.getField(view, "mWindow");
        } else {
            //API23以下，DecorView是PhoneWindow的内部类，隐式持有PhoneWindow对象
            window = ReflectUtil.getExternalField(view);
        }
        if (window == null) {
            throw new IllegalArgumentException("view is not a DecorView, cannot get the window");
        }
        if (SkinSetting.RES_TYPE_NAME_COLOR.equals(skinAttr.attrValueTypeName)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                Context applicationContext = view.getContext().getApplicationContext();
                window.setStatusBarColor(resource.getColor(applicationContext, skinAttr.attrValueRefId));
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                //4.4及其以上
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        }
    }
}
