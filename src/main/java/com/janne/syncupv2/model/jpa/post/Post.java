package com.janne.syncupv2.model.jpa.post;

import com.janne.syncupv2.model.jpa.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.jetbrains.annotations.NotNull;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotNull
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "spot_from_id", referencedColumnName = "id")
    private Spot from;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spot_to_id", referencedColumnName = "id")
    private Spot to;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_collection_id", referencedColumnName = "id")
    private ImageCollection imageCollection;

}
