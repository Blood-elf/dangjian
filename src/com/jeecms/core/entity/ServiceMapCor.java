package com.jeecms.core.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;

import org.hibernate.mapping.Map;

public class ServiceMapCor implements Serializable {
	// Fields    

    private Integer id;
    private String organizationName;
    private String transferLongitude;
    private String transferLatitude;
    private String transferAddress;
    private String transferStartdate;
    private String transferEnddate;
    private String transferScope;
    private String transferContactInfo;
    private String serviceAddress;
    private String serviceLongitude;
    private String serviceLatitude;
    private String serviceStartdate;
    private String serviceEnddate;
    private String serviceScope;
    private String serviceContactInfo;
    private Timestamp createTime;
    private Timestamp lastUpdateTime;
    private Boolean isDisabled;
    private Boolean isSame;
    private String serviceUrl;
    private String description;
    private String organizationType;
    private ServiceMap parentOrganization;


   // Constructors

   public String getOrganizationType() {
		return organizationType;
	}

	public void setOrganizationType(String organizationType) {
		this.organizationType = organizationType;
	}

	public ServiceMap getParentOrganization() {
		return parentOrganization;
	}

	public void setParentOrganization(ServiceMap parentOrganization) {
		this.parentOrganization = parentOrganization;
	}

/** default constructor */
   public ServiceMapCor() {
   }

	/** minimal constructor */
   public ServiceMapCor(String organizationName, String transferLongitude, String transferLatitude,String serviceLongitude, String serviceLatitude, Boolean isSame, Timestamp createTime, Timestamp lastUpdateTime, Boolean isDisabled,String organizationType,ServiceMap parentOrganization) {
       this.organizationName = organizationName;
       this.transferLongitude = transferLongitude;
       this.transferLatitude = transferLatitude;
       this.serviceLongitude = serviceLongitude;
       this.serviceLatitude = serviceLatitude;
       this.createTime = createTime;
       this.lastUpdateTime = lastUpdateTime;
       this.isDisabled = isDisabled;
       this.isSame = isSame;
       this.organizationType=organizationType;
       this.parentOrganization=parentOrganization;
   }
   
   /** full constructor */
   public ServiceMapCor(String organizationName, String transferLongitude, String transferLatitude,String serviceLongitude, String serviceLatitude, Boolean isSame, String transferAddress, String transferStartdate, String transferEnddate, String transferScope, String transferContactInfo, String serviceAddress, String serviceStartdate, String serviceEnddate, String serviceScope, String serviceContactInfo, Timestamp createTime, Timestamp lastUpdateTime, Boolean isDisabled, String serviceUrl, String description,String organizationType,ServiceMap parentOrganization) {
       this.organizationName = organizationName;
       this.transferLongitude = transferLongitude;
       this.transferLatitude = transferLatitude;
       this.serviceLongitude = serviceLongitude;
       this.serviceLatitude = serviceLatitude;
       this.transferAddress = transferAddress;
       this.transferStartdate = transferStartdate;
       this.transferEnddate = transferEnddate;
       this.transferScope = transferScope;
       this.transferContactInfo = transferContactInfo;
       this.serviceAddress = serviceAddress;
       this.serviceStartdate = serviceStartdate;
       this.serviceEnddate = serviceEnddate;
       this.serviceScope = serviceScope;
       this.serviceContactInfo = serviceContactInfo;
       this.createTime = createTime;
       this.lastUpdateTime = lastUpdateTime;
       this.isDisabled = isDisabled;
       this.isSame = isSame;
       this.serviceUrl = serviceUrl;
       this.description = description;
       this.organizationType=organizationType;
       this.parentOrganization=parentOrganization;
   }

  
   // Property accessors

   public Integer getId() {
       return this.id;
   }
   
   public void setId(Integer id) {
       this.id = id;
   }

   public String getOrganizationName() {
       return this.organizationName;
   }
   
   public void setOrganizationName(String organizationName) {
       this.organizationName = organizationName;
   }

   public String getTransferLongitude() {
	return transferLongitude;
}

public void setTransferLongitude(String transferLongitude) {
	this.transferLongitude = transferLongitude;
}

public String getTransferLatitude() {
	return transferLatitude;
}

public void setTransferLatitude(String transferLatitude) {
	this.transferLatitude = transferLatitude;
}

public String getServiceLongitude() {
	return serviceLongitude;
}

public void setServiceLongitude(String serviceLongitude) {
	this.serviceLongitude = serviceLongitude;
}

public String getServiceLatitude() {
	return serviceLatitude;
}

public void setServiceLatitude(String serviceLatitude) {
	this.serviceLatitude = serviceLatitude;
}

public Boolean getIsSame() {
	return isSame;
}

public void setIsSame(Boolean isSame) {
	this.isSame = isSame;
}

public String getTransferAddress() {
       return this.transferAddress;
   }
   
   public void setTransferAddress(String transferAddress) {
       this.transferAddress = transferAddress;
   }

   public String getTransferStartdate() {
       return this.transferStartdate;
   }
   
   public void setTransferStartdate(String transferStartdate) {
       this.transferStartdate = transferStartdate;
   }

   public String getTransferEnddate() {
       return this.transferEnddate;
   }
   
   public void setTransferEnddate(String transferEnddate) {
       this.transferEnddate = transferEnddate;
   }

   public String getTransferScope() {
       return this.transferScope;
   }
   
   public void setTransferScope(String transferScope) {
       this.transferScope = transferScope;
   }

   public String getTransferContactInfo() {
       return this.transferContactInfo;
   }
   
   public void setTransferContactInfo(String transferContactInfo) {
       this.transferContactInfo = transferContactInfo;
   }

   public String getServiceAddress() {
       return this.serviceAddress;
   }
   
   public void setServiceAddress(String serviceAddress) {
       this.serviceAddress = serviceAddress;
   }

   public String getServiceStartdate() {
       return this.serviceStartdate;
   }
   
   public void setServiceStartdate(String serviceStartdate) {
       this.serviceStartdate = serviceStartdate;
   }

   public String getServiceEnddate() {
       return this.serviceEnddate;
   }
   
   public void setServiceEnddate(String serviceEnddate) {
       this.serviceEnddate = serviceEnddate;
   }

   public String getServiceScope() {
       return this.serviceScope;
   }
   
   public void setServiceScope(String serviceScope) {
       this.serviceScope = serviceScope;
   }

   public String getServiceContactInfo() {
       return this.serviceContactInfo;
   }
   
   public void setServiceContactInfo(String serviceContactInfo) {
       this.serviceContactInfo = serviceContactInfo;
   }

   public Timestamp getCreateTime() {
       return this.createTime;
   }
   
   public void setCreateTime(Timestamp createTime) {
       this.createTime = createTime;
   }

   public Timestamp getLastUpdateTime() {
       return this.lastUpdateTime;
   }
   
   public void setLastUpdateTime(Timestamp lastUpdateTime) {
       this.lastUpdateTime = lastUpdateTime;
   }

   public Boolean getIsDisabled() {
       return this.isDisabled;
   }
   
   public void setIsDisabled(Boolean isDisabled) {
       this.isDisabled = isDisabled;
   }

   public String getServiceUrl() {
       return this.serviceUrl;
   }
   
   public void setServiceUrl(String serviceUrl) {
       this.serviceUrl = serviceUrl;
   }

   public String getDescription() {
       return this.description;
   }
   
   public void setDescription(String description) {
       this.description = description;
   }
}
