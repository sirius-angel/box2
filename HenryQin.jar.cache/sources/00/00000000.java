package com.github.catvod.spider;

import android.content.Context;
import android.net.UrlQuerySanitizer;
import android.text.TextUtils;
import com.github.catvod.crawler.Spider;
import com.github.catvod.crawler.SpiderDebug;
import com.github.catvod.spider.merge.AbstractC0485a6;
import com.github.catvod.spider.merge.C0492ae;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import okhttp3.Call;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class Ali extends Spider {

    /* renamed from: J */
    private static long f18J = 0;

    /* renamed from: g */
    private static String f21g = "";

    /* renamed from: l */
    private static long f22l;

    /* renamed from: dV */
    private static Map<String, String> f20dV = new HashMap();

    /* renamed from: rl */
    private static Map<String, Long> f25rl = new HashMap();

    /* renamed from: cY */
    private static Pattern f19cY = Pattern.compile("(https:\\/\\/www.aliyundrive.com\\/s\\/[^\\\"]+)");

    /* renamed from: HL */
    private static Pattern f17HL = Pattern.compile("www.aliyundrive.com\\/s\\/([^\\/]+)(\\/folder\\/([^\\/]+))?");

    /* renamed from: lN */
    private static Map<String, Map<String, String>> f23lN = new HashMap();

    /* renamed from: q3 */
    private static ReentrantLock f24q3 = new ReentrantLock();

    /* renamed from: zJ */
    private static long f26zJ = 0;

    /* renamed from: HL */
    private static String m6291HL(String str, String str2, String str3) {
        int i;
        String m4685cY;
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("share_id", str);
            jSONObject.put("category", "live_transcoding");
            jSONObject.put("file_id", str3);
            jSONObject.put("template_id", "");
            HashMap<String, String> m6287g = m6287g();
            m6287g.put("x-share-token", str2);
            m6287g.put("authorization", f21g);
            JSONObject jSONObject2 = new JSONObject(m6285rl("https://api.aliyundrive.com/v2/file/get_share_link_video_preview_play_info", jSONObject.toString(), m6287g));
            ArrayList arrayList = new ArrayList();
            arrayList.add("FHD");
            arrayList.add("HD");
            arrayList.add("SD");
            JSONArray jSONArray = jSONObject2.getJSONObject("video_preview_play_info").getJSONArray("live_transcoding_task_list");
            Iterator it = arrayList.iterator();
            String str4 = "";
            while (true) {
                i = 0;
                if (!it.hasNext()) {
                    break;
                }
                String str5 = (String) it.next();
                if (!str4.isEmpty()) {
                    break;
                }
                while (true) {
                    if (i < jSONArray.length()) {
                        JSONObject jSONObject3 = jSONArray.getJSONObject(i);
                        if (jSONObject3.getString("template_id").equals(str5)) {
                            str4 = jSONObject3.getString("url");
                            break;
                        }
                        i++;
                    }
                }
            }
            if (TextUtils.isEmpty(str4)) {
                str4 = jSONArray.getJSONObject(0).getString("url");
            }
            HashMap hashMap = new HashMap();
            C0492ae.m4687Yl(str4, m6287g(), hashMap);
            String m4688V = C0492ae.m4688V(C0492ae.m4685cY(hashMap), m6287g());
            String str6 = m4685cY.substring(0, m4685cY.lastIndexOf("/")) + "/";
            ArrayList arrayList2 = new ArrayList();
            HashMap hashMap2 = new HashMap();
            String[] split = m4688V.split("\n");
            int length = split.length;
            int i2 = 0;
            while (i < length) {
                String str7 = split[i];
                if (str7.contains("x-oss-expires")) {
                    i2++;
                    hashMap2.put("" + i2, str6 + str7);
                    str7 = Proxy.localProxyUrl() + "?do=ali&type=media&share_id=" + str + "&file_id=" + str3 + "&media_id=" + i2;
                }
                arrayList2.add(str7);
                i++;
            }
            f23lN.put(str3, hashMap2);
            return TextUtils.join("\n", arrayList2);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /* renamed from: J */
    protected static long m6290J() {
        return (System.currentTimeMillis() / 1000) + f26zJ;
    }

    /* renamed from: cY */
    private static void m6289cY() {
        long m6290J = m6290J();
        if (f21g.isEmpty() || f22l - m6290J <= 600) {
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("refresh_token", "27b06d5221a24199b15f2e573d847430");
                JSONObject jSONObject2 = new JSONObject(m6285rl("https://api.aliyundrive.com/token/refresh", jSONObject.toString(), m6287g()));
                f21g = jSONObject2.getString("token_type") + " " + jSONObject2.getString("access_token");
                long j = jSONObject2.getLong("expires_in");
                f18J = j;
                f22l = m6290J + j;
            } catch (JSONException e) {
                SpiderDebug.log(e);
            }
        }
    }

    /* renamed from: g */
    private static HashMap<String, String> m6287g() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.54 Safari/537.36");
        hashMap.put("Referer", "https://www.aliyundrive.com/");
        return hashMap;
    }

    /* renamed from: l */
    private static synchronized String m6286l(String str, String str2) {
        synchronized (Ali.class) {
            try {
                long m6290J = m6290J();
                String str3 = f20dV.get(str);
                Long l = f25rl.get(str);
                if (!TextUtils.isEmpty(str3) && l.longValue() - m6290J > 600) {
                    return str3;
                }
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("share_id", str);
                jSONObject.put("share_pwd", str2);
                JSONObject jSONObject2 = new JSONObject(m6285rl("https://api.aliyundrive.com/v2/share_link/get_share_token", jSONObject.toString(), m6287g()));
                String string = jSONObject2.getString("share_token");
                f25rl.put(str, Long.valueOf(m6290J + jSONObject2.getLong("expires_in")));
                f20dV.put(str, string);
                return string;
            } catch (JSONException e) {
                SpiderDebug.log(e);
                return "";
            }
        }
    }

    public static Object[] proxyM3U8(Map<String, String> map) {
        try {
            String str = map.get("share_id");
            return new Object[]{200, "application/octet-stream", new ByteArrayInputStream(m6291HL(str, m6286l(str, ""), map.get("file_id")).getBytes())};
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object[] proxyMedia(Map<String, String> map) {
        try {
            String str = map.get("share_id");
            String str2 = map.get("file_id");
            String str3 = map.get("media_id");
            String m6286l = m6286l(str, "");
            f24q3.lock();
            String str4 = f23lN.get(str2).get(str3);
            if (new Long(new UrlQuerySanitizer(str4).getValue("x-oss-expires")).longValue() - m6290J() <= 60) {
                m6291HL(str, m6286l, str2);
                str4 = f23lN.get(str2).get(str3);
            }
            f24q3.unlock();
            new HashMap();
            AbstractC0485a6.AbstractC0487V abstractC0487V = new AbstractC0485a6.AbstractC0487V() { // from class: com.github.catvod.spider.Ali.1
                @Override // com.github.catvod.spider.merge.AbstractC0485a6
                protected void onFailure(Call call, Exception exc) {
                }

                /* JADX INFO: Access modifiers changed from: protected */
                public void onResponse(Response response) {
                }
            };
            C0492ae.m4678rl(C0492ae.m4690J(), str4, null, m6287g(), abstractC0487V);
            return new Object[]{200, "video/MP2T", abstractC0487V.getResult().body().byteStream()};
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /* renamed from: rl */
    private static String m6285rl(String str, String str2, Map<String, String> map) {
        AbstractC0485a6.AbstractC0486J abstractC0486J = new AbstractC0485a6.AbstractC0486J() { // from class: com.github.catvod.spider.Ali.2
            @Override // com.github.catvod.spider.merge.AbstractC0485a6
            protected void onFailure(Call call, Exception exc) {
                exc.printStackTrace();
            }

            /* JADX INFO: Access modifiers changed from: protected */
            public void onResponse(String str3) {
            }
        };
        C0492ae.m4677zJ(C0492ae.m4690J(), str, str2, map, abstractC0486J);
        return abstractC0486J.getResult();
    }

    public static Object[] vod(Map<String, String> map) {
        String str = map.get("type");
        if (str.equals("m3u8")) {
            return proxyM3U8(map);
        }
        if (!str.equals("media")) {
            return null;
        }
        return proxyMedia(map);
    }

    /* renamed from: dV */
    protected void m6288dV() {
        try {
            f26zJ = new JSONObject(C0492ae.m4688V("http://worldtimeapi.org/api/timezone/Asia/Shanghai", null)).getLong("unixtime") - (System.currentTimeMillis() / 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String detailContent(List<String> list) {
        try {
            String trim = list.get(0).trim();
            Matcher matcher = f17HL.matcher(trim);
            if (!matcher.find()) {
                return "";
            }
            String group = matcher.group(1);
            String group2 = matcher.groupCount() == 3 ? matcher.group(3) : "";
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("share_id", group);
            JSONObject jSONObject2 = new JSONObject(m6285rl("https://api.aliyundrive.com/adrive/v3/share_link/get_share_by_anonymous", jSONObject.toString(), m6287g()));
            JSONArray jSONArray = jSONObject2.getJSONArray("file_infos");
            if (jSONArray.length() == 0) {
                return "";
            }
            JSONObject jSONObject3 = null;
            if (!TextUtils.isEmpty(group2)) {
                int i = 0;
                while (true) {
                    if (i >= jSONArray.length()) {
                        break;
                    }
                    JSONObject jSONObject4 = jSONArray.getJSONObject(i);
                    if (jSONObject4.getString("file_id").equals(jSONObject4.getString("file_id"))) {
                        jSONObject3 = jSONObject4;
                        break;
                    }
                    i++;
                }
                if (jSONObject3 == null) {
                    return "";
                }
            } else {
                jSONObject3 = jSONArray.getJSONObject(0);
                group2 = jSONObject3.getString("file_id");
            }
            JSONObject jSONObject5 = new JSONObject();
            jSONObject5.put("vod_id", trim);
            jSONObject5.put("vod_name", jSONObject2.getString("share_name"));
            jSONObject5.put("vod_pic", jSONObject2.getString("avatar"));
            jSONObject5.put("vod_content", trim);
            jSONObject5.put("vod_play_from", "阿里云盘");
            ArrayList arrayList = new ArrayList();
            if (!jSONObject3.getString("type").equals("folder")) {
                if (!jSONObject3.getString("type").equals("file") || !jSONObject3.getString("category").equals("video")) {
                    return "";
                }
                group2 = "root";
            }
            String m6286l = m6286l(group, "");
            Map<String, String> hashMap = new HashMap<>();
            listFiles(hashMap, group, m6286l, group2);
            ArrayList<String> arrayList2 = new ArrayList(hashMap.keySet());
            Collections.sort(arrayList2);
            for (String str : arrayList2) {
                arrayList.add(str + "$" + hashMap.get(str));
            }
            ArrayList arrayList3 = new ArrayList();
            for (int i2 = 0; i2 < 4; i2++) {
                arrayList3.add(TextUtils.join("#", arrayList));
            }
            jSONObject5.put("vod_play_url", TextUtils.join("$$$", arrayList3));
            JSONObject jSONObject6 = new JSONObject();
            JSONArray jSONArray2 = new JSONArray();
            jSONArray2.put(jSONObject5);
            jSONObject6.put("list", jSONArray2);
            return jSONObject6.toString();
        } catch (Exception e) {
            SpiderDebug.log(e);
            return "";
        }
    }

    public void init(Context context) {
        super.init(context);
        m6288dV();
    }

    public void listFiles(Map<String, String> map, String str, String str2, String str3) {
        Exception e;
        String str4;
        try {
            String str5 = "https://api.aliyundrive.com/adrive/v3/file/list";
            HashMap<String, String> m6287g = m6287g();
            m6287g.put("x-share-token", str2);
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("image_thumbnail_process", "image/resize,w_160/format,jpeg");
            jSONObject.put("image_url_process", "image/resize,w_1920/format,jpeg");
            jSONObject.put("limit", 200);
            jSONObject.put("order_by", "updated_at");
            jSONObject.put("order_direction", "DESC");
            jSONObject.put("parent_file_id", str3);
            jSONObject.put("share_id", str);
            jSONObject.put("video_thumbnail_process", "video/snapshot,t_1000,f_jpg,ar_auto,w_300");
            String str6 = "";
            ArrayList<String> arrayList = new ArrayList();
            int i = 1;
            while (i <= 50 && (i < 2 || !TextUtils.isEmpty(str6))) {
                jSONObject.put("marker", str6);
                JSONObject jSONObject2 = new JSONObject(m6285rl(str5, jSONObject.toString(), m6287g));
                JSONArray jSONArray = jSONObject2.getJSONArray("items");
                int i2 = 0;
                while (i2 < jSONArray.length()) {
                    JSONObject jSONObject3 = jSONArray.getJSONObject(i2);
                    if (jSONObject3.getString("type").equals("folder")) {
                        arrayList.add(jSONObject3.getString("file_id"));
                        str4 = str5;
                    } else {
                        str4 = str5;
                        if (jSONObject3.getString("mime_type").contains("video")) {
                            String replace = jSONObject3.getString("name").replace("#", "_").replace("$", "_");
                            map.put(replace, str + "+" + str2 + "+" + jSONObject3.getString("file_id"));
                        }
                    }
                    i2++;
                    str5 = str4;
                }
                str6 = jSONObject2.getString("next_marker");
                i++;
                str5 = str5;
            }
            for (String str7 : arrayList) {
                try {
                    listFiles(map, str, str2, str7);
                } catch (Exception e2) {
                    e = e2;
                    e.printStackTrace();
                    return;
                }
            }
        } catch (Exception e3) {
            e = e3;
        }
    }

    public String playerContent(String str, String str2, List<String> list) {
        try {
            m6289cY();
            String[] split = str2.split("\\+");
            String str3 = split[0];
            String str4 = split[1];
            String str5 = Proxy.localProxyUrl() + "?do=ali&type=m3u8&share_id=" + str3 + "&file_id=" + split[2];
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("parse", "0");
            jSONObject.put("playUrl", "");
            jSONObject.put("url", str5);
            jSONObject.put("header", "");
            return jSONObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            SpiderDebug.log(e);
            return "";
        }
    }

    public String searchContent(String str, boolean z) {
        try {
            String trim = str.trim();
            if (!f19cY.matcher(trim).find()) {
                return "";
            }
            JSONArray jSONArray = new JSONArray();
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("vod_id", trim);
            jSONObject.put("vod_name", trim);
            jSONArray.put(jSONObject);
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("list", jSONArray);
            return jSONObject2.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}