package org.iso.registry.api.registry.registers.gcp.datum;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.iso.registry.api.IdentifiedItemProposalDTO;
import org.iso.registry.api.registry.registers.gcp.ExtentDTO;
import org.iso.registry.core.model.datum.DatumItem;
import org.iso.registry.core.model.datum.EllipsoidItem;
import org.iso.registry.core.model.datum.GeodeticDatumItem;
import org.iso.registry.core.model.datum.PrimeMeridianItem;
import org.iso.registry.core.model.iso19115.extent.EX_Extent;
import org.springframework.util.StringUtils;

import de.geoinfoffm.registry.api.RegisterItemProposalDTO;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;

public class DatumItemProposalDTO extends IdentifiedItemProposalDTO
{
	public enum DatumType {
		ENGINEERING,
		GEODETIC,
		IMAGE,
		VERTICAL
	}
	
	private DatumType type;
	
	private String anchorDefinition;
	private ExtentDTO domainOfValidity;
	private String realizationEpoch;
	private String scope;
	private String coordinateReferenceEpoch;
	
	private EllipsoidItemProposalDTO ellipsoid;
	private PrimeMeridianItemProposalDTO primeMeridian;
		
	public DatumItemProposalDTO() { 
		this.domainOfValidity = new ExtentDTO();
	}
	
	public DatumItemProposalDTO(DatumItem item) {
		super(item);
	}
	
	public DatumItemProposalDTO(Proposal proposal) {
		super(proposal);
	}

	public DatumType getType() {
		return this.type;
	}

	public void setType(DatumType type) {
		this.type = type;
	}

	public String getAnchorDefinition() {
		return anchorDefinition;
	}

	public void setAnchorDefinition(String anchorDefinition) {
		this.anchorDefinition = anchorDefinition;
	}

	public ExtentDTO getDomainOfValidity() {
		return domainOfValidity;
	}

	public void setDomainOfValidity(ExtentDTO domainOfValidity) {
		this.domainOfValidity = domainOfValidity;
	}

	public String getRealizationEpoch() {
		return realizationEpoch;
	}

	public void setRealizationEpoch(String realizationEpoch) {
		this.realizationEpoch = realizationEpoch;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getCoordinateReferenceEpoch() {
		return coordinateReferenceEpoch;
	}

	public void setCoordinateReferenceEpoch(String coordinateReferenceEpoch) {
		this.coordinateReferenceEpoch = coordinateReferenceEpoch;
	}

	public EllipsoidItemProposalDTO getEllipsoid() {
		return ellipsoid;
	}

	public void setEllipsoid(EllipsoidItemProposalDTO ellipsoid) {
		this.ellipsoid = ellipsoid;
	}

	public PrimeMeridianItemProposalDTO getPrimeMeridian() {
		return primeMeridian;
	}

	public void setPrimeMeridian(PrimeMeridianItemProposalDTO primeMeridian) {
		this.primeMeridian = primeMeridian;
	}
	
	@Override
	public List<RegisterItemProposalDTO> getDependentProposals() {
		return super.findDependentProposals(this.getEllipsoid(), this.getPrimeMeridian());
	}

	@Override
	public void setAdditionalValues(RE_RegisterItem item, EntityManager entityManager) {
		super.setAdditionalValues(item, entityManager);
		
		if (item instanceof DatumItem) {
			DatumItem datum = (DatumItem)item;

			datum.setAnchorDefinition(this.getAnchorDefinition());

			if (this.getDomainOfValidity() != null) {
				EX_Extent extent = this.getDomainOfValidity().getExtent(datum.getDomainOfValidity());
				datum.setDomainOfValidity(extent);
			}
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date realizationEpoch = null;
			Date coordinateReferenceEpoch = null;
			try {
				realizationEpoch = df.parse(this.getRealizationEpoch());
				 if (!StringUtils.isEmpty(this.getCoordinateReferenceEpoch())) {
					 coordinateReferenceEpoch = df.parse(this.getCoordinateReferenceEpoch());
				 }
				datum.setRealizationEpoch(realizationEpoch);
				datum.setCoordinateReferenceEpoch(coordinateReferenceEpoch);
			}
			catch (ParseException e) {
				// Ignore
			}
			datum.setScope(this.getScope());
			
			if (item instanceof GeodeticDatumItem) {
				GeodeticDatumItem geodeticDatum = (GeodeticDatumItem)item;
				
				if (this.getEllipsoid() != null) {
					EllipsoidItem ellipsoid = entityManager.find(EllipsoidItem.class, this.getEllipsoid().getReferencedItemUuid());
					geodeticDatum.setEllipsoid(ellipsoid);
				}
				if (this.getPrimeMeridian() != null) {
					PrimeMeridianItem pm = entityManager.find(PrimeMeridianItem.class, this.getPrimeMeridian().getReferencedItemUuid());
					geodeticDatum.setPrimeMeridian(pm);
				}
			}
		}
	}

	@Override
	public void loadAdditionalValues(RE_RegisterItem item) {
		super.loadAdditionalValues(item);
		
		if (item instanceof DatumItem) {
			DatumItem datum = (DatumItem)item;

			this.setAnchorDefinition(datum.getAnchorDefinition());

			if (datum.getDomainOfValidity() != null) {
				this.setDomainOfValidity(new ExtentDTO(datum.getDomainOfValidity()));
			}
			
			if (datum.getRealizationEpoch() != null) {
				DateFormat df = new SimpleDateFormat("yyyy");
				this.setRealizationEpoch(df.format(datum.getRealizationEpoch()));
			}
			
			this.setScope(datum.getScope());
			
			if (item instanceof GeodeticDatumItem) {
				GeodeticDatumItem geodeticDatum = (GeodeticDatumItem)item;
				
				if (geodeticDatum.getEllipsoid() != null) {
					this.setEllipsoid(new EllipsoidItemProposalDTO(geodeticDatum.getEllipsoid()));
				}
				if (geodeticDatum.getPrimeMeridian() != null) {
					this.setPrimeMeridian(new PrimeMeridianItemProposalDTO(geodeticDatum.getPrimeMeridian()));
				}
			}
		}
	}
}
