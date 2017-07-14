package com.seatgeek.placesautocompletedemo.network;

import com.seatgeek.placesautocompletedemo.model.Message;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Ravi Tamada on 21/02/17.
 * www.androidhive.info
 */

public interface ApiInterface {
    @GET("all_comment")
    Call<List<Message>> getInbox();
}
