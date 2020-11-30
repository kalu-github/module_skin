package lib.kalu.skin.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * description: 缓存
 * create by kalu on 2020-06-03
 */
public final class CacheUtil {

    public enum Key {

        SKIN, // 皮肤
    }


    public static final boolean setCache(@NonNull Context context, @NonNull Key key, @Nullable String value) {

        boolean result;
        FileOutputStream out = null;
        BufferedWriter writer = null;

        try {

            StringBuilder builder = new StringBuilder();
            builder.append("user@");

            String string1 = key.toString();
            String toLowerCase = string1.toLowerCase();
            builder.append(toLowerCase);

            builder.append("@");
            String string = builder.toString();
            Log.e("SkinManager", "setCache => fileName = " + string);

//            File file = new File(string);
//            if (!file.exists()) {
//                file.createNewFile();
//            }

            out = context.openFileOutput(string, Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));

            // LogUtil.e("welcome1presenter", "setCache => key = " + key.toString() + ", account = " + account + ", value = " + value);
            writer.write(value);
            writer.flush();
            result = true;

        } catch (Exception e) {
            result = false;
            Log.e("SkinManager", "setCache => " + e.getMessage(), e);
        } finally {

            if (null != out) {
                try {
                    out.close();
                } catch (Exception e) {
                    Log.e("SkinManager", "setCache => " + e.getMessage(), e);
                }
            }

            if (null != writer) {
                try {
                    writer.close();
                } catch (Exception e) {
                    Log.e("SkinManager", "setCache => " + e.getMessage(), e);
                }
            }
        }
        return result;
    }

    public static final String getCache(@NonNull Context context, @NonNull Key key) {

        FileInputStream in = null;
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();

        try {

            StringBuilder builder = new StringBuilder();
            builder.append("user@");

            String string1 = key.toString();
            String toLowerCase = string1.toLowerCase();
            builder.append(toLowerCase);

            builder.append("@");
            String filename = builder.toString();
            Log.e("SkinManager", "getCache => fileName = " + filename);

            String absolutePath = context.getFilesDir().getAbsolutePath();

            File file = new File(absolutePath + File.separator + filename);

            // succ
            if (null != file && file.exists()) {

                in = context.openFileInput(filename);
                reader = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line);
                }

            }
            // fail
            else {
                content.append("");
            }

        } catch (Exception e) {
        } finally {

            if (null != in) {
                try {
                    in.close();
                } catch (Exception e) {
                }
            }

            if (null != reader) {
                try {
                    reader.close();
                } catch (Exception e) {
                }
            }
        }

        String value = content.toString();
        return value;
    }
}
