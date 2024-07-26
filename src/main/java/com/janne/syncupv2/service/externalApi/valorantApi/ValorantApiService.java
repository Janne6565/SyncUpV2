package com.janne.syncupv2.service.externalApi.valorantApi;

import com.janne.syncupv2.model.dto.incomming.externalApi.valorantApi.maps.ValorantApiMapDto;
import com.janne.syncupv2.model.dto.incomming.externalApi.valorantApi.maps.ValorantApiMapRequestDto;
import lombok.RequiredArgsConstructor;
import okhttp3.internal.http2.ConnectionShutdownException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ValorantApiService {

    @Value("${external.api.valorant.endpoint}")
    private String apiEndpoint;
    private final WebClient webClient;

    public ValorantApiMapDto[] getMaps() throws ConnectionShutdownException {
        try {
            ValorantApiMapRequestDto mapRequestResult = webClient.get()
                    .uri(apiEndpoint + "/maps")
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(ValorantApiMapRequestDto.class).block();
            return Objects.requireNonNull(mapRequestResult).getData();
        } catch (Exception e) {
            throw new ConnectionShutdownException();
        }
    }
}
