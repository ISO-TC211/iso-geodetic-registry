package org.iso.registry.api;

import org.isotc211.iso19135.RE_RegisterItem_Type;
import org.springframework.stereotype.Component;

import de.geoinfoffm.registry.api.EntityUnitOfWork;
import de.geoinfoffm.registry.api.converter.BeanConverter;
import de.geoinfoffm.registry.api.converter.ItemVisitor;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;

@Component
public class BeanConverterImpl implements BeanConverter
{

	@Override
	public void updateModelBeanFromXmlBean(RE_RegisterItem modelBean, RE_RegisterItem_Type xmlBean, EntityUnitOfWork unitOfWork, boolean updateNonRequired, boolean updateRequired) {

	}

	@Override
	public void accept(RE_RegisterItem modelBean, boolean followRequired, boolean followNonRequired,
			boolean followContainments, boolean followNonContainments, boolean followDerived, boolean followNonDerived,
			boolean followPredecessors, boolean followSuccessors, ItemVisitor itemVisitor) {
	}

}
