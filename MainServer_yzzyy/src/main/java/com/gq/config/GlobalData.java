package com.gq.config;

public enum GlobalData
{
    
    mysqljndinnamespace("java:comp/jdbc/TestDB");
    private String value;
    
    GlobalData(String value)
    {
        this.value = value;
    }
    
    public String getValue()
    {
        return value;
    }
    
    public void setValue(String value)
    {
        this.value = value;
    }
    
}
