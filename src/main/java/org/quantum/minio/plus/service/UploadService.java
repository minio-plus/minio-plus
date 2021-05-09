package org.quantum.minio.plus.service;

import org.quantum.minio.plus.dto.ComposeUploadPartDTO;
import org.quantum.minio.plus.dto.UploadPartDTO;
import org.quantum.minio.plus.dto.query.PartQuery;

import java.util.List;

/**
 * @author jpx10
 */
public interface UploadService {

    /**
     * 获取上传部分列表
     * @param partQuery 部分查询
     * @return
     */
    List<UploadPartDTO> getPartList(PartQuery partQuery);

    /**
     * 合成上传部分
     * @param dto 传输对象
     * @return
     */
    String composePart(ComposeUploadPartDTO dto);
}
