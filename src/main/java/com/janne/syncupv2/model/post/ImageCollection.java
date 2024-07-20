package com.janne.syncupv2.model.post;

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
    private Image imageLooking;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_standing", referencedColumnName = "id")
    private Image imageStanding;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_landing", referencedColumnName = "id")
    private Image imageLanding;
    
}