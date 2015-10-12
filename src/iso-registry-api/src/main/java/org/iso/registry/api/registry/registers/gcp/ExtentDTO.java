package org.iso.registry.api.registry.registers.gcp;

import java.util.ArrayList;
import java.util.List;

import org.iso.registry.core.model.iso19115.extent.EX_Extent;
import org.iso.registry.core.model.iso19115.extent.EX_GeographicBoundingBox;
import org.iso.registry.core.model.iso19115.extent.EX_GeographicExtent;

public class ExtentDTO
{
	private String description;
	private List<EX_GeographicBoundingBox> geographicBoundingBoxes;

	public ExtentDTO() {
		this.geographicBoundingBoxes = new ArrayList<>();
	}
	
	public ExtentDTO(EX_Extent extent) {
		if (extent == null) {
			throw new NullPointerException("null extent");
		}
		
		this.description = extent.getDescription();
		
		if (extent.getGeographicElement() != null) {
			for (EX_GeographicExtent geographicExtent : extent.getGeographicElement()) {
				if (geographicExtent instanceof EX_GeographicBoundingBox) {
					this.getGeographicBoundingBoxes().add((EX_GeographicBoundingBox)geographicExtent);
				}
			}
		}
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the geographicBoundingBoxes
	 */
	public List<EX_GeographicBoundingBox> getGeographicBoundingBoxes() {
		if (this.geographicBoundingBoxes == null) {
			this.geographicBoundingBoxes = new ArrayList<>();
		}
		return geographicBoundingBoxes;
	}

	/**
	 * @param geographicBoundingBoxes the geographicBoundingBoxes to set
	 */
	public void setGeographicBoundingBoxes(List<EX_GeographicBoundingBox> geographicBoundingBoxes) {
		this.geographicBoundingBoxes = geographicBoundingBoxes;
	}
	
	/**
	 * Creates a new {@link EX_Extent} object and fills it with values from this DTO.<br>
	 * <br>
	 * <b>This should never be used to replace an existing EX_Extent object. Otherwise data may be lost.</b>
	 * To replace/update an existing object, use {@link #copyValues(EX_Extent)}.
	 * @return a new EX_Extent
	 */
	public EX_Extent getNewExtent() {
		EX_Extent result = new EX_Extent();
		this.copyValues(result);
		
		return result;
	}
	
	/**
	 * Copies values from this DTO to the given {@link EX_Extent} instance. If the instance is <code>null</code>, 
	 * a new {@link EX_Extent} object is returned.
	 * @param extent Existing extent or null
	 * @return New or updated extent
	 */
	public EX_Extent getExtent(EX_Extent extent) {
		if (extent == null) {
			return getNewExtent();
		}
		else {
			this.copyValues(extent);
			return extent;
		}
	}
	
	public void copyValues(EX_Extent extent) {
		if (extent == null) {
			throw new NullPointerException("null extent");
		}
		
		extent.setDescription(this.getDescription());
		
		extent.getGeographicElement().clear();
		for (EX_GeographicBoundingBox boundingBox : this.getGeographicBoundingBoxes()) {
			if (isValidBoundingBox(boundingBox)) {
				extent.getGeographicElement().add(boundingBox);
			}
		}
	}

	private boolean isValidBoundingBox(EX_GeographicBoundingBox boundingBox) {
		return boundingBox != null && boundingBox.getEastBoundLongitude() != null && boundingBox.getWestBoundLongitude() != null && boundingBox.getNorthBoundLatitude() != null && boundingBox.getSouthBoundLatitude() != null;
	}
}
