package com.jeecms.core.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import com.jeecms.common.hibernate4.PriorityInterface;

public class ServiceMap extends ServiceMapCor implements PriorityInterface{
	public ServiceMap(){
		super();
	}
	
	public ServiceMap(String organizationName,String transferLongitude, String transferLatitude,String serviceLongitude, String serviceLatitude, Boolean isSame, Timestamp createTime, Timestamp lastUpdateTime, Boolean isDisabled,String organizationType,ServiceMap parentOrganization){
		super(organizationName, transferLongitude, transferLatitude,serviceLongitude,serviceLatitude,isSame,createTime, lastUpdateTime, isDisabled,organizationType,parentOrganization);
	}
	
	 public ServiceMap(String organizationName,String transferLongitude, String transferLatitude,String serviceLongitude, String serviceLatitude, Boolean isSame, String transferAddress, String transferStartdate, String transferEnddate, String transferScope, String transferContactInfo, String serviceAddress, String serviceStartdate, String serviceEnddate, String serviceScope, String serviceContactInfo, Timestamp createTime, Timestamp lastUpdateTime, Boolean isDisabled, String serviceUrl, String description,String organizationType,ServiceMap parentOrganization){
		 super(organizationName,transferLongitude, transferLatitude,serviceLongitude,serviceLatitude,isSame, transferAddress,transferStartdate,transferEnddate,transferScope, transferContactInfo,  serviceAddress, serviceStartdate, serviceEnddate, serviceScope,serviceContactInfo,createTime, lastUpdateTime, isDisabled,  serviceUrl,  description,organizationType,parentOrganization);
	 }
	 
	@Override
	public Number getPriority() {
		// TODO Auto-generated method stub
		return 0;
	}
}
