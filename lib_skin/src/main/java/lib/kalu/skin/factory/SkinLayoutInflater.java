package lib.kalu.skin.factory;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.LayoutInflater.Factory;
import android.view.View;

import java.util.HashMap;

import lib.kalu.skin.SkinManager;
import lib.kalu.skin.model.AttributeModel;
import lib.kalu.skin.attribute.SkinAttrParserManager;
import lib.kalu.skin.setting.SkinSetting;
import lib.kalu.skin.util.ReflectUtil;

/**
 * description: 实现LayoutInflater
 * created by kalu on 2020-11-10
 */
public class SkinLayoutInflater implements Factory {

    private static final String TAG = SkinLayoutInflater.class.getSimpleName();
    private Factory mViewCreateFactory;

    public static void setFactory(LayoutInflater inflater) {
        inflater.setFactory(new SkinLayoutInflater());
    }

    public static void setFactory(@NonNull final Activity activity) {

        LayoutInflater inflater = activity.getLayoutInflater();
        SkinLayoutInflater factory = new SkinLayoutInflater();

        // AppCompatActivity本身包含一个factory,将TextView等转换为AppCompatTextView.java, 参考：AppCompatDelegateImplV9.java
        if (activity instanceof AppCompatActivity) {
            factory.setInterceptFactory(new Factory() {
                @Override
                public View onCreateView(String name, Context context, AttributeSet attrs) {

                    AppCompatDelegate delegate = ((AppCompatActivity) activity).getDelegate();
                    return delegate.createView(null, name, context, attrs);
                }
            });
        }
        inflater.setFactory(factory);
    }

    //因为LayoutInflater的setFactory方法只能调用一次，当框架外需要处理view的创建时，可以调用此方法
    public void setInterceptFactory(Factory factory) {
        mViewCreateFactory = factory;
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        if (SkinSetting.DEBUG) {
            Log.d(TAG, "SkinInflaterFactory onCreateView(), create view name=" + name + "  ");
        }
        View view = null;
        if (mViewCreateFactory != null) {
            //给框架外提供创建View的机会
            view = mViewCreateFactory.onCreateView(name, context, attrs);
        }
        if (isSupportSkin(attrs)) {
            if (view == null) {
                view = createView(context, name, attrs);
            }
            if (view != null) {
                parseAndSaveSkinAttr(attrs, view);
            }
        }

        return view;
    }

    private View createView(Context context, String name, AttributeSet attrs) {
        View view = null;
        try {
            LayoutInflater inflater = LayoutInflater.from(context);
            assertInflaterContext(inflater, context);

            if (-1 == name.indexOf('.')) {
                if ("View".equals(name) || "ViewStub".equals(name) || "ViewGroup".equals(name)) {
                    view = inflater.createView(name, "android.view.", attrs);
                }
                if (view == null) {
                    view = inflater.createView(name, "android.widget.", attrs);
                }
                if (view == null) {
                    view = inflater.createView(name, "android.webkit.", attrs);
                }
            } else {
                view = inflater.createView(name, null, attrs);
            }

        } catch (Exception ex) {
            Log.e(TAG, "createView(), create view failed", ex);
            view = null;
        }
        return view;
    }

    //只有在xml中设置了View的属性skin:enable，才支持xml属性换肤
    public boolean isSupportSkin(@NonNull AttributeSet attrs) {
        boolean enable = attrs.getAttributeBooleanValue(SkinSetting.SKIN_XML_NAMESPACE, SkinSetting.ATTR_SKIN_ENABLE, false);
        return enable;
    }

    //获取xml中指定的换肤属性，比如：skin:attrs = "textColor|background", 假如为空，表示支持所有能够支持的换肤属性
    private @Nullable String getXmlSpecifiedAttrs(@NonNull AttributeSet attrs) {
        return attrs.getAttributeValue(SkinSetting.SKIN_XML_NAMESPACE, SkinSetting.SUPPORTED_ATTR_SKIN_LIST);
    }

    private void parseAndSaveSkinAttr(AttributeSet attrs, View view) {
        String specifiedAttrs = getXmlSpecifiedAttrs(attrs);
        String[] specifiedAttrsList = null;
        if (specifiedAttrs != null && specifiedAttrs.trim().length() > 0) {
            specifiedAttrsList = specifiedAttrs.split("\\|");
        }
        HashMap<String, AttributeModel> viewAttrs = SkinAttrParserManager.parseSkinAttr(attrs, view, specifiedAttrsList);
        if (viewAttrs == null || viewAttrs.size() == 0) {
            return;
        }

        //设置view的皮肤属性
        SkinManager.getManager().deployViewSkinAttrs(view, viewAttrs);
        //save view attribute
        SkinManager.getManager().saveSkinView(view, viewAttrs);
    }

    //在低版本系统中会出inflaterContext为空的问题， 因此需要处理inflaterContext为空的情况
    private void assertInflaterContext(LayoutInflater inflater, Context context) {
        Context inflaterContext = inflater.getContext();
        if (inflaterContext == null) {
            ReflectUtil.setField(inflater, "mContext", context);
        }

        //设置mConstructorArgs的第一个参数context
        Object[] constructorArgs = (Object[]) ReflectUtil.getField(inflater, "mConstructorArgs");
        if (null == constructorArgs || constructorArgs.length < 2) {
            //异常，一般不会发生
            constructorArgs = new Object[2];
            ReflectUtil.setField(inflater, "mConstructorArgs", constructorArgs);
        }

        //如果mConstructorArgs的第一个参数为空，则设置为mContext
        if (null == constructorArgs[0]) {
            constructorArgs[0] = inflater.getContext();
        }
    }
}
