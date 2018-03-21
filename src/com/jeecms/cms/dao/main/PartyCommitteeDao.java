package com.jeecms.cms.dao.main;

import java.util.List;

import com.jeecms.cms.entity.main.PartyCommittee;

/**
 * 内容DAO接口。
 * 
 */
public interface PartyCommitteeDao {

	/**
	 * 获得内容列表
	 * 
	 * @param PCB_id
	 *            partyCommitteeBranchId。
	 * @param partyCommittee_Id
	 *            党工委id。
	 * @param type_Id
	 *            党工委组织类型。可以为null。
	 * @param delete
	 *            状态。
	 * @return
	 */
	public List<PartyCommittee> getList();
	
	public List<PartyCommittee> getPartyCommitteeById(Integer id);
//	public List<Content> getListForCollection(Integer siteId, Integer memberId, 
//			Integer first, Integer count);
//	
//	public Content findById(Integer id);
//
//	public Content save(PartyCommittee bean);
//
//	public Content updateByUpdater(Updater<Content> updater);
//
//	public Content deleteById(Integer id);

}