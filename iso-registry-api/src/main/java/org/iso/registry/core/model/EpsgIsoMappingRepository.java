package org.iso.registry.core.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import de.geoinfoffm.registry.core.Repository;

@org.springframework.stereotype.Repository
public interface EpsgIsoMappingRepository extends Repository<EpsgIsoMapping>
{
	@Query("SELECT m.isoCode FROM EpsgIsoMapping m WHERE m.itemClass = :itemClass AND m.epsgCode = :epsgCode")
	public Integer findByItemClassAndEpsgCode(@Param("itemClass") String itemClass, @Param("epsgCode") Integer epsgCode);
}
