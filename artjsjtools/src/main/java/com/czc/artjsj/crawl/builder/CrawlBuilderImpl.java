package com.czc.artjsj.crawl.builder;

import com.czc.artjsj.crawl.CrawlingUtil;
import com.czc.artjsj.log.L;
import com.czc.artjsj.utils.TextUtils;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * 爬虫工具-构建抓取接口实现类
 *
 * @author KLXair<BR />
 * 2019/10/12 上午12:15:30
 */
public class CrawlBuilderImpl extends CrawlBuilder {

    /**
     * 需要抓取的单个url
     *
     * @param url 需要抓取的一个URL地址<BR/>
     * @author KLXair<BR />
     * 2019/10/12 上午12:15:30
     */
    @Override
    public CrawlBuilder cUrl(String url) {
        this.url = url;
        return this;
    }

    /**
     * 需要抓取的URL列表地址
     *
     * @param urlList 需要抓取的URL列表地址<BR/>
     * @author KLXair<BR />
     * 2019/10/12 上午12:15:30
     */
    @Override
    public CrawlBuilder cUrl(List<String> urlList) {
        this.urlList = urlList;
        return this;
    }

    /**
     * 抓取URL使用的请求头（Map对象，可与cAddHeader一起使用，后添加的会覆盖前面添加的）
     *
     * @param headers 设置抓取的时候的请求头（Map对象，可与cAddHeader一起使用，后添加的会覆盖前面添加的）
     * @author KLXair<BR />
     * 2019/10/12 上午12:15:30
     */
    @Override
    public CrawlBuilder cHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    /**
     * 抓取URL使用的请求头
     * <p>
     * 设置抓取的时候的请求头
     *
     * @author KLXair<BR />
     * 2019/10/12 上午12:15:30
     */
    @Override
    public CrawlBuilder cAddHeader(String key, String val) {
        if (this.headers == null) {
            headers = new LinkedHashMap<>();
        }
        headers.put(key, val);
        return this;
    }

    /**
     * 抓取的HTML标签选择器使用
     *
     * @param select 使用格式为： div[class=test]
     * @author KLXair<BR />
     * 2019/10/12 上午12:15:30
     */
    @Override
    public CrawlBuilder cSelect(String select) {
        if (TextUtils.isEmpty(this.select)) {
            this.select = select;
        } else {
            this.select += (" " + select);
        }
        L.e("拼接完成后的select：" + this.select);
        return this;
    }

    /**
     * 调用该方法开始执行（当个请求地址使用）
     *
     * <font color="#FF0000">注意：如果当个url为空，但是urlList不为空，
     * 调用此方法会正常抓取urlList的第1个url内容</font>
     *
     * @author KLXair<BR />
     * 2019/10/12 上午12:15:30
     */
    @Override
    public Elements start() {
        if (TextUtils.isEmpty(url)) {
            if (urlList != null) {
                if (urlList.size() > 0) {
                    return CrawlingUtil.getInstance().crawling(urlList.get(0), headers, select);
                }
            }
        }
        return CrawlingUtil.getInstance().crawling(url, headers, select);

    }

    /**
     * 调用该方法开始执行（多个请求地址使用，输出为List的Elements）
     *
     * <font color="#FF0000">注意：如果当个urlList为空，但是url不为空， 调用此方法会正常抓取url内容</font>
     *
     * @author KLXair<BR />
     * 2019/10/12 上午12:15:30
     */
    @Override
    public List<Elements> startList() {
        if (urlList != null) {
            if (urlList.size() <= 0) {
                if (!TextUtils.isEmpty(url)) {
                    List<String> urlList = new ArrayList<String>();
                    urlList.add(url);
                    return CrawlingUtil.getInstance().crawling(urlList, headers, select);
                }
            }
        }
        return CrawlingUtil.getInstance().crawling(urlList, headers, select);
    }

}
