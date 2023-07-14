package timofey.config;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Component
@Scope("singleton")
public class SourceInit {
   private final String resourcePath = "src/main/resources/sources.properties";

    private Map<String,String> resourceMap;
    private Properties properties;

    {
        properties = new Properties();
        resourceMap = new HashMap<String,String>();

        try {
            FileInputStream FIS = new FileInputStream(resourcePath);
            properties.load(FIS);
            for (String key: properties.stringPropertyNames()){
                resourceMap.put(key, properties.getProperty(key));
            }
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }

    }

    public Map<String, String> getResourceMap() {
        return resourceMap;
    }
}
