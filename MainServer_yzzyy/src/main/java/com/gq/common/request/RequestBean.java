package com.gq.common.request;

public abstract class RequestBean<B>
{
    private B body;
    
    private RequestHeader header;
    
    public B getBody()
    {
        return body;
    }
    
    public void setBody(B body)
    {
        this.body = body;
    }
    
    public RequestHeader getHeader()
    {
        return header;
    }
    
    public void setHeader(RequestHeader header)
    {
        this.header = header;
    }
    
}
