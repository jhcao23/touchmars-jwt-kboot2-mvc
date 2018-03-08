package technology.touchmars.template.util;

import java.util.stream.Stream;

public class ObjectUtils {

	@SafeVarargs
	public static <T> T firstNonNull(T...values){
		return Stream.<T>of(values).filter(v -> v!=null).findFirst().get();
	}
	
}
