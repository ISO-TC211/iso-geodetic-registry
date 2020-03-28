package org.iso.registry.core.model.operation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.envers.Audited;
import org.iso.registry.core.model.IdentifiedItem;
import org.iso.registry.core.model.IdentifiedItemWithObjectUsage;
import org.iso.registry.core.model.ObjectDomain;
import org.iso.registry.core.model.ObjectUsage;
import org.iso.registry.core.model.crs.CoordinateReferenceSystemItem;
import org.iso.registry.core.model.iso19115.dataquality.DQ_PositionalAccuracy;
import org.iso.registry.core.model.iso19115.extent.EX_Extent;

import de.geoinfoffm.registry.core.model.iso19135.RE_AdditionInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;

@Access(AccessType.FIELD)
@Audited @Entity
public abstract class CoordinateOperationItem extends IdentifiedItemWithObjectUsage
{
	private static final long serialVersionUID = -2482111385843464273L;

	private String operationVersion;

	@Deprecated
	@ManyToOne(cascade = CascadeType.ALL)
	private EX_Extent domainOfValidity;
	
	@Deprecated
	@ElementCollection
	private List<String> scope;
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<DQ_PositionalAccuracy> coordinateOperationAccuracy;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private CoordinateReferenceSystemItem sourceCrs;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private CoordinateReferenceSystemItem targetCrs;
	
	protected CoordinateOperationItem() {
	}

	public CoordinateOperationItem(RE_Register register, RE_ItemClass itemClass, String name, String definition,
			RE_AdditionInformation additionInformation, Collection<ObjectDomain> domains) {

		super(register, itemClass, name, definition, additionInformation, domains);
	}

	public String getOperationVersion() {
		return operationVersion;
	}

	public void setOperationVersion(String operationVersion) {
		this.operationVersion = operationVersion;
	}

	@Deprecated
	public EX_Extent getDomainOfValidity() {
		return domainOfValidity;
	}
	
	@Deprecated
	public void setDomainOfValidity(EX_Extent domainOfValidity) {
		this.domainOfValidity = domainOfValidity;
	}

	@Deprecated
	public List<String> getScope() {
		return scope;
	}
	
	@Deprecated
	public void setScope(List<String> scope) {
		this.scope = scope;
	}
	
	@Deprecated
	public void addScope(String scope) {
		if (this.scope == null) {
			this.scope = new ArrayList<>();
		}
		this.scope.add(scope);
	}

	@Deprecated
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
