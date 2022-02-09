package com.github.peixunxposed;

import android.text.TextUtils;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class MainHook implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (!TextUtils.isEmpty(lpparam.packageName) && lpparam.packageName.equals("com.hpbr.bosszhipin")) {
            new Hook(lpparam);
        }
    }
}
