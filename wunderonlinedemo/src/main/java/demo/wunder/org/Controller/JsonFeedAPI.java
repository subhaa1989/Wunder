package demo.wunder.org.Controller;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Query;

/**
 * Created by subha on 4/20/2017.
 */

public interface JsonFeedAPI {
    String ENDPOINT = "https://s3-us-west-2.amazonaws.com/wunderbucket/";

    @GET("locations.json")
    Call<Placemarks> loadData();
}

