package ro.mpp2025.domain;

import ro.mpp2025.utils.AgeConvertor;

public class Child implements Entity<Integer> {
    private Integer id;
    private final String name;
    private final String cnp;

    public Child(String name, String cnp) {
        this.name = name;
        this.cnp = cnp;
    }

    public String getName() {
        return name;
    }

    public String getCNP() {
        return cnp;
    }

    public int getAge() {
        return AgeConvertor.getAgeFromCNP(cnp);
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String toString() {
        return id + "|" + name + "|" + cnp;
    }
}
