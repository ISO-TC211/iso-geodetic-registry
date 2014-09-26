package org.iso.registry.api.registry.registers.gcp.crs;

import java.util.List;

import javax.persistence.EntityManager;

import org.iso.registry.api.registry.registers.gcp.cs.CoordinateSystemItemProposalDTO;
import org.iso.registry.api.registry.registers.gcp.datum.DatumItemProposalDTO;
import org.iso.registry.core.model.crs.CompoundCoordinateReferenceSystemItem;
import org.iso.registry.core.model.crs.CoordinateReferenceSystemItem;
import org.iso.registry.core.model.crs.SingleCoordinateReferenceSystemItem;
import org.iso.registry.core.model.cs.CoordinateSystemItem;
import org.iso.registry.core.model.datum.DatumItem;
import org.iso.registry.core.model.iso19115.extent.EX_Extent;
import org.isotc211.iso19135.RE_RegisterItem_Type;

import de.geoinfoffm.registry.api.RegisterItemProposalDTO;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;
import de.geoinfoffm.registry.soap.Addition_Type;

public class CoordinateReferenceSystemItemProposalDTO extends ReferenceSystemItemProposalDTO
{
	public enum CoordinateReferenceSystemType {
		COMPOUND,
		ENGINEERING,
		GEODETIC,
		PROJECTED,
		VERTICAL
	}
//	private RS_Identifier identifier;
	private CoordinateReferenceSystemType type;
	private String scope;
	
	private CoordinateSystemItemProposalDTO coordinateSystem;
	private DatumItemProposalDTO datum;
	
	private CoordinateReferenceSystemItemProposalDTO sourceCrs;
	// TODO Projection
	
	private CoordinateReferenceSystemItemProposalDTO horizontalCrs;
	private CoordinateReferenceSystemItemProposalDTO verticalCrs;
	
	public CoordinateReferenceSystemItemProposalDTO() {
		super();
	}
	
	public CoordinateReferenceSystemItemProposalDTO(CoordinateReferenceSystemItem crs) {
		super(crs);
	}	

	public CoordinateReferenceSystemItemProposalDTO(RE_RegisterItem_Type item, RE_SubmittingOrganization sponsor) {
		super(item, sponsor);
	}

	public CoordinateReferenceSystemItemProposalDTO(Addition_Type proposal, RE_SubmittingOrganization sponsor) {
		super(proposal, sponsor);
	}

	public CoordinateReferenceSystemItemProposalDTO(Proposal proposal) {
		super(proposal);
	}
	
	public CoordinateReferenceSystemType getType() {
		return type;
	}

