package org.iso.registry.client.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletResponse;

import org.hsqldb.lib.StringUtil;
import org.iso.registry.api.registry.IsoProposalService;
import org.iso.registry.core.model.iso19115.extent.EX_Extent;
import org.iso.registry.core.model.iso19115.extent.ExtentRepository;
import org.iso.registry.core.model.operation.GeneralOperationParameterItem;
import org.iso.registry.core.model.operation.OperationMethodItem;
import org.iso.registry.core.model.operation.OperationMethodItemRepository;
import org.iso.registry.core.model.operation.OperationParameterItem;
import org.iso.registry.core.model.operation.OperationParameterItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import de.geoinfoffm.registry.core.Entity;

@Controller
@RequestMapping("/entities")
public class DataController
{
	@Autowired
	private OperationMethodItemRepository methodRepository;

	@Autowired
	private OperationParameterItemRepository parameterRepository;

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private ExtentRepository extentRepository;
	
	@Autowired
	private IsoProposalService isoProposalService;

	@Autowired
	private MessageSource messageSource;

	@RequestMapping(value = "/by-class/{className}", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public @ResponseBody List<Object[]> findAll(@PathVariable("className") String className, 
												@RequestParam(value = "orderBy", defaultValue = "name") String orderBy,
												@RequestParam(value = "where", required = false) String where,
												@RequestParam(value = "q", required = false) String search,
												@RequestParam(value = "isIdentifiedItem", defaultValue = "true") boolean isIdentifiedItem) {
		StringBuilder q = new StringBuilder();
		if (StringUtils.isEmpty(orderBy) || "null".equals(orderBy)) {
			orderBy = "name";
		}
		
		q.append("SELECT i.uuid, ");
		if (isIdentifiedItem) {
			q.append("i.identifier, ");
		}
		else {
			q.append("-1 AS identifier, ");
		}
		q.append("i.name, i.description FROM " + className + " i WHERE i.status = 'VALID'");

		if (!StringUtil.isEmpty(search)) {
			search = "%" + search + "%";
			q.append(" AND (LOWER(i.name) LIKE '" + search.toLowerCase() + "')");
			if (isIdentifiedItem) {
				q.append(" OR CAST(i.identifier AS text) LIKE '" + search + "')");
			}
		}
		if (!StringUtils.isEmpty(where)) {
			q.append(" AND (");
			q.append(where);
			q.append(")");
		}
//		if (filters != null && !filters.isEmpty()) {
//			q.append(" WHERE");
//			boolean first = true;
//			for (String where : filters.get("where")) {
//				String[] parts = where.split(":");
//				if (first) {
//					first = false;
//				}
//				else {
//					q.append(" AND");
//				}
//				q.append(" i.");
//				q.append(parts[0]);
//				q.append(" = ");
//				if (parts.length == 2) {
//					q.append("'");
//					q.append(parts[1]);
//					q.append("'");
//				}
//				else if (parts.length == 3 && parts[1].equalsIgnoreCase("int")) {
//					q.append(parts[1]);
//				}
//			}
//		}
		q.append(" ORDER BY i.");
		q.append(orderBy);
		
		Query query = entityManager.createQuery(q.toString());
//		query.setParameter("className", className);
//		if (!StringUtils.isEmpty(search)) {
//			query.setParameter("search", search);
//		}
		return query.getResultList();
	}
	
	@RequestMapping(value = "/methods", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public @ResponseBody List<Object[]> findConversionMethods(@RequestParam(value = "q", required = false) String search) {
		StringBuilder q = new StringBuilder();
		q.append("SELECT i.uuid, i.identifier, i.name FROM OperationMethodItem i WHERE i.status = 'VALID'");
		if (!StringUtils.isEmpty(search)) {
			search = "%" + search + "%";
			q.append(" AND (LOWER(i.name) LIKE '" + search.toLowerCase() + "' OR CAST(i.identifier AS text) LIKE '" + search + "')");
		}
		q.append(" ORDER BY i.name");
		
		Query query = entityManager.createQuery(q.toString());
		return query.getResultList();
	}
	
	@RequestMapping(value = "/axes", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public @ResponseBody List<Object[]> findCoordinateAxes(@RequestParam(value = "q", required = false) String search,
														   @RequestParam(value = "uuid", required = false) UUID uuid) {
		StringBuilder q = new StringBuilder();
		if (uuid != null) {
			q.append("SELECT i.uuid, i.identifier, i.name, i.axisAbbreviation, i.axisDirection, i.axisUnit.name FROM CoordinateSystemAxisItem i WHERE i.uuid = '" + uuid.toString() + "'");
			Query query = entityManager.createQuery(q.toString());
			return query.getResultList();
		}
		
		q.append("SELECT i.uuid, i.identifier, i.name, i.axisAbbreviation, i.axisDirection, i.axisUnit.name FROM CoordinateSystemAxisItem i WHERE i.status = 'VALID'");
		if (!StringUtils.isEmpty(search)) {
			search = "%" + search + "%";
			q.append(" AND (");
			
			q.append("LOWER(i.name) LIKE '");
			q.append(search.toLowerCase());
			q.append("'");
			
			q.append(" OR LOWER(i.axisDirection.identifier) LIKE '");
			q.append(search.toLowerCase());
			q.append("'");

			q.append(" OR LOWER(i.axisUnit.name) LIKE '");
			q.append(search.toLowerCase());
			q.append("'");

			q.append("OR CAST(i.identifier AS text) LIKE '");
			q.append(search);
			q.append("'");
			
			q.append(")");
		}
		q.append(" ORDER BY i.name");
		
		Query query = entityManager.createQuery(q.toString());
		return query.getResultList();
	}

	@RequestMapping(value = "/extents", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public @ResponseBody List<Object[]> findExtents(@RequestParam(value = "q", required = false) String search,
													@RequestParam(value = "uuid", required = false) UUID uuid) {
		StringBuilder q = new StringBuilder();
		if (uuid != null) {
			q.append("SELECT e.uuid, e.description FROM EX_Extent e WHERE e.uuid = '" + uuid.toString() + "'");
			Query query = entityManager.createQuery(q.toString());
			query.setMaxResults(1);
			return query.getResultList();
		}
		
		q.append("SELECT e.uuid, e.description FROM EX_Extent e WHERE e.description IS NOT NULL");
		if (!StringUtils.isEmpty(search)) {
			search = "%" + search + "%";
			q.append(" AND (");
			
			q.append("LOWER(e.description) LIKE '");
			q.append(search.toLowerCase());
			q.append("'");
			
			q.append(")");
		}
		q.append(" ORDER BY e.description");
		
		Query query = entityManager.createQuery(q.toString());
		List<Object[]> queryResult = query.getResultList();
		List<Object[]> result = new ArrayList<>();
		Set<String> descriptions = new HashSet<>();
		for (Object[] obj : queryResult) {
			if (!descriptions.contains((String)obj[1])) {
				result.add(obj);
				descriptions.add((String)obj[1]);
			}
		}
		
		return result;
	}
	
	@RequestMapping(value = "/extents/json", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public @ResponseBody EX_Extent findGeographicElements(@RequestParam(value = "uuid") UUID uuid) {
		EX_Extent extent = extentRepository.findOne(uuid);
		if (extent != null) {
			extent.getGeographicElement().size();
			extent.getVerticalElement().size();
 			Entity.unproxify(extent.getGeographicElement());
			Entity.unproxify(extent.getVerticalElement());
			
			return extent;
		}
		
		return null;
	}

	@RequestMapping(value = "/parameters/{methodUuid}", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public @ResponseBody List<Object[]> findParametersForMethod(@PathVariable("methodUuid") UUID methodUuid,
																  @RequestParam(value = "sEcho", defaultValue = "1") String sEcho) {
//		String jpql = "SELECT i.parameter.uuid, i.parameter.code, i.parameter.name FROM OperationMethodItem i WHERE i.uuid ='" + methodUuid.toString() + "'";
		
		List<Object[]> result = new ArrayList<>();
		OperationMethodItem method = methodRepository.findOne(methodUuid);
		if (method != null) {
			for (GeneralOperationParameterItem parameter : method.getParameter()) {
				result.add(new Object[] { parameter.getUuid().toString(), parameter.getIdentifier(), parameter.getName() });
			}
		}
		else {
			result.add(new Object[] { "", "", String.format("Operation method %s does not exist", methodUuid) });
		}
	
		return result;
//		return new DatatablesResult(result.size(), result.size(), sEcho, method.getParameter());
//		return entityManager.createQuery(jpql).getResultList();
	}

	@RequestMapping(value = "/parameter/{parameterUuid}", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public ResponseEntity<OperationParameterInfo> getParameterName(@PathVariable("parameterUuid") UUID parameterUuid) {
		OperationParameterItem parameter = parameterRepository.findOne(parameterUuid);

		if (parameter == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		OperationParameterInfo result = new OperationParameterInfo(parameter.getUuid(), parameter.getName());
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/identifier/next-available", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public @ResponseBody Integer findNextAvailableIdentifier() {
		return isoProposalService.findNextAvailableIdentifier();
	}
	
	@RequestMapping(value = "/identifier/check-availability", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public @ResponseBody Long checkIdentifierAvailability(@RequestParam("identifier") Long identifier) {
		if (identifier == null) {
			return -1L;
		}
		
		String jpql = "SELECT COUNT(i.identifier) FROM IdentifiedItem i WHERE i.status = 'VALID' AND i.identifier = " + identifier.toString();
		Long count = (Long)entityManager.createQuery(jpql).getResultList().get(0);
		return count;
	}

	@RequestMapping(value = "/by-uuid/{itemUuid}", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public @ResponseBody List<Object[]> findByUuid(@PathVariable("itemUuid") UUID uuid, @RequestParam(value = "orderBy", defaultValue = "name") String orderBy) {
		String jpql = "SELECT i.uuid, i.identifier, i.name FROM RE_RegisterItem i WHERE uuid = '" + uuid.toString() + "' ORDER BY i." + orderBy;
		return entityManager.createQuery(jpql).getResultList();
	}
	
	@RequestMapping(value = "/fragments/informationsource")
	public String getInformationSourceFragment(@RequestParam("index") String index, @RequestParam(value = "objectPath", required = false) String objectPath, final Model model) {
		if (!StringUtils.isEmpty(objectPath)) {
			model.addAttribute("objectPath", objectPath);
		}
		model.addAttribute("isProposal", "true");
		return "registry/registers/gcp/infosrc_panel_content :: informationSourcePanelContent(index='" + index + "')";
	}

	@RequestMapping(value = "/fragments/objectusage")
	public String getObjectUsageFragment(@RequestParam("index") String index, @RequestParam(value = "objectPath", required = false) String objectPath, final Model model) {
		if (!StringUtils.isEmpty(objectPath)) {
			model.addAttribute("objectPath", objectPath);
		}

		model.addAttribute("isProposal", "true");
		model.addAttribute("useWeakBinding", "true");
		return "registry/registers/gcp/objectusage_panel_content :: objectUsagePanelContent(index='" + index + "')";
	}

	@RequestMapping(value = "/fragments/citationpopup")
	public String getCitationPopupFragment(@RequestParam("index") String index, @RequestParam(value = "objectPath", required = false) String objectPath, final Model model) {
		if (!StringUtils.isEmpty(objectPath)) {
			model.addAttribute("objectPath", objectPath);
		}
		model.addAttribute("isProposal", "true");

		return "registry/registers/gcp/infosrc_panel :: citationPopup(index='" + index + "')";
	}

	public static class OperationParameterInfo {
		public final UUID uuid;
		public final String name;

		public OperationParameterInfo(UUID uuid, String name) {
			this.uuid = uuid;
			this.name = name;
		}
	}

	@RequestMapping(value = "/message/{code:.+}", produces = "text/plain", method = RequestMethod.GET)
	public @ResponseBody String getMessage(final HttpServletResponse response, @PathVariable("code") final String code) {
		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");
		return messageSource.getMessage(code, null, "<invalid code>", LocaleContextHolder.getLocale());
	}
}
