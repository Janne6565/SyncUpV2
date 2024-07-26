package com.janne.syncupv2.model.dto.incomming.requests.post;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateSpotRequest {
    @NotBlank(message = "mapId cant be blank")
    private String mapId;
    private float x;
    private float y;
}
