package org.iso.registry.api.registry;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.geoinfoffm.registry.api.RegisterService;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;
import de.geoinfoffm.registry.persistence.ItemClassRepository;
import de.geoinfoffm.registry.persistence.RegisterRepository;

@Transactional
@Service
public class RegistryFixService
{
	@Autowired
	private ItemClassRepository itemClassRepository;

	@Autowired
	private RegisterRepository registerRepository;

	@Autowired
	private RegisterService registerService;

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
}
