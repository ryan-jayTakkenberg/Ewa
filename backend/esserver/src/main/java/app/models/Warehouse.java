package app.models;

import java.util.ArrayList;
import java.util.List;

public class Warehouse {

    public static final List<Warehouse> list = new ArrayList<>();

    private long id;

    public Warehouse(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
