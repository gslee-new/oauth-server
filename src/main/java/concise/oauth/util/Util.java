package concise.oauth.util;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
public class Util {
    public static synchronized String convertToSHA256(String text) {
        StringBuffer sb = new StringBuffer();

        try {
            MessageDigest instance = MessageDigest.getInstance("SHA-256");
            instance.update(text.getBytes());
            byte[] digest = instance.digest();
            for(byte b : digest) {
                sb.append(String.format("%02x", b));
            }
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage());
        }

        return sb.toString();
    }

    public static synchronized <T,T2> T dataBinding(T t, T2 t2) {
        Objects.requireNonNull(t2);

        List<Field> fields = Arrays.asList(t2.getClass().getDeclaredFields());

        for (Field f: fields) {
            f.setAccessible(true);
            try {
                Field declaredField = t.getClass().getDeclaredField(f.getName());
                declaredField.setAccessible(true);
                declaredField.set(t, f.get(t2));
            } catch (NoSuchFieldException e) {
                log.debug("필드가 존재하지 않습니다.");
            } catch (IllegalAccessException e) {
                log.debug("필드에 접근할 수 없습니다.");
            }
        }

        return t;

    }
}
