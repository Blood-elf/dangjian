package com.jeecms.cms.action.front;

import static com.jeecms.cms.Constants.TPLDIR_SPECIAL;
import static com.jeecms.cms.Constants.TPL_SUFFIX;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jeecms.cms.action.directive.EnumOpt;
import com.jeecms.cms.action.directive.MyCondition;
import com.jeecms.cms.action.directive.PartyCommitteeDirective;
import com.jeecms.cms.entity.assist.CmsSearchWords;
import com.jeecms.cms.entity.main.Channel;
import com.jeecms.cms.entity.main.Party;
import com.jeecms.cms.entity.main.PartyCommittee;
import com.jeecms.cms.manager.assist.CmsSearchWordsMng;
import com.jeecms.cms.manager.main.ChannelMng;
import com.jeecms.cms.service.SearchWordsCache;
import com.jeecms.common.util.StrUtils;
import com.jeecms.common.web.RequestUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.web.util.CmsUtils;
import com.jeecms.core.web.util.FrontUtils;
import com.jeecms.my.freemarker.template.TemplateMyConditionsModel;


@Controller
public class SearchAct {
	public static final String SEARCH_INPUT = "tpl.searchInput";
	public static final String SEARCH_RESULT = "tpl.searchResult";
	public static final String SEARCH_ERROR = "tpl.searchError";
	public static final String SEARCH_JOB = "tpl.searchJob";
	public static final String SEARCH_ORGANIZE_LIFE = "tpl.searchOrganizeLife";
	@Autowired
	private PartyCommitteeDirective partyCommitteeDirective;
	
	@RequestMapping(value = "/search*.jspx", method = RequestMethod.GET)
	public String index(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		// 将request中所有参数保存至model中。
		model.putAll(RequestUtils.getQueryParams(request));
		FrontUtils.frontData(request, model, site);
		FrontUtils.frontPageData(request, model);
		String q = RequestUtils.getQueryParam(request, "q");
		String channelId = RequestUtils.getQueryParam(request, "channelId");
		if (StringUtils.isBlank(q) && StringUtils.isBlank(channelId)) {
			return FrontUtils.getTplPath(request, site.getSolutionPath(),
					TPLDIR_SPECIAL, SEARCH_INPUT);
		} else {
			String parseQ=parseKeywords(q);
			model.addAttribute("input",q);
			model.addAttribute("q",parseQ);
			searchWordsCache.cacheWord(q);
			return FrontUtils.getTplPath(request, site.getSolutionPath(),
					TPLDIR_SPECIAL, SEARCH_RESULT);
		}
	}
	
	@RequestMapping(value = "/searchJob*.jspx", method = RequestMethod.GET)
	public String searchJob(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		String q = RequestUtils.getQueryParam(request, "q");
		String category = RequestUtils.getQueryParam(request, "category");
		String workplace = RequestUtils.getQueryParam(request, "workplace");
		String parseQ="";
		model.putAll(RequestUtils.getQueryParams(request));
		FrontUtils.frontData(request, model, site);	
		FrontUtils.frontPageData(request, model);
		if (StringUtils.isBlank(q)) {
			model.remove("q");
		}else{
			//处理lucene查询字符串中的关键字
			parseQ=parseKeywords(q);
			parseQ=StrUtils.xssEncode(parseQ);
			if(!q.equals(parseQ)){
				return "redirect:searchJob.jspx";
			}
			model.addAttribute("q",parseQ);
		}
		model.addAttribute("input",parseQ);
		model.addAttribute("queryCategory",category);
		model.addAttribute("queryWorkplace",workplace);
		return FrontUtils.getTplPath(request, site.getSolutionPath(),
				TPLDIR_SPECIAL, SEARCH_JOB);
	}
	
	@RequestMapping("/search/v_ajax_list.jspx")
	public void ajaxList(HttpServletRequest request,HttpServletResponse response, ModelMap model) throws JSONException {
		JSONObject object = new JSONObject();
		Map<String,String>wordsMap=new LinkedHashMap<String, String>();
		String word=RequestUtils.getQueryParam(request, "term");
		if(StringUtils.isNotBlank(word)){
			List<CmsSearchWords>words=manager.getList(CmsUtils.getSiteId(request),
					word,null,CmsSearchWords.HIT_DESC,0,20,true);
			for(CmsSearchWords w:words){
				wordsMap.put(w.getName(), w.getName());
			}
		}
		object.put("words", wordsMap);
		ResponseUtils.renderJson(response, object.get("words").toString());
	}
	
