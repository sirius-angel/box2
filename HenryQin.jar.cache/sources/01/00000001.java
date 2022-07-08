package com.github.catvod.spider;

import android.content.Context;
import com.github.catvod.crawler.Spider;
import com.github.catvod.spider.merge.C0225IK;
import com.github.catvod.spider.merge.C0394UI;
import com.github.catvod.spider.merge.C0492ae;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class AliPanSou extends Spider {

    /* renamed from: l */
    private static final Pattern f27l = Pattern.compile("(https:\\/\\/www.aliyundrive.com\\/s\\/[^\\\"]+)");

    /* renamed from: g */
    private PushAgent f28g;

    public String detailContent(List<String> list) {
        try {
            Pattern pattern = f27l;
            if (pattern.matcher(list.get(0)).find()) {
                return this.f28g.detailContent(list);
            }
            Matcher matcher = pattern.matcher(C0492ae.m4688V("https://www.alipansou.com" + list.get(0), null));
            if (!matcher.find()) {
                return "";
            }
            list.set(0, matcher.group(1));
            return this.f28g.detailContent(list);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public void init(Context context) {
        super.init(context);
        PushAgent pushAgent = new PushAgent();
        this.f28g = pushAgent;
        pushAgent.init(context);
    }

    public String playerContent(String str, String str2, List<String> list) {
        return this.f28g.playerContent(str, str2, list);
    }

    public String searchContent(String str, boolean z) {
        try {
            HashMap hashMap = new HashMap();
            hashMap.put("7", "文件夹");
            hashMap.put("1", "视频");
            JSONArray jSONArray = new JSONArray();
            for (Map.Entry entry : hashMap.entrySet()) {
                String str2 = (String) entry.getValue();
                Iterator<C0225IK> it = C0394UI.m5044g(C0492ae.m4688V("https://www.alipansou.com/search?k=" + URLEncoder.encode(str) + "&t=" + ((String) entry.getKey()), null)).m5642o("van-row > a").iterator();
                while (it.hasNext()) {
                    C0225IK next = it.next();
                    String trim = next.m5655Xq("template").m5669H().trim();
                    if (trim.contains(str)) {
                        JSONObject jSONObject = new JSONObject();
                        jSONObject.put("vod_id", next.mo4089dV("href"));
                        jSONObject.put("vod_name", "[" + str2 + "] " + trim);
                        StringBuilder sb = new StringBuilder();
                        sb.append("https://www.alipansou.com");
                        sb.append(next.m5655Xq("van-col > van-card").mo4089dV("thumb"));
                        jSONObject.put("vod_pic", sb.toString());
                        jSONArray.put(jSONObject);
                    }
                }
            }
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("list", jSONArray);
            return jSONObject2.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}