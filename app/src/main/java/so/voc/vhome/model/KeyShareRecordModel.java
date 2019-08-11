package so.voc.vhome.model;

import java.util.List;

import so.voc.vhome.util.Util;

/**
 * Fun：
 *
 * @Author：Linus_Xie
 * @Date：2019/6/17 16:22
 */
public class KeyShareRecordModel {


    /**
     * pageNumber : 1
     * pageSize : 1
     * totalCount : 4
     * pageCount : 4
     * content : [{"id":102,"createDate":"2019-06-18 09:37:29","modifyDate":"2019-06-18 09:37:29","name":null,"sharerName":"187****7988","invalidDate":"2019-06-19 09:37:29","validateCode":"7f9db24117b84cef943009935243c77c","statusName":"未接收","confirmed":false,"refused":false}]
     */

    private int pageNumber;
    private int pageSize;
    private int totalCount;
    private int pageCount;
    private List<ContentBean> content;

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public List<ContentBean> getContent() {
        return content;
    }

    public void setContent(List<ContentBean> content) {
        this.content = content;
    }

    public static class ContentBean {
        /**
         * id : 102
         * createDate : 2019-06-18 09:37:29
         * modifyDate : 2019-06-18 09:37:29
         * name : null
         * sharerName : 187****7988
         * invalidDate : 2019-06-19 09:37:29
         * validateCode : 7f9db24117b84cef943009935243c77c
         * statusName : 未接收
         * confirmed : false
         * refused : false
         */

        private int id;
        private String createDate;
        private String modifyDate;
        private String name;
        private String sharerName;
        private String invalidDate;
        private String validateCode;
        private String statusName;
        private boolean confirmed;
        private boolean refused;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getModifyDate() {
            return modifyDate;
        }

        public void setModifyDate(String modifyDate) {
            this.modifyDate = modifyDate;
        }

        public String getName() {
            return Util.initNullStr(name).isEmpty() ? "" : name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSharerName() {
            return sharerName;
        }

        public void setSharerName(String sharerName) {
            this.sharerName = sharerName;
        }

        public String getInvalidDate() {
            return invalidDate;
        }

        public void setInvalidDate(String invalidDate) {
            this.invalidDate = invalidDate;
        }

        public String getValidateCode() {
            return validateCode;
        }

        public void setValidateCode(String validateCode) {
            this.validateCode = validateCode;
        }

        public String getStatusName() {
            return statusName;
        }

        public void setStatusName(String statusName) {
            this.statusName = statusName;
        }

        public boolean isConfirmed() {
            return confirmed;
        }

        public void setConfirmed(boolean confirmed) {
            this.confirmed = confirmed;
        }

        public boolean isRefused() {
            return refused;
        }

        public void setRefused(boolean refused) {
            this.refused = refused;
        }
    }
}
