package org.iso.registry.api.registry;

import org.iso.registry.core.model.ProposalNote;

import de.geoinfoffm.registry.api.ProposalService;
import de.geoinfoffm.registry.core.IllegalOperationException;
import de.geoinfoffm.registry.core.UnauthorizedException;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.RegistryUser;
import de.geoinfoffm.registry.core.model.iso19135.InvalidProposalException;

/**
 * Extensions for ISO registry
 * 
 * @author Florian Esser
 *
 */
public interface IsoProposalService extends ProposalService
{
	Proposal returnProposal(Proposal proposal, String noteToSubmitter) throws InvalidProposalException, IllegalOperationException, UnauthorizedException;
	
	void deleteProposal(Proposal proposal) throws IllegalOperationException, UnauthorizedException;
	
	ProposalNote createNote(Proposal proposal, RegistryUser author, String note);

	Integer findNextAvailableIdentifier();

}
