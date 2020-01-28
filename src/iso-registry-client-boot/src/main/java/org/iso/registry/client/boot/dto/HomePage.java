package org.iso.registry.client.boot.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HomePage {
    private String version;
}
