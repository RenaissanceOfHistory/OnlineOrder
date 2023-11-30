package free.lifetime.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import free.lifetime.model.User;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

/**
 * @create: 2023/11/12
 * @Description:
 * @FileName: JWTUtil
 */
public class JWTUtil {
    private static final String CLAIM_USER_ID = "userId";
    private static final String CLAIM_USERNAME = "username";
    private static final String CLAIM_USER_PASSWORD = "userPassword";

    private static final String SECRET = UUID.randomUUID().toString();

    private JWTUtil() {}

    public static String token(User user) {
        return JWT.create()
                .withClaim(CLAIM_USER_ID, user.getId())
                .withClaim(CLAIM_USERNAME, user.getUsername())
                .withClaim(CLAIM_USER_PASSWORD, user.getPassword())
                .withIssuedAt(new Date())   // 现在发布，30分钟后过期
                .withExpiresAt(Date.from(LocalDateTime.now().plusMinutes(30).atZone(ZoneId.systemDefault()).toInstant()))
                .sign(Algorithm.HMAC256(SECRET));
    }

    public static User getUser(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return User.builder()
                .id(jwt.getClaim(CLAIM_USER_ID).asLong())
                .username(jwt.getClaim(CLAIM_USERNAME).asString())
                .password(jwt.getClaim(CLAIM_USER_PASSWORD).asString())
                .build();
    }
}
