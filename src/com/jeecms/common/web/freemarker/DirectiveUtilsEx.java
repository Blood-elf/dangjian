package com.jeecms.common.web.freemarker;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.jeecms.cms.action.directive.EnumOpt;
import com.jeecms.cms.action.directive.MyCondition;
import com.jeecms.my.freemarker.template.TemplateMyConditionsModel;

import org.apache.commons.lang.StringUtils;

import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
public class DirectiveUtilsEx extends DirectiveUtils{
	
	public static final String MY_CONDITIONS = "myConditions";
	public static List<MyCondition> getMyConditions(Map<String, TemplateModel> params) throws TemplateException {
		TemplateModel model0 = params.get(MY_CONDITIONS);
		if (model0 == null) {
			return null;
		}
		TemplateMyConditionsModel model = new TemplateMyConditionsModel();
		JSONArray jsonArray = JSONArray.fromObject(model0.toString());
		@SuppressWarnings("unchecked")
		Iterator<JSONObject> itr = jsonArray.iterator();
		while (itr.hasNext()) {
			JSONObject c = (JSONObject) itr.next();
			if(StringUtils.isBlank((String) c.get("field"))){
				throw new MustListMyConditionException(MY_CONDITIONS);
			}
			model.add(new MyCondition((String) c.get("field"),StringUtils.isNotBlank((String) c.get("opt"))?EnumOpt.valueOf((String) c.get("opt")):null,(String) c.get("value")));
		}
		return model;
	}
}
