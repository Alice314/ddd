package utils;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import org.apache.commons.lang3.RandomStringUtils;

public class PasswordUtil {
    public static String getSalt(){
        return RandomStringUtils.randomAlphabetic(64);
    }
    public static String sha256(String salt,String pwdPlain){
        return Hashing.sha256().newHasher().putString(salt + pwdPlain, Charsets.UTF_8).hash().toString();
    }
}
