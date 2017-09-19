package com.pb;

import com.pb.data.ApplicationConfig;
import com.pb.data.MerchantData;
import com.pb.data.MerchantImageData;
import com.pb.data.executor.ImageExecutor;
import com.pb.data.executor.JDBCExecutor;
import com.pb.data.jdbc.JDBCReader;
import com.pb.data.jdbc.JDBCWriter;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by pb on 2017/5/3.
 */
public class RunSearch {
    public static final ExecutorService IMAGE_THREAD_POOL = Executors.newFixedThreadPool(8);
    public static final ExecutorService PAGE_THREAD_POOL = Executors.newFixedThreadPool(8);
    public static final String host = "http://www.dianping.com";
    public static String area = "";

    public static void main(String[] args) {

        String url = "/search/keyword/329/0_茶馆/o2";
        try {
            //url= URLEncoder.encode(url,"UTF-8");
            area = "";
            parseMerchantListPage(url);
            //  ImageExecutor.FIXED_THREAD_POOL.shutdown();
            while (!ImageExecutor.FIXED_THREAD_POOL.isTerminated()) {
                Thread.sleep(4000);
            }
            //    JDBCExecutor.FIXED_THREAD_POOL.shutdown();
            while (!JDBCExecutor.FIXED_THREAD_POOL.isTerminated()) {
                Thread.sleep(3000);
            }
            // IMAGE_THREAD_POOL.shutdown();
            while (!IMAGE_THREAD_POOL.isTerminated()) {
                Thread.sleep(3000);
            }

            //  PAGE_THREAD_POOL.shutdown();
            while (!PAGE_THREAD_POOL.isTerminated()) {
                Thread.sleep(3000);
            }
            System.out.println("主程序执行完成");
        } catch (Exception e) {
            System.err.println("主程序执行失败");
            e.printStackTrace();
        }
    }

    public static void parseMerchantListPage(String url) throws IOException {
        Document document = readHtml(url, "", "");
        Elements txts = document.getElementsByClass("txt");
        if (area == "") {
            area = document.getElementsByClass("J-city").text();
        }
        for (final Element element : txts) {
            PAGE_THREAD_POOL.execute(new Runnable() {
                public void run() {
                    try {
                        parseMerchantInfo(element);
                    } catch (IOException e) {
                        System.err.println("解析如下html失败");
                        System.err.println(element.html());
                        e.printStackTrace();
                    }
                }
            });

        }
        Elements nextEs = document.getElementsByClass("next");
        if (nextEs.size() == 0) {
            return;
        }
        url = nextEs.get(0).attr("href");
        parseMerchantListPage(url);
    }

    private static void parseMerchantInfo(Element element) throws IOException {
        String text = element.child(0).child(0).attr("href");
        final String id = text.substring(text.lastIndexOf("/") + 1);
        if (JDBCReader.read(Integer.valueOf(id)) != null) {
            return;
        }
        Document merchantPage = readHtml(text, "", "");
        Elements starEs = merchantPage.getElementsByClass("mid-rank-stars");
        String stars = "".intern();
        if (starEs.size() > 0) {
            stars = starEs.get(0).attr("title");
        }
        if (starEs.size() == 0) {
            JDBCWriter.writeNoFound(text, "", "");
            return;
        }
        Element apE = merchantPage.getElementById("avgPriceTitle");
        String avgPrice = "".intern();
        if (apE != null) {
            avgPrice = apE.text();
        }
        final String shopName = merchantPage.getElementsByClass("shop-name").get(0).ownText();
        String address = merchantPage.getElementsByClass("address").get(0).child(2).text();
        StringBuilder sbTel = new StringBuilder();
        Element telE = merchantPage.getElementsByClass("tel").first();
        Elements telEs = telE.getElementsByClass("item");
        for (Element e : telEs) {
            sbTel.append(e.text()).append(" ");
        }
        Element otherInfoElement = merchantPage.getElementsByClass("J-other").get(0);
        String businessTime = otherInfoElement.child(0).child(1).text();
        Elements bqs = otherInfoElement.getElementsByClass("J_license");
        String businessQualification = "".intern();
        if (bqs.size() > 0) {
            businessQualification = bqs.get(0).child(1).text();
        }

        MerchantData data = new MerchantData(Integer.valueOf(id), shopName, address, sbTel.toString(), stars, businessQualification, businessTime, avgPrice);
        data.setArea(area);
       /* IMAGE_THREAD_POOL.execute(new Runnable() {
            public void run() {
                parseMerchantImage(id,shopName);
            }
        });*/
        JDBCExecutor.execute(data);
    }

