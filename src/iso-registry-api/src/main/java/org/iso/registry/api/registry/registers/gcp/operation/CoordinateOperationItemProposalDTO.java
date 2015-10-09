package org.iso.registry.api.registry.registers.gcp.operation;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;

import org.iso.registry.api.IdentifiedItemProposalDTO;
import org.iso.registry.api.registry.registers.gcp.ExtentDTO;
import org.iso.registry.api.registry.registers.gcp.UnitOfMeasureItemProposalDTO;
import org.iso.registry.api.registry.registers.gcp.crs.CoordinateReferenceSystemItemProposalDTO;
import org.iso.registry.core.model.UnitOfMeasureItem;
import org.iso.registry.core.model.crs.CoordinateReferenceSystemItem;
import org.iso.registry.core.model.iso19115.dataquality.DQ_AbsoluteExternalPositionalAccuracy;
import org.iso.registry.core.model.iso19115.dataquality.DQ_PositionalAccuracy;
import org.iso.registry.core.model.iso19115.dataquality.DQ_Result;
import org.iso.registry.core.model.iso19115.extent.EX_Extent;
import org.iso.registry.core.model.operation.CoordinateOperationItem;
import org.isotc211.iso19135.RE_RegisterItem_Type;
import org.isotc211.iso19139.common.UnitOfMeasure_PropertyType;

import de.geoinfoffm.registry.api.soap.Addition_Type;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;

public abstract class CoordinateOperationItemProposalDTO extends IdentifiedItemProposalDTO
{
	private String operationVersion;
	private ExtentDTO domainOfValidity;
	private List<String> scope;
//	private List<DQ_PositionalAccuracy> coordinateOperationAccuracy;
	private Float accuracy;
	private UnitOfMeasureItemProposalDTO accuracyUom;
	private CoordinateReferenceSystemItemProposalDTO sourceCrs;
	private CoordinateReferenceSystemItemProposalDTO targetCrs;

	public CoordinateOperationItemProposalDTO() {
		this.domainOfValidity = new ExtentDTO();
	}

	public CoordinateOperationItemProposalDTO(String itemClassName) {
		super(itemClassName);
		// TODO Auto-generated constructor stub
	}

	public CoordinateOperationItemProposalDTO(CoordinateOperationItem item) {
		super(item);
		// TODO Auto-generated constructor stub
	}

	public CoordinateOperationItemProposalDTO(Addition_Type proposal, RE_SubmittingOrganization sponsor) {
		super(proposal, sponsor);
		// TODO Auto-generated constructor stub
	}

	public CoordinateOperationItemProposalDTO(Proposal proposal) {
		super(proposal);
		// TODO Auto-generated constructor stub
	}

