package com.jeecms.cms.entity.main;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PartyCommitteeType implements Serializable{
	private static final long serialVersionUID = 1L;
	private int id;
	private String name;
	private List<PartyCommittee> branchList;
	private Set partyCommittees = new HashSet(0);
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public PartyCommitteeType(String name) {
		this.name = name;
	}
	public PartyCommitteeType() {
	}
	public Set getPartyCommittees() {
		return partyCommittees;
	}
	public void setPartyCommittees(Set partyCommittees) {
		this.partyCommittees = partyCommittees;
	}
	public List<PartyCommittee> getBranchList() {
		return branchList;
	}
	public void setBranchList(List<PartyCommittee> branchList) {
		this.branchList = branchList;
	}
}
