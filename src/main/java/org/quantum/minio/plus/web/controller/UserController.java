package org.quantum.minio.plus.web.controller;

import org.quantum.minio.plus.web.vo.UserPublicInfoVO;
import org.quantum.minio.plus.dto.AuthDTO;
import org.quantum.minio.plus.dto.UserLoginDTO;
import org.quantum.minio.plus.service.UserService;
import org.quantum.nucleus.component.dto.SingleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author ike
 * @date 2021 年 03 月 30 日 11:47
 */
@RequestMapping("/user")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public SingleResponse<AuthDTO> login(@RequestBody UserLoginDTO dto) {
        AuthDTO authDto = userService.login(dto);
        return SingleResponse.of(authDto);
    }

    @GetMapping("/current")
    public SingleResponse<UserPublicInfoVO> getCurrentInfo() {
        UserPublicInfoVO vo = new UserPublicInfoVO();
        vo.setUsername("admin");
        vo.setAvatar("https://i.gtimg.cn/club/item/face/img/2/15922_100.gif");
        vo.setPermissions(new String[]{"admin", "editor"});
        return SingleResponse.of(vo);
    }

    @GetMapping("/logout")
    public SingleResponse logout(){
        return SingleResponse.buildSuccess();
    }
}
