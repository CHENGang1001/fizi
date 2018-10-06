package com.qiniu;

import com.qiniu.util.Auth;

public class TestController {
    public static void main(String[] args) {
    	
        //设置需要操作的账号的AK和SK
        String ACCESS_KEY = "F2v5e74teoiCJUX7Svic-95y25M9Fp8qOvDR-U4P";
        String SECRET_KEY = "qEQwQR_AIkDpyWz3zDjJBHkYar32NFv0h_5RfbRX";
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        System.out.println(auth.uploadToken("fuzi"));
    }
}