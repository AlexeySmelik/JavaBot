package JavaBot.data_classes;

import java.io.IOException;

public interface Loader {
    void load(Store store) throws IOException;
}
