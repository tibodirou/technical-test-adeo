package adeo.leroymerlin.cdp.util;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class TestFileLoader {
    private static final Logger logger = LoggerFactory.getLogger(TestFileLoader.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String getFileContent(String fileName) {
        try {
            return Files.readString(getPath(fileName));
        } catch (IOException e) {
            logger.error("Error reading file", e);
            return null;
        }
    }

    public static <T> List<T> getListFromJsonFile(String fileName, Class<T> clazz) {
        try {
            String fileContent = Files.readString(getPath(fileName));
            TypeFactory typeFactory = objectMapper.getTypeFactory();
            return objectMapper.readValue(fileContent, typeFactory.constructCollectionType(List.class, clazz));
        } catch (JsonMappingException e) {
            logger.error("Error mapping JSON to object list", e);
            return null;
        } catch (IOException e) {
            logger.error("Error reading file", e);
            return null;
        }
    }

    private static Path getPath(String fileName) {
        String cleanedPath = null;
        try {
            cleanedPath = Paths.get(TestFileLoader.class.getClassLoader().getResource(fileName).toURI()).toString();
        } catch (URISyntaxException e) {
            logger.error("Error getting path", e);
        }
        return Path.of(cleanedPath);
    }
}
