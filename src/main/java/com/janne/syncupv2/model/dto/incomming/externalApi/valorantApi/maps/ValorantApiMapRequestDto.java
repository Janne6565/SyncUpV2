package com.janne.syncupv2.model.dto.incomming.externalApi.valorantApi.maps;

import lombok.Data;

@Data
public class ValorantApiMapRequestDto {
    private int status;
    private ValorantApiMapDto[] data;
}
