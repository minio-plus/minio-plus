package org.quantum.minio.plus.dto;

/**
 * @author ike
 * @date 2021 年 05 月 07 日 11:12
 */
public class UploadPartDTO {

    private String eTag;

    private Integer partNumber;

    private Long Size;

    public String getETag() {
        return eTag;
    }

    public void setETag(String eTag) {
        this.eTag = eTag;
    }

    public Integer getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(Integer partNumber) {
        this.partNumber = partNumber;
    }

    public Long getSize() {
        return Size;
    }

    public void setSize(Long size) {
        Size = size;
    }
}
