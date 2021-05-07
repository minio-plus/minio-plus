package org.quantum.minio.plus.web.controller;

import org.quantum.minio.plus.dto.DiskSpaceDTO;
import org.quantum.nucleus.component.dto.SingleResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 系统 控制器
 * @author ike
 * @date 2021 年 05 月 07 日 15:42
 */
@RequestMapping("/system")
public class SystemController {

    /**
     * 获取磁盘空间
     * @return
     */
    @GetMapping("/disk/space")
    public SingleResponse<DiskSpaceDTO> getDiskSpace(){
        return SingleResponse.buildSuccess();
    }
}
