package com.jeecms.cms.dao.main.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.jeecms.cms.dao.main.PartyCommitteeDao;
import com.jeecms.cms.entity.main.ContentCheck;
import com.jeecms.cms.entity.main.PartyCommittee;
import com.jeecms.common.hibernate4.Finder;
import com.jeecms.common.hibernate4.HibernateBaseDao;

@Repository
public class PartyCommitteeDaoImpl extends HibernateBaseDao<PartyCommittee, Integer> implements PartyCommitteeDao{

	@Override
	@SuppressWarnings("unchecked")
	public List getList() {
		PartyCommittee p = new PartyCommittee();
		Finder f = Finder.create();
		f.append("select bean from PartyCommittee bean ");
		f.setCacheable(true);
		List list = find(f);

		return list;
	}
	private Finder getFinder(){
		Finder f = Finder.create();
		f.append("select bean from PartyCommittee bean ");
//		if (len == 1) {
//			f.append(" where bean.channel.id=:channelId ");
//			f.setParam("channelId", channelIds[0]);
//		} else {
//			f.append(" where bean.channel.id in (:channelIds)  ");
//			f.setParamList("channelIds", channelIds);
//		}
		return f;
	}
	@Override
	protected Class<PartyCommittee> getEntityClass() {
		return PartyCommittee.class;
	}
	@Override
	public List<PartyCommittee> getPartyCommitteeById(Integer id) {
		PartyCommittee p = new PartyCommittee();
		Finder f = Finder.create();
		f.append("select bean from PartyCommittee bean where bean.id=:id");
		f.setParam("id", id);
		f.setCacheable(true);
		List list = super.find(f);
//		for (Object obj : list) {
//			p.add((PartyCommittee) obj);
//		}
		return list;
//		return list.isEmpty()?null:(PartyCommittee)list.get(0);
	}
}
