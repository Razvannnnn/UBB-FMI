package mpp2025.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "games")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numberPoints", nullable = false)
    private int numberPoints;

    @Column(name = "success", nullable = false)
    private Boolean success;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "duration", nullable = false)
    private int duration;

    @Column(name = "i1")
    private String i1;
    @Column(name = "i2")
    private String i2;
    @Column(name = "i3")
    private String i3;
    @Column(name = "i4")
    private String i4;
    @Column(name = "i5")
    private String i5;
    @Column(name = "i6")
    private String i6;

    @Column(name = "r1")
    private String r1;
    @Column(name = "r2")
    private String r2;
    @Column(name = "r3")
    private String r3;
    @Column(name = "r4")
    private String r4;
    @Column(name = "r5")
    private String r5;
    @Column(name = "r6")
    private String r6;

    @Column(name = "p1")
    private int p1;
    @Column(name = "p2")
    private int p2;
    @Column(name = "p3")
    private int p3;
    @Column(name = "p4")
    private int p4;
    @Column(name = "p5")
    private int p5;
    @Column(name = "p6")
    private int p6;


}
