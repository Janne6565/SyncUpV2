package com.janne.syncupv2.model.dto.outgoing.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpotDto {
    private String id;
    private float x;
    private float y;
    private String mapId;
}
