package demo.wunder.org.WunderOnlineTestDemo;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import demo.wunder.org.Controller.CarDetails;
import demo.wunder.org.Controller.JsonFeedAPI;
import demo.wunder.org.Controller.Placemarks;
import demo.wunder.org.Controller.Util.FileSharedPreference;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {

     List<CarDetails> cd = new ArrayList<CarDetails>();
    FileSharedPreference fsp = new FileSharedPreference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(isOnline() == false)
        {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG)
                    .show();
            finish();
        }

        cd = fsp.loadJsonLocation(getApplicationContext());
        if(cd == null || cd.size() == 0)
        {
            //load data
            getJsonData("locations.json");

        }
        else
            initiateUI();

    }


    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    public void getJsonData(String data)

    {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(JsonFeedAPI.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        JsonFeedAPI jsonfeed = retrofit.create(JsonFeedAPI.class);
        jsonfeed.loadData().enqueue(new Callback<Placemarks>() {
            @Override
            public void onResponse(Call<Placemarks> call, Response<Placemarks> response) {
                if (response.isSuccessful()) {
                    Placemarks changesList = response.body();
                    cd = changesList.getCarDetailsList();
                    fsp.storeJsonLocation(getApplicationContext(),cd);
                    initiateUI();

                } else {
                    Log.d("WUNDER err",response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<Placemarks> call, Throwable t) {
                Log.d("WUNDER err",t.getMessage());
            }
        });

    }

    private void initiateUI() {

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Car Details"));
        tabLayout.addTab(tabLayout.newTab().setText("Wunder"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PageAdapter adapter = new PageAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }
}
