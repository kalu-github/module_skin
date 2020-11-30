package lib.kalu.skin.setting;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * description: 换肤设置
 * created by kalu on 2020-11-10
 */
public final class SkinSetting {
    /***
     * 支持的命名空间
     */
    public static final String SKIN_XML_NAMESPACE = "http://schemas.android.com/android/skin";

    /**
     * 界面元素支持换肤的属性
     */
    public static final String ATTR_SKIN_ENABLE = "enable";
    public static final String SUPPORTED_ATTR_SKIN_LIST = "attrs";

    public static final String SKIN_APK_SUFFIX = ".skin";
    public static final String PREF_CUSTOM_SKIN_PATH = "music_skin_custom_path";

    public static final boolean DEBUG = false;


    /**
     * 属性值对应的类型是color
     */
    public static final String RES_TYPE_NAME_COLOR = "color";

    /**
     * 属性值对应的类型是drawable
     */
    public static final String RES_TYPE_NAME_DRAWABLE = "drawable";
    public static final String RES_TYPE_NAME_MIPMAP = "mipmap";

    public static final String PREFERENCE_NAME = "xskin_loader_pref";


    public static String getSkinApkPath(Context context) {
        return getString(context, PREF_CUSTOM_SKIN_PATH, null);
    }

    public static void saveSkinApkPath(@NonNull Context context, @NonNull String path) {
        putString(context, PREF_CUSTOM_SKIN_PATH, path);
    }

    private static boolean putString(@NonNull Context context, @NonNull String key, @NonNull String value) {

        boolean result = false;
        FileOutputStream fos = null;
        BufferedWriter bw = null;

        try {

            StringBuilder builder = new StringBuilder();
            builder.append("skin@");

            String toLowerCase = key.toLowerCase();
            builder.append(toLowerCase);

            builder.append("@");
            String string = builder.toString();

            fos = context.openFileOutput(string, Context.MODE_PRIVATE);
            bw = new BufferedWriter(new OutputStreamWriter(fos));

            bw.write(value);
            bw.flush();

            result = true;

        } catch (Exception e) {
        } finally {

            if (null != fos) {
                try {
                    fos.close();
                } catch (Exception e) {
                }
            }

            if (null != bw) {
                try {
                    bw.close();
                } catch (Exception e) {
                }
            }
        }
        return result;
    }

    private static String getString(@NonNull Context context, @NonNull String key, @NonNull String defaultValue) {

        FileInputStream fis = null;
        BufferedReader br = null;
        StringBuilder stringBuilder = new StringBuilder();

        try {

            StringBuilder builder = new StringBuilder();
            builder.append("skin@");

            String toLowerCase = key.toLowerCase();
            builder.append(toLowerCase);

            builder.append("@");
            String filename = builder.toString();
            String absolutePath = context.getFilesDir().getAbsolutePath();

            File file = new File(absolutePath + File.separator + filename);

            if (null != file && file.exists()) {
                fis = context.openFileInput(filename);
                br = new BufferedReader(new InputStreamReader(fis));
                String line;
                while ((line = br.readLine()) != null) {
                    stringBuilder.append(line);
                }
            }

        } catch (Exception e) {
        } finally {

            if (null != fis) {
                try {
                    fis.close();
                } catch (Exception e) {
                }
            }

            if (null != br) {
                try {
                    br.close();
                } catch (Exception e) {
                }
            }
        }

        if (stringBuilder.length() == 0) {
            stringBuilder.append(defaultValue);
        }

        String value = stringBuilder.toString();
        return value;
    }

    public static boolean isCurrentAttrSpecified(@NonNull String attr, @Nullable String[] attrList) {
        if (attrList == null || attrList.length == 0) {
            return true;
        }
        for (String a : attrList) {
            if (a != null && (a.trim()).equals(attr)) {
                return true;
            }
        }
        return false;
    }
}
