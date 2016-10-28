package cn.com.mod.office.lightman.api.resp;

import java.io.Serializable;

import cn.com.mod.office.lightman.api.BaseResp;

/**
 * Created by Administrator on 2016/10/15.
 */
public class LoginResp extends BaseResp {
    private Session session;
    private Operator operator;

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public static class Session implements Serializable{

        /**
         * session_id : 866e7240ca0925b4e06a62601da875ff52757da5
         * operator_id : 4
         */

        private String session_id;
        private String operator_id;

        public String getSession_id() {
            return session_id;
        }

        public void setSession_id(String session_id) {
            this.session_id = session_id;
        }

        public String getOperator_id() {
            return operator_id;
        }

        public void setOperator_id(String operator_id) {
            this.operator_id = operator_id;
        }
    }
    public static class Operator{

        /**
         * operator_id : 4
         * operator_name : test
         * org_id : 0001002000000000000
         * phone_no : 1333333334
         * email : 133@122.com
         */

        private String operator_id;
        private String operator_name;
        private String org_id;
        private String phone_no;
        private String email;

        public String getOperator_id() {
            return operator_id;
        }

        public void setOperator_id(String operator_id) {
            this.operator_id = operator_id;
        }

        public String getOperator_name() {
            return operator_name;
        }

        public void setOperator_name(String operator_name) {
            this.operator_name = operator_name;
        }

        public String getOrg_id() {
            return org_id;
        }

        public void setOrg_id(String org_id) {
            this.org_id = org_id;
        }

        public String getPhone_no() {
            return phone_no;
        }

        public void setPhone_no(String phone_no) {
            this.phone_no = phone_no;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
