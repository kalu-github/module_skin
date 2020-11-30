package lib.kalu.skin;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.res.Resources;
import android.support.annotation.ColorRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

import lib.kalu.skin.attribute.SkinAttrParserManager;
import lib.kalu.skin.attribute.SkinAttrParserManagerXml;
import lib.kalu.skin.base.BaseSkinParserXml;
import lib.kalu.skin.model.AttributeModel;
import lib.kalu.skin.resource.SkinResourceManagerDefault;
import lib.kalu.skin.util.CacheUtil;
import lib.kalu.skin.util.SkinUtil;

/**
 * description: Manager
 * created by kalu on 2020-11-10
 */
public final class SkinManager {

    // 使用这个map保存所有需要换肤的view和其对应的换肤属性及资源
    // 使用WeakHashMap两个作用，1.避免内存泄漏，2.避免重复的view被添加
    // 使用HashMap存SkinAttr，为了避免同一个属性值存了两次
    private volatile transient WeakHashMap<View, HashMap<String, AttributeModel>> SKIN_MAP = new WeakHashMap<>();

    private SkinManager() {
    }

    @MainThread
    public static SkinManager getManager() {
        return SkinManagerHolder.skinManager;
    }

    public static final class SkinManagerHolder {
        static SkinManager skinManager = new SkinManager();
    }

    // 在provider中自动初始化，不用手动调用
    @MainThread
    public void init(@NonNull Context context) {

        try {

            String string = CacheUtil.getCache(context, CacheUtil.Key.SKIN);
            JSONObject object = new JSONObject(string);

            String absolutePath = context.getFilesDir().getAbsolutePath();
            String skinName = object.optString("skin", null);
            Log.e("SkinManager", "init => skinName = " + skinName);

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(absolutePath);
            stringBuilder.append(File.separator);
            stringBuilder.append(skinName);

            String skinPath = stringBuilder.toString();
            Log.e("SkinManager", "init => skinPath = " + skinPath);

            // 容错
            if (TextUtils.isEmpty(skinName) || !new File(skinPath).exists()) {
                loadSkin(context, null, false);
            }
            // 默认
            else {
                loadSkin(context, skinPath, false);
            }

        } catch (Exception e) {
            Log.e("SkinManager", "init => " + e.getMessage(), e);
            loadSkin(context, null, true);
        }
    }

