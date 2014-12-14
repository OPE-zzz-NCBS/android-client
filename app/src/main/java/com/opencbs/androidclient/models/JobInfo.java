package com.opencbs.androidclient.models;

import java.util.Date;

public class JobInfo {
    public static final int STATUS_PENDING = 0;
    public static final int STATUS_DONE = 1;
    public static final int STATUS_FAILED = 2;

    public String uuid;
    public String jobType;
    public String extra;
    public Date createdAt;
    public String description;
    public int status;
}