	public CoordinateOperationItemProposalDTO(RE_RegisterItem_Type item, RE_SubmittingOrganization sponsor) {
		super(item, sponsor);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void setAdditionalValues(RE_RegisterItem registerItem, EntityManager entityManager) {
		super.setAdditionalValues(registerItem, entityManager);

		if (registerItem instanceof CoordinateOperationItem) {
			CoordinateOperationItem item = (CoordinateOperationItem)registerItem;

			item.setOperationVersion(this.getOperationVersion());

			if (this.getDomainOfValidity() != null) {
				EX_Extent extent = this.getDomainOfValidity().getExtent(item.getDomainOfValidity());
				item.setDomainOfValidity(extent);
			}

			if (this.getScope() != null) {
				if (item.getScope() != null) {
					while (!item.getScope().isEmpty()) {
						item.getScope().remove(0);
					}
				}
				item.addScopes(this.getScope());
			}
			
//			if (this.getCoordinateOperationAccuracy() != null) {
//				for (DQ_PositionalAccuracy pa : this.getCoordinateOperationAccuracy()) {
//					item.addCoordinateOperationAccuracy(pa);
//				}
//			}
			
			if (this.getAccuracy() != null && this.getAccuracyUom() != null) {
				UnitOfMeasureItem uom = entityManager.find(UnitOfMeasureItem.class, this.getAccuracyUom().getReferencedItemUuid());
				TransformationAccuracy trafoAccuracy = new TransformationAccuracy(this.getAccuracy(), uom);
				DQ_AbsoluteExternalPositionalAccuracy accuracy = new DQ_AbsoluteExternalPositionalAccuracy();
				accuracy.setResult(trafoAccuracy);
				item.addCoordinateOperationAccuracy(accuracy);

			}
			
			if (this.getSourceCrs() != null && this.getSourceCrs().getReferencedItemUuid() != null) {
				CoordinateReferenceSystemItem crs = entityManager.find(CoordinateReferenceSystemItem.class, this.getSourceCrs().getReferencedItemUuid());
				item.setSourceCrs(crs);
			}

			if (this.getTargetCrs() != null && this.getTargetCrs().getReferencedItemUuid() != null) {
				CoordinateReferenceSystemItem crs = entityManager.find(CoordinateReferenceSystemItem.class, this.getTargetCrs().getReferencedItemUuid());
				item.setTargetCrs(crs);
			}
		}
	}

	@Override
	public void loadAdditionalValues(RE_RegisterItem registerItem) {
		super.loadAdditionalValues(registerItem);
		
		if (registerItem instanceof CoordinateOperationItem) {
			CoordinateOperationItem item = (CoordinateOperationItem)registerItem;

			this.setOperationVersion(item.getOperationVersion());

			if (item.getDomainOfValidity() != null) {
				this.setDomainOfValidity(new ExtentDTO(item.getDomainOfValidity()));
			}

			if (item.getScope() != null) {
				this.scope = new ArrayList<>();
				this.scope.addAll(item.getScope());
			}
			
			if (item.getCoordinateOperationAccuracy() != null && !item.getCoordinateOperationAccuracy().isEmpty()) {
				// Only one entry is currently supported
				DQ_PositionalAccuracy positionalAccuracy = item.getCoordinateOperationAccuracy().get(0);
				if (positionalAccuracy.getResult() != null && positionalAccuracy.getResult() instanceof TransformationAccuracy) {
					TransformationAccuracy accuracy = (TransformationAccuracy)positionalAccuracy.getResult();
					TransformationAccuracyValue accuracyValue = (TransformationAccuracyValue)accuracy.getValue();
					UnitOfMeasureItem accuracyUom = (UnitOfMeasureItem)accuracy.getValueUnit();
					this.setAccuracy(accuracyValue.getAccuracy());
					this.setAccuracyUom(new UnitOfMeasureItemProposalDTO(accuracyUom));
				}
			}
			
			if (item.getSourceCrs() != null) {
				this.setSourceCrs(new CoordinateReferenceSystemItemProposalDTO(item.getSourceCrs()));
			}

			if (item.getTargetCrs() != null) {
				this.setTargetCrs(new CoordinateReferenceSystemItemProposalDTO(item.getTargetCrs()));
			}
		}
	}


	public String getOperationVersion() {
		return operationVersion;
	}

	public void setOperationVersion(String operationVersion) {
		this.operationVersion = operationVersion;
	}

	public ExtentDTO getDomainOfValidity() {
		return domainOfValidity;
	}

	public void setDomainOfValidity(ExtentDTO domainOfValidity) {
		this.domainOfValidity = domainOfValidity;
	}

	public List<String> getScope() {
		return scope;
	}

	public void setScope(List<String> scope) {
		this.scope = scope;
	}

//	public List<? extends DQ_PositionalAccuracy> getCoordinateOperationAccuracy() {
//		return coordinateOperationAccuracy;
//	}
//
//	public <T extends DQ_PositionalAccuracy> void setCoordinateOperationAccuracy(List<T> coordinateOperationAccuracy) {
//		this.coordinateOperationAccuracy = new ArrayList<>();
//		this.coordinateOperationAccuracy.addAll(coordinateOperationAccuracy);
//	}
//	
//	public void addCoordinateOperationAccuracy(DQ_PositionalAccuracy coordinateOperationAccuracy) {
//		if (this.coordinateOperationAccuracy == null) {
//			this.coordinateOperationAccuracy = new ArrayList<>();
//		}
//		this.coordinateOperationAccuracy.add(coordinateOperationAccuracy);
//	}

	public Float getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(Float accuracy) {
		this.accuracy = accuracy;
	}

	public UnitOfMeasureItemProposalDTO getAccuracyUom() {
		return accuracyUom;
	}

	public void setAccuracyUom(UnitOfMeasureItemProposalDTO accuracyUom) {
		this.accuracyUom = accuracyUom;
	}

	public CoordinateReferenceSystemItemProposalDTO getSourceCrs() {
		return sourceCrs;
	}

	public void setSourceCrs(CoordinateReferenceSystemItemProposalDTO sourceCrs) {
		this.sourceCrs = sourceCrs;
	}

	public CoordinateReferenceSystemItemProposalDTO getTargetCrs() {
		return targetCrs;
	}

	public void setTargetCrs(CoordinateReferenceSystemItemProposalDTO targetCrs) {
		this.targetCrs = targetCrs;
	}
}
