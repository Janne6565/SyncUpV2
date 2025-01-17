package com.janne.syncupv2.controller.unauthorized.maps;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.janne.syncupv2.model.dto.outgoing.map.MapDto;
import com.janne.syncupv2.service.maps.MapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/unauthorized/map")
public class UnauthorizedMapsController {

    private final MapService mapService;
    private final ObjectMapper objectMapper;

    @GetMapping("/{mapId}")
    public ResponseEntity<MapDto> getMapById(@PathVariable String mapId) {
        return ResponseEntity.ok(objectMapper.convertValue(mapService.getMapById(mapId), MapDto.class));
    }

    @GetMapping("/")
    public ResponseEntity<MapDto[]> getMaps() {
        return ResponseEntity.ok(objectMapper.convertValue(mapService.getAllMaps(), MapDto[].class));
    }
}
