package com.example.saaq_automobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import com.google.android.gms.maps.GoogleMap;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.saaq_automobile.helpers.AppConstants;
import com.example.saaq_automobile.model.User;
import com.example.saaq_automobile.model.dashboardFragment;
import com.example.saaq_automobile.model.mailFragment;
import com.example.saaq_automobile.model.vehicleFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;

import com.example.saaq_automobile.model.profileFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ValueEventListener {

    private DrawerLayout drawer;
    String unm,mail;
    //textview for storing name and mail of the specific user
    TextView userName,mailAddress;
    FirebaseAuth mFirebaseAuth;
    DatabaseReference userChild;

    //Using Map view for locating office
    MapView mapview;

    //Storing package name and class of other application
    String package_name = "com.example.roadsafety";
    String class_name = "com.example.roadsafety.MainActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        initialize();
    }

    private void initialize()
    {
        mFirebaseAuth = FirebaseAuth.getInstance();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Using Navigation view for fetching current user and mail address
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        userName =  navigationView.getHeaderView(0).findViewById(R.id.nav_username);
        mailAddress = navigationView.getHeaderView(0).findViewById(R.id.nav_userMail);
        drawer = findViewById(R.id.drawer_layout);

        //Menu Button in navigation drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();
        findViewById(R.id.nav_username);


        getUserDetailsFromFirebase();

    }


    private void getUserDetailsFromFirebase() {

        if (mFirebaseAuth.getCurrentUser() != null) {
            Log.e("Dashboard.java", mFirebaseAuth.getCurrentUser().getEmail());
        } else {
            Log.e("Dashboard.java", "Firebase user null");
        }

        String userId = mFirebaseAuth.getCurrentUser().getUid();
        userChild = FirebaseDatabase.getInstance().getReference(AppConstants.firebaseUsersReference).child(userId);
        userChild.addValueEventListener(this);
        // firebase database call -> userId -> to retreive user object
    }

    @Override
    public void onBackPressed() {

        if(drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new dashboardFragment()).commit();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new profileFragment()).commit();
                break;
            case R.id.nav_driving:
                launchDriving();
                break;
            case R.id.nav_vehicle_registration:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new vehicleFragment()).commit();
                break;
            case R.id.nav_road_safety:
                launchRoadSafety();
                break;
            case R.id.nav_mail:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new mailFragment()).commit();
                break;
            case R.id.nav_call:
                Intent iCall = new Intent(Intent.ACTION_CALL);
                iCall.setData(Uri.parse("tel:1-855-564-3170"));
                startActivity(iCall);
                break;
            case R.id.nav_logout:
                logoutProfile();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void launchDriving() {

        String dpname="com.example.saaq";
        String dcname="com.example.saaq.MainActivity";
        Intent inDriving = new Intent();
        inDriving.setAction(Intent.ACTION_MAIN);
        inDriving.addCategory(Intent.CATEGORY_LAUNCHER);
        inDriving.setComponent(new ComponentName(dpname,dcname));
        try {
            startActivity(inDriving);
        }catch (Exception e)
        {
            Log.e("Dashboard.java", " Unable to start Driving Fragment");
        }
    }

    private void logoutProfile() {
        AlertDialog.Builder logout = new AlertDialog.Builder(Dashboard.this);
        logout.setTitle("Logout").setMessage("Are you sure you want to Logout ?");

        logout.setPositiveButton("Affirmative", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);
            }
        });

        logout.setNegativeButton("Negative", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert11 = logout.create();
        alert11.show();
    }

    private void launchRoadSafety() {
        Intent inRoadSafety = new Intent();
        inRoadSafety.setAction(Intent.ACTION_MAIN);
        inRoadSafety.addCategory(Intent.CATEGORY_LAUNCHER);
        inRoadSafety.setComponent(new ComponentName(package_name,class_name));
        try {
            startActivity(inRoadSafety);
        }catch (Exception e)
        {
            Log.e("Dashboard.java", " Unable to start Road Safety");
        }
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        if(snapshot.exists())
        {
            User loggedINuser = snapshot.getValue(User.class);
            //loggedINuser.userID = userId;
            Log.e("Dashboard.java", loggedINuser.getUsername()+" is logged in");
            Log.e("Dashboard.java", loggedINuser.toString()+" is logged in");
            userName.setText(loggedINuser.getFirstName()+" "+loggedINuser.getLastName());
            mailAddress.setText(loggedINuser.getEmail());
        }
        else
        {
            Log.e("Dashboard.java", "Firebase user details not fetched !!!");
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }


}