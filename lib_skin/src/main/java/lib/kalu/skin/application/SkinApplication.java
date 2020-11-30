package lib.kalu.skin.application;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.view.LayoutInflater;

import lib.kalu.skin.SkinManager;
import lib.kalu.skin.factory.SkinLayoutInflater;

/**
 * description: SkinApplication
 * created by kalu on 2020-11-17
 */
public class SkinApplication extends Application implements Application.ActivityLifecycleCallbacks {

    @Override
    public void onCreate() {
        super.onCreate();
        // 回调
        registerActivityLifecycleCallbacks(this);
        // 初始化
        SkinLayoutInflater.setFactory(LayoutInflater.from(this));
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        // 1.关联布局文件
        SkinLayoutInflater.setFactory(activity);
        // 2.关联状态栏
        SkinManager.getManager().setWindowStatusBarColor(getApplicationContext(), activity.getWindow(), onSkinStatusColor());
    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }

    protected @ColorRes
    int onSkinStatusColor() {
        return -1;
    }
}
