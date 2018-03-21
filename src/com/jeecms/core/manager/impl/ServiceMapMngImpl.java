package com.jeecms.core.manager.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.page.Pagination;
import com.jeecms.core.dao.ServiceMapDao;
import com.jeecms.core.entity.CmsUser;
import com.jeecms.core.entity.ServiceMap;
import com.jeecms.core.manager.ServiceMapMng;
import com.sun.star.util.DateTime;
@Service
@Transactional
public class ServiceMapMngImpl implements ServiceMapMng {
	@Autowired
	private ServiceMapDao dao;
	@Override
	public List<ServiceMap> findAll() {
		// TODO Auto-generated method stub
		return dao.findAll();
	}
	@Override
	@Transactional(readOnly = true)
	public Pagination getPage(String organizationName,String organizationType,int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		return dao.getPage(organizationName,organizationType,pageNo, pageSize);
	}
	@Override
	public void save(String organizationName, String transferLongitude, String transferLatitude,String serviceLongitude, String serviceLatitude, Boolean isSame, String transferAddress, String transferStartdate, String transferEnddate, String transferScope, String transferContactInfo, String serviceAddress, String serviceStartdate, String serviceEnddate, String serviceScope, String serviceContactInfo, Timestamp createTime, Timestamp lastUpdateTime, Boolean isDisabled, String serviceUrl, String description,String organizationType,ServiceMap parentOrganization) {
		// TODO Auto-generated method stub
		if(createTime==null){
			createTime=new Timestamp(System.currentTimeMillis()); 
		}
		if(lastUpdateTime==null){
			lastUpdateTime=new Timestamp(System.currentTimeMillis()); 
		}
		if(isDisabled==null){
			isDisabled=false;
		}
		ServiceMap serviceMap=new ServiceMap(organizationName,transferLongitude,transferLatitude,serviceLongitude,serviceLatitude,isSame, transferAddress, transferStartdate, transferEnddate, transferScope, transferContactInfo, serviceAddress, serviceStartdate, serviceEnddate,  serviceScope,serviceContactInfo,createTime,lastUpdateTime,isDisabled, serviceUrl, description,organizationType,parentOrganization);
		dao.save(serviceMap);
	}
	@Override
	public ServiceMap findById(Integer id) {
		return dao.findById(id);
	}
	@Override
	public ServiceMap[] deleteByIds(Integer[] ids) {
		ServiceMap[] beans = new ServiceMap[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
		
	}
	@Override
	public ServiceMap deleteById(Integer id) {
		return dao.deleteById(id);
	}
	@Override
	public ServiceMap updateById(Integer id,String organizationName, String transferLongitude, 
			String transferLatitude,String serviceLongitude,
			String serviceLatitude, Boolean isSame,
			String transferAddress, String transferStartdate,
			String transferEnddate, String transferScope,
			String transferContactInfo, String serviceAddress,
			String serviceStartdate, String serviceEnddate,
			String serviceScope, String serviceContactInfo,
			Timestamp createTime, Timestamp lastUpdateTime, Boolean isDisabled,
			String serviceUrl, String description,Integer parentOrganizationId) {
		if(lastUpdateTime==null){
			lastUpdateTime=new Timestamp(System.currentTimeMillis()); 
		}
		if(isDisabled==null){
			isDisabled=false;
		}
		ServiceMap parentServiceMap=null;
		if(parentOrganizationId!=null){
			parentServiceMap=findById(parentOrganizationId);
		}
		ServiceMap serviceMap=findById(id);
		if(organizationName!=null){
			serviceMap.setOrganizationName(organizationName);
		}
		serviceMap.setTransferLongitude(transferLongitude);
		serviceMap.setTransferLatitude(transferLatitude);
		serviceMap.setServiceLongitude(serviceLongitude);
		serviceMap.setServiceLatitude(serviceLatitude);
		serviceMap.setIsSame(isSame);
		serviceMap.setTransferAddress(transferAddress);
		serviceMap.setTransferContactInfo(transferContactInfo);
		serviceMap.setTransferEnddate(transferEnddate);
		serviceMap.setTransferScope(transferScope);
		serviceMap.setTransferStartdate(transferStartdate);
		serviceMap.setServiceAddress(serviceAddress);
		serviceMap.setServiceContactInfo(serviceContactInfo);
		serviceMap.setServiceEnddate(serviceEnddate);
		serviceMap.setServiceScope(serviceScope);
		serviceMap.setServiceStartdate(serviceStartdate);
		serviceMap.setDescription(description);
		if(parentServiceMap!=null){
			serviceMap.setParentOrganization(parentServiceMap);
		}
		return serviceMap;
	}
	@Override
	public List<Map<String,Object>> findByProperty(String value) {
		List<ServiceMap> serviceMapList=dao.findByProperty(value);
		List<Map<String,Object>> reusltList=new ArrayList<Map<String,Object>>();
		for(ServiceMap serviceMap:serviceMapList){
			Map<String,Object> result=new HashMap<String, Object>();
			result.put("organizationName", serviceMap.getOrganizationName()) ;
			result.put("transferLongitude", serviceMap.getTransferLongitude()) ;
			result.put("transferLatitude", serviceMap.getTransferLatitude());
			result.put("serviceLongitude", serviceMap.getServiceLongitude());
			result.put("serviceLatitude", serviceMap.getServiceLatitude());
			result.put("isSame", serviceMap.getIsSame());
			result.put("transferAddress", serviceMap.getTransferAddress());
			result.put("transferContactInfo", serviceMap.getTransferContactInfo());
			result.put("transferEnddate", serviceMap.getTransferEnddate());
			result.put("transferScope", serviceMap.getTransferScope());
			result.put("transferStartdate", serviceMap.getTransferStartdate());
			result.put("serviceAddress", serviceMap.getServiceAddress());
			result.put("serviceContactInfo", serviceMap.getServiceContactInfo());
			result.put("serviceEnddate", serviceMap.getServiceEnddate());
			result.put("serviceScope", serviceMap.getServiceScope());
			result.put("serviceStartdate", serviceMap.getServiceStartdate());
			reusltList.add(result);
		}
		return reusltList;
	}
	@Override
	public boolean countByOrgname(String organizationName) {
		// TODO Auto-generated method stub
		return dao.countByOrgname(organizationName) <= 0;
	}
	@Override
	public List<ServiceMap> getList() {
		
		return dao.getList();
	}
	
}
