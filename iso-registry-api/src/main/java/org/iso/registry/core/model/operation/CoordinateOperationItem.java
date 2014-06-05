package org.iso.registry.core.model.operation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
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
	
	@ManyToMany(cascade = CascadeType.ALL)
	private List<DQ_PositionalAccuracy> coordinateOperationAccuracy;
	
	@ManyToOne
	private CoordinateReferenceSystemItem sourceCrs;
	
	@ManyToOne
	private CoordinateReferenceSystemItem targetCrs;
	
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
	
	public void addScope(String scope) {
		if (this.scope == null) {
			this.scope = new ArrayList<>();
		}
		this.scope.add(scope);
	}

	public void addScopes(Collection<String> scopes) {
		if (this.scope == null) {
			this.scope = new ArrayList<>();
		}
		this.scope.addAll(scopes);
	}

	public List<DQ_PositionalAccuracy> getCoordinateOperationAccuracy() {
		return coordinateOperationAccuracy;
	}

	public void setCoordinateOperationAccuracy(List<DQ_PositionalAccuracy> coordinateOperationAccuracy) {
		this.coordinateOperationAccuracy = coordinateOperationAccuracy;
	}
	
	public void addCoordinateOperationAccuracy(DQ_PositionalAccuracy coordinateOperationAccuracy) {
		if (this.coordinateOperationAccuracy == null) {
			this.coordinateOperationAccuracy = new ArrayList<>();
		}
		this.coordinateOperationAccuracy.add(coordinateOperationAccuracy);
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
	
}
