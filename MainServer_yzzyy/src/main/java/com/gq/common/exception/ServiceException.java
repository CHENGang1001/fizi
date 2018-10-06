package com.gq.common.exception;

import com.gq.config.ReturnCode;

public class ServiceException extends Exception
{
    
    private static final long serialVersionUID = 1L;
    
    private ReturnCode scode;
    
    public ServiceException()
    {
        super();
    }
    
    public ServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
    public ServiceException(String message, Throwable cause)
    {
        super(message, cause);
        
    }
    
    public ServiceException(String message)
    {
        super(message);
    }
    
    public ServiceException(Throwable cause)
    {
        //        if(case IsInstanceOf xxx){
        //            this.scode=xxxx
        //        }
        super(cause);
    }
    
    public ServiceException(ReturnCode status, Exception e)
    {
        this.scode = status;
    }
    
    public ServiceException(ReturnCode status)
    {
        this.scode = status;
    }
    
    public ReturnCode getScode()
    {
        return scode;
    }
    
    /**
     * @param scode
     * the scode to set
     */
    public void setScode(ReturnCode scode)
    {
        this.scode = scode;
    }
    
}
