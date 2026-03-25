package com.spring.cmd;

public class ParentRegisterCommand {

    private String parent_link_code;
    private String login_id;
    private String pwd;
    private String pwd_confirm;
    private String parent_name;
    private String phone;
    private String email;

    public String getParent_link_code() {
        return parent_link_code;
    }

    public void setParent_link_code(String parent_link_code) {
        this.parent_link_code = parent_link_code;
    }

    public String getLogin_id() {
        return login_id;
    }

    public void setLogin_id(String login_id) {
        this.login_id = login_id;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getPwd_confirm() {
        return pwd_confirm;
    }

    public void setPwd_confirm(String pwd_confirm) {
        this.pwd_confirm = pwd_confirm;
    }

    public String getParent_name() {
        return parent_name;
    }

    public void setParent_name(String parent_name) {
        this.parent_name = parent_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}