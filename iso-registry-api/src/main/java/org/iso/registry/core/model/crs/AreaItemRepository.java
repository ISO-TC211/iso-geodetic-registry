package org.iso.registry.core.model.crs;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import de.geoinfoffm.registry.core.EntityRepository;

@Repository
public interface AreaItemRepository extends EntityRepository<AreaItem>
{
	AreaItem findByCode(Integer code);
	
	@Query("SELECT a FROM AreaItem a ORDER BY a.code")
	List<AreaItem> findAllOrderBy(/*@Param("orderField") String orderField*/);
	
	@Query("SELECT a.code, a.name FROM AreaItem a ORDER BY a.code")
	public List<Object[]> findAllOrderByCode();

	@Query("SELECT a.code, a.name FROM AreaItem a ORDER BY a.name")
	public List<Object[]> findAllOrderByName();
}
