package com.example.saaq_automobile.model;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.saaq_automobile.R;
import com.example.saaq_automobile.helpers.AppConstants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class vehicleFragment extends Fragment implements AdapterView.OnItemSelectedListener, ValueEventListener, View.OnClickListener {

    Spinner vehicleSpinner;
    RadioGroup rgBought;
    RadioButton rbIndividual,rbBusiness;
    Button btnAppointment;

    String userId;
    String selectedVehicle;

    DatabaseReference vehicleDatabase,vehicleChild,userChild;
    String[] vehiclesDatabase;
    ArrayList vehicleDataList;
    ArrayAdapter vehicleDataAdapter;
    User user_Details;

    //Fetching current user details
    FirebaseAuth mFirebaseAuthVehicleFragment;
    FirebaseUser mFirebaseUserVehicleFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_vehicle, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();
    }

    private void initialize() {
        vehicleSpinner = getActivity().findViewById(R.id.spinner);
        vehicleSpinner.setOnItemSelectedListener(this);

        rgBought = getActivity().findViewById(R.id.rgBoughtVehicle);
        rgBought.setOnClickListener(this);

        rbIndividual = getActivity().findViewById(R.id.rbIndividual);
        rbIndividual.setOnClickListener(this);

        rbBusiness = getActivity().findViewById(R.id.rbBusiness);
        rbIndividual.setOnClickListener(this);
        btnAppointment = getActivity().findViewById(R.id.btnAppointment);
        btnAppointment.setOnClickListener(this);

        mFirebaseAuthVehicleFragment = FirebaseAuth.getInstance();
        mFirebaseUserVehicleFragment = mFirebaseAuthVehicleFragment.getCurrentUser();
        userId = mFirebaseUserVehicleFragment.getUid();


        //Creating Array adapter instance having vehicle list
        vehicleDataList = new ArrayList<>();
        vehicleDataAdapter= new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,vehicleDataList);
        //Setting the Array adapter data on the spinner
        vehicleSpinner.setAdapter(vehicleDataAdapter);
        retrieveData();
    }


    private void retrieveData() {
        vehicleDatabase = FirebaseDatabase.getInstance().getReference().child(AppConstants.firebaseVehicleReference );
        vehicleDatabase.addValueEventListener(this);

        userChild = FirebaseDatabase.getInstance().getReference(AppConstants.firebaseUsersReference).child(userId);
        userChild.addValueEventListener(this);

        Toast.makeText(getActivity(),"Appointment Booked Successfully !!",Toast.LENGTH_LONG).show();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        selectedVehicle = parent.getItemAtPosition(position).toString();
        if(selectedVehicle.equals("Automobile"))
        {
            Log.e("vehicleFragment.java", "Booking appointment for Automobile");
        }
        else if(selectedVehicle.equals( "Motorcycle"))
        {
            Log.e("vehicleFragment.java", "Booking appointment for Motorcycle");
        }
        else
        {
            Log.e("vehicleFragment.java", "Booking appointment for Bus or Minibus");
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        vehicleDataList.clear();
        for (DataSnapshot item : snapshot.getChildren()){
            String data = item.getKey();
            vehicleDataAdapter.add(data);
        }
        vehicleDataAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        throw error.toException();

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id)
        {
            case R.id.btnAppointment: bookAnAppointment();
        }
    }

    private void bookAnAppointment() {

        if(selectedVehicle.equals("Automobile"))
        {
            Automobile a = new Automobile();
        }
    }
}
