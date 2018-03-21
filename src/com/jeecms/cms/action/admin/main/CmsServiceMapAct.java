package com.jeecms.cms.action.admin.main;

import static com.jeecms.common.page.SimplePage.cpn;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.cms.entity.assist.CmsWebservice;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.web.CookieUtils;
import com.jeecms.common.web.RequestUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsConfigItem;
import com.jeecms.core.entity.CmsGroup;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.entity.CmsUser;
import com.jeecms.core.entity.CmsUserExt;
import com.jeecms.core.entity.ServiceMap;
import com.jeecms.core.manager.CmsConfigItemMng;
import com.jeecms.core.manager.CmsGroupMng;
import com.jeecms.core.manager.ServiceMapMng;
import com.jeecms.core.web.WebErrors;
import com.jeecms.core.web.util.CmsUtils;
import com.sun.xml.internal.ws.resources.UtilMessages;
@Controller
public class CmsServiceMapAct {
	private static final Logger log = LoggerFactory
			.getLogger(CmsServiceMapAct.class);
	@Autowired
	private ServiceMapMng service;
	@Autowired
	private CmsGroupMng cmsGroupMng;
	@Autowired
	private CmsConfigItemMng cmsConfigItemMng;
	@RequestMapping(value="/front/web_list.do")
	public void serviceList(String value,Integer pageNo,HttpServletRequest request,HttpServletResponse response, ModelMap model) {
		String v=request.getParameter("value");
		JSONObject json = new JSONObject(); 
		try {
			List<ServiceMap> pagination = service.findByProperty(value);
			json.put("pagination", pagination);
			ResponseUtils.renderJson(response, json.toString()); 
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;
	}
	
	//社区列表
	@RequiresPermissions("serviceMap:v_list")
	@RequestMapping("/serviceMap/v_list.do")
	public String list(String queryOrganizationName,String organizationType ,Integer pageNo,HttpServletRequest request, ModelMap model) {
		Pagination pagination = service.getPage(queryOrganizationName,organizationType,cpn(pageNo),
				CookieUtils.getPageSize(request));
		model.addAttribute("pagination", pagination);
		model.addAttribute("queryOrganizationName", queryOrganizationName);
		String result=null;
		if(organizationType.equals("COMMUNITY")){
			result="serviceMap/list";
		}else if(organizationType.equals("STREET")){
			result="serviceMap/list_street";
		}else if(organizationType.equals("ENTERPRISE")){
			result="serviceMap/list_snterprise";
		}
		return result;
	}
	
	@RequiresPermissions("serviceMap:v_add")
	@RequestMapping("/serviceMap/v_add.do")
	public String add(ModelMap model,HttpServletRequest request,String organizationType) {
		CmsSite site=CmsUtils.getSite(request);
		List<ServiceMap> groupList = service.getList();
		List<CmsConfigItem>registerItems=cmsConfigItemMng.getList(site.getConfig().getId(), CmsConfigItem.CATEGORY_REGISTER);
		model.addAttribute("registerItems", registerItems);
		model.addAttribute("groupList", groupList);
		String result=null;
		if(organizationType.equals("COMMUNITY")){
			result="serviceMap/add";
		}else if(organizationType.equals("STREET")){
			result="serviceMap/add_street";
		}else if(organizationType.equals("ENTERPRISE")){
			result="serviceMap/add_snterprise";
		}
		return result;
	}
	
	@RequiresPermissions("serviceMap:o_save")
	@RequestMapping("/serviceMap/o_save.do")
	public String save(String organizationName, String transferAddressCoordinates,
			Boolean isSame,String transferAddress, String transferStartdate,
			String transferEnddate, String transferScope, String transferContactInfo,
			String serviceAddress, String serviceStartdate, String serviceEnddate,
			String serviceScope, String serviceContactInfo, Timestamp createTime,
			Timestamp lastUpdateTime,String serviceAddressCoordinates,
			String organizationType,Integer  parentOrganizationId,
			Boolean isDisabled, String serviceUrl, String description,
			ServiceMap bean,HttpServletRequest request, ModelMap model) {
		WebErrors errors = WebErrors.create(request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		ServiceMap parentOrganization=null;
		if(parentOrganizationId!=null){
			parentOrganization=service.findById(parentOrganizationId);
		}
		String transferLongitude=null;
		String transferLatitude=null;
		String serviceLongitude=null;
		String serviceLatitude=null;
		if(organizationType.equals("ENTERPRISE")){
			transferLongitude="10";
			transferLatitude="10";
			isSame=isSame==null?true:false;
		}else{
			String[] transferLd=transferAddressCoordinates.split(",");
			transferLongitude=transferLd[0];
			transferLatitude=transferLd[1];
			if(!isSame){
				String[] serviceLd=serviceAddressCoordinates.split(",");
				serviceLongitude=serviceLd[0];
				serviceLatitude=serviceLd[1];
			}
		}
		service.save(organizationName,transferLongitude,transferLatitude,serviceLongitude,serviceLatitude,isSame, transferAddress, transferStartdate, transferEnddate, transferScope, transferContactInfo, serviceAddress, serviceStartdate, serviceEnddate, serviceScope, serviceContactInfo, createTime, lastUpdateTime, isDisabled, serviceUrl, description,organizationType,parentOrganization);
//		return "redirect:v_list.do?organizationType=STREET";
		String result=null;
		if(organizationType.equals("COMMUNITY")){
			result="redirect:v_list.do?organizationType=COMMUNITY";
		}else if(organizationType.equals("STREET")){
			result="redirect:v_list.do?organizationType=STREET";
		}else if(organizationType.equals("ENTERPRISE")){
			result="redirect:v_list.do?organizationType=ENTERPRISE";
		}
		return result;
	}
	
	@RequiresPermissions("serviceMap:v_edit")
	@RequestMapping("/serviceMap/v_edit.do")
	public String edit(Integer id,String organizationType,
			HttpServletRequest request, ModelMap model) {
		ServiceMap serviceMap=service.findById(id);
		List<ServiceMap> groupList = service.getList();
		model.addAttribute("serviceMap", serviceMap);
		model.addAttribute("groupList", groupList);
		String result=null;
		if(organizationType.equals("COMMUNITY")){
			result="serviceMap/edit";
		}else if(organizationType.equals("STREET")){
			result="serviceMap/edit_street";
		}else if(organizationType.equals("ENTERPRISE")){
			result="serviceMap/edit_snterprise";
		}
		return result;
//		return "serviceMap/edit";
	}
	
	@RequiresPermissions("serviceMap:o_update")
	@RequestMapping("/serviceMap/o_update.do")
	public String update(Integer id,String organizationName,String transferAddressCoordinates,
			Boolean isSame, String serviceAddressCoordinates,
			String transferAddress, String transferStartdate,
			String transferEnddate, String transferScope, String transferContactInfo,
			String serviceAddress, String serviceStartdate, String serviceEnddate,
			String serviceScope, String serviceContactInfo, Timestamp createTime,
			Timestamp lastUpdateTime,String organizationType,Integer  parentOrganizationId,
			Boolean isDisabled, String serviceUrl, String description,
			ServiceMap bean,HttpServletRequest request, ModelMap model) {
			String transferLongitude=null;
			String transferLatitude=null;
			String serviceLongitude=null;
			String serviceLatitude=null;
			if(organizationType.equals("ENTERPRISE")){
				transferLongitude="10";
				transferLatitude="10";
				if(isSame==null){
					isSame=true;
				}
			}else{
				String[] transferLd=transferAddressCoordinates.split(",");
				transferLongitude=transferLd[0];
				transferLatitude=transferLd[1];
				if(!isSame){
					String[] serviceLd=serviceAddressCoordinates.split(",");
					serviceLongitude=serviceLd[0];
					serviceLatitude=serviceLd[1];
				}
			}
			service.updateById(id,organizationName, transferLongitude,transferLatitude,serviceLongitude,serviceLatitude,isSame, transferAddress, transferStartdate, transferEnddate, transferScope, transferContactInfo, serviceAddress, serviceStartdate, serviceEnddate, serviceScope, serviceContactInfo, createTime, lastUpdateTime, isDisabled, serviceUrl, description,parentOrganizationId);
			String result=null;
			if(organizationType.equals("COMMUNITY")){
				result="redirect:v_list.do?organizationType=COMMUNITY";
			}else if(organizationType.equals("STREET")){
				result="redirect:v_list.do?organizationType=STREET";
			}else if(organizationType.equals("ENTERPRISE")){
				result="redirect:v_list.do?organizationType=ENTERPRISE";
			}
			return result;
	}
	
	
	@RequiresPermissions("serviceMap:o_delete")
	@RequestMapping("/serviceMap/o_delete.do")
	public String delete(Integer[] ids,Integer pageNo,String organizationType, HttpServletRequest request,
			ModelMap model) {
		ServiceMap[] beans = service.deleteByIds(ids);
		String result=null;
		if(organizationType.equals("COMMUNITY")){
			result="redirect:v_list.do?organizationType=COMMUNITY";
		}else if(organizationType.equals("STREET")){
			result="redirect:v_list.do?organizationType=STREET";
		}else if(organizationType.equals("ENTERPRISE")){
			result="redirect:v_list.do?organizationType=ENTERPRISE";
		}
		return result;
	}
	
	
	@RequiresPermissions("serviceMap:v_check_organizationName")
	@RequestMapping(value = "/serviceMap/v_check_organizationName.do")
	public void checkOrganizationname(HttpServletRequest request, HttpServletResponse response) {
		String organizationName=RequestUtils.getQueryParam(request,"organizationName");
		String pass;
		if (StringUtils.isBlank(organizationName)) {
			pass = "false";
		} else {
			pass = service.countByOrgname(organizationName) ? "true" : "false";
		}
		ResponseUtils.renderJson(response, pass);
	}
}
