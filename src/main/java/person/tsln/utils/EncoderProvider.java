package person.tsln.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class EncoderProvider {
    @Bean
    public MessageDigest getMD5() throws NoSuchAlgorithmException {
        return MessageDigest.getInstance("MD5");
    }

    @Bean
    public BASE64Encoder getBase64Encoder(){
        return new BASE64Encoder();
    }
}
