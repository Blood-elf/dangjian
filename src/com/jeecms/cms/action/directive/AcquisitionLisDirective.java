package com.jeecms.cms.action.directive;

import static com.jeecms.cms.Constants.TPL_STYLE_LIST;
import static com.jeecms.cms.Constants.TPL_SUFFIX;
import static com.jeecms.common.web.Constants.UTF8;
import static com.jeecms.core.web.util.FrontUtils.PARAM_STYLE_LIST;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import com.jeecms.common.web.freemarker.DefaultObjectWrapperBuilderFactory;
import com.jeecms.common.web.freemarker.DirectiveUtils;
import com.jeecms.common.web.freemarker.ParamsRequiredException;
import com.jeecms.common.web.freemarker.DirectiveUtils.InvokeType;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.web.util.FrontUtils;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class AcquisitionLisDirective implements TemplateDirectiveModel{
	/**
	 * 模板名称
	 */
	public static final String TPL_NAME = "content_page";

	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		CmsSite site = FrontUtils.getSite(env);
		Map<String, TemplateModel> paramWrap = new HashMap<String, TemplateModel>(
				params);

		@SuppressWarnings("unchecked")
		String url = DirectiveUtils.getString("url", params);
		String startHtml = DirectiveUtils.getString("startHtml", params);
		String endHtml = DirectiveUtils.getString("endHtml", params);
		Integer count = DirectiveUtils.getInt("count", params);
		String titleLen = DirectiveUtils.getString("titleLen", params);
		String charset = DirectiveUtils.getString("charset", params);
		
		List<String> lis =this.getList(url, startHtml, endHtml, count, charset);
		paramWrap.put("tag_lis", DefaultObjectWrapperBuilderFactory.getDefaultObjectWrapper().wrap(lis));
		
		Map<String, TemplateModel> origMap = DirectiveUtils
				.addParamsToVariable(env, paramWrap);
		InvokeType type = DirectiveUtils.getInvokeType(params);
		String listStyle = DirectiveUtils.getString(PARAM_STYLE_LIST, params);
		if (InvokeType.sysDefined == type) {
			if (StringUtils.isBlank(listStyle)) {
				throw new ParamsRequiredException(PARAM_STYLE_LIST);
			}
			env.include(TPL_STYLE_LIST + listStyle + TPL_SUFFIX, UTF8, true);
			FrontUtils.includePagination(site, params, env);
		} else if (InvokeType.userDefined == type) {
			if (StringUtils.isBlank(listStyle)) {
				throw new ParamsRequiredException(PARAM_STYLE_LIST);
			}
			FrontUtils.includeTpl(TPL_STYLE_LIST, site, env);
			FrontUtils.includePagination(site, params, env);
		} else if (InvokeType.custom == type) {
			FrontUtils.includeTpl(TPL_NAME, site, params, env);
			FrontUtils.includePagination(site, params, env);
		} else if (InvokeType.body == type) {
			if (body != null) {
				body.render(env.getOut());
				FrontUtils.includePagination(site, params, env);
			}
		} else {
			throw new RuntimeException("invoke type not handled: " + type);
		}
		DirectiveUtils.removeParamsFromVariable(env, paramWrap, origMap);
	}

	public List<String> getList(String url,String startHtml,String endHtml,Integer count,String charset){
		List<String> lis = new ArrayList<String>();
		
		charset=StringUtils.isNotBlank(charset)?charset:"Utf-8";
		
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		CloseableHttpClient client = httpClientBuilder.build();  
		CharsetHandlerr handler = new CharsetHandlerr(charset);
		try {
			HttpGet httpget = new HttpGet(new URI(url));
			String base = url.substring(0, url.indexOf("/", url
					.indexOf("//") + 2));
			String html = client.execute(httpget, handler);
			int start = html.indexOf(startHtml);
			if (start == -1) {
				return lis;
			}
			start += startHtml.length();
			int end = html.indexOf(endHtml, start);
			if (end == -1) {
				return lis;
			}
			String s = html.substring(start, end);
			start = 0;
			String link;
			String linkStart="<li>";
			String linkEnd="</li>";
			while ((start = s.indexOf(linkStart, start)) != -1) {
//				start += linkStart.length();
				end = s.indexOf(linkEnd, start);
				end += linkEnd.length();
				if (end == -1) {
					return lis;
				} else {
					link = s.substring(start, end);
					//内容地址前缀
//					if(StringUtils.isNotBlank(acqu.getContentPrefix())){
//						link=acqu.getContentPrefix()+link;
//					}
//					if (!link.startsWith("http")) {
//						link = base + link;
//					}
//					log.debug("content link: {}", link);
					lis.add(link);
					start = end;
				}
			}
		} catch (Exception e) {
		}
		return lis;
	}

	public class CharsetHandlerr implements ResponseHandler<String> {
		private String charset;

		public CharsetHandlerr(String charset) {
			this.charset = charset;
		}

		public String handleResponse(HttpResponse response)
				throws ClientProtocolException, IOException {
			StatusLine statusLine = response.getStatusLine();
			if (statusLine.getStatusCode() >= 300) {
				throw new HttpResponseException(statusLine.getStatusCode(),
						statusLine.getReasonPhrase());
			}
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				if (!StringUtils.isBlank(charset)) {
					return EntityUtils.toString(entity, charset);
				} else {
					return EntityUtils.toString(entity);
				}
			} else {
				return null;
			}
		}
	}
}
