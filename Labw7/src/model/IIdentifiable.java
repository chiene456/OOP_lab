/****************/
/*
 * Mã sinh viên: 202418853
 * Họ tên: Nguyễn Quang Chiến
 */
/****************/
package model;

/**
 * Interface dành cho các đối tượng có mã định danh.
 */
public interface IIdentifiable {

    /**
     * Lấy mã định danh của đối tượng.
     *
     * @return mã định danh
     */
    String getId();
}