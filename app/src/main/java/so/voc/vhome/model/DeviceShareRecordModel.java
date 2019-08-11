package so.voc.vhome.model;

import java.util.List;

/**
 * Fun：
 *
 * @Author：Linus_Xie
 * @Date：2019/6/5 14:04
 */
public class DeviceShareRecordModel {


    /**
     * pageNumber : 1
     * pageSize : 20
     * totalCount : 1
     * pageCount : 1
     * content : [{"id":26,"createDate":"2019-06-14 09:34:23","modifyDate":"2019-06-14 09:34:23","alias":null,"statusName":"未接收"}]
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
         * id : 26
         * createDate : 2019-06-14 09:34:23
         * modifyDate : 2019-06-14 09:34:23
         * alias : null
         * statusName : 未接收
         */

        private int id;
        private String createDate;
        private String modifyDate;
        private Object alias;
        private String statusName;

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

        public Object getAlias() {
            return alias;
        }

        public void setAlias(Object alias) {
            this.alias = alias;
        }

        public String getStatusName() {
            return statusName;
        }

        public void setStatusName(String statusName) {
            this.statusName = statusName;
        }

        @Override
        public String toString() {
            return "ContentBean{" +
                    "id=" + id +
                    ", createDate='" + createDate + '\'' +
                    ", modifyDate='" + modifyDate + '\'' +
                    ", alias=" + alias +
                    ", statusName='" + statusName + '\'' +
                    '}';
        }
    }
}
