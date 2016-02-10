package com.vw.visitreporting.service.referencedata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.vw.visitreporting.dao.referencedata.GeographicalAreaDao;
import com.vw.visitreporting.dao.referencedata.GeographicalRegionDao;
import com.vw.visitreporting.dao.referencedata.GeographicalStructureCategoryDao;
import com.vw.visitreporting.entity.referencedata.GeographicalArea;
import com.vw.visitreporting.entity.referencedata.GeographicalRegion;
import com.vw.visitreporting.entity.referencedata.GeographicalStructureCategory;


@Service
public class GeographicalStructureServiceImpl implements GeographicalStructureService {

	private GeographicalStructureCategoryDao geographicalStructureCategoryDao;

	private GeographicalRegionDao geographicalRegionDao;

	private GeographicalAreaDao geographicalAreaDao;
	

	/**
	 * Create or update a GeographicalStructureCategory entity in the database.
	 * @return the (possibly different) hibernate entity instance for this entity.
	 */
	public GeographicalStructureCategory saveCategory(GeographicalStructureCategory category) {
		return geographicalStructureCategoryDao.save(category);
	}
	
	/**
	 * Create or update a GeographicalRegion entity in the database.
	 * @return the (possibly different) hibernate entity instance for this entity.
	 */
	public GeographicalRegion saveRegion(GeographicalRegion region) {
		return geographicalRegionDao.save(region);
	}
	
	/**
	 * Create or update a GeographicalArea entity in the database.
	 * @return the (possibly different) hibernate entity instance for this entity.
	 */
	public GeographicalArea saveArea(GeographicalArea area) {
		return geographicalAreaDao.save(area);
	}

	

	@Autowired
	public void setGeographicalStructureCategoryDao(GeographicalStructureCategoryDao geographicalStructureCategoryDao){
		this.geographicalStructureCategoryDao = geographicalStructureCategoryDao;
	}
	
	@Autowired
	public void setGeographicalRegionDao(GeographicalRegionDao geographicalRegionDao){
		this.geographicalRegionDao = geographicalRegionDao;
	}

	@Autowired
	public void setGeographicalAreaDao(GeographicalAreaDao geographicalAreaDao){
		this.geographicalAreaDao = geographicalAreaDao;
	}
}
