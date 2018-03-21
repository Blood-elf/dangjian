package com.jeecms.core.manager;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.jeecms.common.page.Pagination;
import com.jeecms.core.entity.CmsUser;
import com.jeecms.core.entity.ServiceMap;
public interface ServiceMapMng {
	public List<ServiceMap> findAll();
	public void save(String organizationName, String transferLongitude, String transferLatitude,String serviceLongitude, String serviceLatitude, Boolean isSame, String transferAddress, String transferStartdate, String transferEnddate, String transferScope, String transferContactInfo, String serviceAddress, String serviceStartdate, String serviceEnddate, String serviceScope, String serviceContactInfo, Timestamp createTime, Timestamp lastUpdateTime, Boolean isDisabled, String serviceUrl, String description,String organizationType,ServiceMap parentOrganization);
	public Pagination getPage(String organizationName,String organizationType,int pageNo, int pageSize);
	public ServiceMap findById(Integer id);
	public ServiceMap[] deleteByIds(Integer[] ids);
	public ServiceMap deleteById(Integer id);
	public ServiceMap updateById(Integer id,String organizationName, String transferLongitude, String transferLatitude,String serviceLongitude, String serviceLatitude, Boolean isSame, String transferAddress, String transferStartdate, String transferEnddate, String transferScope, String transferContactInfo, String serviceAddress, String serviceStartdate, String serviceEnddate, String serviceScope, String serviceContactInfo, Timestamp createTime, Timestamp lastUpdateTime, Boolean isDisabled, String serviceUrl, String description,Integer parentOrganizationId);
	public List findByProperty(String value);
	public boolean countByOrgname(String organizationName);
	public List<ServiceMap> getList();
}
