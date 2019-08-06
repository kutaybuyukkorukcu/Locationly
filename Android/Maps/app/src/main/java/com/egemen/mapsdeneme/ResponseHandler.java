package com.egemen.mapsdeneme;

import retrofit2.Call;

public interface ResponseHandler {
    void onSucces(ResponseBody data, int statusCode, String message );
    void onFail(Call<ResponseBody> call, Throwable t);
}
