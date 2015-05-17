package org.iso.registry.core.model;

import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import de.geoinfoffm.registry.core.Repository;

@org.springframework.stereotype.Repository
public interface EpsgIsoMappingRepository extends Repository<EpsgIsoMapping>
{
	@Query("SELECT m.isoUuid FROM EpsgIsoMapping m WHERE m.itemClass = :itemClass AND m.epsgCode = :epsgCode")
	public UUID findByItemClassAndEpsgCode(@Param("itemClass") String itemClass, @Param("epsgCode") Integer epsgCode);

	@Query("SELECT m.isoUuid FROM EpsgIsoMapping m WHERE m.epsgCode = :epsgCode")
	public UUID findByEpsgCode(@Param("epsgCode") Integer epsgCode);
	
	@Query("SELECT m.epsgCode FROM EpsgIsoMapping m WHERE m.isoUuid = :isoUuid")
	public Integer findByUuid(@Param("isoUuid") UUID uuid);
}
