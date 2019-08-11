package so.voc.vhome.model;

import java.util.List;

/**
 * Fun：
 *
 * @Author：Linus_Xie
 * @Date：2019/6/20 15:40
 */
public class NotifyDetailModel {


    /**
     * pageNumber : 1
     * pageSize : 20
     * totalCount : 11
     * pageCount : 1
     * content : [{"id":20,"createDate":"2019-06-27 14:27:03","modifyDate":"2019-06-27 14:27:03","content":"来自叶文华的设备分享","hasRead":true,"notificationType":"SHARE","notificationTypeName":"分享通知","interactionType":"DEVICE_SHARE","interactionTypeName":"设备分享","deviceShareView":{"id":66,"alias":null,"deviceShareWay":"APP","deviceId":1,"deviceName":"明月松间照","sharerId":2,"sharerName":"叶*华","receiverId":3,"receiverName":"*哥","identityCode":null,"deviceMemberDateConstraintType":"CONSTRAINT","beginDate":"2019-06-27 14:26:00","endDate":"2019-06-27 15:26:00","maxOpenTimes":null,"confirmed":false,"refused":false,"expired":false}}]
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
         * id : 20
         * createDate : 2019-06-27 14:27:03
         * modifyDate : 2019-06-27 14:27:03
         * content : 来自叶文华的设备分享
         * hasRead : true
         * notificationType : SHARE
         * notificationTypeName : 分享通知
         * interactionType : DEVICE_SHARE
         * interactionTypeName : 设备分享
         * deviceShareView : {"id":66,"alias":null,"deviceShareWay":"APP","deviceId":1,"deviceName":"明月松间照","sharerId":2,"sharerName":"叶*华","receiverId":3,"receiverName":"*哥","identityCode":null,"deviceMemberDateConstraintType":"CONSTRAINT","beginDate":"2019-06-27 14:26:00","endDate":"2019-06-27 15:26:00","maxOpenTimes":null,"confirmed":false,"refused":false,"expired":false}
         */

        private int id;
        private String createDate;
        private String modifyDate;
        private String content;
        private boolean hasRead;
        private String notificationType;
        private String notificationTypeName;
        private String interactionType;
        private String interactionTypeName;
        private DeviceShareViewBean deviceShareView;

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

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public boolean isHasRead() {
            return hasRead;
        }

        public void setHasRead(boolean hasRead) {
            this.hasRead = hasRead;
        }

        public String getNotificationType() {
            return notificationType;
        }

        public void setNotificationType(String notificationType) {
            this.notificationType = notificationType;
        }

        public String getNotificationTypeName() {
            return notificationTypeName;
        }

        public void setNotificationTypeName(String notificationTypeName) {
            this.notificationTypeName = notificationTypeName;
        }

        public String getInteractionType() {
            return interactionType;
        }

        public void setInteractionType(String interactionType) {
            this.interactionType = interactionType;
        }

        public String getInteractionTypeName() {
            return interactionTypeName;
        }

        public void setInteractionTypeName(String interactionTypeName) {
            this.interactionTypeName = interactionTypeName;
        }

        public DeviceShareViewBean getDeviceShareView() {
            return deviceShareView;
        }

        public void setDeviceShareView(DeviceShareViewBean deviceShareView) {
            this.deviceShareView = deviceShareView;
        }

        public static class DeviceShareViewBean {
            /**
             * id : 66
             * alias : null
             * deviceShareWay : APP
             * deviceId : 1
             * deviceName : 明月松间照
             * sharerId : 2
             * sharerName : 叶*华
             * receiverId : 3
             * receiverName : *哥
             * identityCode : null
             * deviceMemberDateConstraintType : CONSTRAINT
             * beginDate : 2019-06-27 14:26:00
             * endDate : 2019-06-27 15:26:00
             * maxOpenTimes : null
             * confirmed : false
             * refused : false
             * expired : false
             */

            private int id;
            private String alias;
            private String deviceShareWay;
            private int deviceId;
            private String deviceName;
            private int sharerId;
            private String sharerName;
            private int receiverId;
            private String receiverName;
            private Object identityCode;
            private String deviceMemberDateConstraintType;
            private String beginDate;
            private String endDate;
            private String maxOpenTimes;
            private boolean confirmed;
            private boolean refused;
            private boolean expired;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getAlias() {
                return alias;
            }

            public void setAlias(String alias) {
                this.alias = alias;
            }

            public String getDeviceShareWay() {
                return deviceShareWay;
            }

            public void setDeviceShareWay(String deviceShareWay) {
                this.deviceShareWay = deviceShareWay;
            }

            public int getDeviceId() {
                return deviceId;
            }

            public void setDeviceId(int deviceId) {
                this.deviceId = deviceId;
            }

            public String getDeviceName() {
                return deviceName;
            }

            public void setDeviceName(String deviceName) {
                this.deviceName = deviceName;
            }

            public int getSharerId() {
                return sharerId;
            }

            public void setSharerId(int sharerId) {
                this.sharerId = sharerId;
            }

            public String getSharerName() {
                return sharerName;
            }

            public void setSharerName(String sharerName) {
                this.sharerName = sharerName;
            }

            public int getReceiverId() {
                return receiverId;
            }

            public void setReceiverId(int receiverId) {
                this.receiverId = receiverId;
            }

            public String getReceiverName() {
                return receiverName;
            }

            public void setReceiverName(String receiverName) {
                this.receiverName = receiverName;
            }

            public Object getIdentityCode() {
                return identityCode;
            }

            public void setIdentityCode(Object identityCode) {
                this.identityCode = identityCode;
            }

            public String getDeviceMemberDateConstraintType() {
                return deviceMemberDateConstraintType;
            }

            public void setDeviceMemberDateConstraintType(String deviceMemberDateConstraintType) {
                this.deviceMemberDateConstraintType = deviceMemberDateConstraintType;
            }

            public String getBeginDate() {
                return beginDate;
            }

            public void setBeginDate(String beginDate) {
                this.beginDate = beginDate;
            }

            public String getEndDate() {
                return endDate;
            }

            public void setEndDate(String endDate) {
                this.endDate = endDate;
            }

            public String getMaxOpenTimes() {
                return maxOpenTimes;
            }

            public void setMaxOpenTimes(String maxOpenTimes) {
                this.maxOpenTimes = maxOpenTimes;
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

            public boolean isExpired() {
                return expired;
            }

            public void setExpired(boolean expired) {
                this.expired = expired;
            }
        }
    }
}
