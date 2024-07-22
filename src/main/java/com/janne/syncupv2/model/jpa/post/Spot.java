package com.janne.syncupv2.model.jpa.post;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    private long id;

    private float x;
    private float y;
}
