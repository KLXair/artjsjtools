package com.czc.artjsj.crawl.builder;

import org.jsoup.select.Elements;

import java.util.List;
import java.util.Map;

/**
 * 爬虫工具-构建抓取接口类
 * 
 * @author KLXair<BR/>
 *         2019/10/12 上午12:15:30
 */
public abstract class CrawlBuilder {
	protected String url;// 需要抓取的URL地址
	protected List<String> urlList;// 需要抓取的URL列表地址（该对象不为空，则优先使用该对象，将忽略url字段）
	protected Map<String, String> headers;// 设置抓取的时候的请求头
	protected String select;// 抓取来的HTML标签选择器使用

	/**
	 * 需要抓取的单个url
	 * 
	 * @param url
	 *            需要抓取的一个URL地址<BR/>
	 * @author KLXair<BR/>
	 *         2019/10/12 上午12:15:30
	 */
	public abstract CrawlBuilder cUrl(String url);

	/**
	 * 需要抓取的URL列表地址
	 * 
	 * @param urlList
	 *            需要抓取的URL列表地址<BR/>
	 * @author KLXair<BR/>
	 *         2019/10/12 上午12:15:30
	 */
	public abstract CrawlBuilder cUrl(List<String> urlList);

	/**
	 * 抓取URL使用的请求头（Map对象，可与cAddHeader一起使用，后添加的会覆盖前面添加的）
	 * 
	 * @param headers
	 *            设置抓取的时候的请求头（Map对象，可与cAddHeader一起使用，后添加的会覆盖前面添加的）
	 * @author KLXair<BR/>
	 *         2019/10/12 上午12:15:30
	 */
	public abstract CrawlBuilder cHeaders(Map<String, String> headers);

	/**
	 * 抓取URL使用的请求头
	 * 
	 * @param headers
	 *            设置抓取的时候的请求头
	 * @author KLXair<BR/>
	 *         2019/10/12 上午12:15:30
	 */
	public abstract CrawlBuilder cAddHeader(String key, String val);

	/**
	 * 抓取的HTML标签选择器使用
	 * 
	 * @param select
	 *            使用格式为： div[class=test]
	 * @author KLXair<BR/>
	 *         2019/10/12 上午12:15:30
	 */
	public abstract CrawlBuilder cSelect(String select);

	/**
	 * 调用该方法开始执行（当个请求地址使用）
	 * 
	 * <font color="#FF0000">注意：如果当个url为空，但是urlList不为空，
	 * 调用此方法会正常抓取urlList的第1个url内容</font>
	 * 
	 * @author KLXair<BR/>
	 *         2019/10/12 上午12:15:30
	 */
	public abstract Elements start();

	/**
	 * 调用该方法开始执行（多个请求地址使用，输出为List的Elements）
	 * 
	 * <font color="#FF0000">注意：如果当个urlList为空，但是url不为空， 调用此方法会正常抓取url内容</font>
	 * 
	 * @author KLXair<BR/>
	 *         2019/10/12 上午12:15:30
	 */
	public abstract List<Elements> startList();

}
