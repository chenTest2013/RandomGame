package me.veryyoung.wechat.randomgame;

import android.app.ActivityManager;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static me.veryyoung.wechat.randomgame.Main.WECHAT_PACKAGE_NAME;

/**
 * Created by veryyoung on 2016/10/28.
 */

public class HideModuleHook {

    private static final String TAG = "RandomGame";

    public void hook(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        findAndHookMethod("android.app.ApplicationPackageManager", loadPackageParam.classLoader, "getInstalledApplications", int.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                List<ApplicationInfo> applicationList = (List) param.getResult();
                List<ApplicationInfo> resultapplicationList = new ArrayList<>();
                for (ApplicationInfo applicationInfo : applicationList) {
                    String packageName = applicationInfo.packageName;
                    if (isTarget(packageName)) {
                        Log.i(TAG, "Hid package: " + packageName);
                    } else {
                        resultapplicationList.add(applicationInfo);
                    }
                }
                param.setResult(resultapplicationList);
            }
        });
        findAndHookMethod("android.app.ApplicationPackageManager", loadPackageParam.classLoader, "getInstalledPackages", int.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                List<PackageInfo> packageInfoList = (List) param.getResult();
                List<PackageInfo> resultpackageInfoList = new ArrayList<>();

                for (PackageInfo packageInfo : packageInfoList) {
                    String packageName = packageInfo.packageName;
                    if (isTarget(packageName)) {
                        Log.i(TAG, "Hid package: " + packageName);
                    } else {
                        resultpackageInfoList.add(packageInfo);
                    }
                }
                param.setResult(resultpackageInfoList);
            }
        });
        findAndHookMethod("android.app.ApplicationPackageManager", loadPackageParam.classLoader, "getPackageInfo", String.class, int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                String packageName = (String) param.args[0];
                if (isTarget(packageName)) {
                    param.args[0] = WECHAT_PACKAGE_NAME;
                    Log.i(TAG, "Fake package: " + packageName + " as " + WECHAT_PACKAGE_NAME);
                }
            }
        });
        findAndHookMethod("android.app.ApplicationPackageManager", loadPackageParam.classLoader, "getApplicationInfo", String.class, int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                String packageName = (String) param.args[0];
                if (isTarget(packageName)) {
                    param.args[0] = WECHAT_PACKAGE_NAME;
                    Log.i(TAG, "Fake package: " + packageName + " as " + WECHAT_PACKAGE_NAME);
                }
            }
        });
        findAndHookMethod("android.app.ActivityManager", loadPackageParam.classLoader, "getRunningServices", int.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                List<ActivityManager.RunningServiceInfo> serviceInfoList = (List) param.getResult();
                List<ActivityManager.RunningServiceInfo> resultList = new ArrayList<>();

                for (ActivityManager.RunningServiceInfo runningServiceInfo : serviceInfoList) {
                    String serviceName = runningServiceInfo.process;
                    if (isTarget(serviceName)) {
                        Log.i(TAG, "Hid service: " + serviceName);
                    } else {
                        resultList.add(runningServiceInfo);
                    }
                }
                param.setResult(resultList);
            }
        });
        findAndHookMethod("android.app.ActivityManager", loadPackageParam.classLoader, "getRunningTasks", int.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                List<ActivityManager.RunningTaskInfo> serviceInfoList = (List) param.getResult();
                List<ActivityManager.RunningTaskInfo> resultList = new ArrayList<>();

                for (ActivityManager.RunningTaskInfo runningTaskInfo : serviceInfoList) {
                    String taskName = runningTaskInfo.baseActivity.flattenToString();
                    if (isTarget(taskName)) {
                        Log.i(TAG, "Hid task: " + taskName);
                    } else {
                        resultList.add(runningTaskInfo);
                    }
                }
                param.setResult(resultList);
            }
        });
        findAndHookMethod("android.app.ActivityManager", loadPackageParam.classLoader, "getRunningAppProcesses", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfos = (List) param.getResult();
                List<ActivityManager.RunningAppProcessInfo> resultList = new ArrayList<>();

                for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcessInfos) {
                    String processName = runningAppProcessInfo.processName;
                    if (isTarget(processName)) {
                        Log.i(TAG, "Hid process: " + processName);
                    } else {
                        resultList.add(runningAppProcessInfo);
                    }
                }
                param.setResult(resultList);
            }
        });
    }


    private boolean isTarget(String name) {
        return name.contains("veryyoung") || name.contains("xposed");
    }

}