package com.jeecms.core.dao;

import java.util.List;

import org.hibernate.Query;

import com.jeecms.common.page.Pagination;
import com.jeecms.core.entity.ServiceMap;

public interface ServiceMapDao {

	public static final String ORGANIZATION_NAME = "organizationName";
	public static final String LONGITUDE = "longitude";
	public static final String LATITUDE = "latitude";
	public static final String TRANSFER_ADDRESS = "transferAddress";
	public static final String TRANSFER_STARTDATE = "transferStartdate";
	public static final String TRANSFER_ENDDATE = "transferEnddate";
	public static final String TRANSFER_SCOPE = "transferScope";
	public static final String TRANSFER_CONTACT_INFO = "transferContactInfo";
	public static final String SERVICE_ADDRESS = "serviceAddress";
	public static final String SERVICE_STARTDATE = "serviceStartdate";
	public static final String SERVICE_ENDDATE = "serviceEnddate";
	public static final String SERVICE_SCOPE = "serviceScope";
	public static final String SERVICE_CONTACT_INFO = "serviceContactInfo";
	public static final String IS_DISABLED = "isDisabled";
	public static final String SERVICE_URL = "serviceUrl";
	public static final String DESCRIPTION = "description";
	
	
	public Pagination getPage(String organizationName,String organizationType,int pageNo, int pageSize);
	
	
	public abstract ServiceMap save(ServiceMap transientInstance);

	public abstract void delete(ServiceMap persistentInstance);

	public abstract ServiceMap findById(java.lang.Integer id);
	public ServiceMap deleteById(Integer id);
	public abstract List findByExample(ServiceMap instance);

	public abstract List findByProperty(String value);

	public abstract List findByOrganizationName(Object organizationName);

	public abstract List findByLongitude(Object longitude);

	public abstract List findByLatitude(Object latitude);

	public abstract List findByTransferAddress(Object transferAddress);

	public abstract List findByTransferStartdate(Object transferStartdate);

	public abstract List findByTransferEnddate(Object transferEnddate);

	public abstract List findByTransferScope(Object transferScope);

	public abstract List findByTransferContactInfo(Object transferContactInfo);

	public abstract List findByServiceAddress(Object serviceAddress);

	public abstract List findByServiceStartdate(Object serviceStartdate);

	public abstract List findByServiceEnddate(Object serviceEnddate);

	public abstract List findByServiceScope(Object serviceScope);

	public abstract List findByServiceContactInfo(Object serviceContactInfo);

	public abstract List findByIsDisabled(Object isDisabled);

	public abstract List findByServiceUrl(Object serviceUrl);

	public abstract List findByDescription(Object description);

	public abstract List findAll();

	public abstract ServiceMap merge(ServiceMap detachedInstance);

	public abstract void attachDirty(ServiceMap instance);

	public abstract void attachClean(ServiceMap instance);
	
	public int countByOrgname(String orgname);
	public List<ServiceMap> getList();
}
