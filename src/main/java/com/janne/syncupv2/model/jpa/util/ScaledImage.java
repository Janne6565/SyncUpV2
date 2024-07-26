package com.janne.syncupv2.model.jpa.util;

import jakarta.persistence.*;
import lombok.*;
import org.jetbrains.annotations.NotNull;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Table(name = "images")
public class ScaledImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String fullScaleUrl;

    @NotNull
    private String thumbnailUrl;

    @NotNull
    private String deleteFullScaleToken;

    private String deleteThumbnailToken;
}