	public void setType(CoordinateReferenceSystemType type) {
		this.type = type;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public CoordinateSystemItemProposalDTO getCoordinateSystem() {
		return coordinateSystem;
	}

	public void setCoordinateSystem(CoordinateSystemItemProposalDTO coordinateSystem) {
		this.coordinateSystem = coordinateSystem;
	}

	public DatumItemProposalDTO getDatum() {
		return datum;
	}

	public void setDatum(DatumItemProposalDTO datum) {
		this.datum = datum;
	}

	public CoordinateReferenceSystemItemProposalDTO getSourceCrs() {
		return sourceCrs;
	}

	public void setSourceCrs(CoordinateReferenceSystemItemProposalDTO sourceCrs) {
		this.sourceCrs = sourceCrs;
	}

	public CoordinateReferenceSystemItemProposalDTO getHorizontalCrs() {
		return horizontalCrs;
	}

	public void setHorizontalCrs(CoordinateReferenceSystemItemProposalDTO horizontalCrs) {
		this.horizontalCrs = horizontalCrs;
	}

	public CoordinateReferenceSystemItemProposalDTO getVerticalCrs() {
		return verticalCrs;
	}

	public void setVerticalCrs(CoordinateReferenceSystemItemProposalDTO verticalCrs) {
		this.verticalCrs = verticalCrs;
	}

	@Override
	public List<RegisterItemProposalDTO> getDependentProposals() {
		return super.findDependentProposals(this.getDatum());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setAdditionalValues(RE_RegisterItem registerItem, EntityManager entityManager) {
		super.setAdditionalValues(registerItem, entityManager);
		
		if (registerItem instanceof CoordinateReferenceSystemItem) {
			CoordinateReferenceSystemItem item = (CoordinateReferenceSystemItem)registerItem;

			item.setScope(this.getScope());
			
			if (this.getDomainOfValidity() != null) {
				EX_Extent itemExtent = this.getDomainOfValidity().getExtent(item.getDomainOfValidity());
				item.setDomainOfValidity(itemExtent);
			}
			
			if (this.getCoordinateSystem() != null && (registerItem instanceof SingleCoordinateReferenceSystemItem<?>)) {
				SingleCoordinateReferenceSystemItem<?> singleCrs = (SingleCoordinateReferenceSystemItem<?>)registerItem;
				CoordinateSystemItem cs = entityManager.find(CoordinateSystemItem.class, this.getCoordinateSystem().getReferencedItemUuid());
				singleCrs.setCoordinateSystem(cs);
			}
			
			if (this.getDatum() != null && (registerItem instanceof SingleCoordinateReferenceSystemItem<?>)) {
				SingleCoordinateReferenceSystemItem<DatumItem> singleCrs = (SingleCoordinateReferenceSystemItem<DatumItem>)registerItem;
				DatumItem datum = entityManager.find(DatumItem.class, this.getDatum().getReferencedItemUuid());
				singleCrs.setDatum(datum);
			}
			
			if (this.getSourceCrs() != null && (registerItem instanceof SingleCoordinateReferenceSystemItem<?>)) {
				SingleCoordinateReferenceSystemItem<DatumItem> singleCrs = (SingleCoordinateReferenceSystemItem<DatumItem>)registerItem;
				CoordinateReferenceSystemItem crs = entityManager.find(CoordinateReferenceSystemItem.class, this.getSourceCrs().getReferencedItemUuid());
				if (crs == null) {
					new Object();
				}
				if (crs instanceof SingleCoordinateReferenceSystemItem<?>) {
					singleCrs.setBaseCrs((SingleCoordinateReferenceSystemItem<DatumItem>)crs);
				}
				else {
					throw new RuntimeException(String.format("Illegal CRS used as base CRS: %d is not a Single CRS", crs.getIdentifier()));
				}
			}
			
			if (this.getHorizontalCrs() != null && (registerItem instanceof CompoundCoordinateReferenceSystemItem)) {
				// By convention, the first CRS is the horizontal component
				CompoundCoordinateReferenceSystemItem compoundCrs = (CompoundCoordinateReferenceSystemItem)registerItem;
				CoordinateReferenceSystemItem horizontalCrs = entityManager.find(CoordinateReferenceSystemItem.class, this.getHorizontalCrs().getReferencedItemUuid());
				if (horizontalCrs instanceof SingleCoordinateReferenceSystemItem) {
					compoundCrs.addComponentReferenceSystem((SingleCoordinateReferenceSystemItem<? extends DatumItem>)horizontalCrs);
				}
				else {
					throw new RuntimeException(String.format("Illegal CRS used as part of Compound CRS: %d is not a Single CRS", horizontalCrs.getIdentifier()));
				}
			}
			if (this.getVerticalCrs() != null && (registerItem instanceof CompoundCoordinateReferenceSystemItem)) {
				// By convention, the first CRS is the horizontal component
				CompoundCoordinateReferenceSystemItem compoundCrs = (CompoundCoordinateReferenceSystemItem)registerItem;
				CoordinateReferenceSystemItem verticalCrs = entityManager.find(CoordinateReferenceSystemItem.class, this.getVerticalCrs().getReferencedItemUuid());
				if (verticalCrs instanceof SingleCoordinateReferenceSystemItem) {
					compoundCrs.addComponentReferenceSystem((SingleCoordinateReferenceSystemItem<? extends DatumItem>)verticalCrs);
				}
				else {
					throw new RuntimeException(String.format("Illegal CRS used as part of Compound CRS: %d is not a Single CRS", verticalCrs.getIdentifier()));
				}
			}
			
//			item.setAliases(aliases);
		}
	}

	@Override
	public void loadAdditionalValues(RE_RegisterItem item) {
		super.loadAdditionalValues(item);
		
		if (item instanceof CoordinateReferenceSystemItem) {
			CoordinateReferenceSystemItem crsItem = (CoordinateReferenceSystemItem)item;

			this.setScope(crsItem.getScope());

			if (crsItem instanceof SingleCoordinateReferenceSystemItem<?>) {
				SingleCoordinateReferenceSystemItem<?> scrsItem = (SingleCoordinateReferenceSystemItem<?>)crsItem;
			
				if (scrsItem.getCoordinateSystem() != null) {
					this.setCoordinateSystem(new CoordinateSystemItemProposalDTO(scrsItem.getCoordinateSystem()));
				}
				
				if (scrsItem.getDatum() != null) {
					this.setDatum(new DatumItemProposalDTO(scrsItem.getDatum())); 
				}
				
				if (scrsItem.getBaseCrs() != null) {
					this.setSourceCrs(new CoordinateReferenceSystemItemProposalDTO(scrsItem.getBaseCrs()));
				}

			}
			
			if (crsItem instanceof CompoundCoordinateReferenceSystemItem) {
				CompoundCoordinateReferenceSystemItem ccrsItem = (CompoundCoordinateReferenceSystemItem)crsItem;

				if (ccrsItem.getComponentReferenceSystem().size() > 0) {
					this.setHorizontalCrs(new CoordinateReferenceSystemItemProposalDTO(ccrsItem.getComponentReferenceSystem().get(0)));
				}
				if (ccrsItem.getComponentReferenceSystem().size() > 1) {
					this.setVerticalCrs(new CoordinateReferenceSystemItemProposalDTO(ccrsItem.getComponentReferenceSystem().get(1)));
				}
			}
		}
	}


}
