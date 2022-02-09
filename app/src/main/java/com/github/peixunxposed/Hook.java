package com.github.peixunxposed;

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.github.peixunxposed.modle.Peixun;
import com.github.peixunxposed.util.AESUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.cookie.SerializableCookie;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class Hook {
    public static String SERVERURL = "http://49.233.41.73:8806/api/v1/company_info";
    private static Context application;
    private static List<Peixun> peixunList;
    private static final String TAG = "培训机构识别模块：";

    private final String setF1ListItem = "net.bosszhipin.api.bean.ServerJobCardBean";
    private final String bossitem = "com.hpbr.bosszhipin.module.main.views.card.JobCardView";
    private final String bossCom = "com.hpbr.bosszhipin.module.main.adapter.CompanyAllConcernedAdapter";
    private final String bossChat = "com.hpbr.bosszhipin.module.contacts.activity.ChatNewActivity";
    private final String bossDetail = "com.hpbr.bosszhipin.module.position.holder.ctb.JobComInfoCtBViewHolder";

    public Hook(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        XposedHelpers.findAndHookMethod(Application.class, "attach", new Object[]{Context.class, new XC_MethodHook() { // from class: com.zengdq.peixunxposed.Main.1
            protected void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) throws Throwable {
                ClassLoader classLoader = ((Context) methodHookParam.args[0]).getClassLoader();
                Context unused = Hook.application = (Context) methodHookParam.args[0];
                if (loadPackageParam.packageName.equals("com.hpbr.bosszhipin")) {
                    init();
                }

                try {
                    Class<?> bossitemClazz = classLoader.loadClass(bossitem);
                    if (bossitemClazz != null) {
                        XposedHelpers.findAndHookMethod(bossitemClazz, "setSearchListItem", new Object[]{setF1ListItem, new XC_MethodHook() { // from class: com.zengdq.peixunxposed.Main.1.3
                            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) throws Throwable {
                                Object obj = methodHookParam.args[0];
                                setJob(obj, "brandName", (String)getJob(obj, "brandName"));
                                methodHookParam.args[0] = obj;
                            }
                        }});
                        XposedHelpers.findAndHookMethod(bossitemClazz, "setF1ListItem", new Object[]{setF1ListItem, new XC_MethodHook() { // from class: com.zengdq.peixunxposed.Main.1.4
                            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) throws Throwable {
                                Object obj = methodHookParam.args[0];
                                setJob(obj, "brandName", (String) getJob(obj, "brandName"));
                                methodHookParam.args[0] = obj;
                            }
                        }});
                    } else {
                        XposedBridge.log(String.format("%s加载类%s异常,找不到类", TAG, bossitem));
                    }
                } catch (Exception e) {
                    XposedBridge.log(String.format("%s加载类%s异常%s", TAG, bossitem, e.toString()));
                }
                try {
                    Class<?> bossComClazz = classLoader.loadClass(bossCom);
                    if (bossComClazz != null) {
                        XposedHelpers.findAndHookMethod(bossComClazz, "a", new Object[]{List.class, new XC_MethodHook() { // from class: com.zengdq.peixunxposed.Main.1.5
                            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) throws Throwable {
                                List list = (List) methodHookParam.args[0];
                                for (Object obj : list) {
                                    setJob(obj, SerializableCookie.NAME, getJob(obj, SerializableCookie.NAME));
                                }
                                methodHookParam.args[2] = list;
                            }
                        }});
                    } else {
                        XposedBridge.log(String.format("%s加载类%s异常,找不到类", TAG, bossCom));
                    }
                } catch (Exception e) {
                    XposedBridge.log(String.format("%s加载类%s异常%s", TAG, bossCom, e.toString()));
                }

                try {
                    Class<?> bossChatClazz = classLoader.loadClass(bossChat);
                    if (bossChatClazz != null) {
                        XposedHelpers.findAndHookMethod(bossChatClazz, "b", new Object[]{"com.hpbr.bosszhipin.data.db.entry.ContactBean", new XC_MethodHook() { // from class: com.zengdq.peixunxposed.Main.1.8
                            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) throws Throwable {
                                Object obj = methodHookParam.args[0];
                                setJob(obj, "bossCompanyName", (String) getJob(obj, "bossCompanyName"));
                                methodHookParam.args[0] = obj;
                            }
                        }});
                    } else {
                        XposedBridge.log(String.format("%s加载类%s异常,找不到类", TAG, bossChat));
                    }
                } catch (Exception e) {
                    XposedBridge.log(String.format("%s加载类%s异常%s", TAG, bossChat, e.toString()));
                }
                try {
                    Class<?> bossDetailClazz = classLoader.loadClass(bossDetail);
                    if (bossDetailClazz != null) {
                        XposedHelpers.findAndHookMethod(bossDetailClazz, "a", new Object[]{"com.hpbr.bosszhipin.module.position.entity.detail.JobComInfo", View.OnClickListener.class, new XC_MethodHook() { // from class: com.zengdq.peixunxposed.Main.1.9
                            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) throws Throwable {
                                Object obj = methodHookParam.args[0];
                                Object job = getJob(obj, "brand");
                                setJob(job, "comName", getJob(job, "comName"));
                                setJob(obj, "brand", job);
                                methodHookParam.args[0] = obj;
                            }
                        }});
                    } else {
                        XposedBridge.log(String.format("%s加载类%s异常,找不到类", TAG, bossDetail));
                    }
                } catch (Exception e) {
                    XposedBridge.log(String.format("%s加载类%s异常%s", TAG, bossDetail, e.toString()));
                }
            }
        }});
    }

    public void init() {
        XposedBridge.log("进行初始化,版本" + getVersion() + "===");
        ((PostRequest) ((PostRequest) OkGo.post(SERVERURL).headers("mmp", "mmp")).headers("version", getVersion())).execute(new StringCallback() { // from class: com.zengdq.peixunxposed.Main.5
            @Override // com.lzy.okgo.callback.Callback
            public void onSuccess(Response<String> response) {
                JsonObject asJsonObject = new JsonParser().parse(response.body()).getAsJsonObject();
                if (asJsonObject.get("code").toString().equals("500")) {
                    XposedBridge.log("初始化成功,无需更新");
                    return;
                }
                try {
                    writeData(asJsonObject.get(CacheEntity.DATA).toString());
                    try {
                        peixunList = new ArrayList();
                        peixunList = (List) new Gson().fromJson(new JsonParser().parse(readData()).getAsJsonObject().get("peixun").toString(), new TypeToken<List<Peixun>>() { // from class: com.zengdq.peixunxposed.Main.5.1
                        }.getType());
                        XposedBridge.log("初始化成功,无需更新:" + peixunList.size());
                    } catch (Exception e) {
                        XposedBridge.log("初始化异常：" + e.getMessage());
                        e.printStackTrace();
                    }
                } catch (Exception e2) {
                    XposedBridge.log("初始化异常1：" + e2.getMessage());
                    e2.printStackTrace();
                }
                XposedBridge.log("初始化成功");
            }
        });
    }

    public String getVersion() {
        XposedBridge.log("开始读文件");
        try {
            XposedBridge.log("开始读文件try");
            return new JSONObject(readData()).get("version").toString();
        } catch (Exception e) {
            XposedBridge.log("读文件异常，返回0");
            return "0";
        }
    }

    public String readData() throws Exception {
        return readFileData("peixun69.json");
    }

    public String readFileData(String str) throws Exception {
        FileInputStream openFileInput = application.openFileInput(str);
        InputStreamReader inputStreamReader = new InputStreamReader(openFileInput, "UTF-8");
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuffer stringBuffer = new StringBuffer();
        while (true) {
            String readLine = bufferedReader.readLine();
            if (readLine != null) {
                stringBuffer.append(readLine);
            } else {
                bufferedReader.close();
                inputStreamReader.close();
                openFileInput.close();
                return AESUtil.decrypt(stringBuffer.toString().replace("\"", "").trim(), "zengdq520LiuYing");
            }
        }
    }

    public void writeData(String str) {
        writeFileData("peixun69.json", str);
    }

    public void writeFileData(String str, String str2) {
        try {
            application.deleteFile(str);
            FileOutputStream openFileOutput = application.openFileOutput(str, 0);
            openFileOutput.write(str2.getBytes());
            openFileOutput.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object getJob(Object obj, String str) {
        try {
            return XposedHelpers.getObjectField(obj, str);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public void setJob(Object obj, String str, Object obj2) {
        try {
            XposedHelpers.setObjectField(obj, str, isPeixun(obj2.toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String isPeixun(String str) throws Exception {
        if (str == null) {
            return "";
        }
        String str2 = str.split("<---->")[0];
        if (peixunList == null) {
            peixunList = new ArrayList();
            try {
                peixunList = (List) new Gson().fromJson(new JSONObject(readData()).get("peixun").toString(), new TypeToken<List<Peixun>>() { // from class: com.zengdq.peixunxposed.Main.4
                }.getType());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        for (Peixun peixun : peixunList) {
            if (peixun.getName().contains(str2) || str2.contains(peixun.getName())) {
                return str2 + "<---->" + peixun.getType();
            }
        }
        return str2;
    }

}
