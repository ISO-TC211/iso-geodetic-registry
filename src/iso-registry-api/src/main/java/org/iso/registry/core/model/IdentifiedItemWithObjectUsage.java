package org.iso.registry.core.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.MappedSuperclass;

import org.hibernate.envers.Audited;

import de.geoinfoffm.registry.core.model.iso19135.RE_AdditionInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;

/**
 * Base class for item classes that must implement the ObjectUsage interface
 * 
 * @author Florian Esser
 *
 */
@Audited @MappedSuperclass
public abstract class IdentifiedItemWithObjectUsage extends IdentifiedItem implements ObjectUsage
{
	@ElementCollection
	private Set<ObjectDomain> domains;

	public IdentifiedItemWithObjectUsage() {
		super();
	}

	public IdentifiedItemWithObjectUsage(RE_Register register, RE_ItemClass itemClass, String name, String definition,
			RE_AdditionInformation additionInformation, Collection<ObjectDomain> domains) {
		super(register, itemClass, name, definition, additionInformation);

		this.getDomains().addAll(domains);
	}

	@Override
	public Collection<ObjectDomain> getDomains() {
		if (domains == null) {
			domains = new HashSet<>();
		}
		return domains;
	}

	@Override
	public void setDomains(Collection<ObjectDomain> domains) {
		this.domains = new HashSet<>();
		this.domains.addAll(domains);
	}
}
