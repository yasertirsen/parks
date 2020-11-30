package com.example.parks.constant;

public class Authority {
    public static final String[] USER_AUTHORITIES = {"u:r"};
    public static final String[] DEVELOPER_AUTHORITIES = {"u:r", "u:u"};
    public static final String[] MANAGER_AUTHORITIES = {"u:r", "u:u"};
    public static final String[] ADMIN_AUTHORITIES = {"u:r", "u:c", "u:u"};
    public static final String[] SUPER_ADMIN_AUTHORITIES = {"u:r", "u:c", "u:u", "u:d"};
}
