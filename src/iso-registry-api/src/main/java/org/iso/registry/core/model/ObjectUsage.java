package org.iso.registry.core.model;

import java.util.Collection;

public interface ObjectUsage
{
	Collection<ObjectDomain> getDomains();

	void setDomains(Collection<ObjectDomain> domains);
}
