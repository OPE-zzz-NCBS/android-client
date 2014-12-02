package com.opencbs.androidclient.api.response;

import com.opencbs.androidclient.model.Branch;
import com.opencbs.androidclient.model.City;
import com.opencbs.androidclient.model.District;
import com.opencbs.androidclient.model.EconomicActivity;
import com.opencbs.androidclient.model.Region;

import java.util.List;

public class LookupDataResponse {
    public EconomicActivity[] economicActivities;
    public List<Branch> branches;
    public List<City> cities;
    public List<District> districts;
    public List<Region> regions;
}
