package com.janne.syncupv2.model.jpa.post;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "spots")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Spot {
    @Id
    @GeneratedValue
    private Long id;

    private float x;
    private float y;

    @ManyToOne
    @JoinColumn(name = "map_id", referencedColumnName = "id")
    private Map map;
}
