package JavaBot.loader;

import JavaBot.data_classes.Store;

import java.io.IOException;

public interface Loader {
    void load(Store store) throws IOException;
}
