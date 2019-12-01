package Core;

import java.util.HashMap;
import java.util.List;

public class Buffer {
    public static List<HashMap<String, String>> newsMap;

    public static void setNewsMap(List<HashMap<String, String>> newsMap) {
        Buffer.newsMap = newsMap;
    }

    public static List<HashMap<String, String>> getNewsMap() {
        return newsMap;
    }
}
