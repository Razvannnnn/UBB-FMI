package ro.mpp2025.domain;

public interface Entity<ID> {
    void setId(ID id);
    ID getId();
}
