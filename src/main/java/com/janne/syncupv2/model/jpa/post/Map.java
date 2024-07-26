package com.janne.syncupv2.model.jpa.post;

import com.janne.syncupv2.model.jpa.util.ScaledImage;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "maps")
public class Map {

    @Id
    private String id;

    @NotNull
    private String name;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "display_icon_id", referencedColumnName = "id")
    private ScaledImage displayIcon;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "list_view_icon_id", referencedColumnName = "id")
    private ScaledImage listViewIcon;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "list_view_icon_tall_id", referencedColumnName = "id")
    private ScaledImage listViewIconTall;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "splash_image_id", referencedColumnName = "id")
    private ScaledImage splashImage;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "stylized_image_id", referencedColumnName = "id")
    private ScaledImage stylizedImage;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "premier_image_id", referencedColumnName = "id")
    private ScaledImage premierImage;
}