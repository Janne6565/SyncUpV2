package com.janne.syncupv2.service.externalApi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImgurConfig {
    private String imgurUploadEndpoint;
    private String clientId;
}
