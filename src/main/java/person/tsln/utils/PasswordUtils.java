package person.tsln.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

import java.security.MessageDigest;

@Service
public class PasswordUtils {
    private final MessageDigest md5;
    private final BASE64Encoder b64en;

    @Autowired
    public PasswordUtils(MessageDigest md5, BASE64Encoder b64en){
        this.md5 = md5;
        this.b64en = b64en;
    }

    public String getEncodedPassword(String password){
        return b64en.encode(md5.digest(password.getBytes()));
    }

    public boolean compare(String password,String passwordEncodedString){
        return passwordEncodedString.equals(getEncodedPassword(password));
    }


}