    public static void parseMerchantImage(String id, String name, String area) {
        String baseImgUrl = String.format("/shop/%s/photos", id);
        String href = "".intern();
        try {
            Document baseImgPage = readHtml(baseImgUrl, id, name);
            Element imgNav = baseImgPage.getElementById("photoNav");
            if (imgNav == null) {
                return;
            }
            if(area ==""){
                area = baseImgPage.getElementsByClass("J-city").text();
            }
            Elements navAs = imgNav.getElementsByAttribute("a");
            boolean hasFaces = false;

            for (Element e : navAs) {
                if (e.text().startsWith("门面")) {
                    href = e.attr("href");
                    Document faceImgPage = readHtml(href, id, name);
                    Element eImgA = faceImgPage.getElementsByClass("img").get(0).child(1);
                    href = eImgA.attr("href");
                    hasFaces = true;
                    break;
                }
            }
            if (!hasFaces) {
                href = baseImgPage.getElementsByClass("img").get(0).child(1).attr("href");
            }

            parseImagePage(href, id, name, 0, area);
        } catch (IOException e) {
            System.err.println("解析图片失败:");
            System.err.println(href);
            e.printStackTrace();
        }
    }

    public static void parseImagePage(String href, String id, String name, int excuteNum, String area) throws IOException {
        if (excuteNum > 10) {
            return;
        }

        Document faceImgPage = readHtml(href, id, name);
        Element img = faceImgPage.getElementById("J_main-img");
        String src = img.attr("src");
        if (JDBCReader.readImage(src) == null) {
            ImageExecutor.execute(new MerchantImageData(Integer.valueOf(id), name, src, area));
        }
        Element nextE = faceImgPage.getElementById("next");
        if (nextE.hasAttr("href")) {
            String nextHref = nextE.attr("href");
            if (nextHref != null && nextHref != "") {
                parseImagePage(nextHref, id, name, ++excuteNum, area);
            }
        }
    }

    public static Document readHtml(String src, String id, String name) throws IOException {
        String url = ApplicationConfig.constructFullPath(src);
        HttpGet get = new HttpGet(url);
        HttpClient client = HttpClients.createDefault();
        get.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:53.0) Gecko/20100101 Firefox/53.0");
        get.setHeader("Referer", url);
        get.setHeader("Connection", "keep-alive");
        get.setHeader("Upgrade-Insecure-Requests", "1");
        get.setHeader("Cookie", "JSESSIONID=AF94082BB09EDD32E5F088A13EF4207E; cy=8; cye=chengdu; _hc.v=14450866-a676-237b-6b37-4b3db73349b8.1497592974; __utma=1.47095483.1497592974.1497592974.1497592974.1; __utmc=1; __utmz=1.1497592974.1.1.utmcsr=baidu|utmccn=(organic)|utmcmd=organic; s_ViewType=10; aburl=1; _lxsdk_cuid=15caf7f78c3c8-0786762e80a9b78-12646f4a-1fa400-15caf7f78c4c8; _lxsdk=15caf7f78c3c8-0786762e80a9b78-12646f4a-1fa400-15caf7f78c4c8; PHOENIX_ID=0a017912-15caf7fe006-1e993965; __mta=141292173.1497593074240.1497593074240.1497596838599.2; _lxsdk_s=15cafba289b-61e-862-a74%7C%7C20");
        HttpResponse response = client.execute(get);
        int code = response.getStatusLine().getStatusCode();
        if (code != 200) {
            if (code == 403) {
                try {
                    Thread.currentThread().sleep(2000);
                    return readHtml(src, id, name);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.err.println("获取失败，Code:" + code);
            System.err.println(src);
            JDBCWriter.writeNoFound(src, id, name);
            throw new IOException(String.format("获取失败，url：%s", src));
        }
        StringBuffer sb = new StringBuffer("");
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String s = "";
            while ((s = br.readLine()) != null) {
                sb.append(s + "\r\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Jsoup.parse(sb.toString(), ApplicationConfig.HOST);
    }
}
