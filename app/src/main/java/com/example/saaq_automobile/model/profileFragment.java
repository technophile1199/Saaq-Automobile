package com.example.saaq_automobile.model;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.saaq_automobile.R;
import com.example.saaq_automobile.helpers.AppConstants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.UUID;

public class profileFragment extends Fragment implements View.OnClickListener, ValueEventListener {

    TextView edEmail;
    EditText edFname,edLname,edPhone;
    ImageView profilePic;
    Button btnUpdate, btnDelete, btnBrowse;
    FirebaseAuth mAuthProfile;
    FirebaseUser mFirebaseUser;

    DatabaseReference userDatabase,userChild;
    FirebaseStorage storage;
    StorageReference storageReference;
    User user_Details;
    HashMap updatedData;

    int IMAGE_REQUEST = 71;
    Uri filePath;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();
    }

    private void initialize() {
        edFname = getActivity().findViewById(R.id.edFname);
        edLname = getActivity().findViewById(R.id.edLname);
        edEmail = getActivity().findViewById(R.id.edEmail);
        edPhone = getActivity().findViewById(R.id.edPhone);
        profilePic = getActivity().findViewById(R.id.profilePic);

        //Storing username for fetching further details.
        mAuthProfile = FirebaseAuth.getInstance();

        btnUpdate = getActivity().findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(this);
        btnDelete = getActivity().findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(this);
        btnBrowse = getActivity().findViewById(R.id.btnBrowse);
        btnBrowse.setOnClickListener(this);

        userDatabase = FirebaseDatabase
                .getInstance()
                .getReference("Users");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        find();

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            //case R.id.insert_btn : add();break;
            case R.id.btnUpdate : updateProfile(); break;
            case R.id.btnBrowse: selectPhoto(); break;
            //case R.id.btnDelete : deleteProfile();break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null)
        {
            //recieving the image file
            filePath = data.getData();
            //convert filepath -->Bitmap
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),filePath);
                profilePic.setImageBitmap(bitmap);
                Log.e("profileFragment.java", "Image succesfull");

            }catch (Exception e)
            {
                Log.e("profileFragment.java",e.getMessage());
            }
        }
    }

    private void selectPhoto() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(i.ACTION_GET_CONTENT);
        startActivityForResult(i.createChooser(i,"Select photo"),IMAGE_REQUEST);
    }

    private void updateProfile() {
        String fname = edFname.getText().toString();
        String lname = edLname.getText().toString();
        String phoneNummber = edPhone.getText().toString();

        updatedData = new HashMap();
        updatedData.put("fname",fname);
        updatedData.put("lname",lname);
        updatedData.put("cell",phoneNummber);

        mFirebaseUser = mAuthProfile.getCurrentUser();
        if(mFirebaseUser != null) {
            String userId = mAuthProfile.getCurrentUser().getUid();
            uploadPhoto();

            userChild = FirebaseDatabase.getInstance().getReference(AppConstants.firebaseUsersReference).child(userId);
            userChild.updateChildren(updatedData).addOnSuccessListener(new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    Toast.makeText(getActivity(), "Updated Successfully", Toast.LENGTH_SHORT).show();

                }
            });
        }
            userChild.addValueEventListener(this);

            Log.e("Updating_userDetails", mAuthProfile.getCurrentUser().getEmail());

        }


    private void find()
    {

        try{
            mFirebaseUser = mAuthProfile.getCurrentUser();
            if(mFirebaseUser != null) {
                String userId = mAuthProfile.getCurrentUser().getUid();
                uploadPhoto();

                userChild = FirebaseDatabase.getInstance().getReference(AppConstants.firebaseUsersReference).child(userId);
                userChild.addValueEventListener(this);

                Log.e("Fetching_userDetails", mAuthProfile.getCurrentUser().getEmail());

            }
            else {
                Log.e("Fetching_userDetails", " Fetching Firebase user null");
            }
        }
        catch(Exception e)
        {
            Log.d("Fetching_userDetails",e.getMessage());
        }
    }


    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        if(snapshot.exists()) {
            user_Details= snapshot.getValue(User.class);
            edFname.setText(user_Details.getFirstName());
            edLname.setText(user_Details.getLastName());
            edEmail.setText(user_Details.getEmail());
            edPhone.setText(user_Details.getPhoneNumber());

            String urlPhoto = snapshot.child("profilePic").getValue().toString();
            Log.e("Fetching_photo", urlPhoto);
            Picasso.with(getActivity()).load(urlPhoto).placeholder(R.drawable.noimage).into(profilePic);
        }
        else
        {
            Log.e("Fetching_userDetails", "Fetching Data failed");
        }
    }

    private void uploadPhoto() {
        if(filePath !=null)
        {

            Log.e("Uploading Photo", "Successful  !!");
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading Photo");
            progressDialog.show();

            String user_ID = mFirebaseUser.getUid();
            StorageReference refPhoto = storageReference.child("images/"+ user_ID);
            refPhoto.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Uploaded Succesfully", Toast.LENGTH_SHORT).show();


                        profilePic.setImageURI(filePath);

                        refPhoto.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {

                            String photoUploadUrl = task.getResult().toString();

                            //User userPhoto = new User();
                            user_Details.setProfilePic(photoUploadUrl);
                            userDatabase = FirebaseDatabase.getInstance().getReference(AppConstants.firebaseUsersReference);
                            userDatabase.child(mFirebaseUser.getUid()).setValue(user_Details);
                            Log.e("Fetching_photoURL", photoUploadUrl);
                            Log.e("Fetching_photoURL", user_Details.toString());
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                    double progress = 100.0 * snapshot.getBytesTransferred()/snapshot.getTotalByteCount();
                    progressDialog.setMessage("Uploaded: "+ (int) progress+"%");
                }
            });
        }
        else
        {
            Log.e("Uploading Photo", "uploading Unsuccessful  !!");
        }

    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}
