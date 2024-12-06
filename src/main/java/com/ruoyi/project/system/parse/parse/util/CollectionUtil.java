package com.ruoyi.project.system.parse.parse.util;

import java.util.List;

public class CollectionUtil {

	public static <T> T safeGet(List<T> list, int index) {
		if (list.size() - 1 >= index) {
			return list.get(index);
		}
		return null;
	}
}
