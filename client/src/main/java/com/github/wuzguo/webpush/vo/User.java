
package com.github.wuzguo.webpush.vo;


import com.github.wuzguo.webpush.common.annotation.Comment;

import java.io.Serializable;
import java.util.Arrays;

/**
 * TODO 前端用户对象
 *
 * @author wuzguo at 2017/5/11 10:57
 */
public class User implements Serializable {
    /**
     * 用户id
     **/
    @Comment(value = "用户id", required = true)
    private String uid;
    /**
     * 用户名称
     **/
    @Comment(value = "用户名称")
    private String name;
    /**
     * 角色ids
     **/
    @Comment(value = "用户角色集合")
    private String[] roles;
    /**
     * 分组ids
     **/
    @Comment(value = "用户群组集合")
    private String[] groups;

    public User() {
    }

    public User(String uid, String name) {
        this.uid = uid;
        this.name = name;
    }

    public User(String uid, String name, String[] roles, String[] groups) {
        this.uid = uid;
        this.name = name;
        this.roles = roles;
        this.groups = groups;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getRoles() {
        return roles;
    }

    public void setRoles(String[] roles) {
        this.roles = roles;
    }

    public String[] getGroups() {
        return groups;
    }

    public void setGroups(String[] groups) {
        this.groups = groups;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", roles=" + Arrays.toString(roles) +
                ", groups=" + Arrays.toString(groups) +
                '}';
    }
}
