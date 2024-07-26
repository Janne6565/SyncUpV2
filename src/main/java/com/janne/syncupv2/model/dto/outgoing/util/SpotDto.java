package com.janne.syncupv2.model.dto.outgoing.util;

import com.janne.syncupv2.model.dto.outgoing.post.MapDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpotDto {
    private Long id;
    private float x;
    private float y;
    private MapDto map;
}
