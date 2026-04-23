package Domain;

import java.text.Format;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Formatter;
import java.util.stream.LongStream;

public class Prietenie extends Entity<Long> {

    LocalDateTime date;

    Long idUser1;
    Long idUser2;

    public Prietenie(Long idUser1, Long idUser2) {
        this.idUser1 = idUser1;
        this.idUser2 = idUser2;
    }

    public Long getIdUtilizator1() {
        return idUser1;
    }

    public Long getIdUtilizator2() {
        return idUser2;
    }

    public LocalDateTime getDate() {
        return date;
    }
}
