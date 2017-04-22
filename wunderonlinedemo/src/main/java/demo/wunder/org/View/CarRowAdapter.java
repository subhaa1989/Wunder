package demo.wunder.org.View;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import demo.wunder.org.Controller.CarDetails;
import demo.wunder.org.WunderOnlineTestDemo.R;

/**
 * Created by subha on 4/17/2017.
 */

public class CarRowAdapter extends RecyclerView.Adapter<CarRowAdapter.MyViewHolder>{

    private List<CarDetails> carDetailsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView address, name;

        public MyViewHolder(View view) {
            super(view);
            address = (TextView) view.findViewById(R.id.address);
            name = (TextView) view.findViewById(R.id.name);

        }
    }


    public CarRowAdapter(List<CarDetails> carDetailsList) {
        this.carDetailsList = carDetailsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.carlist_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CarDetails car = carDetailsList.get(position);
        holder.address.setText(car.getAddress());
        holder.name.setText(car.getName());
    }

    @Override
    public int getItemCount() {
        return carDetailsList.size();
    }
}