    /**
     * 更换皮肤时，通知view更换资源
     *
     * @return
     */
    private boolean notifyDataSetChanged() {

        try {

            View view;
            HashMap<String, AttributeModel> viewAttrs;
            Iterator iter = SKIN_MAP.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                view = (View) entry.getKey();
                viewAttrs = (HashMap<String, AttributeModel>) entry.getValue();
                if (view != null) {
                    deployViewSkinAttrs(view, viewAttrs);
                }
            }

            Log.e("SkinManager", "notifyDataSetChanged => status = true");
            return true;

        } catch (Exception e) {

            Log.e("SkinManager", "notifyDataSetChanged => " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * 加载App: 默认主题
     */
    public boolean loadDefault(@NonNull Context context) {
        boolean b = loadSkin(context, null);
        return b;
    }

    public boolean loadSkin(@NonNull Context context, @Nullable String skinPath) {

        boolean b = loadSkin(context, skinPath, true);
        return b;
    }

//    public @NonNull
//    int loadSkinLayout(@NonNull Context context, @NonNull String skinLayout, @LayoutRes int defaultLayout) {
//
//        try {
//
//            String string = CacheUtil.getCache(context, CacheUtil.Key.SKIN);
//            JSONObject object = new JSONObject(string);
//
//            String absolutePath = context.getFilesDir().getAbsolutePath();
//            String skinName = object.optString("skin", null);
//            Log.e("SkinManager", "loadSkinLayout => skinName = " + skinName);
//
//            StringBuilder stringBuilder = new StringBuilder();
//            stringBuilder.append(absolutePath);
//            stringBuilder.append(File.separator);
//            stringBuilder.append(skinName);
//
//            String skinPath = stringBuilder.toString();
//            Log.e("SkinManager", "loadSkinLayout => skinPath = " + skinPath);
//
//            // 容错
//            if (TextUtils.isEmpty(skinName) || !new File(skinPath).exists()) {
//                return defaultLayout;
//            }
//            // 默认
//            else {
//
//                String packageName = getPluginPackageName();
//                Resources resources = getPluginResources();
//                int identifier = resources.getIdentifier(skinLayout, "layout", packageName);
//                return identifier;
//            }
//
//        } catch (Exception e) {
//            Log.e("SkinManager", "loadSkinLayout => " + e.getMessage(), e);
//            return defaultLayout;
//        }
//    }

    public @NonNull
    int getIdentifierLayout(@NonNull String name, @LayoutRes int layout) {

        try {

            String packageName = getPluginPackageName();
            Resources resources = getPluginResources();
            Log.e("SkinManager", "packageName => " + packageName);
            Log.e("SkinManager", "resources => " + resources);

            int identifier = resources.getIdentifier(name, "layout", packageName);
            return identifier;

        } catch (Exception e) {
            Log.e("SkinManager", "getIdentifierLayout => " + e.getMessage(), e);
            return layout;
        }
    }

    public @Nullable
    String getResourceName(@IdRes int res) {

        try {

            Resources resources = getPluginResources();
            Log.e("SkinManager", "getResourceName => resources = " + resources);

            String name = resources.getResourceName(res);
            Log.e("SkinManager", "getResourceName => name = " + name);
            return name;

        } catch (Exception e) {
            Log.e("SkinManager", "getResourceName => " + e.getMessage(), e);
            return null;
        }
    }

    public @Nullable
    View getContentView(@NonNull Context context, @NonNull String name) {

        try {

            String packageName = getPluginPackageName();
            Resources resources = getPluginResources();
            int identifier = resources.getIdentifier(name, "layout", packageName);
            LayoutInflater localinflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View inflate = localinflater.inflate(identifier, null);
            return inflate;

        } catch (Exception e) {
            Log.e("SkinManager", "loadSkinLayout => " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * 加载已经用户默认设置的皮肤资源
     *
     * @param context
     * @param skinPath    皮肤绝对路径
     * @param updateCache 是否更新缓存文件
     * @return
     */
    private boolean loadSkin(@NonNull Context context, @Nullable String skinPath, boolean updateCache) {
        Log.e("SkinManager", "loadSkin => context = " + context + ", updateCache = " + updateCache + ", skinPath = " + skinPath);

        // 默认主题
        if (null == context || TextUtils.isEmpty(skinPath) || !new File(skinPath).exists()) {

            // 更新变量
            SkinResourceManagerDefault.getManager().setPluginInfo(null, null, null);
            boolean status = notifyDataSetChanged();

            // 更新缓存
            if (updateCache && status) {

                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("{\"skin\":\"\"}");

                String toString = stringBuilder.toString();
                CacheUtil.setCache(context, CacheUtil.Key.SKIN, toString);
                Log.e("SkinManager", "loadSkin => 更新缓存1, " + CacheUtil.getCache(context, CacheUtil.Key.SKIN));
            }

            return status;
        }
        // 加载主题
        else {

            PackageInfo packageInfo = SkinUtil.getInstance(context).getPackageInfo(context, skinPath);
            Resources pluginResources = SkinUtil.getInstance(context).getPluginResources(context, skinPath);

            // 错误
            if (null == packageInfo || null == pluginResources || TextUtils.isEmpty(packageInfo.packageName)) {
                boolean b = loadSkin(context, null, true);
                return b;
            }
            // 成功
            else {

                SkinResourceManagerDefault.getManager().setPluginInfo(pluginResources, packageInfo.packageName, skinPath);
                boolean result = notifyDataSetChanged();

                // 失败
                if (!result) {
                    boolean b = loadSkin(context, null, true);
                    return b;
                }
                // 成功
                else {

                    if (updateCache) {

                        int i = skinPath.lastIndexOf(File.separator);
                        String skinName = skinPath.substring(++i);

                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("{\"skin\":\"");
                        stringBuilder.append(skinName);
                        stringBuilder.append("\"}");

                        String toString = stringBuilder.toString();
                        CacheUtil.setCache(context, CacheUtil.Key.SKIN, toString);
                        Log.e("SkinManager", "loadSkin => 更新缓存2, " + CacheUtil.getCache(context, CacheUtil.Key.SKIN));
                    }

                    return true;
                }
            }
        }
    }

    public void setTextViewColor(@NonNull Context context, @NonNull View view, int resId) {
        setSkinViewResource(context, view, SkinAttrParserManagerXml.TEXT_COLOR, resId);
    }

    public void setHintTextColor(@NonNull Context context, @NonNull View view, int resId) {
        setSkinViewResource(context, view, SkinAttrParserManagerXml.TEXT_COLOR_HINT, resId);
    }

    public void setViewBackground(@NonNull Context context, View view, int resId) {
        setSkinViewResource(context, view, SkinAttrParserManagerXml.BACKGROUND, resId);
    }

    public void setImageDrawable(@NonNull Context context, View view, int resId) {
        setSkinViewResource(context, view, SkinAttrParserManagerXml.IMAGE_SRC, resId);
    }

    public void setListViewSelector(@NonNull Context context, View view, int resId) {
        setSkinViewResource(context, view, SkinAttrParserManagerXml.LIST_SELECTOR, resId);
    }

    public void setListViewDivider(@NonNull Context context, View view, int resId) {
        setSkinViewResource(context, view, SkinAttrParserManagerXml.DIVIDER, resId);
    }

    public void setWindowStatusBarColor(@NonNull Context context, Window window, @ColorRes int resId) {

        if (resId == -1)
            return;

        View decorView = window.getDecorView();
        setSkinViewResource(context, decorView, SkinAttrParserManagerXml.ACTIVITY_STATUS_BAR_COLOR, resId);
    }

    public void setProgressBarIndeterminateDrawable(@NonNull Context context, View view,
                                                    int resId) {
        setSkinViewResource(context, view, SkinAttrParserManagerXml.PROGRESSBAR_INDETERMINATE_DRAWABLE, resId);
    }

    /**
     * 设置可以换肤的view的属性
     *
     * @param view     设置的view
     * @param attrName 这个取值只能是 {@link SkinAttrParserManagerXml#BACKGROUND} {@link SkinAttrParserManagerXml#DIVIDER} {@link SkinAttrParserManagerXml#TEXT_COLOR}
     *                 {@link SkinAttrParserManagerXml#LIST_SELECTOR} {@link SkinAttrParserManagerXml#IMAGE_SRC} 等等
     * @param resId    资源id
     */
    @MainThread
    public void setSkinViewResource(@NonNull Context context, @NonNull View
            view, @NonNull String attrName, int resId) {
        if (TextUtils.isEmpty(attrName)) {
            return;
        }

        AttributeModel attr = SkinAttrParserManager.parseSkinAttr(context, attrName, resId);
        if (attr != null) {
            doSkinAttrsDeploying(view, attr);
            saveSkinView(view, attr);
        }
    }

    //将View保存到被监听的view列表中,使得在换肤时能够及时被更新
    public void saveSkinView(@NonNull View view, @Nullable HashMap<String, AttributeModel> map) {

        if (null == view || null == map || map.size() == 0)
            return;

        HashMap<String, AttributeModel> originalSkinAttr = SKIN_MAP.get(view);
        if (originalSkinAttr != null && originalSkinAttr.size() > 0) {
            originalSkinAttr.putAll(map);
            SKIN_MAP.put(view, originalSkinAttr);
        } else {
            SKIN_MAP.put(view, map);
        }
    }

    private void saveSkinView(View view, AttributeModel viewAttr) {
        if (view == null || viewAttr == null) {
            return;
        }
        HashMap<String, AttributeModel> viewAttrs = new HashMap<>();
        viewAttrs.put(viewAttr.attrName, viewAttr);
        saveSkinView(view, viewAttrs);
    }

    public void removeObservableView(View view) {
        SKIN_MAP.remove(view);
    }

    public void clear() {
        SKIN_MAP.clear();
    }

    public void deployViewSkinAttrs(@Nullable View view, @Nullable HashMap<String, AttributeModel> viewAttrs) {
        if (view == null || viewAttrs == null || viewAttrs.size() == 0) {
            return;
        }
        Iterator iter = viewAttrs.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            AttributeModel attr = (AttributeModel) entry.getValue();
            doSkinAttrsDeploying(view, attr);
        }
    }

    //将新皮肤的属性部署到view上
    private void doSkinAttrsDeploying(@Nullable View view, @Nullable AttributeModel
            skinAttr) {
        BaseSkinParserXml deployer = SkinAttrParserManagerXml.of(skinAttr);
        if (deployer != null) {
            deployer.parserXml(view, skinAttr, SkinResourceManagerDefault.getManager());
        }
    }

    public String getPluginPackageName() {
        return SkinResourceManagerDefault.getManager().getPluginPackageName();
    }

    public Resources getPluginResources() {
        return SkinResourceManagerDefault.getManager().getPluginResource();
    }

    public boolean isUsingDefaultSkin() {
        return null == getPluginResources();
    }

    public String getPluginSkinPath() {
        return SkinResourceManagerDefault.getManager().getPluginSkinPath();
    }

    public int getSkinViewMapSize() {
        return SKIN_MAP.size();
    }
}