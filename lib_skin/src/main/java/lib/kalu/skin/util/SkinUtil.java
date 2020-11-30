package lib.kalu.skin.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import dalvik.system.DexClassLoader;
import lib.kalu.skin.model.SkinModel;

/**
 * description: 插件工具类
 * created by kalu on 2020-11-10
 */
public final class SkinUtil {

    //    private Context mContext;
    private final Map<String, SkinModel> mPluginInfoHolder = new HashMap<>();
    private static SkinUtil sInstance;
    private static final String TAG = "PluginLoadUtils";

    private SkinUtil(Context context) {
//        mContext = context.getApplicationContext();
    }

    public static SkinUtil getInstance(Context context) {
        if (sInstance == null) {
            synchronized (SkinUtil.class) {
                if (sInstance == null) {
                    sInstance = new SkinUtil(context);
                }
            }
        }
        return sInstance;
    }

    public static SkinUtil getInstance() {
        return sInstance;
    }

    public SkinModel install(@NonNull Context context, @NonNull String apkPath) {
        SkinModel pluginInfo = mPluginInfoHolder.get(apkPath);
        if (pluginInfo != null) {
            return pluginInfo;
        }

        DexClassLoader dexClassLoader = createDexClassLoader(context, apkPath);
        AssetManager assetManager = createAssetManager(apkPath);
        Resources resources = createResources(context, assetManager);
        Resources.Theme theme = null;
        if (resources != null) {
            theme = resources.newTheme();
//            theme.applyStyle(R.style.AppTheme, false);
        }
        PackageInfo packageInfo = createPackageInfo(context, apkPath);

        pluginInfo = new SkinModel(apkPath, dexClassLoader, resources, theme, packageInfo);
        mPluginInfoHolder.put(apkPath, pluginInfo);
        return pluginInfo;
    }

    public DexClassLoader getClassLoader(@NonNull Context context, @NonNull String apkPath) {
        SkinModel pluginInfo = mPluginInfoHolder.get(apkPath);
        if (pluginInfo != null) {
            ClassLoader loader = pluginInfo.getClassLoader();
            if (loader != null) {
                return (DexClassLoader) loader;
            }
        }
        return createDexClassLoader(context, apkPath);
    }

    public PackageInfo getPackageInfo(@NonNull Context context, @NonNull String apkPath) {
        SkinModel pluginInfo = mPluginInfoHolder.get(apkPath);
        if (pluginInfo != null) {
            PackageInfo info = pluginInfo.getPackageInfo();
            if (info != null) {
                return info;
            }
        }
        return createPackageInfo(context, apkPath);
    }

    public Resources getPluginResources(@NonNull Context context, @NonNull String apkPath) {
        SkinModel pluginInfo = mPluginInfoHolder.get(apkPath);
        if (pluginInfo != null) {
            Resources res = pluginInfo.getResources();
            if (res != null) {
                return res;
            }
        }
        return createResources(context, apkPath);
    }

    public AssetManager getPluginAssets(@NonNull String apkPath) {
        SkinModel pluginInfo = mPluginInfoHolder.get(apkPath);
        if (pluginInfo != null) {
            Resources resources = pluginInfo.getResources();
            if (resources != null) {
                AssetManager assetManager = resources.getAssets();
                if (assetManager != null) {
                    return assetManager;
                }
            }
        }
        return createAssetManager(apkPath);
    }

    /**
     * 创建插件classloader
     */
    private DexClassLoader createDexClassLoader(@NonNull Context context, @NonNull String dexPath) {

        Context applicationContext = context.getApplicationContext();
        String dex = applicationContext.getDir("dex", Context.MODE_PRIVATE).getAbsolutePath();
        ClassLoader classLoader = applicationContext.getClassLoader();

        DexClassLoader loader = new DexClassLoader(dexPath, dex, null, classLoader);
        return loader;
    }

    /**
     * 创建AssetManager对象
     */
    private AssetManager createAssetManager(@NonNull String dexPath) {
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, dexPath);
            return assetManager;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 创建Resource对象
     */
    @SuppressWarnings("deprecation")
    private Resources createResources(@NonNull Context context, @NonNull AssetManager assetManager) {
        if (assetManager == null) {
            Log.e(TAG, " create Resources failed assetManager is NULL !! ");
            return null;
        }

        Context applicationContext = context.getApplicationContext();
        Resources superRes = applicationContext.getResources();
        Resources resources = new Resources(assetManager, superRes.getDisplayMetrics(), superRes.getConfiguration());
        return resources;
    }

    private Resources createResources(@NonNull Context context, @NonNull String dexPath) {
        AssetManager assetManager = createAssetManager(dexPath);
        if (assetManager != null) {
            Context applicationContext = context.getApplicationContext();
            return createResources(applicationContext, assetManager);
        }
        return null;
    }

    private PackageInfo createPackageInfo(@NonNull Context context, @NonNull String apkFilepath) {

        Context applicationContext = context.getApplicationContext();
        PackageManager pm = applicationContext.getPackageManager();
        PackageInfo pkgInfo = null;
        try {
            pkgInfo = pm.getPackageArchiveInfo(apkFilepath,
                    PackageManager.GET_ACTIVITIES |
                            PackageManager.GET_SERVICES |
                            PackageManager.GET_META_DATA);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pkgInfo;
    }

    private Class<?> loadClassByClassLoader(@NonNull ClassLoader classLoader, @NonNull String className) {
        Class<?> clazz = null;
        try {
            clazz = classLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return clazz;
    }
}
