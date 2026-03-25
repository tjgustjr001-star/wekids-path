package com.spring.dto;

import java.util.Date;
import java.util.List;

public class MemberVO {

    private int member_id;
    private String login_id;
    private String pwd;
    private String email;
    private String phone;
    private String role_code;
    private String role_name;
    private String account_status;
    private int login_fail_count;
    private Date last_login_at;
    private Date locked_at;
    private Date pwd_changed_at;
    private Date created_at;
    private Date updated_at;

    private List<AuthorityVO> authorities;

    // profile fields
    private String name;
    private Date birth;
    private String gender;
    private String intro;
    private String profile_image;

    public int getMember_id() {
        return member_id;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole_code() {
        return role_code;
    }

    public void setRole_code(String role_code) {
        this.role_code = role_code;
    }

    public String getRole_name() {
        return role_name;
    }

    public void setRole_name(String role_name) {
        this.role_name = role_name;
    }

    public String getAccount_status() {
        return account_status;
    }

    public void setAccount_status(String account_status) {
        this.account_status = account_status;
    }

    public int getLogin_fail_count() {
        return login_fail_count;
    }

    public void setLogin_fail_count(int login_fail_count) {
        this.login_fail_count = login_fail_count;
    }

    public Date getLast_login_at() {
        return last_login_at;
    }

    public void setLast_login_at(Date last_login_at) {
        this.last_login_at = last_login_at;
    }

    public Date getLocked_at() {
        return locked_at;
    }

    public void setLocked_at(Date locked_at) {
        this.locked_at = locked_at;
    }

    public Date getPwd_changed_at() {
        return pwd_changed_at;
    }

    public void setPwd_changed_at(Date pwd_changed_at) {
        this.pwd_changed_at = pwd_changed_at;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public List<AuthorityVO> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<AuthorityVO> authorities) {
        this.authorities = authorities;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }
}
