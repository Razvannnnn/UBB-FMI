package chat.model;

/**
 * Created by grigo on 3/17/17.
 */
public interface Identifiable<ID> {
    void setId(ID id);
    ID getId();
}
