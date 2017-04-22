package demo.wunder.org.WunderOnlineTestDemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import demo.wunder.org.Controller.CarDetails;
import demo.wunder.org.Controller.Util.FileSharedPreference;
import demo.wunder.org.View.CarRowAdapter;
import demo.wunder.org.View.DividerItemDecoration;


/**
 * Created by subha on 4/19/2017.
 */

public class CarListFragment extends Fragment {

    private List<CarDetails> carList =  new ArrayList<CarDetails>();
    private RecyclerView recyclerView;
    private CarRowAdapter mAdapter;
    FileSharedPreference fsp = new FileSharedPreference();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.carlist, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        prepareCarData();
        mAdapter = new CarRowAdapter(carList);
        recyclerView.swapAdapter(mAdapter,false);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        prepareCarData();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void prepareCarData()
    {
        //TO be replace with sharedpreference
        carList = fsp.loadJsonLocation(getActivity().getApplicationContext());
    }


}
