package org.iso.registry.api.registry.registers.gcp.crs;

import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;

import org.iso.registry.api.registry.registers.gcp.cs.CoordinateSystemItemProposalDTO;
import org.iso.registry.api.registry.registers.gcp.datum.DatumItemProposalDTO;
import org.iso.registry.api.registry.registers.gcp.operation.SingleOperationItemProposalDTO;
import org.iso.registry.api.registry.registers.gcp.operation.SingleOperationType;
import org.iso.registry.core.model.crs.CompoundCoordinateReferenceSystemItem;
import org.iso.registry.core.model.crs.CoordinateReferenceSystemItem;
import org.iso.registry.core.model.crs.GeneralDerivedCoordinateReferenceSystemItem;
import org.iso.registry.core.model.crs.ProjectedCoordinateReferenceSystemItem;
import org.iso.registry.core.model.crs.SingleCoordinateReferenceSystemItem;
import org.iso.registry.core.model.cs.CoordinateSystemItem;
import org.iso.registry.core.model.datum.DatumItem;
import org.iso.registry.core.model.iso19115.extent.EX_Extent;
import org.iso.registry.core.model.operation.ConversionItem;
import org.iso.registry.core.model.operation.SingleOperationItem;
import org.iso.registry.core.model.operation.TransformationItem;
import org.isotc211.iso19135.RE_RegisterItem_Type;

import de.geoinfoffm.registry.api.RegisterItemProposalDTO;
import de.geoinfoffm.registry.api.soap.AbstractCoordinateReferenceSystemItemProposal_Type;
import de.geoinfoffm.registry.api.soap.AbstractRegisterItemProposal_Type;
import de.geoinfoffm.registry.api.soap.AbstractSingleCoordinateReferenceSystemItemProposal_Type;
import de.geoinfoffm.registry.api.soap.Addition_Type;
import de.geoinfoffm.registry.api.soap.CoordinateSystemItemProposal_PropertyType;
import de.geoinfoffm.registry.api.soap.DatumItemProposal_PropertyType;
import de.geoinfoffm.registry.api.soap.SingleCoordinateReferenceSystemItemProposal_PropertyType;
import de.geoinfoffm.registry.api.soap.SingleOperationItemProposal_PropertyType;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;

public class CoordinateReferenceSystemItemProposalDTO extends ReferenceSystemItemProposalDTO
{
	public enum CoordinateReferenceSystemType {
		COMPOUND,
		ENGINEERING,
		GEODETIC,
		PROJECTED,
		VERTICAL,
	}
//	private RS_Identifier identifier;
	private CoordinateReferenceSystemType type;
	private String scope;
	
	private CoordinateSystemItemProposalDTO coordinateSystem;
	private DatumItemProposalDTO datum;
	
	private CoordinateReferenceSystemItemProposalDTO sourceCrs;
	
	private CoordinateReferenceSystemItemProposalDTO baseCrs;
	private SingleOperationItemProposalDTO operation;
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

