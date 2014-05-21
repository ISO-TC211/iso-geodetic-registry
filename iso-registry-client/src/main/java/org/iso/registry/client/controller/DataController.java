package org.iso.registry.client.controller;

import java.util.List;

import javax.persistence.EntityManager;

import org.iso.registry.core.model.crs.AreaItemRepository;
import org.iso.registry.core.model.cs.CoordinateSystemItemRepository;
import org.iso.registry.core.model.datum.DatumItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;

@Controller
@RequestMapping("/data")
public class DataController
{
	@Autowired private AreaItemRepository areaRepository;
	@Autowired private DatumItemRepository datumRepository;
	@Autowired private CoordinateSystemItemRepository csRepository;
	
	@Autowired
	private EntityManager entityManager;
	
	@RequestMapping(value = "/{entityName}", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public @ResponseBody List<Object[]> findAll(@PathVariable("entityName") String entityName, @RequestParam(value = "orderBy", defaultValue = "code") String orderBy) {
		String jpql = "SELECT i.uuid, i.code, i.name FROM " + entityName + " i ORDER BY i." + orderBy;
		return entityManager.createQuery(jpql).getResultList();
	}
	

//	@RequestMapping(value = "/area", method = RequestMethod.GET/*, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE*/)
//	@Transactional(readOnly = true)
//	public @ResponseBody List<Object[]> getAreas(@RequestParam(value = "orderBy", defaultValue = "code") String orderBy) {
//		if (orderBy.equalsIgnoreCase("name")) {
//			return areaRepository.findAllOrderByName();
//		}
//		else {
//			return areaRepository.findAllOrderByCode();
//		}
//	}
//
//	@RequestMapping(value = "/datum", method = RequestMethod.GET/*, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE*/)
//	@Transactional(readOnly = true)
//	public @ResponseBody List<Object[]> getDatums() {
//		return datumRepository.findAllOrderByCode();
//	}
//
//	@RequestMapping(value = "/cs", method = RequestMethod.GET/*, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE*/)
//	@Transactional(readOnly = true)
//	public @ResponseBody List<Object[]> getCoordinateSystems() {
//		return csRepository.findAllOrderByCode();
//	}
//
}
