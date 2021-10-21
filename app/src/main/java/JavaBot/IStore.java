package JavaBot;

import java.util.ArrayList;

public interface IStore {
    ArrayList<ITuple> get(String name);
    void addT(String topic, ArrayList<ITuple> tuple);
    ArrayList<String> getTopicsName();
}
