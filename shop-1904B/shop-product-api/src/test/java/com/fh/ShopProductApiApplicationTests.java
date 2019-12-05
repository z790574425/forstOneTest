package com.fh;

import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.*;
import org.junit.Test;
import sun.misc.BASE64Encoder;
import java.util.HashMap;
import java.util.Map;
public class ShopProductApiApplicationTests {

    @Test
    public void contextLoads() {
        //jwt如何生成字符串
        //声明头部信息
        Map<String,Object> headerMap=new HashMap<String,Object>();
        headerMap.put("alg","HS256");
        headerMap.put("typ","JWT");
        //设置负载:不要放着涉密的东西比如:银行账号密码，余额，身份证号
        Map<String,Object> payload=new HashMap<String,Object>();
        payload.put("userPhone","13716746058");
        payload.put("userId","1234567898765433");
        Long iat=System.currentTimeMillis();
        //设置jwt的失效时间
        payload.put("exp",iat+60000l);
        payload.put("iat",iat);

        //签名值就是我们的安全密钥
       String token=Jwts.builder()
               .setHeader(headerMap)
               .setPayload(JSON.toJSONString(payload))
               .signWith(SignatureAlgorithm.HS256,getSecretKey("shangfengaa"))
               .compact();
        System.out.println(token);

    }

    @Test
    public void resolverTest(){
        String token="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1NzQ3NTQxNzk4MDksImlhdCI6MTU3NDc1NDExOTgwOSwidXNlcklkIjoiMTIzNDU2Nzg5ODc2NTQzMyIsInVzZXJQaG9uZSI6IjEzNzE2NzQ2MDU4In0.VRa6-1UlhwE8S4Cu-PQe2s_33h1A8rgKyFHLgfYQFiM";
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(getSecretKey("shangfengaa"))
                    .parseClaimsJws(token)
                    .getBody();

        }catch (ExpiredJwtException exp){
            System.out.println("token超时，token失效了");
        }catch (SignatureException sing){
            System.out.println("token解析失败");
        }

    }

    private  static String getSecretKey(String key){
        return new BASE64Encoder().encode(key.getBytes());
    }

}
