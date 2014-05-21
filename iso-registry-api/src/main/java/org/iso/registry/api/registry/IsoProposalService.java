package org.iso.registry.api.registry;

import org.iso.registry.core.model.ProposalNote;

import de.geoinfoffm.registry.api.ProposalService;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.RegistryUser;

/**
 * Extensions for ISO registry
 * 
 * @author Florian Esser
 *
 */
public interface IsoProposalService extends ProposalService
{
	ProposalNote createNote(Proposal proposal, RegistryUser author, String note);
}
