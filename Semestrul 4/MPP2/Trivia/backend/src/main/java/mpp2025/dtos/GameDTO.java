package mpp2025.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mpp2025.domain.Game;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameDTO {
    private Long id;
    private int numberPoints;
    private Boolean success;
    private String username;
    private int duration;

    private String i1;
    private String i2;
    private String i3;
    private String i4;
    private String i5;
    private String i6;

    private String r1;
    private String r2;
    private String r3;
    private String r4;
    private String r5;
    private String r6;

    private int p1;
    private int p2;
    private int p3;
    private int p4;
    private int p5;
    private int p6;


    public Game toGame() {
        return Game.builder()
                .id(this.id)
                .numberPoints(this.numberPoints)
                .success(this.success)
                .username(this.username)
                .duration(this.duration)

                .i1(this.i1)
                .i2(this.i2)
                .i3(this.i3)
                .i4(this.i4)
                .i5(this.i5)
                .i6(this.i6)

                .r1(this.r1)
                .r2(this.r2)
                .r3(this.r3)
                .r4(this.r4)
                .r5(this.r5)
                .r6(this.r6)

                .p1(this.p1)
                .p2(this.p2)
                .p3(this.p3)
                .p4(this.p4)
                .p5(this.p5)
                .p6(this.p6)

                .build();
    }

    public static GameDTO fromGame(Game game) {
        return new GameDTO(
                game.getId(),
                game.getNumberPoints(),
                game.getSuccess(),
                game.getUsername(),
                game.getDuration(),
                game.getI1(), game.getI2(), game.getI3(), game.getI4(), game.getI5(), game.getI6(),
                game.getR1(), game.getR2(), game.getR3(), game.getR4(), game.getR5(), game.getR6(),
                game.getP1(), game.getP2(), game.getP3(), game.getP4(), game.getP5(), game.getP6()
        );
    }

}
