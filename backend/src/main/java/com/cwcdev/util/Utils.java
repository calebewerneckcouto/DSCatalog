package com.cwcdev.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cwcdev.entities.Product;
import com.cwcdev.projetctions.ProductProjection;

public class Utils {

	public static List<Product> replace(List<ProductProjection> ordered, List<Product> unoredered) {

		Map<Long, Product> map = new HashMap<Long, Product>();

		for (Product obj : unoredered) {
			map.put(obj.getId(), obj);
		}
		List<Product> result = new ArrayList<Product>();

		for (ProductProjection obj : ordered) {
			result.add(map.get(obj.getId()));
		}

		return result;
	}

}
