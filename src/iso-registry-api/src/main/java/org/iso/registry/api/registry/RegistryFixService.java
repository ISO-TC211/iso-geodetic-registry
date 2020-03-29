package org.iso.registry.api.registry;

import static org.mockito.Matchers.*;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.iso.registry.api.initialization.IsoRegistryInitializer;
import org.iso.registry.api.registry.registers.gcp.ExtentDTO;
import org.iso.registry.api.registry.registers.gcp.ExtentItemProposalDTO;
import org.iso.registry.core.model.ObjectDomain;
import org.iso.registry.core.model.iso19115.extent.EX_GeographicBoundingBox;
import org.iso.registry.core.model.iso19115.extent.ExtentItem;
import org.iso.registry.core.model.operation.SingleOperationItem;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import de.bespire.LoggerFactory;
import de.geoinfoffm.registry.api.OrganizationService;
import de.geoinfoffm.registry.api.ProposalService;
import de.geoinfoffm.registry.api.RegisterService;
import de.geoinfoffm.registry.core.model.Addition;
import de.geoinfoffm.registry.core.model.Organization;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.ProposalType;
import de.geoinfoffm.registry.core.model.iso19135.InvalidProposalException;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;
import de.geoinfoffm.registry.persistence.ItemClassRepository;
import de.geoinfoffm.registry.persistence.RegisterItemRepository;
import de.geoinfoffm.registry.persistence.RegisterRepository;

@Transactional
@Service
public class RegistryFixService
{
	private static final Logger logger = LoggerFactory.make();

	@Autowired
	private ItemClassRepository itemClassRepository;

	@Autowired
	private RegisterRepository registerRepository;

	@Autowired
	private RegisterItemRepository itemRepository;

	@Autowired
	private RegisterService registerService;

	@Autowired
	private OrganizationService orgService;

	@Autowired
	private ProposalService proposalService;

	@PersistenceContext
	private EntityManager entityManager;

	public void createExtentItemClass(StringBuilder log) {
		log.append("Attempting to add item class 'Extent' to ISO Geodetic Register...\n");

		RE_ItemClass extentItemClass = itemClassRepository.findByName("Extent");
		if (extentItemClass == null) {
			extentItemClass = new RE_ItemClass();
			extentItemClass.setName("Extent");
			extentItemClass = itemClassRepository.save(extentItemClass);
		}

		RE_Register geodeticRegister = registerRepository.findByName("ISO Geodetic Register");
		if (geodeticRegister == null) {
			log.append("Register \"ISO Geodetic Register\" does not exist.\n");
			return;
		}

		if (!registerService.containsItemClass(geodeticRegister, "Extent")) {
			geodeticRegister.addContainedItemClass(extentItemClass);
			geodeticRegister = registerRepository.save(geodeticRegister);
			log.append(">>> Item class 'Extent' added successfully.\n");
		}
		else {
			log.append(">>> Item class 'Extent' already contained, aborting.\n");
		}
	}

	/**
	 * Migrates the extents associated with Conversion and Transformation items to the new Object Domain
	 * mechanism introduced with ISO 19111:2019.
	 * 
	 * The migration workflow checks if an item was already migrated before. If a "domain" does already
	 * exist, the item is skipped.
	 * 
	 * If the extent of the item is already registered or has been seen previously in the current migration
	 * run, that extent is reused if it has the exact same definition as the existing/seen extent. If the
	 * definitions do not match, the operation item is skipped.
	 * 
	 * If an extent is neither already registered and was not seen before, an Addition proposal is created
	 * for it. All proposals are collected in a proposal group (one per migrated item class).
	 * 
	 * Finally, the "domain" attribute of every migrated operation item is populated with the item's
	 * "scope" and a reference to the created/reused extent item.
	 * 
	 * @param log Log shown in the front-end
	 */
	public void migrateCoordinateOperationExtents(StringBuilder log) {
		log.append("Migrating domains of validity for Coordinate Operation items...\n");

		RE_Register geodeticRegister = registerRepository.findByName("ISO Geodetic Register");
		Assert.notNull(geodeticRegister, "ISO Geodetic Register not found");

		RE_ItemClass convItemClass = itemClassRepository.findByName("Conversion");
		Assert.notNull(convItemClass, "Item class 'Conversion' not found");

		RE_ItemClass transformationItemClass = itemClassRepository.findByName("Transformation");
		Assert.notNull(transformationItemClass, "Item class 'Transformation' not found");

		RE_ItemClass extentItemClass = itemClassRepository.findByName("Extent");
		Assert.notNull(extentItemClass, "Item class 'Extent' not found");

		Organization isotc211 = orgService.findByName(IsoRegistryInitializer.ISO_REGISTRY_CONTROLBODY_ORG_NAME);
		Assert.notNull(isotc211, MessageFormat.format("Control body organization \"{0}\" not found", 
				IsoRegistryInitializer.ISO_REGISTRY_CONTROLBODY_ORG_NAME));

		Map<String, UUID> extentItems = new HashMap<>();
		Map<String, ExtentDTO> extents = new HashMap<>();

		try {
			migrateOperationItems(convItemClass, extentItemClass, geodeticRegister, isotc211, extentItems, extents, log);
			migrateOperationItems(transformationItemClass, extentItemClass, geodeticRegister, isotc211, extentItems, extents, log);
		}
		catch (Throwable t) {
			logger.error(t.getMessage(), t);
			log.append("Uncaught exception while applying fix: " + t.getMessage());
		}
	}

