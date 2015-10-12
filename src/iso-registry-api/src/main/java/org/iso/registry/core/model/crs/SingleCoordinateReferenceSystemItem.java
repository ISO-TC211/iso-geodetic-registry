package org.iso.registry.core.model.crs;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.iso.registry.core.model.cs.CoordinateSystemItem;
import org.iso.registry.core.model.datum.DatumItem;
import org.iso.registry.core.model.iso19115.extent.EX_Extent;
import org.iso.registry.core.model.operation.ConversionItem;

import de.geoinfoffm.registry.core.model.iso19135.RE_AdditionInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;

/**
 * Coordinate reference system consisting of one Coordinate System and one Datum (as opposed to a Compound CRS).
 * 
 * Note: In ISO 19111:2003, this class was called SC_CoordinateReferenceSystem.
 * @author Florian.Esser
 * @created 16-Apr-2014 23:49:49
 */
@Access(AccessType.FIELD)
@Table(name = "SingleCRS")
@Audited @Entity
public abstract class SingleCoordinateReferenceSystemItem<D extends DatumItem> extends CoordinateReferenceSystemItem 
{
	/**
	 * Derived association to the datum used by this coordinate reference system.
	 */
	@ManyToOne(targetEntity = DatumItem.class, cascade = CascadeType.PERSIST)
	private D datum;
	
	/**
	 * Association to the coordinate system used by this CRS.
	 */
	@ManyToOne(optional = false)
	private CoordinateSystemItem coordinateSystem;
	
	/**
	 * Set if this CRS is derived
	 */
	@ManyToOne
	private SingleCoordinateReferenceSystemItem<D> baseCrs;

	@ManyToOne
	private ConversionItem conversion;
	
	protected SingleCoordinateReferenceSystemItem() {
		super();
	}

	public SingleCoordinateReferenceSystemItem(RE_Register register, RE_ItemClass itemClass, String name,
			String definition, RE_AdditionInformation additionInformation, EX_Extent domainOfValidity, String scope,
			D datum, CoordinateSystemItem coordinateSystem) {

		super(register, itemClass, name, definition, additionInformation, domainOfValidity, scope);
		
		this.datum = datum;
		this.coordinateSystem = coordinateSystem;
	}

	public D getDatum() {
		return datum;
	}

	public void setDatum(D newVal){
		datum = newVal;
	}

	public CoordinateSystemItem getCoordinateSystem() {
		return coordinateSystem;
	}

	public void setCoordinateSystem(CoordinateSystemItem newVal) {
		coordinateSystem = newVal;
	}
	
	public <T extends SingleCoordinateReferenceSystemItem<D>> T getBaseCrs() {
		return (T)baseCrs;
	}

	public <T extends SingleCoordinateReferenceSystemItem<D>> void setBaseCrs(T baseCrs) {
		this.baseCrs = baseCrs;
	}

	/**
	 * @return the conversion
	 */
	public ConversionItem getConversion() {
		return conversion;
	}

	/**
	 * @param conversion the conversion to set
	 */
	public void setConversion(ConversionItem conversion) {
		this.conversion = conversion;
	}

}//end SC_SingleCRS