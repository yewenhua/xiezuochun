package so.voc.vhome.model;

/**
 * Fun：
 *
 * @Author：Linus_Xie
 * @Date：2019/3/21 15:42
 */
public class OauthModel {


    /**
     * access_token : eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2luZm8iOnsiaWQiOjMsIm5hbWUiOiLmmKXlk6UiLCJoZWFkSWNvbiI6IjIwMTlfMDdfMTdfNzI3MjhjZWQtN2QzMi00ZmQ0LWIxMzUtYWIxMDJjOTY2NGZlLmpwZyIsIm1vYmlsZSI6IjE4NzU3NzA3OTg4IiwiZW1haWwiOm51bGwsInBhc3N3b3JkIjpudWxsLCJ0ZW1wb3JhcnlDb2RlIjpudWxsLCJsb2dpblR5cGUiOiJBUFBfVVNFUk5BTUVfUEFTU1dPUkQiLCJtZW1iZXJUeXBlIjoiUkVHSVNURVIifSwidXNlcl9uYW1lIjoiMTg3NTc3MDc5ODgiLCJzY29wZSI6WyJhbGwiXSwiZXhwIjoxNTYzMzkyMTc2LCJhdXRob3JpdGllcyI6WyJST0xFX01FTUJFUiJdLCJqdGkiOiJkZjk3MjUxYy1kNjNjLTQ3Y2MtYjdlOC03Mjc4NzdlZmQzMTQiLCJjbGllbnRfaWQiOiJjbGllbnRfYXBwIn0.skiYY2jOl7ARQ2Mku0QcTATAzcLOoUAzRQFpP09WE-8guPJpFtpD4xNLYl4LDJmCWZ3BE7tGTLvBH81kDbK0V9Byd_NCJig7GRci0puH7fCViGpMkiwR98ni7TG3Rv7UOAvRvsI54ul5tUOG9IBKC-GACQDelQWCzm9FyyHOdYoHs68JBwyK5y8FtSaXVJJP1B3hndwJ_X9Bnp3AS5joANzrs4PI7QwEZ1kizTyb-rlJHWglc3szcLheQToHinTzfe8HmO9UeVGAge45FO0apwIVtL5RAaShyyQTPGM5NPXZuX0LEzBspkTWdLWC6uAmpI6j32Lr6DEw3EN0zY661Q
     * token_type : bearer
     * refresh_token : eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2luZm8iOnsiaWQiOjMsIm5hbWUiOiLmmKXlk6UiLCJoZWFkSWNvbiI6IjIwMTlfMDdfMTdfNzI3MjhjZWQtN2QzMi00ZmQ0LWIxMzUtYWIxMDJjOTY2NGZlLmpwZyIsIm1vYmlsZSI6IjE4NzU3NzA3OTg4IiwiZW1haWwiOm51bGwsInBhc3N3b3JkIjpudWxsLCJ0ZW1wb3JhcnlDb2RlIjpudWxsLCJsb2dpblR5cGUiOiJBUFBfVVNFUk5BTUVfUEFTU1dPUkQiLCJtZW1iZXJUeXBlIjoiUkVHSVNURVIifSwidXNlcl9uYW1lIjoiMTg3NTc3MDc5ODgiLCJzY29wZSI6WyJhbGwiXSwiYXRpIjoiZGY5NzI1MWMtZDYzYy00N2NjLWI3ZTgtNzI3ODc3ZWZkMzE0IiwiZXhwIjoxNTY1OTQwOTc2LCJhdXRob3JpdGllcyI6WyJST0xFX01FTUJFUiJdLCJqdGkiOiI5M2FkMDg2Mi0yNTg0LTQ2YzktOTg5ZS02ZDBkYjUzZWI0NTYiLCJjbGllbnRfaWQiOiJjbGllbnRfYXBwIn0.ljheHkF3c6owzNmiqAE7seyaov-qQ_Yzea6579R2j31emsFbdrKJv7_WKw1XrMUdKt2my8avIVfH9VDrSx1_MvbcgwFE26x_v2R1drHqxSSA4cKw_tMvU3wzwUvQ4HV4mtxww27SpV1eP_g2-eh3XXZzWQIn4hc8ObbcAnUJ5ig4PGOuf6UKJl1m0BmM8WCsOHgkc7xOdSUEMNe-k71IUYa4tZKn1b6sLTcTADROQ1Jej2RwitdC1XxXF1CZv697Rv81X1_UsKhZz67o41i_URaCoSoFcYDOcFZ5MccOAFlq36HrBdx0JjvToG6Qgxu2F52ic0QaUFTLWBaAOdS28g
     * expires_in : 43199
     * scope : all
     * user_info : {"id":3,"name":"春哥","headIcon":"2019_07_17_72728ced-7d32-4fd4-b135-ab102c9664fe.jpg","mobile":"18757707988","email":null,"password":null,"temporaryCode":null,"loginType":"APP_USERNAME_PASSWORD","memberType":"REGISTER"}
     * jti : df97251c-d63c-47cc-b7e8-727877efd314
     */

    private String access_token;
    private String token_type;
    private String refresh_token;
    private int expires_in;
    private String scope;
    private UserInfoBean user_info;
    private String jti;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public UserInfoBean getUser_info() {
        return user_info;
    }

    public void setUser_info(UserInfoBean user_info) {
        this.user_info = user_info;
    }

    public String getJti() {
        return jti;
    }

    public void setJti(String jti) {
        this.jti = jti;
    }

    public static class UserInfoBean {
        /**
         * id : 3
         * name : 春哥
         * headIcon : 2019_07_17_72728ced-7d32-4fd4-b135-ab102c9664fe.jpg
         * mobile : 18757707988
         * email : null
         * password : null
         * temporaryCode : null
         * loginType : APP_USERNAME_PASSWORD
         * memberType : REGISTER
         */

        private int id;
        private String name;
        private String headIcon;
        private String mobile;
        private Object email;
        private Object password;
        private Object temporaryCode;
        private String loginType;
        private String memberType;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getHeadIcon() {
            return headIcon;
        }

        public void setHeadIcon(String headIcon) {
            this.headIcon = headIcon;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public Object getEmail() {
            return email;
        }

        public void setEmail(Object email) {
            this.email = email;
        }

        public Object getPassword() {
            return password;
        }

        public void setPassword(Object password) {
            this.password = password;
        }

        public Object getTemporaryCode() {
            return temporaryCode;
        }

        public void setTemporaryCode(Object temporaryCode) {
            this.temporaryCode = temporaryCode;
        }

        public String getLoginType() {
            return loginType;
        }

        public void setLoginType(String loginType) {
            this.loginType = loginType;
        }

        public String getMemberType() {
            return memberType;
        }

        public void setMemberType(String memberType) {
            this.memberType = memberType;
        }
    }
}
