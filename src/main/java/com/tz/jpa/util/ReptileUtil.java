package com.tz.jpa.util;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
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
            .connectTimeout(3000, TimeUnit.MILLISECONDS)
            .build();

    @lombok.Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Data<T> {

        private List<String> links;
        private List<T> results;
    }

    public static Data parse(String html, String baseUrl) throws URISyntaxException, FileNotFoundException {
        Document doc = Jsoup.parse(html);
        // 获取链接列表
        List<String> links = doc.select("#photos > ul > li > a > img").stream().map(element -> element.attr("src")).collect(Collectors.toList());
        for (String link : links) {
            URI uri = new URI(link);
            File file = new File(uri);

        }
        /*// 获取数据列表
        List<Map<String, String>> results = doc.select("#comments > div.comment-item")
                .stream()
                .map(div -> {
                    Map<String, String> data = new HashMap<>();

                    String author = div.selectFirst("h3 > span.comment-info > a").text();
                    String date = div.selectFirst("h3 > span.comment-info > span.comment-time").text();
                    Element rating = div.selectFirst("h3 > span.comment-info > span.rating");
                    String star = null;
                    if (rating != null) {
                        // allstar40 rating
                        star = rating.attr("class");
                        star = star.substring(7, 9);
                    }
                    String vote = div.selectFirst("h3 > span.comment-vote > span.votes").text();
                    String comment = div.selectFirst("div.comment > p").text();

                    data.put("author", author);
                    data.put("date", date);
                    if (star != null) {
                        data.put("star", star);
                    }
                    data.put("vote", vote);
                    data.put("comment", comment);

                    return data;
                })
                .collect(Collectors.toList());*/

        return new Data(links, null);
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
                .addHeader("Cookie", "gr_user_id=b6c0778d-f8df-4963-b057-bd321593de1e; bid=T-M5aFmoLY0; __yadk_uid=WvMJfSHd1cjUFrFQTdN9KnkIOkR2AFZu; viewed=\"26311273_26877306_26340992_26649178_3199438_3015786_27038473_10793398_26754665\"; ll=\"108296\"; ps=y; dbcl2=\"141556470:E4oz3is9RMY\"; ap=1; _vwo_uuid_v2=E57494AA9988242B62FB576F22211CE4|e95afc3b3a6c74f0b9d9106c6546e73e; ck=OvCX; __utma=30149280.1283677058.1481968276.1531194536.1531389580.35; __utmc=30149280; __utmz=30149280.1524482884.31.29.utmcsr=baidu|utmccn=(organic)|utmcmd=organic; __utmv=30149280.14155; __utma=223695111.1691619874.1522208966.1531194536.1531389615.5; __utmc=223695111; __utmz=223695111.1524483025.2.2.utmcsr=baidu|utmccn=(organic)|utmcmd=organic; _pk_ref.100001.4cf6=%5B%22%22%2C%22%22%2C1531389615%2C%22https%3A%2F%2Fwww.baidu.com%2Flink%3Furl%3D0saOVVzXJiEvkbYGxCXZ849EweAjA2om6cIvPZ7FxE35FrmKU8CfOHm1cC9Xs0JS%26wd%3D%26eqid%3De5307bbf0006c241000000045addc33f%22%5D; _pk_id.100001.4cf6=cee42334e421195b.1522208966.5.1531389615.1531200476.; push_noty_num=0; push_doumail_num=0")
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

    public static void main(String[] args) throws URISyntaxException, FileNotFoundException {
        String url = "https://movie.douban.com/celebrity/1054531/";
        String html = download(url);
        Data data = parse(html, "");
        System.out.println(data.toString());
    }
}
