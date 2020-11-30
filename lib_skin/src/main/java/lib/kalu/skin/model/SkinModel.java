package lib.kalu.skin.model;

import android.content.pm.PackageInfo;
import android.content.res.Resources;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;

import java.io.Serializable;

@Keep
public final class SkinModel implements Serializable {

    private String filePath;
    private transient ClassLoader classLoader;
    private transient Resources resources;
    private transient PackageInfo packageInfo;
    private transient Resources.Theme theme;

    public SkinModel(@NonNull String localPath, ClassLoader pluginClassLoader, Resources pluginRes,
                     Resources.Theme pluginTheme, PackageInfo packageInfo) {
        this.classLoader = pluginClassLoader;
        this.resources = pluginRes;
        this.theme = pluginTheme;
        this.filePath = localPath;
        this.packageInfo = packageInfo;
    }

    public SkinModel() {
    }

    public SkinModel(String localPath) {
        this.filePath = localPath;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public PackageInfo getPackageInfo() {
        return packageInfo;
    }

    public void setPackageInfo(PackageInfo packageInfo) {
        this.packageInfo = packageInfo;
    }

    public String getPackageName() {
        if (packageInfo == null) {
            return null;
        }
        return packageInfo.packageName;
    }

    public Resources getResources() {
        return resources;
    }

    public void setResources(Resources resources) {
        this.resources = resources;
    }

    public void setTheme(int themeId) {
        Resources.Theme theme = resources.newTheme();
        theme.applyStyle(themeId, true);
        setTheme(theme);
    }

    public void setTheme(Resources.Theme theme) {
        this.theme = theme;
    }

    public Resources.Theme getTheme() {
        return theme;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((filePath == null) ? 0 : filePath.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SkinModel other = (SkinModel) obj;
        if (filePath == null) {
            if (other.filePath != null)
                return false;
        } else if (!filePath.equals(other.filePath))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "SkinModel{" +
                "filePath='" + filePath + '\'' +
                ", classLoader=" + classLoader +
                ", resources=" + resources +
                ", packageInfo=" + packageInfo +
                ", theme=" + theme +
                '}';
    }
}