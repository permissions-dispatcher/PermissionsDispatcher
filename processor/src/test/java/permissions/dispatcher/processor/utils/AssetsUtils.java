package permissions.dispatcher.processor.utils;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Provides util methods which Load files from assets dir.
 */
public final class AssetsUtils {

    private AssetsUtils() {
    }

    public static String readString(String fileName) {
        try (FileInputStream fis = new FileInputStream(new File("src/test/assets", fileName))) {
            return IOUtils.toString(fis, "UTF-8");
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
