package com.gq.common.filter.helper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.springframework.util.StreamUtils;

public class BodyReaderHttpServletRequestWrapper extends HttpServletRequestWrapper
{
    private final byte[] body;
    
    public BodyReaderHttpServletRequestWrapper(HttpServletRequest request)
    {
        super(request);
        body = this.readBytes(request);
    }
    
    @Override
    public BufferedReader getReader() throws IOException
    {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }
    
    @Override
    public ServletInputStream getInputStream() throws IOException
    {
        final ByteArrayInputStream bais = new ByteArrayInputStream(body);
        return new ServletInputStream()
        {
            
            @Override
            public int read() throws IOException
            {
                return bais.read();
            }

			@Override
            public boolean isFinished() {
	            // TODO Auto-generated method stub
	            return false;
            }

			@Override
            public boolean isReady() {
	            // TODO Auto-generated method stub
	            return false;
            }

			@Override
            public void setReadListener(ReadListener arg0) {
	            // TODO Auto-generated method stub
            }
            
        };
    }
    
    private byte[] readBytes(HttpServletRequest request)
    {
        try
        {
            return StreamUtils.copyToByteArray(request.getInputStream());
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return new byte[0];
        }
    }
    
}
