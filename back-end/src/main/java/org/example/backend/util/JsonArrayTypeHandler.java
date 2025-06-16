package org.example.backend.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 自定义TypeHandler，用于处理JSON和String数组之间的转换
 */
public class JsonArrayTypeHandler extends BaseTypeHandler<String[]> {
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String[] parameter, JdbcType jdbcType) throws SQLException {
        try {
            ps.setString(i, objectMapper.writeValueAsString(parameter));
        } catch (JsonProcessingException e) {
            throw new SQLException("Error converting String[] to JSON string: " + e.getMessage(), e);
        }
    }
    
    @Override
    public String[] getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return parseJsonToStringArray(rs.getString(columnName));
    }
    
    @Override
    public String[] getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return parseJsonToStringArray(rs.getString(columnIndex));
    }
    
    @Override
    public String[] getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return parseJsonToStringArray(cs.getString(columnIndex));
    }
    
    private String[] parseJsonToStringArray(String json) throws SQLException {
        if (json == null) {
            return null;
        }
        
        try {
            return objectMapper.readValue(json, String[].class);
        } catch (JsonProcessingException e) {
            throw new SQLException("Error parsing JSON to String[]: " + e.getMessage(), e);
        }
    }
} 