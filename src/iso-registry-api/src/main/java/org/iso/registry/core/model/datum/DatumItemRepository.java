package org.iso.registry.core.model.datum;

import java.util.List;

import org.iso.registry.core.model.IdentifiedItemRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DatumItemRepository extends IdentifiedItemRepository<DatumItem>
{
	@Query("SELECT a.identifier, a.name FROM DatumItem a ORDER BY a.identifier")
	public List<Object[]> findAllOrderByCode();

	@Query("SELECT a.identifier, a.name FROM DatumItem a ORDER BY a.name")
	public List<Object[]> findAllOrderByName();
}
