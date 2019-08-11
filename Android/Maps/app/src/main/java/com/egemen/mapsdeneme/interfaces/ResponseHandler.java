package com.egemen.mapsdeneme.interfaces;

import com.egemen.mapsdeneme.model.ResponseBody;

import retrofit2.Call;

public interface ResponseHandler {
    void onSuccess(ResponseBody data, int statusCode, String message );
    void onFail(Call<ResponseBody> call);
}

