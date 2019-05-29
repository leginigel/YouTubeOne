package jacklin.com.youtubefxc.api;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

public class Suggestion {
    @Attribute(name = "data", required = false)
    private String data;

    public String getData() {
        return data;
    }
}
