package org.iso.registry.core.model.operation;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.hibernate.envers.Audited;
import org.iso.registry.core.model.IdentifiedItem;
import org.iso.registry.core.model.crs.AreaItem;
import org.iso.registry.core.model.crs.CoordinateReferenceSystemItem;
import org.iso.registry.core.model.iso19115.dataquality.DQ_PositionalAccuracy;

import de.geoinfoffm.registry.core.model.iso19135.RE_AdditionInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;

@Access(AccessType.FIELD)
@Audited @Entity
public abstract class CoordinateOperationItem extends IdentifiedItem
{
	private static final long serialVersionUID = -2482111385843464273L;

	private String operationVersion;
	
	@ManyToOne
	private AreaItem domainOfValidity;
	
	@ElementCollection
	private List<String> scope;
	
	@ManyToMany
	private List<DQ_PositionalAccuracy> coordinateOperationAccuracy;
	
	@ManyToOne
	private CoordinateReferenceSystemItem sourceCrs;
	
	@ManyToOne
	private CoordinateReferenceSystemItem targetCrs;
	
	@ManyToOne
	private PassThroughOperationItem passThruOperation;
	
	@ManyToMany
	private List<ConcatenatedOperationItem> concatOperation;

	protected CoordinateOperationItem() {
	}

	public CoordinateOperationItem(RE_Register register, RE_ItemClass itemClass, String name, String definition,
			RE_AdditionInformation additionInformation) {

		super(register, itemClass, name, definition, additionInformation);
	}

	public String getOperationVersion() {
		return operationVersion;
	}

	public void setOperationVersion(String operationVersion) {
		this.operationVersion = operationVersion;
	}

	public AreaItem getDomainOfValidity() {
		return domainOfValidity;
	}

	public void setDomainOfValidity(AreaItem domainOfValidity) {
		this.domainOfValidity = domainOfValidity;
	}

	public List<String> getScope() {
		return scope;
	}

	public void setScope(List<String> scope) {
		this.scope = scope;
	}

	public List<DQ_PositionalAccuracy> getCoordinateOperationAccuracy() {
		return coordinateOperationAccuracy;
	}

	public void setCoordinateOperationAccuracy(List<DQ_PositionalAccuracy> coordinateOperationAccuracy) {
		this.coordinateOperationAccuracy = coordinateOperationAccuracy;
	}

	public CoordinateReferenceSystemItem getSourceCrs() {
		return sourceCrs;
	}

	public void setSourceCrs(CoordinateReferenceSystemItem sourceCrs) {
		this.sourceCrs = sourceCrs;
	}

	public CoordinateReferenceSystemItem getTargetCrs() {
		return targetCrs;
	}

	public void setTargetCrs(CoordinateReferenceSystemItem targetCrs) {
		this.targetCrs = targetCrs;
	}

	public PassThroughOperationItem getPassThruOperation() {
		return passThruOperation;
	}

	public void setPassThruOperation(PassThroughOperationItem passThruOperation) {
		this.passThruOperation = passThruOperation;
	}

	public List<ConcatenatedOperationItem> getConcatOperation() {
		return concatOperation;
	}

	public void setConcatOperation(List<ConcatenatedOperationItem> concatOperation) {
		this.concatOperation = concatOperation;
	}
	
	public void addConcatOperation(ConcatenatedOperationItem concatOperation) {
		if (this.concatOperation == null) {
			this.concatOperation = new ArrayList<>();
		}
		this.concatOperation.add(concatOperation);
	}
	
}
