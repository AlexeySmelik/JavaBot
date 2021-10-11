package JavaBot;

import java.util.ArrayList;

public interface IStore {
    IStore get();
    void addT(String theme, ArrayList<ITuple> tuple);
}
