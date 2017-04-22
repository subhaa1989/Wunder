package demo.wunder.org.Controller.Util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import demo.wunder.org.Controller.CarDetails;

/**
 * Created by subha on 4/22/2017.
 */

public class FileSharedPreference {
    public static final String PREFS_NAME = "WUNDER";
    public static final String FAVORITES = "CAR";

    public FileSharedPreference() {
        super();
    }
    public void storeJsonLocation(Context context, List<CarDetails> carList) {
// used for store arrayList in json format
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
        editor = settings.edit();
        Gson gson = new Gson();
        String jsonCarList = gson.toJson(carList);
        editor.putString(FAVORITES, jsonCarList);
        editor.commit();
    }
    public ArrayList loadJsonLocation(Context context) {
// used for retrieving arraylist from json formatted string
        SharedPreferences settings;
        List<CarDetails> favorites;
        settings = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
        if (settings.contains(FAVORITES)) {
            String jsonFavorites = settings.getString(FAVORITES, null);
            Gson gson = new Gson();
            CarDetails[] favoriteItems = gson.fromJson(jsonFavorites,CarDetails[].class);
            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList(favorites);
        } else
            return null;
        return (ArrayList) favorites;
    }
    public void addFavorite(Context context, CarDetails cdetails) {
        List favorites = loadJsonLocation(context);
        if (favorites == null)
            favorites = new ArrayList();
        favorites.add(cdetails);
        storeJsonLocation(context, favorites);
    }
    public void removeFavorite(Context context, CarDetails cdetails) {
        ArrayList favorites = loadJsonLocation(context);
        if (favorites != null) {
            favorites.remove(cdetails);
            storeJsonLocation(context, favorites);
        }
    }
}
