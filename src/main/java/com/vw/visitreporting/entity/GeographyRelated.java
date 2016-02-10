package com.vw.visitreporting.entity;

import java.util.Collection;
import com.vw.visitreporting.entity.referencedata.GeographicalArea;
import com.vw.visitreporting.entity.referencedata.GeographicalRegion;


public interface GeographyRelated {

	Collection<GeographicalArea> getRelatedAreas();
	
	Collection<GeographicalRegion> getRelatedRegions();
}
