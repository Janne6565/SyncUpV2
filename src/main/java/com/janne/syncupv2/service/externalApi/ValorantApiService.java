package com.janne.syncupv2.service.externalApi;

import com.janne.syncupv2.model.jpa.post.Map;
import org.springframework.beans.factory.annotation.Value;

public class ValorantApiService {

    @Value("${external.api.valorant.endpoint}")
    private String apiEndpoint;

    public Map[] getMaps() {
        return new Map[0];
    }
}
