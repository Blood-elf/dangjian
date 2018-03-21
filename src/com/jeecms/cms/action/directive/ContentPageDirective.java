package com.jeecms.cms.action.directive;

import static com.jeecms.cms.Constants.TPL_STYLE_LIST;
import static com.jeecms.cms.Constants.TPL_SUFFIX;
import static com.jeecms.common.web.Constants.UTF8;
import static com.jeecms.common.web.freemarker.DirectiveUtils.OUT_LIST;
import static com.jeecms.common.web.freemarker.DirectiveUtils.OUT_PAGINATION;
import static com.jeecms.core.web.util.FrontUtils.PARAM_STYLE_LIST;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import com.jeecms.cms.action.directive.abs.AbstractContentDirective;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.web.freemarker.DefaultObjectWrapperBuilderFactory;
import com.jeecms.common.web.freemarker.DirectiveUtils;
import com.jeecms.common.web.freemarker.ParamsRequiredException;
import com.jeecms.common.web.freemarker.DirectiveUtils.InvokeType;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.web.util.FrontUtils;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

import com.jeecms.common.web.freemarker.DirectiveUtilsEx;
import com.jeecms.my.freemarker.template.TemplateStringModel;
/**
 * 内容分页标签
 */
public class ContentPageDirective extends AbstractContentDirective {
	/**
	 * 模板名称
	 */
	public static final String TPL_NAME = "content_page";

	@SuppressWarnings("unchecked")
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		CmsSite site = FrontUtils.getSite(env);
		//魔改3期
		List<MyCondition> myConditions = DirectiveUtilsEx.getMyConditions(params);
		
	 	if(myConditions!=null&&myConditions.size() > 0){
	 		for (MyCondition c : myConditions) {
				String key = "s_"+c.getField();
				String opt = "o_"+c.getField();
				String value=c.getValue();
		 		params.put(key, new TemplateStringModel(value));
		 		params.put(opt, new TemplateStringModel(c.getOpt()));
			}
	 	}
	 	Pagination page = (Pagination) super.getData(params, env);
		Map<String, TemplateModel> paramWrap = new HashMap<String, TemplateModel>(
				params);
		paramWrap.put(OUT_PAGINATION, DefaultObjectWrapperBuilderFactory.getDefaultObjectWrapper().wrap(page));
		paramWrap.put(OUT_LIST, DefaultObjectWrapperBuilderFactory.getDefaultObjectWrapper().wrap(page.getList()));
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
	
	@Override
	protected boolean isPage() {
		return true;
	}

}
