package com.gq.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils
{
    private static ObjectMapper mapper = null;
    
    static
    {
        mapper = new ObjectMapper();
    }
    
    public static <T> String toJson(T clazz)
    {
        
        try
        {
            return mapper.writeValueAsString(clazz);
        }
        catch (JsonProcessingException e)
        {
        
        }
        return clazz.toString() + ": 转换成json错误";
        
    }
}
