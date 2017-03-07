package org.t246osslab.easybuggy.utils;

import java.util.Date;

public class Administrator {
    
    private String userId = null;

    private int loginFailedCount = 0;

    private Date lastLoginFailedTime = null;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