	public CoordinateReferenceSystemItemProposalDTO(AbstractCoordinateReferenceSystemItemProposal_Type itemDetails) {
		super(itemDetails);
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

	public CoordinateReferenceSystemItemProposalDTO getBaseCrs() {
		return baseCrs;
	}

	public void setBaseCrs(CoordinateReferenceSystemItemProposalDTO baseCrs) {
		this.baseCrs = baseCrs;
	}

	public SingleOperationItemProposalDTO getOperation() {
		return operation;
	}

	public void setOperation(SingleOperationItemProposalDTO operation) {
		this.operation = operation;
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
	public List<RegisterItemProposalDTO> getAggregateDependencies() {
		return super.findDependentProposals(this.getDatum(), this.getCoordinateSystem(), this.getBaseCrs(), this.getHorizontalCrs(), this.getVerticalCrs(), this.getOperation());
	}

	@Override
	protected void initializeFromItemDetails(AbstractRegisterItemProposal_Type itemDetails) {
		super.initializeFromItemDetails(itemDetails);
	
		if (itemDetails instanceof AbstractSingleCoordinateReferenceSystemItemProposal_Type) {
			AbstractSingleCoordinateReferenceSystemItemProposal_Type xmlProposal = (AbstractSingleCoordinateReferenceSystemItemProposal_Type) itemDetails;
	
			this.setScope(xmlProposal.getScope());	
			
			final SingleCoordinateReferenceSystemItemProposal_PropertyType baseCrsProperty = xmlProposal.getBaseCrs();
			if (baseCrsProperty != null) {
				final CoordinateReferenceSystemItemProposalDTO dto;
				if (baseCrsProperty.isSetAbstractSingleCoordinateReferenceSystemItemProposal()) {
					dto = new CoordinateReferenceSystemItemProposalDTO(baseCrsProperty.getAbstractSingleCoordinateReferenceSystemItemProposal());
				}
				else if (baseCrsProperty.isSetUuidref()) {
					dto = new CoordinateReferenceSystemItemProposalDTO();
					dto.setReferencedItemUuid(UUID.fromString(baseCrsProperty.getUuidref()));
				}
				else {
					throw new RuntimeException("unexpected reference");
				}
				
				this.setBaseCrs(dto);
			}
			final CoordinateSystemItemProposal_PropertyType coordinateSystemProperty = xmlProposal.getCoordinateSystem();
			if (coordinateSystemProperty != null) {
				final CoordinateSystemItemProposalDTO dto;
				if (coordinateSystemProperty.isSetAbstractCoordinateSystemItemProposal()) {
					dto = new CoordinateSystemItemProposalDTO(coordinateSystemProperty.getAbstractCoordinateSystemItemProposal());
				}
				else if (coordinateSystemProperty.isSetUuidref()) {
					dto = new CoordinateSystemItemProposalDTO();
					dto.setReferencedItemUuid(UUID.fromString(coordinateSystemProperty.getUuidref()));
				}
				else {
					throw new RuntimeException("unexpected reference");
				}
				
				this.setCoordinateSystem(dto);
			}
			final DatumItemProposal_PropertyType datumProperty = xmlProposal.getDatum();
			if (datumProperty != null) {
				final DatumItemProposalDTO dto;
				if (datumProperty.isSetAbstractDatumItemProposal()) {
					dto = new DatumItemProposalDTO(datumProperty.getAbstractDatumItemProposal());
				}
				else if (datumProperty.isSetUuidref()) {
					dto = new DatumItemProposalDTO();
					dto.setReferencedItemUuid(UUID.fromString(datumProperty.getUuidref()));
				}
				else {
					throw new RuntimeException("unexpected reference");
				}
				
				this.setDatum(dto);
			}
			final SingleOperationItemProposal_PropertyType operationProperty = xmlProposal.getOperation();
			if (operationProperty != null) {
				final SingleOperationItemProposalDTO dto;
				if (operationProperty.isSetAbstractSingleOperationItemProposal()) {
					dto = new SingleOperationItemProposalDTO(operationProperty.getAbstractSingleOperationItemProposal());
				}
				else if (operationProperty.isSetUuidref()) {
					dto = new SingleOperationItemProposalDTO();
					dto.setReferencedItemUuid(UUID.fromString(operationProperty.getUuidref()));
				}
				else {
					throw new RuntimeException("unexpected reference");
				}
				
				this.setOperation(dto);
			}
		}	
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
			
			if (hasReferencedItem(this.getCoordinateSystem()) && (registerItem instanceof SingleCoordinateReferenceSystemItem<?>)) {
				SingleCoordinateReferenceSystemItem<?> singleCrs = (SingleCoordinateReferenceSystemItem<?>)registerItem;
				CoordinateSystemItem cs = entityManager.find(CoordinateSystemItem.class, this.getCoordinateSystem().getReferencedItemUuid());
				singleCrs.setCoordinateSystem(cs);
			}
			
			if (hasReferencedItem(this.getDatum()) && (registerItem instanceof SingleCoordinateReferenceSystemItem<?>)) {
				SingleCoordinateReferenceSystemItem<DatumItem> singleCrs = (SingleCoordinateReferenceSystemItem<DatumItem>)registerItem;
				DatumItem datum = entityManager.find(DatumItem.class, this.getDatum().getReferencedItemUuid());
				singleCrs.setDatum(datum);
			}
			
			if (hasReferencedItem(this.getSourceCrs()) && (registerItem instanceof SingleCoordinateReferenceSystemItem<?>)) {
				SingleCoordinateReferenceSystemItem<DatumItem> singleCrs = (SingleCoordinateReferenceSystemItem<DatumItem>)registerItem;
				CoordinateReferenceSystemItem crs = entityManager.find(CoordinateReferenceSystemItem.class, this.getSourceCrs().getReferencedItemUuid());
//				if (crs instanceof SingleCoordinateReferenceSystemItem<?>) {
//					singleCrs.setBaseCrs((SingleCoordinateReferenceSystemItem<DatumItem>)crs);
//				}
//				else {
//					throw new RuntimeException(String.format("Illegal CRS used as base CRS: %d is not a Single CRS", crs.getIdentifier()));
//				}
			}
			
			if (hasReferencedItem(this.getHorizontalCrs()) && (registerItem instanceof CompoundCoordinateReferenceSystemItem)) {
				// By convention, the first CRS is the horizontal component
				CompoundCoordinateReferenceSystemItem compoundCrs = (CompoundCoordinateReferenceSystemItem)registerItem;
				CoordinateReferenceSystemItem horizontalCrs = entityManager.find(CoordinateReferenceSystemItem.class, this.getHorizontalCrs().getReferencedItemUuid());
				compoundCrs.getComponentReferenceSystem().clear();
				if (horizontalCrs instanceof SingleCoordinateReferenceSystemItem) {
					compoundCrs.getComponentReferenceSystem().add((SingleCoordinateReferenceSystemItem<? extends DatumItem>)horizontalCrs);
				}
				else {
					throw new RuntimeException(String.format("Illegal CRS used as part of Compound CRS: %d is not a Single CRS", horizontalCrs.getIdentifier()));
				}
			}
			if (hasReferencedItem(this.getVerticalCrs()) && (registerItem instanceof CompoundCoordinateReferenceSystemItem)) {
				// By convention, the second CRS is the vertical component
				CompoundCoordinateReferenceSystemItem compoundCrs = (CompoundCoordinateReferenceSystemItem)registerItem;
				CoordinateReferenceSystemItem verticalCrs = entityManager.find(CoordinateReferenceSystemItem.class, this.getVerticalCrs().getReferencedItemUuid());
				if (verticalCrs instanceof SingleCoordinateReferenceSystemItem) {
					compoundCrs.getComponentReferenceSystem().add((SingleCoordinateReferenceSystemItem<? extends DatumItem>)verticalCrs);
				}
				else {
					throw new RuntimeException(String.format("Illegal CRS used as part of Compound CRS: %d is not a Single CRS", verticalCrs.getIdentifier()));
				}
			}
			
			if (hasReferencedItem(this.getBaseCrs()) && this.getBaseCrs().getReferencedItemUuid() != null && (registerItem instanceof SingleCoordinateReferenceSystemItem)) {
				SingleCoordinateReferenceSystemItem singleCrs = (SingleCoordinateReferenceSystemItem)registerItem;
				SingleCoordinateReferenceSystemItem baseCrs = entityManager.find(SingleCoordinateReferenceSystemItem.class, this.getBaseCrs().getReferencedItemUuid());
				singleCrs.setBaseCrs(baseCrs);
			}
			
			if (hasReferencedItem(this.getOperation()) && (registerItem instanceof SingleCoordinateReferenceSystemItem)) {
				SingleCoordinateReferenceSystemItem singleCrs = (SingleCoordinateReferenceSystemItem)registerItem;
				SingleOperationItem operation = entityManager.find(SingleOperationItem.class, this.getOperation().getReferencedItemUuid());
				singleCrs.setOperation(operation);
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
				
//				if (scrsItem.getBaseCrs() != null) {
//					this.setSourceCrs(new CoordinateReferenceSystemItemProposalDTO(scrsItem.getBaseCrs()));
//				}

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
			
			if (crsItem instanceof GeneralDerivedCoordinateReferenceSystemItem) {
				GeneralDerivedCoordinateReferenceSystemItem<?> derivedCrs = (GeneralDerivedCoordinateReferenceSystemItem<?>)crsItem;
				if (derivedCrs.getOperation() != null) {
					SingleOperationItemProposalDTO operation = new SingleOperationItemProposalDTO(derivedCrs.getOperation());
					if (derivedCrs.getOperation() instanceof ConversionItem) {
						operation.setOperationType(SingleOperationType.CONVERSION);						
					}
					else if (derivedCrs.getOperation() instanceof TransformationItem) {
						operation.setOperationType(SingleOperationType.TRANSFORMATION);
					}
					else {
						throw new IllegalStateException(String.format("Derived CRS operation of type '%s' is not supported", derivedCrs.getOperation().getClass().getCanonicalName()));
					}
				}
			}
			
			if (crsItem instanceof ProjectedCoordinateReferenceSystemItem) {
				ProjectedCoordinateReferenceSystemItem projectedCrs = (ProjectedCoordinateReferenceSystemItem)crsItem;
				if (projectedCrs.getBaseCrs() != null) {
					this.setBaseCrs(new GeodeticCoordinateReferenceSystemItemProposalDTO(projectedCrs.getBaseCrs()));
				}
			}
		}
	}

}
