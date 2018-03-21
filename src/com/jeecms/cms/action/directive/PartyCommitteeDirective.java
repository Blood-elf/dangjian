package com.jeecms.cms.action.directive;

import static com.jeecms.cms.Constants.TPL_STYLE_LIST;
import static com.jeecms.cms.Constants.TPL_SUFFIX;
import static com.jeecms.common.web.Constants.UTF8;
import static com.jeecms.core.web.util.FrontUtils.PARAM_STYLE_LIST;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.jeecms.cms.dao.main.PartyCommitteeDao;
import com.jeecms.cms.entity.main.Party;
import com.jeecms.cms.entity.main.PartyCommittee;
import com.jeecms.cms.entity.main.PartyCommitteeType;
import com.jeecms.common.web.freemarker.DefaultObjectWrapperBuilderFactory;
import com.jeecms.common.web.freemarker.DirectiveUtils;
import com.jeecms.common.web.freemarker.ParamsRequiredException;
import com.jeecms.common.web.freemarker.DirectiveUtils.InvokeType;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.web.util.FrontUtils;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class PartyCommitteeDirective implements TemplateDirectiveModel{
	/**
	 * 模板名称
	 */
	public static final String TPL_NAME = "content_page";

	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		CmsSite site = FrontUtils.getSite(env);
		Map<String, TemplateModel> paramWrap = new HashMap<String, TemplateModel>(
				params);

		String sponsorId = DirectiveUtils.getString("sponsor", params);
	
		if(StringUtils.isNotBlank(sponsorId)){
			Integer branchId = Integer.parseInt(sponsorId.split("-")[2]);
			List<PartyCommittee> branchs =getPartyCommitteeById(branchId);
			paramWrap.put("branchName", DefaultObjectWrapperBuilderFactory.getDefaultObjectWrapper().wrap(branchs.get(0).getBranchName()));
			paramWrap.put("mainName", DefaultObjectWrapperBuilderFactory.getDefaultObjectWrapper().wrap(branchs.get(0).getPartyCommittee().getName()));
			paramWrap.put("branchs", DefaultObjectWrapperBuilderFactory.getDefaultObjectWrapper().wrap(branchs));
		}else{
			List<Party> partys = getParty();
			String partysJson = this.getJsonString();
			paramWrap.put("partys", DefaultObjectWrapperBuilderFactory.getDefaultObjectWrapper().wrap(partys));
			paramWrap.put("partysJson", DefaultObjectWrapperBuilderFactory.getDefaultObjectWrapper().wrap(partysJson));
		}
		Map<String, TemplateModel> origMap = DirectiveUtils
				.addParamsToVariable(env, paramWrap);
		InvokeType type = DirectiveUtils.getInvokeType(params);
		String listStyle = DirectiveUtils.getString(PARAM_STYLE_LIST, params);
		if (InvokeType.sysDefined == type) {
			if (StringUtils.isBlank(listStyle)) {
				throw new ParamsRequiredException(PARAM_STYLE_LIST);
			}
			env.include(TPL_STYLE_LIST + listStyle + TPL_SUFFIX, UTF8, true);
			FrontUtils.includePagination(site, params, env);
		} else if (InvokeType.userDefined == type) {
			if (StringUtils.isBlank(listStyle)) {
				throw new ParamsRequiredException(PARAM_STYLE_LIST);
			}
			FrontUtils.includeTpl(TPL_STYLE_LIST, site, env);
			FrontUtils.includePagination(site, params, env);
		} else if (InvokeType.custom == type) {
			FrontUtils.includeTpl(TPL_NAME, site, params, env);
			FrontUtils.includePagination(site, params, env);
		} else if (InvokeType.body == type) {
			if (body != null) {
				body.render(env.getOut());
				FrontUtils.includePagination(site, params, env);
			}
		} else {
			throw new RuntimeException("invoke type not handled: " + type);
		}
		DirectiveUtils.removeParamsFromVariable(env, paramWrap, origMap);
	}
	
	public List<Party> getParty() {
		List<PartyCommittee> branchList = getList();
		List<Integer> ints = new ArrayList<Integer>();
		List<List<Integer>> typeInts = new ArrayList<List<Integer>>();
		List<Party> partys = new ArrayList<Party>();
		for (PartyCommittee partyCommittee : branchList) {
			Integer i = partyCommittee.getPartyCommittee().getId();
			PartyCommitteeType type = partyCommittee.getType();
			Party newP;
			Integer iIndex=ints.indexOf(i);
			if(iIndex>-1){
				newP=partys.get(iIndex);
			}else{
				ints.add(i);
				newP = new Party();
			}
			newP.setId(partyCommittee.getPartyCommittee().getId());
			newP.setName(partyCommittee.getCommitteeName());
			newP.getBranchList().add(partyCommittee);
			Integer ii=newP.getTypeList().indexOf(type);
			if(ii>-1){
//				newP.getTypeList().get(ii).getBranchList().add(partyCommittee);
			}else{
				newP.getTypeList().add(type);
			}
			if(iIndex>-1){
				partys.set(iIndex, newP);
			}else{
				partys.add(newP);
			}
		}
		return partys;
	}
	public String getJsonString() {
		List<Party> partys = this.getParty();
		if(partys.size()<1)return null;
		Iterator<Party> itr = partys.iterator();
		String json = "[";
		while (itr.hasNext()) {
			Party p = (Party) itr.next();
			json+=p.toJsonString()+",";
		}
		int len = json.length();
		if(",".equals(json.substring(len-1,len)))
			json = json.substring(0, len-1);
		json+="]";
		return json;
	}
	@Autowired
	public PartyCommitteeDao dao;
	//这种得到的没有分类别分组织，只是最简单那张表
	public List<PartyCommittee> getList() {
		return dao.getList();
	}
	public List<PartyCommittee> getPartyCommitteeById(Integer id){
		return dao.getPartyCommitteeById(id);
	}
}
