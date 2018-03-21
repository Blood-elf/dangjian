package com.jeecms.my.freemarker.template;

import java.util.ArrayList;
import java.util.Iterator;
import com.jeecms.cms.action.directive.MyCondition;

import freemarker.template.TemplateModel;

public class TemplateMyConditionsModel extends ArrayList<MyCondition> implements TemplateModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String toJsonString() {
		if(this.size()<1)return null;
		Iterator<MyCondition> itr = this.iterator();
		String json = "[";
		while (itr.hasNext()) {
			MyCondition myCondition = (MyCondition) itr.next();
			json+=myCondition.toJsonString()+",";
		}
		int len = json.length();
		if(",".equals(json.substring(len-1,len)))
			json = json.substring(0, len-1);
		json+="]";
		return json;
	}
}
