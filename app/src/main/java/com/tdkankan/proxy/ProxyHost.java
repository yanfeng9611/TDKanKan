package com.tdkankan.proxy;

import static java.lang.Thread.sleep;

import com.tdkankan.Data.GlobalConfig;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

public class ProxyHost {

    public static Proxy getProxyHost() {
        if (GlobalConfig.proxyCacheList.isEmpty()) {
            ProxyHost proxyHost = new ProxyHost();
            try{
                proxyHost.spiderProxy();
                sleep(5000);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        int size = GlobalConfig.proxyCacheList.size();
        Random random = new Random();
        ArrayList<String> list = GlobalConfig.proxyCacheList.get(random.nextInt(size));
        return new Proxy(Proxy.Type.HTTP, new InetSocketAddress(list.get(0), Integer.parseInt(list.get(1))));
    }

    public static String getProxyAgent() {
        String[] proxy = {"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:46.0) Gecko/20100101 Firefox/46.0",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.87 Safari/537.36 OPR/37.0.2178.32",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/534.57.2 (KHTML, like Gecko) Version/5.1.7 Safari/534.57.2",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2486.0 Safari/537.36 Edge/13.10586",
                "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko",
                "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0)",
                "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)",
                "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; WOW64; Trident/4.0)",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 BIDUBrowser/8.3 Safari/537.36",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.80 Safari/537.36 Core/1.47.277.400 QQBrowser/9.4.7658.400",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 UBrowser/5.6.12150.8 Safari/537.36",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36 SE 2.X MetaSr 1.0",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36 TheWorld 7",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.108 Safari/537.36",
                "Mozilla/5.0 (Windows NT 6.1; W…) Gecko/20100101 Firefox/60.0"};
        Random random = new Random();
        int idx = random.nextInt(proxy.length);
        return proxy[idx];
    }


    public void spiderProxy() throws IOException {
        String url = "";
        for (int idx = 1; idx < 10; idx++) {
            url = "https://www.89ip.cn/index_" + idx + ".html";
            ProxyHost proxyHost = new ProxyHost();
            proxyHost.getProxy(url);
        }
    }

    public void getProxy(String url) throws IOException {
        Document doc = Jsoup.connect(url).data("query", "Java").userAgent(ProxyHost.getProxyAgent()).get();
        Elements elements = doc.getElementsByTag("tr");
        int idx = 0;
        ArrayList<String> list = null;
        MyThreadSaveProxyIP myThreadSaveProxyIP;
        for (Element element : elements) {
            if (idx++ == 0) continue;
            myThreadSaveProxyIP = new MyThreadSaveProxyIP(element);
            new Thread(myThreadSaveProxyIP).start();
        }
    }

    class MyThreadSaveProxyIP implements Runnable {
        Element element;
        public MyThreadSaveProxyIP(Element element) {
            this.element = element;
        }

        @Override
        public void run() {
            String host = element.getElementsByTag("td").get(0).text().trim().replace(" ", "");
            String port = element.getElementsByTag("td").get(1).text().trim().replace(" ", "");
            ArrayList<String> list = new ArrayList();
            list.add(host);
            list.add(port);
            boolean isValidProxyIP = checkProxyIP(host, Integer.parseInt(port));
            if (isValidProxyIP){
                GlobalConfig.proxyCacheList.add(list);
            }
        }
    }

    private boolean checkProxyIP(String host, int port) {
        try {
            HttpURLConnection connection = null;

            URL url = new URL("http://www.baidu.com");
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port));
            connection = (HttpURLConnection) url.openConnection(proxy);
//            connection = (HttpURLConnection) url.openConnection();
            //设置请求方式
            connection.setRequestMethod("POST");   //设置请求方式为POST
            connection.setDoOutput(true);   //允许写出
            connection.setDoInput(true);    //允许读入
            connection.setUseCaches(false);    //不使用缓存
            connection.setConnectTimeout(GlobalConfig.jsoupTimeOut);
            connection.setReadTimeout(GlobalConfig.jsoupTimeOut);
            connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            int responseCode = connection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }
}
