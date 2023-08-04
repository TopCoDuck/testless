package com.tetless.backend.config;

import java.util.HashMap;

public class JpaProperties extends HashMap<String, Object> {
	public JpaProperties(String showSql, String formatSql, String useSqlComments) {
		put("hibernate.show_sql", showSql);
		put("hibernate.format_sql", formatSql);
		put("hibernate.use_sql_comments", useSqlComments);
		put("hibernate.physical_naming_strategy",
				"org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy");
	}
}
