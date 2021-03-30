package org.quantum.minio.plus.service;

import org.quantum.minio.plus.dto.AuthDTO;
import org.quantum.minio.plus.dto.UserLoginDTO;

/**
 * 用户服务
 * @author jpx10
 */
public interface UserService {

     /**
      * 登录
      * @param dto 传输对象
      * @return
      */
     AuthDTO login(UserLoginDTO dto);
}
