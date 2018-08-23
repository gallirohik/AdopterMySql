package com.mysql.adaptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

import com.mysql.adaptor.annotation.Metric;

public class MysqlRefelctoion {
	public static void main(String[] args) throws Exception {
		MysqlMetrics metrics = new MysqlMetrics();
		Class<?> c = metrics.getClass();
		Method[] mtd = c.getDeclaredMethods();
		Map<String, Method> mysqlMetrics = new HashMap<>();
		Set<String> keys = mysqlMetrics.keySet();
		for (Method md : mtd) {
			if (md.isAnnotationPresent(Metric.class)) {
				Annotation an = md.getAnnotation(Metric.class);
				Metric mt = (Metric) an;
				mysqlMetrics.put(mt.name(), md);
			}
		}
		for (String key : keys) {
			mysqlMetrics.get(key).invoke(metrics, null);
		}
	}

}
