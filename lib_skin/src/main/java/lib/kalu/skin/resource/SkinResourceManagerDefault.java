package lib.kalu.skin.resource;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import lib.kalu.skin.base.BaseSkinResource;
import lib.kalu.skin.setting.SkinSetting;

/**
 * description: 单例：静态内部类
 * created by kalu on 2020-11-10
 */
public final class SkinResourceManagerDefault implements BaseSkinResource {

    private static final String TAG = SkinResourceManagerDefault.class.getSimpleName();

    private SkinResourceManagerDefault() {
    }

    public final static SkinResourceManagerDefault getManager() {
        return SkinResourceManagerImplHolder.single;
    }

    private final static class SkinResourceManagerImplHolder {
        private final static SkinResourceManagerDefault single = new SkinResourceManagerDefault();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public String getPluginPackageName() {
        String packageName = (String) wrMap.get(WR_KEY_PACKAGENAME);
        return packageName;
    }

    @Override
    public Resources getPluginResource() {
        Resources resources = (Resources) wrMap.get(WR_KEY_RESOURCES);
        return resources;
    }

    @Override
    public String getPluginSkinPath() {
        String skinPath = (String) wrMap.get(WR_KEY_SKINPATH);
        return skinPath;
    }

    @Override
    public void setPluginInfo(@NonNull Resources resources, @NonNull String packageName, @NonNull String skinPath) {
        wrMap.put(WR_KEY_RESOURCES, resources);
        wrMap.put(WR_KEY_PACKAGENAME, packageName);
        wrMap.put(WR_KEY_SKINPATH, skinPath);
    }

    @Override
    public int getColor(@NonNull Context context, @ColorRes int resId) throws Resources.NotFoundException {

        int originColor = ContextCompat.getColor(context, resId);
        if (null == getPluginResource()) {
            return originColor;
        }

        // 获取换肤资源
        Resources resources = context.getResources();
        String resourceEntryName = resources.getResourceEntryName(resId);

        int trueResId = getPluginResource().getIdentifier(resourceEntryName, SkinSetting.RES_TYPE_NAME_COLOR, getPluginPackageName());
        int trueColor = 0;

        try {
            trueColor = getPluginResource().getColor(trueResId);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            trueColor = originColor;
        }

        return trueColor;
    }


    @Override
    public ColorStateList getColorStateList(@NonNull Context context, @ColorRes int resId) throws Resources.NotFoundException {
        boolean isExtendSkin = true;

        if (null == getPluginResource()) {
            isExtendSkin = false;
        }

        Resources mDefaultResources = context.getResources();
        String resName = mDefaultResources.getResourceEntryName(resId);
        if (isExtendSkin) {
            int trueResId = getPluginResource().getIdentifier(resName, SkinSetting.RES_TYPE_NAME_COLOR, getPluginPackageName());
            ColorStateList trueColorList = null;
            if (trueResId == 0) { // 如果皮肤包没有复写该资源，但是需要判断是否是ColorStateList
                try {
                    ColorStateList originColorList = mDefaultResources.getColorStateList(resId);
                    return originColorList;
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                    if (SkinSetting.DEBUG) {
                        Log.d(TAG, "resName = " + resName + " NotFoundException : " + e.getMessage());
                    }
                }
            } else {
                try {
                    trueColorList = getPluginResource().getColorStateList(trueResId);
                    if (SkinSetting.DEBUG) {
                        Log.d(TAG, "getColorStateList the trueColorList is = " + trueColorList);
                    }
                    return trueColorList;
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                    Log.e(TAG, "resName = " + resName + " NotFoundException :" + e.getMessage());
                }
            }
        } else {
            try {
                ColorStateList originColorList = mDefaultResources.getColorStateList(resId);
                return originColorList;
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
                Log.e(TAG, "resName = " + resName + " NotFoundException :" + e.getMessage());
            }
        }


        int[][] states = new int[1][1];
        return new ColorStateList(states, new int[]{mDefaultResources.getColor(resId)});
    }

    @Override
    public Drawable getDrawable(@NonNull Context context, @DrawableRes int resId) throws Resources.NotFoundException {

        Resources mDefaultResources = context.getResources();
        Drawable originDrawable = mDefaultResources.getDrawable(resId);
        if (null == getPluginResource()) {
            return originDrawable;
        }
        String resName = mDefaultResources.getResourceEntryName(resId);

        int trueResId = getPluginResource().getIdentifier(resName, SkinSetting.RES_TYPE_NAME_DRAWABLE, getPluginPackageName());

        Drawable trueDrawable;
        try {
            if (android.os.Build.VERSION.SDK_INT < 22) {
                trueDrawable = getPluginResource().getDrawable(trueResId);
            } else {
                trueDrawable = getPluginResource().getDrawable(trueResId, null);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            trueDrawable = originDrawable;
        }
        return trueDrawable;
    }

    @Override
    public Drawable getDrawableForMapmip(@NonNull Context context, @DrawableRes int resId) throws Resources.NotFoundException {

        Resources mDefaultResources = context.getResources();
        Drawable originDrawable = mDefaultResources.getDrawable(resId);
        if (null == getPluginResource()) {
            return originDrawable;
        }
        String resName = mDefaultResources.getResourceEntryName(resId);

        int trueResId = getPluginResource().getIdentifier(resName, SkinSetting.RES_TYPE_NAME_MIPMAP, getPluginPackageName());

        Drawable trueDrawable;
        try {
            if (android.os.Build.VERSION.SDK_INT < 22) {
                trueDrawable = getPluginResource().getDrawable(trueResId);
            } else {
                trueDrawable = getPluginResource().getDrawable(trueResId, null);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            trueDrawable = originDrawable;
        }
        return trueDrawable;
    }
}
