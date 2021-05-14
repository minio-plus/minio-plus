package org.quantum.minio.plus.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.apache.commons.lang3.time.DateUtils;
import org.quantum.minio.plus.ValueResponse;
import org.quantum.minio.plus.dto.AuthDTO;
import org.quantum.minio.plus.dto.UserLoginDTO;
import org.quantum.minio.plus.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author ike
 * @date 2021 年 03 月 30 日 11:59
 */
@Service
public class UserServiceImpl implements UserService {

    @Override
    public ValueResponse<AuthDTO> login(UserLoginDTO dto) {
        Date date = new Date();
        int expiresDays = 7;
        Date expiresAt = DateUtils.addDays(date, expiresDays);

        String accessToken = JWT.create()
                .withExpiresAt(expiresAt)
                .withIssuedAt(date)
                .withJWTId("1")
                .sign(Algorithm.HMAC256(dto.getPassword()));

        AuthDTO authDto = new AuthDTO();
        authDto.setAccessToken(accessToken);
        authDto.setExpiresAt(expiresAt.getTime());
        return ValueResponse.of(authDto);
    }
}
