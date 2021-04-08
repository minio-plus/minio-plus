package org.quantum.minio.plus;

/**
 * 存储类
 * @author jpx10
 */
public enum StorageClass {

    STANDARD("STANDARD"),
    GLACIER("GLACIER");

    private final String value;

    StorageClass(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
