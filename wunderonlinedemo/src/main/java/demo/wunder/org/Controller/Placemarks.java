package demo.wunder.org.Controller;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by subha on 4/21/2017.
 */

public class Placemarks {

    public List<CarDetails> getCarDetailsList() {
        return carDetailsList;
    }

    public void setCarDetailsList(List<CarDetails> carDetailsList) {
        this.carDetailsList = carDetailsList;
    }

    @SerializedName("placemarks")
        private List<CarDetails> carDetailsList;
        // ...
    }


