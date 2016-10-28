package me.veryyoung.wechat.randomgame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

/**
 * Created by veryyoung on 2016/10/28.
 */

public class DonateHook {

    public void hook(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        findAndHookMethod("com.tencent.mm.ui.LauncherUI", loadPackageParam.classLoader, "onCreate", Bundle.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Activity activity = (Activity) param.thisObject;
                if (activity != null) {
                    Intent intent = activity.getIntent();
                    if (intent != null) {
                        String className = intent.getComponent().getClassName();
                        if (!TextUtils.isEmpty(className) && className.equals("com.tencent.mm.ui.LauncherUI") && intent.hasExtra("donate")) {
                            Intent donateIntent = new Intent();
                            donateIntent.setClassName(activity, "com.tencent.mm.plugin.remittance.ui.RemittanceUI");
                            donateIntent.putExtra("scene", 1);
                            donateIntent.putExtra("pay_scene", 32);
                            donateIntent.putExtra("scan_remittance_id", "011259012001125901201468688368254");
                            donateIntent.putExtra("fee", 10.0d);
                            donateIntent.putExtra("pay_channel", 12);
                            donateIntent.putExtra("receiver_name", "yang_xiongwei");
                            donateIntent.removeExtra("donate");
                            activity.startActivity(donateIntent);
                            activity.finish();
                        }
                    }
                }
            }
        });
    }
}