    package iss.parkingapp.domain;

    import jakarta.persistence.Entity;
    import jakarta.persistence.GeneratedValue;
    import jakarta.persistence.Id;
    import jakarta.validation.constraints.NotNull;

    import java.util.Objects;

    @Entity
    public class Masina {
        private Long id_masina;
        private Long id_utilizator;
        private String nr_inmatriculare;
        private String marca;
        private String model;

        public Masina(Long id_utilizator, String nr_inmatriculare, String marca, String model) {
            this.id_utilizator = id_utilizator;
            this.nr_inmatriculare = nr_inmatriculare;
            this.marca = marca;
            this.model = model;
        }

        public Masina() {}

        @Id
        @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
        public Long getId_masina() {
            return id_masina;
        }
        public void setId_masina(Long id) {
            this.id_masina = id;
        }

        @NotNull
        public Long getId_utilizator() {
            return id_utilizator;
        }
        public void setId_utilizator(Long id_utilizator) {
            this.id_utilizator = id_utilizator;
        }

        @NotNull
        public String getNr_inmatriculare() {
            return nr_inmatriculare;
        }
        public void setNr_inmatriculare(String nr_inmatriculare) {
            this.nr_inmatriculare = nr_inmatriculare;
        }

        @NotNull
        public String getMarca() {
            return marca;
        }
        public void setMarca(String marca) {
            this.marca = marca;
        }

        @NotNull
        public String getModel() {
            return model;
        }
        public void setModel(String model) {
            this.model = model;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Masina masina = (Masina) o;
            return Objects.equals(id_utilizator, masina.id_utilizator) && Objects.equals(nr_inmatriculare, masina.nr_inmatriculare) && Objects.equals(marca, masina.marca) && Objects.equals(model, masina.model);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id_utilizator, nr_inmatriculare, marca, model);
        }

        @Override
        public String toString() {
            return "Masina{" +
                    "id_utilizator=" + id_utilizator +
                    ", numar_inmatriculare='" + nr_inmatriculare + '\'' +
                    ", marca='" + marca + '\'' +
                    ", model='" + model + '\'' +
                    '}';
        }
    }
