package com.example.tracksystem.tracksystem;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.gms.auth.api.Auth;

import java.util.Locale;
import java.util.StringTokenizer;

public class TimeTable_Options extends Fragment {

    Spinner from_statin, to_station, train_class;

    Button bu_timeTable;
    AlertDialog.Builder builder;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_time_table__options, null);

        from_statin = (Spinner) view.findViewById(R.id.sp_from);
        to_station = (Spinner) view.findViewById(R.id.sp_to);
        train_class = (Spinner) view.findViewById(R.id.sp_class);

        builder = new AlertDialog.Builder(getActivity());


        ArrayAdapter<CharSequence> stations = ArrayAdapter.createFromResource(getActivity(), R.array.spinnerItems, android.R.layout.simple_spinner_item);
        stations.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        from_statin.setAdapter(stations);
        to_station.setAdapter(stations);
        ArrayAdapter<CharSequence> triancalss = ArrayAdapter.createFromResource(getActivity(), R.array.classSpinn, android.R.layout.simple_spinner_item);
        triancalss.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        train_class.setAdapter(triancalss);

        bu_timeTable = (Button) view.findViewById(R.id.Schedule);


        bu_timeTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String arrival_st = from_statin.getSelectedItem().toString();
                String dep_st = to_station.getSelectedItem().toString();
                String train_calss = train_class.getSelectedItem().toString();


                StringTokenizer st = new StringTokenizer(arrival_st, "(");

                String arrivale_station = st.nextToken();


                StringTokenizer st2 = new StringTokenizer(dep_st, "(");

                String Departure_Station = st2.nextToken();

                if (arrivale_station.equals(Departure_Station)) {
                    builder.setMessage("you entered the same station in from and to");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("arrival_station", arrivale_station);
                    bundle.putString("Departure_Station", Departure_Station);
                    bundle.putString("train_class", train_calss);

                    Fragment fragment = new Time_table();
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction ft = fragmentManager.beginTransaction();
                    ft.replace(R.id.screen_area, fragment);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            }
        });


        return view;
    }


}