	@RequestMapping(value = "/searchCustom*.jspx")
	public String searchCustom(String tpl, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		if(StringUtils.isNotBlank(tpl)){
			// 将request中所有参数保存至model中。
			model.putAll(RequestUtils.getQueryParams(request));
			FrontUtils.frontData(request, model, site);
			FrontUtils.frontPageData(request, model);
			return FrontUtils.getTplPath(site.getSolutionPath(), TPLDIR_SPECIAL,
					tpl);
		}else{
			return FrontUtils.pageNotFound(request, response, model);
		}
	}
	
	public static String parseKeywords(String q){
		char c='\\';
		int cIndex=q.indexOf(c);
		if(cIndex!=-1&&cIndex==0){
			q=q.substring(1);
		}
		if(cIndex!=-1&&cIndex==q.length()-1){
			q=q.substring(0,q.length()-1);
		}
		try {
			String regular = "[\\+\\-\\&\\|\\!\\(\\)\\{\\}\\[\\]\\^\\~\\*\\?\\:\\\\]";
			Pattern p = Pattern.compile(regular);
			Matcher m = p.matcher(q);
			String src = null;
			while (m.find()) {
				src = m.group();
				q = q.replaceAll("\\" + src, ("\\\\" + src));
			}
			q = q.replaceAll("AND", "and").replaceAll("OR", "or").replace("NOT", "not").replace("[", "［").replace("]", "］");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return  q;
	}

	@Autowired
	private CmsSearchWordsMng manager;
	@Autowired
	private SearchWordsCache searchWordsCache;
	private String TPLDIR_CHANNEL = "channel";
	
	@RequestMapping(value = "*/searchOrganizeLifeList*.jspx", method = RequestMethod.GET)
	public String searchOrganizeLife(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		return this.searchOrganizeLife(request, response, model, "alone/alone_organizedLifeList");
	}
	@RequestMapping(value = "*/searchOrganizeLife*.jspx", method = RequestMethod.GET)
	public String searchOrganizeLife(HttpServletRequest request,
			HttpServletResponse response, ModelMap model,String searchOrganizeLifeReturnUrl) {
		//取参数
		CmsSite site = CmsUtils.getSite(request);
		String sponsor = getString(request, "sponsor");
		String party_main_id = getString(request, "party_main");
		String activity_time = getString(request, "activity_time");
		String activity_type = getString(request, "activity_type");
		String status = getString(request, "status");
		List<String> branchIds = new ArrayList<String>();
		
		if(StringUtils.isNotBlank(sponsor)&&StringUtils.isNotBlank(party_main_id)){
			List<Party> partys = partyCommitteeDirective.getParty();
			for (Party party : partys) {
				if(party.getId()==Integer.parseInt(party_main_id)){
					for (PartyCommittee p: party.getBranchList()) {
						if(p.getBranchName().contains(sponsor)){
							branchIds.add(p.getPartyCommittee().getId()+"-"+p.getType().getId()+"-"+p.getId());
						}
					}
				}
			}
		}else if(StringUtils.isNotBlank(sponsor)){
			for (PartyCommittee p: partyCommitteeDirective.getList()) {
				if(p.getBranchName().contains(sponsor)){
					branchIds.add(p.getPartyCommittee().getId()+"-"+p.getType().getId()+"-"+p.getId());
				}
			}
		}
		TemplateMyConditionsModel myConditions = new TemplateMyConditionsModel();
		
		//时间
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    	String timeNow = format.format(new Date());
        if(StringUtils.isNotBlank(activity_time)){
    		Calendar c = Calendar.getInstance();
    		c.setTime(new Date());
        	Date time;
        	if("lastWeek".equals(activity_time)){
        		c.add(Calendar.DATE, -7);
        	}else if("lastMonth".equals(activity_time)){
        		c.add(Calendar.MONTH, -1);
        	}else if("lastThreeMonth".equals(activity_time)){
        		c.add(Calendar.MONTH, -3);
        	}
            time = c.getTime();
        	String timeStr = format.format(time);
        	myConditions.add(new MyCondition("activity_time",EnumOpt.between,timeStr+","+timeNow));
//        	myConditions.add(new MyCondition("activity_time",EnumOpt.before,timeNow));
        }
        //状态判断
        if(StringUtils.isNotBlank(status)){
        	EnumOpt opt = null;
        	if("未开始".equals(status)){
        		opt = EnumOpt.after;
            	myConditions.add(new MyCondition("status","未结束"));
            	myConditions.add(new MyCondition("activity_time",opt,timeNow));
        	}else if("进行中".equals(status)){
        		opt = EnumOpt.before;
            	myConditions.add(new MyCondition("status","未结束"));
            	myConditions.add(new MyCondition("activity_time",opt,timeNow));
        	}else if("已结束".equals(status)){
            	myConditions.add(new MyCondition("status",status));
        	}
        }
		
		if(StringUtils.isNotBlank(activity_type))myConditions.add(new MyCondition("activity_type",EnumOpt.like,activity_type));

		//发布者条件比较复杂
		if(branchIds.size()>0){
			String branchIdsHql ="";
			for (String branchId : branchIds) {
				branchIdsHql += branchId+",";
			}
			myConditions.add(new MyCondition("sponsor", EnumOpt.in, branchIdsHql.substring(0, branchIdsHql.length()-1)));
		}else if(StringUtils.isBlank(sponsor) && StringUtils.isNotBlank(party_main_id)){
			myConditions.add(new MyCondition("sponsor", EnumOpt.start, party_main_id+"-"));
		}else if(StringUtils.isNotBlank(sponsor)){
			myConditions.add(new MyCondition("1", EnumOpt.eq, "2"));
		}
		model.putAll(RequestUtils.getQueryParams(request));
		FrontUtils.frontData(request, model, site);
		FrontUtils.frontPageData(request, model);
		
		model.addAttribute("myConditions",myConditions.toJsonString());
		model.addAttribute("querySponsor",sponsor);
		if(StringUtils.isNotBlank(party_main_id))model.addAttribute("queryPartyMainId",Integer.parseInt(party_main_id));

		model.addAttribute("activity_time",activity_time);
		model.addAttribute("activity_type",activity_type);
		model.addAttribute("status",status);
		Channel channel = channelMng.findById(105);
		model.addAttribute("channel",channel);
		
		String equipment=(String) request.getAttribute("ua");
		String solution = site.getSolutionPath();
		if(StringUtils.isNotBlank(equipment)&&equipment.equals("mobile")){
			solution=site.getMobileSolutionPath();
		}
//		return solution + "/special/organizeLife" + TPL_SUFFIX;
		if(StringUtils.isNotBlank(searchOrganizeLifeReturnUrl))return solution +"/"+searchOrganizeLifeReturnUrl+TPL_SUFFIX;
		return solution + "/" + TPLDIR_CHANNEL + "/organizeLife" + TPL_SUFFIX;
	}

	@RequestMapping(value = "*/searchPlan*.jspx", method = RequestMethod.GET)
	public String searchPlan(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		//取参数
		CmsSite site = CmsUtils.getSite(request);
		String sponsor = getString(request, "sponsor");

		TemplateMyConditionsModel myConditions = new TemplateMyConditionsModel();
		
		if(StringUtils.isNotBlank(sponsor))myConditions.add(new MyCondition("sponsor", EnumOpt.start, sponsor));
		
		model.putAll(RequestUtils.getQueryParams(request));
		FrontUtils.frontData(request, model, site);
		FrontUtils.frontPageData(request, model);
		
		model.addAttribute("myConditions",myConditions.toJsonString());
		if(StringUtils.isNotBlank(sponsor)){
			model.addAttribute("sponsorId",Integer.parseInt(sponsor.split("-")[0]));
			model.addAttribute("sponsorValue",sponsor);
		}
		Channel channel = channelMng.findById(134);
		model.addAttribute("channel",channel);
		
		String equipment=(String) request.getAttribute("ua");
		String solution = site.getSolutionPath();
		if(StringUtils.isNotBlank(equipment)&&equipment.equals("mobile")){
			solution=site.getMobileSolutionPath();
		}
		return solution + "/" + TPLDIR_CHANNEL + "/plan" + TPL_SUFFIX;
	}

	@Autowired ChannelMng channelMng;
	@SuppressWarnings("null")
	@RequestMapping(value = "*/searchVoluntaryService*.jspx", method = RequestMethod.GET)
	public String searchVoluntaryService(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		//取参数
		CmsSite site = CmsUtils.getSite(request);
		String sponsor = replaceCode(getString(request, "sponsor"));
		String party_main_id = getString(request, "party_main");
		String activity_time = getString(request, "activity_time");
		String activity_type = getString(request, "activity_type");
		String status = getString(request, "status");
		List<String> branchIds = new ArrayList<String>();
		
		if(StringUtils.isNotBlank(sponsor)&&StringUtils.isNotBlank(party_main_id)){
			List<Party> partys = partyCommitteeDirective.getParty();
			for (Party party : partys) {
				if(party.getId()==Integer.parseInt(party_main_id)){
					for (PartyCommittee p: party.getBranchList()) {
						if(p.getBranchName().contains(sponsor)){
							branchIds.add(p.getPartyCommittee().getId()+"-"+p.getType().getId()+"-"+p.getId());
						}
					}
				}
			}
		}else if(StringUtils.isNotBlank(sponsor)){
			for (PartyCommittee p: partyCommitteeDirective.getList()) {
				if(p.getBranchName().contains(sponsor)){
					branchIds.add(p.getPartyCommittee().getId()+"-"+p.getType().getId()+"-"+p.getId());
				}
			}
		}
		TemplateMyConditionsModel myConditions = new TemplateMyConditionsModel();
		
		//时间
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    	String timeNow = format.format(new Date());
        if(StringUtils.isNotBlank(activity_time)){
    		Calendar c = Calendar.getInstance();
    		c.setTime(new Date());
        	Date time;
        	if("lastWeek".equals(activity_time)){
        		c.add(Calendar.DATE, -7);
        	}else if("lastMonth".equals(activity_time)){
        		c.add(Calendar.MONTH, -1);
        	}else if("lastThreeMonth".equals(activity_time)){
        		c.add(Calendar.MONTH, -3);
        	}
            time = c.getTime();
        	String timeStr = format.format(time);
        	myConditions.add(new MyCondition("activity_time",EnumOpt.between,timeStr+","+timeNow));
        }
        //状态判断
        if(StringUtils.isNotBlank(status)){
        	EnumOpt opt = null;
        	if("未开始".equals(status)){
        		opt = EnumOpt.after;
            	myConditions.add(new MyCondition("activity_time",opt,timeNow));
        	}else if("进行中".equals(status)){
            	myConditions.add(new MyCondition("activity_time",EnumOpt.before,timeNow));
            	myConditions.add(new MyCondition("end_time",EnumOpt.after,timeNow));
        	}else if("已结束".equals(status)){
            	myConditions.add(new MyCondition("end_time",EnumOpt.realBefore,timeNow));
        	}
        }

		if(StringUtils.isNotBlank(activity_type)){
			myConditions.add(new MyCondition("activity_type",EnumOpt.in,activity_type));
		}
		//发布者条件比较复杂
		if(branchIds.size()>0){
			String branchIdsHql ="";
			for (String branchId : branchIds) {
				branchIdsHql += branchId+",";
			}
			myConditions.add(new MyCondition("sponsor", EnumOpt.in, branchIdsHql.substring(0, branchIdsHql.length()-1)));
		}else if(StringUtils.isBlank(sponsor) && StringUtils.isNotBlank(party_main_id)){
			myConditions.add(new MyCondition("sponsor", EnumOpt.start, party_main_id+"-"));
		}else if(StringUtils.isNotBlank(sponsor)){
			myConditions.add(new MyCondition("1", EnumOpt.eq, "2"));
		}
		
		model.putAll(RequestUtils.getQueryParams(request));
		FrontUtils.frontData(request, model, site);
		FrontUtils.frontPageData(request, model);
		
		model.addAttribute("myConditions",myConditions.toJsonString());
		model.addAttribute("querySponsor",sponsor);
		if(StringUtils.isNotBlank(party_main_id))model.addAttribute("queryPartyMainId",Integer.parseInt(party_main_id));
		model.addAttribute("activity_time",activity_time);
		model.addAttribute("queryActivityType",activity_type);
		model.addAttribute("status",status);
		Channel channel = channelMng.findById(119);
		model.addAttribute("channel",channel);
		
		String equipment=(String) request.getAttribute("ua");
		String solution = site.getSolutionPath();
		if(StringUtils.isNotBlank(equipment)&&equipment.equals("mobile")){
			solution=site.getMobileSolutionPath();
		}
//		return solution + "/special/voluntaryService" + TPL_SUFFIX;
		return solution + "/" + TPLDIR_CHANNEL + "/voluntaryService" + TPL_SUFFIX;
	}
	private String getString(HttpServletRequest request, String string){
		String ss=request.getParameter(string);
		return StringUtils.isBlank(ss)?"":ss;
	}
	private String replaceCode(String str){
		return str.replace("&＃40;", "(").replace("&＃40;", ")");
	}
}
