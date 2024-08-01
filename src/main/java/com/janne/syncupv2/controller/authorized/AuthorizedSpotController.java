package com.janne.syncupv2.controller.authorized;

import com.janne.syncupv2.model.dto.incomming.requests.post.CreateSpotRequest;
import com.janne.syncupv2.model.dto.outgoing.util.SpotDto;
import com.janne.syncupv2.model.jpa.post.Spot;
import com.janne.syncupv2.service.posts.SpotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/authorized/spot")
public class AuthorizedSpotController {

    private final SpotService spotService;
    private final ModelMapper modelMapper;

    @PostMapping("/")
    public ResponseEntity<SpotDto> createSpot(@Valid @RequestBody CreateSpotRequest spotRequest) {
        Spot createdSpot = spotService.createSpot(spotRequest.getX(), spotRequest.getY(), spotRequest.getMapId());
        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(createdSpot, SpotDto.class));
    }
}
