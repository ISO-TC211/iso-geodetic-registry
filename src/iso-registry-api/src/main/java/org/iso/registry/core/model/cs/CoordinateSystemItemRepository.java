package org.iso.registry.core.model.cs;

import java.util.List;

import org.iso.registry.core.model.IdentifiedItemRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CoordinateSystemItemRepository extends IdentifiedItemRepository<CoordinateSystemItem>
{
	@Query("SELECT a.identifier, a.name FROM CoordinateSystemItem a ORDER BY a.identifier")
	public List<Object[]> findAllOrderByIdentifier();

	@Query("SELECT a.identifier, a.name FROM CoordinateSystemItem a ORDER BY a.name")
	public List<Object[]> findAllOrderByName();
}
