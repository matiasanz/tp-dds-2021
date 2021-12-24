package Utils;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

public class Resources {
    public static File getFile(String path){
        URL resource = Resources.class.getClassLoader().getResource(path);
        if (resource == null) {
            throw new IllegalArgumentException("file not found!");
        }

        try{
            return new File(resource.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
