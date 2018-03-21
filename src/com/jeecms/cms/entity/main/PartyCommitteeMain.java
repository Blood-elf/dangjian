package com.jeecms.cms.entity.main;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class PartyCommitteeMain implements Serializable{
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String name;
	private Set partyCommittees = new HashSet(0);
	
	public PartyCommitteeMain() {}
	public PartyCommitteeMain(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Set getPartyCommittees() {
		return partyCommittees;
	}
	public void setPartyCommittees(Set partyCommittees) {
		this.partyCommittees = partyCommittees;
	}
	
}