package org.iso.registry.client;

import java.util.ArrayList;
import java.util.List;

import org.iso.registry.api.registry.registers.gcp.ExtentDTO;
import org.iso.registry.api.registry.registers.gcp.operation.TransformationAccuracy;
import org.iso.registry.api.registry.registers.gcp.operation.TransformationAccuracyValue;
import org.iso.registry.core.model.UnitOfMeasureItem;
import org.iso.registry.core.model.iso19115.dataquality.DQ_AbsoluteExternalPositionalAccuracy;
import org.iso.registry.core.model.iso19115.dataquality.DQ_PositionalAccuracy;
import org.iso.registry.core.model.operation.CoordinateOperationItem;

import de.geoinfoffm.registry.core.model.Appeal;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.SimpleProposal;
import de.geoinfoffm.registry.core.model.Supersession;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;

public class CoordinateOperationItemViewBean extends IdentifiedItemViewBean
{
	private String operationVersion;
	private ExtentDTO domainOfValidity;
	private List<String> scope;
//	private List<DQ_PositionalAccuracy> coordinateOperationAccuracy;
	private Float accuracy;
	private UnitOfMeasureItemViewBean accuracyUom;
	private CoordinateReferenceSystemItemViewBean sourceCrs;
	private CoordinateReferenceSystemItemViewBean targetCrs;

	public CoordinateOperationItemViewBean(RE_RegisterItem item) {
		super(item);
		// TODO Auto-generated constructor stub
	}

	public CoordinateOperationItemViewBean(RE_RegisterItem item, boolean loadDetails) {
		super(item, loadDetails);
		// TODO Auto-generated constructor stub
	}

	public CoordinateOperationItemViewBean(Proposal proposal) {
		super(proposal);
		// TODO Auto-generated constructor stub
	}

	public CoordinateOperationItemViewBean(SimpleProposal proposal) {
		super(proposal);
		// TODO Auto-generated constructor stub
	}

	public CoordinateOperationItemViewBean(Appeal appeal) {
		super(appeal);
		// TODO Auto-generated constructor stub
	}

	public CoordinateOperationItemViewBean(Supersession supersession) {
		super(supersession);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void addAdditionalProperties(RE_RegisterItem registerItem, boolean loadDetails) {
		super.addAdditionalProperties(registerItem, loadDetails);
		
		if (!(registerItem instanceof CoordinateOperationItem)) {
			return;
		}
		
		CoordinateOperationItem item = (CoordinateOperationItem)registerItem;

		this.setOperationVersion(item.getOperationVersion());
		if (item.getDomainOfValidity() != null) {
			this.setDomainOfValidity(new ExtentDTO(item.getDomainOfValidity()));
		}
		if (item.getScope() != null) {
			for (String scope : item.getScope()) {
				this.addScope(scope);
			}
		}

		if (item.getCoordinateOperationAccuracy() != null && !item.getCoordinateOperationAccuracy().isEmpty()) {
			DQ_AbsoluteExternalPositionalAccuracy accuracy = (DQ_AbsoluteExternalPositionalAccuracy)item.getCoordinateOperationAccuracy().get(0);
			if (accuracy.getResult() != null && accuracy.getResult() instanceof TransformationAccuracy) {
				TransformationAccuracy xfAccuracy = (TransformationAccuracy)accuracy.getResult();
				if (xfAccuracy.getValue() instanceof TransformationAccuracyValue) {
					TransformationAccuracyValue xfAccuracyValue = (TransformationAccuracyValue)xfAccuracy.getValue();
					this.setAccuracy(xfAccuracyValue.getAccuracy());
					UnitOfMeasureItem accuracyUom = (UnitOfMeasureItem)xfAccuracy.getValueUnit();
					this.setAccuracyUom(new UnitOfMeasureItemViewBean(accuracyUom, false));
				}
			}
		}
		if (item.getSourceCrs() != null) {
			this.setSourceCrs(new CoordinateReferenceSystemItemViewBean(item.getSourceCrs()));
		}
		if (item.getTargetCrs() != null) {
			this.setTargetCrs(new CoordinateReferenceSystemItemViewBean(item.getTargetCrs()));
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
	
	public void addScope(String scope) {
		if (this.scope == null) {
			this.scope = new ArrayList<>();
		}
		this.scope.add(scope);
	}

	public Float getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(Float accuracy) {
		this.accuracy = accuracy;
	}

//	public List<DQ_PositionalAccuracy> getCoordinateOperationAccuracy() {
//		return coordinateOperationAccuracy;
//	}
//
//	public void setCoordinateOperationAccuracy(List<DQ_PositionalAccuracy> coordinateOperationAccuracy) {
//		this.coordinateOperationAccuracy = coordinateOperationAccuracy;
//	}

	public UnitOfMeasureItemViewBean getAccuracyUom() {
		return accuracyUom;
	}

	public void setAccuracyUom(UnitOfMeasureItemViewBean accuracyUom) {
		this.accuracyUom = accuracyUom;
	}

	public CoordinateReferenceSystemItemViewBean getSourceCrs() {
		return sourceCrs;
	}

	public void setSourceCrs(CoordinateReferenceSystemItemViewBean sourceCrs) {
		this.sourceCrs = sourceCrs;
	}

	public CoordinateReferenceSystemItemViewBean getTargetCrs() {
		return targetCrs;
	}

	public void setTargetCrs(CoordinateReferenceSystemItemViewBean targetCrs) {
		this.targetCrs = targetCrs;
	}

}
