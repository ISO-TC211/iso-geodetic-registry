package org.iso.registry.client.boot.dto;

import de.geoinfoffm.registry.core.model.iso19135.RE_Register;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class HomePage {
    private String version;
    private List<RE_Register> registers;
}
