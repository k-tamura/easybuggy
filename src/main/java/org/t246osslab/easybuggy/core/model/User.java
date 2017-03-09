package org.t246osslab.easybuggy.core.model;

import java.util.Date;

public class User {
    
    private String userId = null;
    private String name = null;
    private String password = null;
    private String secret = null;
    private int loginFailedCount = 0;
    private Date lastLoginFailedTime = null;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
    
    public int getLoginFailedCount() {
        return loginFailedCount;
    }

    public void setLoginFailedCount(int loginFailedCount) {
        this.loginFailedCount = loginFailedCount;
    }

    public Date getLastLoginFailedTime() {
        return lastLoginFailedTime;
    }

    public void setLastLoginFailedTime(Date lastLoginFailedTime) {
        this.lastLoginFailedTime = lastLoginFailedTime;
    }
}
