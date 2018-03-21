package com.jeecms.cms.entity.main;

import java.io.Serializable;

public class PartyCommittee implements Serializable{
	private static final long serialVersionUID = 1L;

	// primary key
	private Integer id;

	// fields
	private String committeeName;
	private String typeName;
	private String branchName;
	private Integer delete;

	//Many to one
	private com.jeecms.cms.entity.main.PartyCommitteeMain partyCommittee;
	private com.jeecms.cms.entity.main.PartyCommitteeType type;

	// collections
//	private java.util.List<com.jeecms.cms.entity.main.PartyCommittee> partyCommitteelist;
//	private java.util.List<com.jeecms.cms.entity.main.PartyCommitteeType> partyCommitteeTypes;
	
	// constructors
	public PartyCommittee () {
		initialize();
	}

	public PartyCommittee (
		PartyCommitteeMain partyCommittee,
		PartyCommitteeType type,
		String branchName,
		Integer delete) {

		this.setPartyCommittee(partyCommittee);
		this.setType(type);
		this.setBranchName(branchName);
		this.setDelete(delete);
		initialize();
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
	}

	protected void initialize () {
	}

	
	public java.lang.Integer getId () {
		return id;
	}
	
	public java.lang.Integer getDelete() {
		return delete;
	}

	public void setDelete(java.lang.Integer delete) {
		this.delete = delete;
	}

	public com.jeecms.cms.entity.main.PartyCommitteeMain getPartyCommittee() {
		return partyCommittee;
	}

	public void setPartyCommittee(com.jeecms.cms.entity.main.PartyCommitteeMain partyCommitteeMain) {
		this.partyCommittee = partyCommitteeMain;
	}

	public com.jeecms.cms.entity.main.PartyCommitteeType getType() {
		return type;
	}

	public void setType(com.jeecms.cms.entity.main.PartyCommitteeType type) {
		this.type = type;
	}

	public java.lang.String getBranchName() {
		return branchName;
	}

	public void setBranchName(java.lang.String branchName) {
		this.branchName = branchName;
	}

	public String getTypeName() {
		if(this.typeName==null){
			setTypeName(this.type.getName());
		}		
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getCommitteeName() {
		if(this.committeeName==null){
			setCommitteeName(this.partyCommittee.getName());
		}
		return committeeName;
	}

	public void setCommitteeName(String committeeName) {
		this.committeeName = committeeName;
	}
}
