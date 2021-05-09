package org.quantum.minio.plus.web.controller;

import cn.hutool.system.oshi.OshiUtil;
import org.quantum.nucleus.component.dto.SingleResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import oshi.hardware.HWDiskStore;

import java.io.File;
import java.util.List;

/**
 * 系统 控制器
 * @author ike
 * @date 2021 年 05 月 07 日 15:42
 */
@RequestMapping("/system")
@RestController
public class SystemController {

    /**
     * 获取磁盘空间
     * @return
     */
    @GetMapping("/disk/space")
    public SingleResponse getDiskSpace() {
        File[] files = File.listRoots();
        List<HWDiskStore> hwDiskStores = OshiUtil.getDiskStores();

        return SingleResponse.of(files[0].getFreeSpace());
    }
}
