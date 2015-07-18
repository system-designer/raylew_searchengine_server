package com.searchengine.domain.dic;

/**
 * Created by RayLew on 2015/5/4.
 * QQ:897929321
 */
public enum RoleType {
    ADMIN(1), USER(0);
    private int roleType;

    //构造枚举值
    private RoleType(int roleType) {
        this.roleType = roleType;
    }

    @Override
    public String toString() {
        return super.toString() + roleType;
    }
}
