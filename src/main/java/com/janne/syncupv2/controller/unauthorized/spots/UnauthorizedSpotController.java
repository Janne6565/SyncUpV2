package com.janne.syncupv2.controller.unauthorized.spots;

import com.janne.syncupv2.model.dto.outgoing.util.SpotDto;
import com.janne.syncupv2.service.posts.SpotService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/unauthorized/spot")
@RequiredArgsConstructor
public class UnauthorizedSpotController {

    private final SpotService spotService;
    private final ModelMapper modelMapper;

    @GetMapping("/")
    public ResponseEntity<SpotDto[]> getSpots() {
        return ResponseEntity.ok(modelMapper.map(spotService.getSpots(), SpotDto[].class));
    }
}
