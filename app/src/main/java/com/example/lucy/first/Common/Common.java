package com.example.lucy.first.Common;

import com.example.lucy.first.Remote.IGoogleAPI;
import com.example.lucy.first.Remote.RetrofitClient;

public class Common {
    public  static final String driver_tbl = "Drivers";
    public  static final String user_driver_tbl = "DriversInformation";
    public  static final String user_client_tbl = "ClientsInformation";
    public  static final String pickup_request = "Order";

    public static final String baseUrl = "https://maps.googleapis.com";
    public static IGoogleAPI getGoogleAPI()
    {
        return RetrofitClient.getClient(baseUrl).create(IGoogleAPI.class);
    }
}
