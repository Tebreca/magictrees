package com.tebreca.magictrees.util;

import java.util.Collection;

public class Collections {

	public static <T> boolean compare(Collection<T> collection1, Collection<T> collection2){
		return collection1.stream().map(collection2::contains).reduce(true, (a, b) -> a && b);
	}


}
