package de.geoinfoffm.registry.iso.importer;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.healthmarketscience.jackcess.Row;

import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;

@Component
public class CoordinateOperationsImporter extends AbstractImporter
{

	@Override
	@Transactional
	protected void importRow(Row row, RE_ItemClass itemClass, RE_SubmittingOrganization sponsor, RE_Register register)
			throws IOException {
		
	}

	@Override
	protected void clearAway() {
	}

	@Override
	@Transactional
	public RE_ItemClass getOrCreateItemClass(RE_Register register, Row row) {
		RE_ItemClass itemClass = getOrCreateItemClass("SingleOperation", register);
		getOrCreateItemClass("Conversion", register);
		getOrCreateItemClass("Transformation", register);
		getOrCreateItemClass("ConcatenatedOperation", register);
		getOrCreateItemClass("OperationMethod", register);
		getOrCreateItemClass("OperationParameter", register);
		getOrCreateItemClass("OperationParameterValue", register);

		return itemClass;

	}

	public RE_ItemClass getOrCreateItemClass(String name, RE_Register register) {
		RE_ItemClass itemClass = itemClassRepository.findByName(name);
		if (itemClass == null) {
			this.addItemClass(name, register);
			itemClass = itemClassRepository.findByName(name);
		}
		
		return itemClass;
	}

}
