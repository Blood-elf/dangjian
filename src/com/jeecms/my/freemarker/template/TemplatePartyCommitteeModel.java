package com.jeecms.my.freemarker.template;

import java.util.List;

import com.jeecms.cms.entity.main.PartyCommittee;

import freemarker.template.TemplateModel;

public class TemplatePartyCommitteeModel implements TemplateModel{

	private List<PartyCommittee> list;
	public List<PartyCommittee> getList() {
		return list;
	}
	public void setList(List<PartyCommittee> list) {
		this.list = list;
	}
}