	@SuppressWarnings("deprecation")
	private void migrateOperationItems(RE_ItemClass operationItemClass, RE_ItemClass extentItemClass, RE_Register geodeticRegister,
			Organization sponsor, Map<String, UUID> extentItemsCache, Map<String, ExtentDTO> extentsCache, StringBuilder log) {

		Set<RE_RegisterItem> opItems = new HashSet<>();
		opItems.addAll(itemRepository.findByItemClass(operationItemClass));

		List<RE_RegisterItem> skippedItems = new ArrayList<>();
		List<Proposal> proposals = new ArrayList<>();
		for (RE_RegisterItem opItem : opItems) {
			SingleOperationItem operation = (SingleOperationItem)opItem;
			log.append(MessageFormat.format("\nConverting {0} \"{1}\" ({2})...\n", operationItemClass.getName(), operation.getName(),
					operation.getUuid()));

			if (!operation.getDomains().isEmpty()) {
				log.append("!!! skipping as it already has at least one domain.\n");
				skippedItems.add(opItem);
				continue;
			}
			else if (operation.getScope().isEmpty()) {
				log.append("!!! skipping as it has no scope. Please migrate manually!\n");
				skippedItems.add(opItem);
				continue;
			}
			else if (operation.getScope().size() > 1) {
				log.append("!!! skipping as it has more than one scope. Please migrate manually!\n");
				skippedItems.add(opItem);
				continue;
			}

			ExtentItem extentItem;
			String extentDescription = operation.getDomainOfValidity().getDescription();
			ExtentDTO operationExtent = new ExtentDTO(operation.getDomainOfValidity());

			if (extentItemsCache.containsKey(extentDescription)) {
				ExtentDTO cachedExtent = extentsCache.get(extentDescription);
				if (!compareExtents(cachedExtent, operationExtent)) {
					log.append(MessageFormat.format("!!! Extent \"{0}\" was seen with different definition before, skipping!\n",
							extentDescription));
					skippedItems.add(opItem);
					continue;
				}

				extentItem = (ExtentItem)itemRepository.findOne(extentItemsCache.get(extentDescription));
				log.append(MessageFormat.format(">>> reusing extent \"{0}\"\n", extentDescription));
			}
			else {
				// Try to find existing extent item that matches the description
				String jpql = "SELECT i FROM ExtentItem i WHERE i.name = :extentName";
				TypedQuery<ExtentItem> query = entityManager.createQuery(jpql, ExtentItem.class);
				query.setParameter("extentName", extentDescription);
				List<ExtentItem> existingExtents = query.getResultList();

				if (existingExtents != null && !existingExtents.isEmpty()) {
					if (existingExtents.size() == 1) {
						extentItem = existingExtents.get(0);
						ExtentDTO itemExtent = new ExtentDTO(extentItem.getExtent());
						if (!compareExtents(itemExtent, operationExtent)) {
							log.append(MessageFormat.format("!!! Extent \"{0}\" exists in register with different definition, skipping\n",
									extentDescription));
							skippedItems.add(opItem);
							continue;
						}

						extentItemsCache.put(extentDescription, extentItem.getUuid());
						extentsCache.put(extentDescription, new ExtentDTO(extentItem.getExtent()));
						log.append(MessageFormat.format(">>> reusing extent \"{0}\"\n", extentDescription));
					}
					else {
						log.append(MessageFormat.format("!!! more than existing extent with name \"{0}\" found, skipping\n", 
								extentDescription));
						skippedItems.add(opItem);
						continue;
					}
				}
				else {
					// If there is no extent item yet, propose it
					ExtentItemProposalDTO proposalDto = createExtentItemProposal(operation, geodeticRegister, extentItemClass,
							sponsor.getSubmittingOrganization(), log);

					Addition addition;
					try {
						addition = proposalService.createAdditionProposal(proposalDto);
						proposals.add(addition);
					}
					catch (InvalidProposalException e) {
						log.append("!!! Failed to create addition proposal: " + e.getMessage());
						logger.error(e.getMessage(), e);
						skippedItems.add(opItem);
						continue;
					}

					log.append(MessageFormat.format(">>> created Extent proposal \"{0}\"\n", addition.getTitle()));
					extentItem = (ExtentItem)addition.getItem();

					extentItemsCache.put(extentDescription, extentItem.getUuid());
					extentsCache.put(extentDescription, new ExtentDTO(extentItem.getExtent()));
				}
			}

			ObjectDomain domain = new ObjectDomain(operation.getScope().get(0), extentItem);
			operation.setDomains(Arrays.asList(domain));
			operation = itemRepository.save(operation);

			log.append(MessageFormat.format(">>> created domain with scope \"{0}\" and extent \"{1}\"\n",
					operation.getName(), extentItem.getName()));
		}

		try {
			proposalService.createProposalGroup("Creation of Extent items for migration of " + operationItemClass.getName() + "s",
					proposals, sponsor.getSubmittingOrganization());
		}
		catch (InvalidProposalException e) {
			log.append(MessageFormat.format("!!! Failed to create proposal group: {0}\n\n", e.getMessage()));
			logger.error(e.getMessage(), e);
		}

		if (skippedItems.isEmpty()) {
			log.append(MessageFormat.format("\n\nAll {0} items were migrated succesfully.", operationItemClass.getName()));
		}
		else {
			log.append("\n\n!!! The following items were skipped, please migrate manually:\n");
			for (RE_RegisterItem skippedItem : skippedItems) {
				log.append(MessageFormat.format("- {0} {1} ({2})\n", skippedItem.getItemClass().getName(), skippedItem.getItemIdentifier(),
						skippedItem.getName()));
			}
		}
	}

