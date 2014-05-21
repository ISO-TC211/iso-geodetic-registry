package org.iso.registry.core.model.datum;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import de.geoinfoffm.registry.core.EntityRepository;

@Repository
public interface DatumItemRepository extends EntityRepository<DatumItem>
{
	DatumItem findByCode(Integer code);
	
	@Query("SELECT a.code, a.name FROM DatumItem a ORDER BY a.code")
	public List<Object[]> findAllOrderByCode();

	@Query("SELECT a.code, a.name FROM DatumItem a ORDER BY a.name")
	public List<Object[]> findAllOrderByName();
}
