package com.findthinks.delay.job.share.lib.utils;

import java.util.*;

public abstract class CollectionUtils {

	public static boolean isEmpty(Collection<?> collection) {
		return (collection == null || collection.isEmpty());
	}

	public static boolean isEmpty(Map<?, ?> map) {
		return (map == null || map.isEmpty());
	}
}
