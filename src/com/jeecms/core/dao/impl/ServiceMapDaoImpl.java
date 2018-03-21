package com.jeecms.core.dao.impl;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.jeecms.common.hibernate4.Finder;
import com.jeecms.common.hibernate4.HibernateBaseDao;
import com.jeecms.common.page.Pagination;
import com.jeecms.core.dao.ServiceMapDao;
import com.jeecms.core.entity.Authentication;
import com.jeecms.core.entity.CmsGroup;
import com.jeecms.core.entity.CmsUser;
import com.jeecms.core.entity.ServiceMap;
@Repository
public class ServiceMapDaoImpl  extends
HibernateBaseDao<ServiceMap, Integer> implements ServiceMapDao{
	  private static final Logger log = LoggerFactory.getLogger(ServiceMapDaoImpl.class);
	/* (non-Javadoc)
	 * @see com.jeecms.core.dao.impl.ServiceMapDao#save(com.jeecms.core.entity.ServiceMap)
	 */
	  
    @Override
	public ServiceMap save(ServiceMap transientInstance) {
        log.debug("saving ServiceMap instance");
        try {
            getSession().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
        
        return transientInstance;
    }
    
	/* (non-Javadoc)
	 * @see com.jeecms.core.dao.impl.ServiceMapDao#delete(com.jeecms.core.entity.ServiceMap)
	 */
	@Override
	public void delete(ServiceMap persistentInstance) {
        log.debug("deleting ServiceMap instance");
        try {
            getSession().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
	@Override
	public ServiceMap deleteById(Integer id) {
		ServiceMap entity = super.get(id);
//真删除
		if (entity != null) {
			//查询子记录
			String hql = "from ServiceMap bean where 1=1 and bean.parentOrganization.id="+id;
			List<ServiceMap> children=find(hql);
			if(children.size()>0){
				for(ServiceMap ser:children){
					deleteById(ser.getId());
				}
			}
			getSession().delete(entity);
		}
		//假删除
//		entity.setIsDisabled(true);
//		entity.setOrganizationName(entity.getOrganizationName()+"(已删除)"+UUID.randomUUID().toString());
		return entity;
	}
    
    /* (non-Javadoc)
	 * @see com.jeecms.core.dao.impl.ServiceMapDao#findById(java.lang.Integer)
	 */
    @Override
	public ServiceMap findById(Integer id) {
    	ServiceMap entity =get(id);
		return entity;
    }
    
    
    /* (non-Javadoc)
	 * @see com.jeecms.core.dao.impl.ServiceMapDao#findByExample(com.jeecms.core.entity.ServiceMap)
	 */
    @Override
	public List findByExample(ServiceMap instance) {
        log.debug("finding ServiceMap instance by example");
        try {
            List results = getSession()
                    .createCriteria("ServiceMap")
                    .add(Example.create(instance))
            .list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }    
    
    /* (non-Javadoc)
	 * @see com.jeecms.core.dao.impl.ServiceMapDao#findByProperty(java.lang.String, java.lang.Object)
	 */
    @Override
	public List findByProperty(String value) {
    	Finder f = Finder.create("select service from ServiceMap service");
		f.append(" where 1=1");
		if (!StringUtils.isBlank(value)) {
			f.append(" and ( service.organizationName like :organizationName");
			f.setParam("organizationName", "%" + value + "%");
			f.append(" or service.transferAddress like :transferAddress");
			f.setParam("transferAddress", "%" + value + "%");
			f.append(" or service.serviceAddress like :serviceAddress ");
			f.setParam("serviceAddress", "%" + value + "%");
			f.append(" or service.transferScope like :transferScope ");
			f.setParam("transferScope", "%" + value + "%");
			f.append(" or service.serviceScope like :serviceScope )");
			f.setParam("serviceScope", "%" + value + "%");
		}
		List<ServiceMap> serviceMapList=find(f);
		List<ServiceMap> result=new ArrayList<ServiceMap>();
		for(ServiceMap serviceMap:serviceMapList){
			if("ENTERPRISE".equals(serviceMap.getOrganizationType())){
					ServiceMap parentService=serviceMap.getParentOrganization();
					result.add(parentService);
			}else{
				result.add(serviceMap);
			}
		}
		Set<ServiceMap> serviceSet=new HashSet<ServiceMap>();
		serviceSet.addAll(result);
		result.addAll(serviceSet);
		return result;
	}

	/* (non-Javadoc)
	 * @see com.jeecms.core.dao.impl.ServiceMapDao#findByOrganizationName(java.lang.Object)
	 */
	@Override
	public List findByOrganizationName(Object organizationName
	) {
		return findByProperty(ORGANIZATION_NAME, organizationName
		);
	}
	
	/* (non-Javadoc)
	 * @see com.jeecms.core.dao.impl.ServiceMapDao#findByLongitude(java.lang.Object)
	 */
	@Override
	public List findByLongitude(Object longitude
	) {
		return findByProperty(LONGITUDE, longitude
		);
	}
	
	/* (non-Javadoc)
	 * @see com.jeecms.core.dao.impl.ServiceMapDao#findByLatitude(java.lang.Object)
	 */
	@Override
	public List findByLatitude(Object latitude
	) {
		return findByProperty(LATITUDE, latitude
		);
	}
	
	/* (non-Javadoc)
	 * @see com.jeecms.core.dao.impl.ServiceMapDao#findByTransferAddress(java.lang.Object)
	 */
	@Override
	public List findByTransferAddress(Object transferAddress
	) {
		return findByProperty(TRANSFER_ADDRESS, transferAddress
		);
	}
	
	/* (non-Javadoc)
	 * @see com.jeecms.core.dao.impl.ServiceMapDao#findByTransferStartdate(java.lang.Object)
	 */
	@Override
	public List findByTransferStartdate(Object transferStartdate
	) {
		return findByProperty(TRANSFER_STARTDATE, transferStartdate
		);
	}
	
	/* (non-Javadoc)
	 * @see com.jeecms.core.dao.impl.ServiceMapDao#findByTransferEnddate(java.lang.Object)
	 */
	@Override
	public List findByTransferEnddate(Object transferEnddate
	) {
		return findByProperty(TRANSFER_ENDDATE, transferEnddate
		);
	}
	
	/* (non-Javadoc)
	 * @see com.jeecms.core.dao.impl.ServiceMapDao#findByTransferScope(java.lang.Object)
	 */
	@Override
	public List findByTransferScope(Object transferScope
	) {
		return findByProperty(TRANSFER_SCOPE, transferScope
		);
	}
	
	/* (non-Javadoc)
	 * @see com.jeecms.core.dao.impl.ServiceMapDao#findByTransferContactInfo(java.lang.Object)
	 */
	@Override
	public List findByTransferContactInfo(Object transferContactInfo
	) {
		return findByProperty(TRANSFER_CONTACT_INFO, transferContactInfo
		);
	}
	
	/* (non-Javadoc)
	 * @see com.jeecms.core.dao.impl.ServiceMapDao#findByServiceAddress(java.lang.Object)
	 */
	@Override
	public List findByServiceAddress(Object serviceAddress
	) {
		return findByProperty(SERVICE_ADDRESS, serviceAddress
		);
	}
	
	/* (non-Javadoc)
	 * @see com.jeecms.core.dao.impl.ServiceMapDao#findByServiceStartdate(java.lang.Object)
	 */
	@Override
	public List findByServiceStartdate(Object serviceStartdate
	) {
		return findByProperty(SERVICE_STARTDATE, serviceStartdate
		);
	}
	
	/* (non-Javadoc)
	 * @see com.jeecms.core.dao.impl.ServiceMapDao#findByServiceEnddate(java.lang.Object)
	 */
	@Override
	public List findByServiceEnddate(Object serviceEnddate
	) {
		return findByProperty(SERVICE_ENDDATE, serviceEnddate
		);
	}
	
	/* (non-Javadoc)
	 * @see com.jeecms.core.dao.impl.ServiceMapDao#findByServiceScope(java.lang.Object)
	 */
	@Override
	public List findByServiceScope(Object serviceScope
	) {
		return findByProperty(SERVICE_SCOPE, serviceScope
		);
	}
	
	/* (non-Javadoc)
	 * @see com.jeecms.core.dao.impl.ServiceMapDao#findByServiceContactInfo(java.lang.Object)
	 */
	@Override
	public List findByServiceContactInfo(Object serviceContactInfo
	) {
		return findByProperty(SERVICE_CONTACT_INFO, serviceContactInfo
		);
	}
	
	/* (non-Javadoc)
	 * @see com.jeecms.core.dao.impl.ServiceMapDao#findByIsDisabled(java.lang.Object)
	 */
	@Override
	public List findByIsDisabled(Object isDisabled
	) {
		return findByProperty(IS_DISABLED, isDisabled
		);
	}
	
	/* (non-Javadoc)
	 * @see com.jeecms.core.dao.impl.ServiceMapDao#findByServiceUrl(java.lang.Object)
	 */
	@Override
	public List findByServiceUrl(Object serviceUrl
	) {
		return findByProperty(SERVICE_URL, serviceUrl
		);
	}
	
	/* (non-Javadoc)
	 * @see com.jeecms.core.dao.impl.ServiceMapDao#findByDescription(java.lang.Object)
	 */
	@Override
	public List findByDescription(Object description
	) {
		return findByProperty(DESCRIPTION, description
		);
	}
	

	/* (non-Javadoc)
	 * @see com.jeecms.core.dao.impl.ServiceMapDao#findAll()
	 */
	@Override
	public List findAll() {
		log.debug("finding all ServiceMap instances");
		try {
			String queryString = "from ServiceMap";
	         Query queryObject = getSession().createQuery(queryString);
			 return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
    /* (non-Javadoc)
	 * @see com.jeecms.core.dao.impl.ServiceMapDao#merge(com.jeecms.core.entity.ServiceMap)
	 */
    @Override
	public ServiceMap merge(ServiceMap detachedInstance) {
        log.debug("merging ServiceMap instance");
        try {
            ServiceMap result = (ServiceMap) getSession()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    /* (non-Javadoc)
	 * @see com.jeecms.core.dao.impl.ServiceMapDao#attachDirty(com.jeecms.core.entity.ServiceMap)
	 */
    @Override
	public void attachDirty(ServiceMap instance) {
        log.debug("attaching dirty ServiceMap instance");
        try {
            getSession().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    /* (non-Javadoc)
	 * @see com.jeecms.core.dao.impl.ServiceMapDao#attachClean(com.jeecms.core.entity.ServiceMap)
	 */
    @Override
	public void attachClean(ServiceMap instance) {
        log.debug("attaching clean ServiceMap instance");
        try {
            getSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

	@Override
	public Pagination getPage(String organizationName,String organizationType,int pageNo, int pageSize) {
		Finder f = Finder.create("select service from ServiceMap service");
		f.append(" where 1=1 and service.isDisabled=false");
		if (!StringUtils.isBlank(organizationName)) {
			f.append(" and service.organizationName like :organizationName");
			f.setParam("organizationName", "%" + organizationName + "%");
			if (!StringUtils.isBlank(organizationType)&&organizationType.equals("COMMUNITY")) {
				f.append(" or  service.parentOrganization.organizationName like :parentName");
				f.setParam("parentName", "%" + organizationName + "%");
			}
		}
		if (!StringUtils.isBlank(organizationType)) {
			f.append(" and service.organizationType like :organizationType");
			f.setParam("organizationType", "%" + organizationType + "%");
		}
		f.append(" order by service.id desc");
		return find(f, pageNo, pageSize);
	}
	
	@Override
	protected Class<ServiceMap> getEntityClass() {
		// TODO Auto-generated method stub
		return ServiceMap.class;
	}

	@Override
	public int countByOrgname(String organizationName) {
		String hql = "select count(*) from ServiceMap bean where bean.organizationName=:organizationName";
		Query query = getSession().createQuery(hql);
		query.setParameter("organizationName", organizationName);
		return ((Number) query.iterate().next()).intValue();	
	}
	
	@Override
	public List<ServiceMap> getList() {
		String hql = "from ServiceMap bean where 1=1 and bean.isDisabled=false and bean.organizationType='STREET' order by bean.id desc";
		return find(hql);
	}

}
