package org.iso.registry.core.model.iso19115.extent;

import java.util.List;

import org.springframework.stereotype.Repository;

import de.geoinfoffm.registry.core.EntityRepository;

@Repository
public interface ExtentRepository extends EntityRepository<EX_Extent>
{
	List<EX_Extent> findByDescription(String description);
}
