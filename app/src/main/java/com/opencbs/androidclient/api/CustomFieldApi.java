package com.opencbs.androidclient.api;

import com.opencbs.androidclient.models.CustomField;

import java.util.List;

import retrofit.http.GET;

public interface CustomFieldApi {
    @GET("/api/custom-fields")
    List<CustomField> getAll();
}
