package com.janne.syncupv2.controller.unauthorized.maps;

import com.janne.syncupv2.model.jpa.post.Map;
import com.janne.syncupv2.service.maps.MapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/unauthorized/maps")
public class UnauthorizedMapsController {

    private final MapService mapService;

    @GetMapping("/{mapId}")
    public ResponseEntity<Map> getMapById(@PathVariable Float mapId) {
        return ResponseEntity.ok(mapService.getMapById(mapId));
    }

    @GetMapping("/")
    public ResponseEntity<Map[]> getMaps() {
        return ResponseEntity.ok(mapService.getAllMaps());
    }

    @PostMapping("/")
    public ResponseEntity<Map> saveMap(@RequestBody Map map) {
        return ResponseEntity.ok(mapService.saveMap(map));
    }

}
