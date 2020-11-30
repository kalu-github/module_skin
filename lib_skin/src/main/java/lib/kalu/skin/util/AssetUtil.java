package lib.kalu.skin.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import lib.kalu.skin.setting.SkinSetting;

/**
 * description: Asset工具类
 * created by kalu on 2020-11-10
 */
public final class AssetUtil {

    /**
     * @param context
     * @param assetFileName asset文件夹下的文件相对路径
     * @param saveFileName  save保存文件名, 保存在内部缓存file目录下
     * @return 保存文件绝对路径
     */
    public static String copyAssetFile(@NonNull Context context, @NonNull String assetFileName, @NonNull String saveFileName) {

        if (null == context || TextUtils.isEmpty(assetFileName) || TextUtils.isEmpty(saveFileName))
            return null;

        long startTime = System.currentTimeMillis();
        InputStream is = null;
        BufferedOutputStream bos = null;
        try {
            is = context.getAssets().open(assetFileName);

            File filesDir = context.getFilesDir();
            if (!filesDir.isDirectory()) {
                filesDir.mkdir();
            }

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("skin");
            stringBuilder.append("@");
            stringBuilder.append(saveFileName);
            stringBuilder.append("@");

            String toString = stringBuilder.toString();
            File destFile = new File(filesDir, toString);
            if (!destFile.exists()) {
                destFile.createNewFile();
            }

            FileOutputStream fos = new FileOutputStream(destFile);
            bos = new BufferedOutputStream(fos);

            byte[] buffer = new byte[256];
            int length = 0;
            while ((length = is.read(buffer)) > 0) {
                bos.write(buffer, 0, length);
            }
            bos.flush();

            String absolutePath = destFile.getAbsolutePath();
            return absolutePath;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (SkinSetting.DEBUG) {
                Log.e("AssetUtil", "copyAssetFile time = " + (System.currentTimeMillis() - startTime));
            }
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (null != bos) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
