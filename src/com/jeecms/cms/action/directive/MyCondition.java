package com.jeecms.cms.action.directive;

import net.sf.json.JSONObject;

/****
 * 针对傻叉Jeecms搜索功能写死而开发
 * @param field	字段
 * @param opt
 * 	eq或不填 -> 等于
 * 	range -> value<field<value2
 * 	all -> 全文搜索（功能还没写，要用自己补
 * @param value	
 * @param value2	通常不用填
 */
public class MyCondition{
	/**
	 * （start左包含，end右包含，like，in包含，eq等于，gt大于，gte大于等于，lt小于，lte小于等于，默认等于）
	 */
	public static final String PARAM_ATTR_START = "start";
	public static final String PARAM_ATTR_END = "end";
	public static final String PARAM_ATTR_LIKE = "like";
	public static final String PARAM_ATTR_IN = "in";
	public static final String PARAM_ATTR_EQ = "eq";
	public static final String PARAM_ATTR_GT = "gt";
	public static final String PARAM_ATTR_GTE = "gte";
	public static final String PARAM_ATTR_LT = "lt";
	public static final String PARAM_ATTR_LTE = "lte";
	
	private String field;
	private String opt;
	private String value;
	public String toJsonString(){
		JSONObject js = new JSONObject();
		js.put("field", field);
		js.put("opt", opt);
		js.put("value", value);
		return js.toString();
	}
	@Override
	public String toString() {
		return "MyCondition [field=" + field + ", opt=" + opt + ", value="
				+ value + "]";
	}
	public MyCondition(String field, EnumOpt opt, String value) {
		super();
		this.setField(field);
		this.setOpt(opt);
		this.setValue(value);
	}
	public MyCondition(String field, String value) {
		this(field,null,value);
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getOpt() {
		return opt;
	}
	public void setOpt(EnumOpt opt) {
		if(opt!=null)
		this.opt = opt.name();
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
