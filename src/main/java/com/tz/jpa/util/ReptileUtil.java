package com.tz.jpa.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * ReptileUtil
 *
 * @author:tongzhou
 * @date: 2019/08/14
 * @description:
 */
@Slf4j
public class ReptileUtil {

    private static OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(6000, TimeUnit.MILLISECONDS)
            .build();

    public static void getPersonImage(String html, String name) {
        Document doc = Jsoup.parse(html);
        // 获取图片详情链接
        String personLink = doc.selectFirst("#photos > ul > li > a").attr("href");
        try {
            downloadPersonImage(personLink, name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查找豆瓣人物编码
     *
     * @param name
     */
    public static List<String> getPersonCodeByName(String name) {
        String url = null;
        try {
            url = "https://movie.douban.com/celebrities/search?search_text=" + URLEncoder.encode(name, "utf-8");
            log.debug("名人URL--->" + url);
            Document doc = Jsoup.parse(download(url));
            if (doc == null) {
                return null;
            }
            // 获取图片详情链接
            List<String> personLinks = doc.select(".content > h3 > a").stream().map(element -> element.attr("href")).collect(Collectors.toList());
            return personLinks;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查找时光网人物编码
     *
     * @param name
     */
    public static String getSGWPersonCodeByName(String name) {
        String url;
        try {
            url = "http://service.channel.mtime.com/Search.api?Ajax_CallBack=true&Ajax_CallBackType=Mtime.Channel.Services&Ajax_CallBackMethod=GetSearchResult&Ajax_CrossDomain=1&Ajax_RequestUrl=http://search.mtime.com/search/?q=" + URLEncoder.encode(name, "utf-8") + "&Ajax_CallBackArgument0=" + name + "&Ajax_CallBackArgument1=0&Ajax_CallBackArgument2=292&Ajax_CallBackArgument3=0&Ajax_CallBackArgument4=1";
            log.debug("名人URL--->" + url);
            String s = HttpClientUtil.doGet(url, null, null);
            String jsonString = s.substring(s.indexOf("{"), s.lastIndexOf("}") + 1);
            JSONObject personObj = JSON.parseObject(jsonString).getJSONObject("value").getJSONObject("personResult").getJSONArray("morePersons").getJSONObject(0);
            if (personObj == null) {
                return null;
            }
            String personId = personObj.getString("personId");
            return personId;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查找时光网人物视频
     *
     */
    public static List<String> getPersonVideoByCode(String name) {
        String code = getSGWPersonCodeByName(name);
        String url;
        try {
            url = "http://people.mtime.com/" + code + "/video.html";
            log.debug("时光网人物URL--->" + url);
            Document doc = Jsoup.connect(url)
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36")
                    .get();
            if (doc == null) {
                return null;
            }
            // 获取图片详情链接
            List<String> videoDataList = doc.getElementsByTag("script").stream().map(element -> element.data()).collect(Collectors.toList());
            videoDataList = videoDataList.stream().filter(s -> s.contains("var hottest_videos")).collect(Collectors.toList());
            List result = new ArrayList();
            String s = videoDataList.get(0);
            String jsonString = s.substring(s.indexOf("{"), s.lastIndexOf("}") + 1);
            JSONObject jsonObject = JSONObject.parseObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("Group");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject videoObj = jsonArray.getJSONObject(i);
                String videoID = videoObj.getString("VideoID");
                String title = videoObj.getString("Title");
                String imagePath = videoObj.getString("ImagePath");
                result.add(videoObj);
                String resString = HttpClientUtil.doGet("http://shortvideo.mtime.cn/play/getPlayUrl?videoId=" + videoID + "&source=1", null, null);
                JSONObject videoRes = JSON.parseObject(resString);
                JSONArray videoArray = videoRes.getJSONObject("data").getJSONArray("playUrlList");
                for (int j = 0; j < videoArray.size(); j++) {
                    JSONObject jsonObject1 = videoArray.getJSONObject(j);
                    String itemName = jsonObject1.getString("name");
                    if ("720p".equals(itemName)) {
                        FileUtil.downLoadFromUrl(jsonObject1.getString("url"), "sp_" + name + "_" + (i + 1) + ".mp4", "D:\\PersonVideo\\sp\\" + name);
                        System.out.println("('" + name + "','" + "http://res.yiyu.test3.ppdata.net/person/video/sp_" + URLEncoder.encode(name, "utf-8") + "_" + (i + 1) + ".mp4" + "',1,now(),now()),");
                    }
                }
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void downloadPersonImage(String url, String name) {
        String id = "";
        if (url.endsWith("/")) {
            id = url.substring(0, url.length() - 1);
        }
        id = id.substring(id.lastIndexOf("/") + 1);
        Document doc = Jsoup.parse(download(url));

        // 获取小图
        List<String> minImageLinks = doc.select(".slide-wrap > ul > li > a > img").stream().map(element -> element.attr("src")).collect(Collectors.toList());
        for (int i = 0; i < (minImageLinks.size() < 6 ? minImageLinks.size() : 6); i++) {
            try {
                String minImageLink = minImageLinks.get(i);
                log.debug("min----------" + minImageLink);
                String maxImageLink = "https://img1.doubanio.com/view/photo/l/public/" + minImageLink.substring(minImageLink.lastIndexOf("/") + 1);
                log.debug("max----------" + maxImageLink);
                FileUtil.downLoadFromUrl(minImageLink, "tj_" + name + "_" + (i + 1) + "_min.jpg", "D:\\personImage\\tj\\" + name);
                FileUtil.downLoadFromUrl(maxImageLink, "tj_" + name + "_" + (i + 1) + ".jpg", "D:\\personImage\\tj\\" + name);
//                log.info("(" + name + ",'" + "tj_" + URLEncoder.encode(name, "utf-8") + "_" + (i+1) + "_min.jpg" + "',2,now(),now()),");
                System.out.println("('" + name + "','" + "http://res.yiyu.test3.ppdata.net/person/tuji/tj_" + URLEncoder.encode(name, "utf-8") + "_" + (i + 1) + "_min.jpg" + "',2,now(),now()),");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 获取大图
        /*List<String> maxImageLinks = doc.select(".article > .photo-show > .photo-wp > .mainphoto > img").stream().map(element -> element.attr("src")).collect(Collectors.toList());
        for (String link : maxImageLinks) {
            try {
                System.out.println("max----------" + link);
                FileUtil.downLoadFromUrl(link, "tj_" + name + "_" + (i+1) + ".jpg", "D:\\personImage\\tuji");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/

    }

    /**
     * 下载网页
     *
     * @param url
     * @return
     */
    public static String download(String url) {
        // 使用Cookie消息头是为了简化登录问题(豆瓣电影评论不登录条件下获取不到全部数据)
        Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36")
                .addHeader("Cookie", "_vwo_uuid_v2=D018A3E5053DC4A7FAC7B29850BE00260|8a89331e59fdf88c767478da5481126f; douban-fav-remind=1; __yadk_uid=JYkudsfrOhUB96UgVANejblMnORrX4PK; __gads=ID=fddb9b15c9f410ca:T=1558943154:S=ALNI_MZpjNLWJUZRiqd5tMJOYRRFG0SZtg; trc_cookie_storage=taboola%2520global%253Auser-id%3Da50e9f36-a806-43de-bcf7-757a30c738dd-tuct212282e; bid=d41T8tFQi2s; ll=\"108296\"; ct=y; push_noty_num=0; push_doumail_num=0; __utmv=30149280.20200; dbcl2=\"202004310:ao24mrFpt9g\"; ck=hvGk; __utmc=30149280; __utmz=30149280.1565919954.52.27.utmcsr=baidu|utmccn=(organic)|utmcmd=organic; __utmc=223695111; __utmz=223695111.1565919954.35.13.utmcsr=baidu|utmccn=(organic)|utmcmd=organic; _pk_ref.100001.4cf6=%5B%22%22%2C%22%22%2C1565931672%2C%22https%3A%2F%2Fwww.baidu.com%2Flink%3Furl%3DseWZWCrUhCOOpXIM2W0vikkpjO7FKMvCJ8BVN-CSrXxLa0H7fanTeuTDoa6d3HRaacPJdSkNXprw0Oh5m-xf1_%26wd%3D%26eqid%3D891d252900145a16000000045d560a56%22%5D; _pk_ses.100001.4cf6=*; __utma=30149280.1999521967.1529376652.1565922548.1565931672.54; __utma=223695111.723824887.1546061336.1565922564.1565931672.37; __utmb=223695111.0.10.1565931672; ap_v=0,6.0; __utmb=30149280.10.10.1565931672; _pk_id.100001.4cf6=701b87983cefda51.1546061336.36.1565934944.1565922629.")
                .get()
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException(response.code() + "," + response.message());
            }
            return response.body().string();
        } catch (IOException e) {
            log.error("下载网页[{}]失败!", url, e);
        }
        return null;
    }

    public static void main(String[] args) throws IOException {
        List<String> personVideoLinks = getPersonVideoByCode("李小龙");
        /*String url = "https://vfx.mtime.cn/Video/2013/12/06/mp4/131206102814904838_480.mp4";
        FileUtil.downLoadFromUrl(url, "test.mp4", "D:\\downloadVideo");*/
    }
}
