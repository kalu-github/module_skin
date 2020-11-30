package lib.kalu.skin.base;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

import java.util.WeakHashMap;

/**
 * description: 换肤功能，替换资源管理接口
 * created by kalu on 2020-11-10
 */
public interface BaseSkinResource {

    String WR_KEY_PACKAGENAME = "wr_key_packagename";
    String WR_KEY_RESOURCES = "wr_key_resources";
    String WR_KEY_SKINPATH = "wr_key_skinpath";
    WeakHashMap<String, Object> wrMap = new WeakHashMap<>(3);

    String getPluginPackageName();

    Resources getPluginResource();

    String getPluginSkinPath();

    void setPluginInfo(@NonNull Resources resources, @NonNull String packageName, @NonNull String skinPath);

    int getColor(@NonNull Context context, @ColorRes int resId) throws Resources.NotFoundException;

    ColorStateList getColorStateList(@NonNull Context context, @ColorRes int resId) throws Resources.NotFoundException;

    Drawable getDrawable(@NonNull Context context, @DrawableRes int resId) throws Resources.NotFoundException;

    Drawable getDrawableForMapmip(@NonNull Context context, @DrawableRes int resId) throws Resources.NotFoundException;
}
