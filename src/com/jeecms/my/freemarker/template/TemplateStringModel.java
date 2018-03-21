package com.jeecms.my.freemarker.template;

import freemarker.template.TemplateModel;

public class TemplateStringModel implements TemplateModel{
	private String str;

	public TemplateStringModel(String str) {
		super();
		this.str = str;
	}
	@Override
	public String toString() {
		return str;
	}
	public String getStr() {
		return str;
	}
	public void setStr(String str) {
		this.str = str;
	}
	
}
