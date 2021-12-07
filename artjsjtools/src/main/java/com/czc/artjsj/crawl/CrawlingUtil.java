package com.czc.artjsj.crawl;

import com.czc.artjsj.crawl.builder.CrawlBuilderImpl;
import com.czc.artjsj.crawl.page.Page;
import com.czc.artjsj.crawl.page.PageParserTool;
import com.czc.artjsj.log.L;
import com.czc.artjsj.okhttp.OkHttpUtils;
import com.czc.artjsj.utils.CheckUtil;
import com.czc.artjsj.utils.TextUtils;
import okhttp3.Response;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 爬虫工具
 *
 * @author KLXair<BR />
 * 2019/10/12 上午12:15:30
 */
public class CrawlingUtil {
    private static volatile CrawlingUtil mInstance;

    public static CrawlBuilderImpl crawl() {
        return new CrawlBuilderImpl();
    }

    public static CrawlingUtil getInstance() {
        if (mInstance == null) {
            synchronized (CrawlingUtil.class) {
                if (mInstance == null) {
                    mInstance = new CrawlingUtil();
                }
            }
        }
        return mInstance;
    }

    /**
     * 开始抓取
     *
     * @param seeds       要抓取的URL队列
     * @param headers     定义过滤器，提取以 url 开头的链接
     * @param cssSelector 抓取选择器
     * @return Elements
     * @author KLXair
     */
    public Elements crawling(String seeds, Map<String, String> headers, String cssSelector) {
        Elements elements;
        if (TextUtils.isEmpty(seeds)) {
            L.e("seeds is null");
            return null;
        }
        if (!CheckUtil.isUrl(seeds)) {
            L.e("seeds illegal");
            return null;
        }

        /*
          合法url开始抓取页面
         */
        Response response;

        Page page = null;
        try {
            response = OkHttpUtils.get().url(seeds).headers(headers).build().readTimeOut(60000).writeTimeOut(60000)
                    .connTimeOut(60000).execute();
            byte[] responseBody = response.body().bytes();
            String contentType = response.header("Content-Type");
            /*
              封装成为页面
             */
            page = new Page(responseBody, seeds, contentType);
        } catch (IOException e) {
            L.e("crawling exception=" + e.getMessage());
            e.printStackTrace();
        }

        /*
        对page进行处理： 访问DOM的某个标签
         */
        elements = PageParserTool.select(page, cssSelector);

        return elements;
    }

    /**
     * 开始抓取
     *
     * @param seeds       要抓取的URL队列
     * @param headers     定义过滤器，提取以 url 开头的链接
     * @param cssSelector 抓取选择器
     */
    public List<Elements> crawling(List<String> seeds, Map<String, String> headers, String cssSelector) {
        List<Elements> listElements = new ArrayList<>();
        Elements elements;
        if (seeds == null) {
            L.e("seeds is null");
            return listElements;
        }
        if (seeds.size() <= 0) {
            L.e("seeds size is null");
            return listElements;
        }

        for (int i = 0; i < seeds.size(); i++) {
            /*
            先从待访问的序列中取出第一个
             */
            if (CheckUtil.isUrl(seeds.get(i))) {
                Response response;
                Page page = null;
                try {
                    response = OkHttpUtils.get().url(seeds.get(i)).headers(headers).build().readTimeOut(60000)
                            .writeTimeOut(60000).connTimeOut(60000).execute();
                    byte[] responseBody = response.body().bytes();
                    String contentType = response.header("Content-Type");
                    page = new Page(responseBody, seeds.get(i), contentType);
                } catch (IOException e) {
                    L.e("crawling exception=" + e.getMessage());
                    e.printStackTrace();
                }

                /*
                对page进行处理： 访问DOM的某个标签
                 */
                elements = PageParserTool.select(page, cssSelector);

                listElements.add(elements);
            } else {
                L.e("num=" + i + "illegal,url=" + seeds.get(i));
            }
        }
        return listElements;
    }
}