	private boolean compareExtents(ExtentDTO first, ExtentDTO second) {
		boolean isIdentical = true;
		if (first.getGeographicBoundingBoxes().size() != second.getGeographicBoundingBoxes().size()) {
			isIdentical = false;
		}
		else {
			List<EX_GeographicBoundingBox> cachedBoxes = first.getGeographicBoundingBoxes();
			List<EX_GeographicBoundingBox> operationBoxes = second.getGeographicBoundingBoxes();

			for (EX_GeographicBoundingBox cachedBox : cachedBoxes) {
				boolean foundBox = false;
				for (EX_GeographicBoundingBox operationBox : operationBoxes) {
					if (operationBox.getEastBoundLongitude().equals(cachedBox.getEastBoundLongitude())
						&& operationBox.getNorthBoundLatitude().equals(cachedBox.getNorthBoundLatitude())
						&& operationBox.getSouthBoundLatitude().equals(cachedBox.getSouthBoundLatitude())
						&& operationBox.getWestBoundLongitude().equals(cachedBox.getWestBoundLongitude())) {

						foundBox = true;
						break;
					}
				}

				if (!foundBox) {
					isIdentical = false;
					break;
				}
			}
		}
		return isIdentical;
	}

	@SuppressWarnings("deprecation")
	private ExtentItemProposalDTO createExtentItemProposal(SingleOperationItem operation, RE_Register targetRegister,
			RE_ItemClass extentItemClass, RE_SubmittingOrganization sponsor, StringBuilder log) {

		ExtentDTO domainOfValidity = new ExtentDTO(operation.getDomainOfValidity());

		String migrationNote = MessageFormat.format("Created as part of the ISO 19111:2019 migration of {0} \"{1}\"",
				operation.getItemClass().getName(), operation.getName());

		ExtentItemProposalDTO extentItemProposal = new ExtentItemProposalDTO();
		extentItemProposal.setControlBodyNotes(migrationNote);
		extentItemProposal.setGeographicBoundingBoxes(domainOfValidity.getGeographicBoundingBoxes());
		extentItemProposal.setItemClassUuid(extentItemClass.getUuid());
		extentItemProposal.setJustification(migrationNote);
		extentItemProposal.setName(domainOfValidity.getDescription());
		extentItemProposal.setProposalType(ProposalType.ADDITION);
		extentItemProposal.setSponsorUuid(sponsor.getUuid());
		extentItemProposal.setTargetRegisterUuid(targetRegister.getUuid());

		return extentItemProposal;
	}
}
