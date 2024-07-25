package com.janne.syncupv2.model.jpa.post;

import com.janne.syncupv2.model.jpa.util.ScaledImage;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "image_collections")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageCollection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_looking", referencedColumnName = "id")
    private ScaledImage imageLooking;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_standing", referencedColumnName = "id")
    private ScaledImage imageStanding;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_landing", referencedColumnName = "id")
    private ScaledImage imageLanding;

    public ScaledImage[] toArray() {
        return new ScaledImage[]{imageLooking, imageStanding, imageLanding};
    }
}