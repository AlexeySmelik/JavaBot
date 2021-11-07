package JavaBot.resources;

import java.io.IOException;

public interface Loader {
    void load(Store store) throws IOException;
}
