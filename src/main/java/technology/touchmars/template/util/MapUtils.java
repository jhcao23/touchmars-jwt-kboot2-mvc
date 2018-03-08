package technology.touchmars.template.util;

import java.util.HashMap;
import java.util.Map;

public class MapUtils {

    public static <K,V> Map<K,V> makeMap(Map<K, V> extraData){
        return extraData==null?new HashMap<K,V>():new HashMap(extraData);
    }
}
