package timofey.config;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Component
@Scope("singleton")
public class SourceInit {
   private final String resourcePath = "config/sources.properties";

    private Map<String,String> resourceMap;
    private Properties properties;

    {
        properties = new Properties();
        resourceMap = new HashMap<String,String>();

        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath);

            properties.load(inputStream);
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
