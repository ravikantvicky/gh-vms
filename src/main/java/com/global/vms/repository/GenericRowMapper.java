package com.global.vms.repository;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.PropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;
import org.springframework.util.ClassUtils;

import com.global.vms.annotation.DbColumn;

public class GenericRowMapper<T> implements RowMapper<T> {

	private Class<T> typeArgumentClass;

	public GenericRowMapper(Class<T> typeArgumentClass) {
		this.typeArgumentClass = typeArgumentClass;
	}

	@Override
	public T mapRow(ResultSet rs, int rowNum) throws SQLException {
		try {
			T obj = typeArgumentClass.newInstance();
			Field[] allFields = obj.getClass().getDeclaredFields();
			for (Field f : allFields) {
				if (f.isAnnotationPresent(DbColumn.class)) {
					DbColumn dbColumn = f.getAnnotation(DbColumn.class);
					String columnName = dbColumn.value();
					if (columnName.equals(""))
						columnName = f.getName();

					PropertyAccessor myAccessor = PropertyAccessorFactory.forDirectFieldAccess(obj);
					myAccessor.setPropertyValue(f.getName(),
							convertValueIfNecessary(rs.getObject(columnName), f.getType()));
				}
			}
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException("Error in column mapping.");
		}
	}

	@SuppressWarnings("unchecked")
	@Nullable
	protected <S> S convertValueIfNecessary(Object value, @Nullable Class<S> targetType) {
		if (targetType == null) {
			return (S) value;
		}
		if (ClassUtils.isAssignableValue(targetType, value)) {
			return (S) value;
		}
		return DefaultConversionService.getSharedInstance().convert(value, targetType);
	}
}
