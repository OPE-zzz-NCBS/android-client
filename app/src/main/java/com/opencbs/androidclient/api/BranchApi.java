package com.opencbs.androidclient.api;

import com.opencbs.androidclient.models.Branch;

import java.util.List;

import retrofit.http.GET;

public interface BranchApi {
    @GET("/api/branches")
    List<Branch> getAll();
}
