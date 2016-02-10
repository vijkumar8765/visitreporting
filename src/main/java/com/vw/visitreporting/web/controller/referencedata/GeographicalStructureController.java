package com.vw.visitreporting.web.controller.referencedata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.vw.visitreporting.service.referencedata.GeographicalStructureService;


@Controller
@RequestMapping("/geographicalstructurecategory")
public class GeographicalStructureController {

	private GeographicalStructureService geographicalStructureService;

	@Autowired
	public void setGeographicalStructureService(GeographicalStructureService geographicalStructureService) {
		this.geographicalStructureService = geographicalStructureService;
	}

}
