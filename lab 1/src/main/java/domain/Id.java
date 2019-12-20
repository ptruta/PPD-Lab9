package domain;

import java.util.Objects;

public class Id {
    int id;

    Id(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Id)) return false;
        Id id1 = (Id) o;
        return this.id == id1.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


}
