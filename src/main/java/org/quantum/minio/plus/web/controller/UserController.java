package org.quantum.minio.plus.web.controller;

import org.quantum.minio.plus.Response;
import org.quantum.minio.plus.ValueResponse;
import org.quantum.minio.plus.web.vo.UserPublicInfoVO;
import org.quantum.minio.plus.dto.AuthDTO;
import org.quantum.minio.plus.dto.UserLoginDTO;
import org.quantum.minio.plus.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 * @author ike
 * @date 2021 年 03 月 30 日 11:47
 */
@RequestMapping("/user")
@RestController
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ValueResponse<AuthDTO> login(@RequestBody UserLoginDTO dto) {
        return userService.login(dto);
    }

    @GetMapping("/current")
    public ValueResponse<UserPublicInfoVO> getCurrentInfo() {
        UserPublicInfoVO vo = new UserPublicInfoVO();
        vo.setUsername("admin");
        vo.setAvatar("https://i.gtimg.cn/club/item/face/img/2/15922_100.gif");
        vo.setPermissions(new String[]{"admin", "editor"});
        return ValueResponse.of(vo);
    }

    @GetMapping("/logout")
    public Response logout(){
        return Response.ok();
    }
}
