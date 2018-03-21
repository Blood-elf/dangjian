package com.jeecms.cms.entity.main;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONObject;

public class Party {
	private int id;
	private String name;
	List<PartyCommitteeType> typeList = new ArrayList<PartyCommitteeType>();
	List<PartyCommittee> branchList= new ArrayList<PartyCommittee>();
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTypeListJsonString(){
		List<PartyCommitteeType> list = this.getTypeList(); 
		if(list.size()<1)return null;
		Iterator<PartyCommitteeType> itr = list.iterator();
		String json = "[";
		while (itr.hasNext()) {
			PartyCommitteeType p = (PartyCommitteeType) itr.next();
			json+=typeToJsonString(p)+",";
		}
		int len = json.length();
		if(",".equals(json.substring(len-1,len)))
			json = json.substring(0, len-1);
		json+="]";
		return json;
	}
	public String getBranchListJsonString(){
		List<PartyCommittee> list = this.getBranchList(); 
		if(list.size()<1)return null;
		Iterator<PartyCommittee> itr = list.iterator();
		String json = "[";
		while (itr.hasNext()) {
			PartyCommittee p = (PartyCommittee) itr.next();
			json+=committeeToJsonString(p)+",";
		}
		int len = json.length();
		if(",".equals(json.substring(len-1,len)))
			json = json.substring(0, len-1);
		json+="]";
		return json;
	}
	public String committeeToJsonString(PartyCommittee p) {
		JSONObject js = new JSONObject();
		js.put("id", p.getId());
		js.put("committeeId", p.getPartyCommittee().getId());
		js.put("typeId", p.getType().getId());
		js.put("committeeName", p.getCommitteeName());
		js.put("typeName", p.getTypeName());
		js.put("branchName", p.getBranchName());
		js.put("delete", p.getDelete());
		return js.toString();
	}
	public String typeToJsonString(PartyCommitteeType p) {
		JSONObject js = new JSONObject();
		js.put("id", p.getId());
		js.put("name", p.getName());
		return js.toString();
	}
	public String toJsonString() {
		JSONObject js = new JSONObject();
		js.put("id", this.getId());
		js.put("name", this.getName());
		js.put("typeList", this.getTypeListJsonString());
		js.put("branchList", this.getBranchListJsonString());
		return js.toString();

	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<PartyCommitteeType> getTypeList() {
		return typeList;
	}
	public void setTypeList(List<PartyCommitteeType> typeList) {
		this.typeList = typeList;
	}
	public List<PartyCommittee> getBranchList() {
		return branchList;
	}
	public void setBranchList(List<PartyCommittee> branchList) {
		this.branchList = branchList;
	}
}
