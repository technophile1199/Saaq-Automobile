package com.example.saaq_automobile.model;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.saaq_automobile.R;

import java.net.URI;

public class mailFragment extends Fragment implements View.OnClickListener {

    EditText edTo,edSubject,edBody;
    Button btnSend;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mail, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();
    }

    private void initialize() {

        String mailTo="mmpandya1906@gmail.com";

        edSubject = getActivity().findViewById(R.id.edSubject);
        edTo = getActivity().findViewById(R.id.edTo);
        edBody = getActivity().findViewById(R.id.edBody);
        edTo.setText("mmpandya1906@gmail.com");
        edTo.setEnabled(false);
        btnSend = getActivity().findViewById(R.id.btnSend);
        btnSend.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        String mailTo="mmpandya1906@gmail.com";
        if(!edSubject.getText().toString().isEmpty() && !edBody.getText().toString().isEmpty())
        {
            edTo.setText(mailTo);
            Intent i = new Intent(Intent.ACTION_SENDTO);
            i.putExtra(Intent.EXTRA_EMAIL,new String[]{"mmpandya1906@gmail.com"});
            i.putExtra(Intent.EXTRA_SUBJECT,edSubject.getText().toString());
            i.putExtra(Intent.EXTRA_TEXT,edBody.getText().toString());
            i.setType("message/rfc822");
            i.setData(Uri.parse("mailto:"));
            if(i.resolveActivity(getActivity().getPackageManager()) != null)
            {
                startActivity(i.createChooser(i,"Send email with ?"));
            }
            else
            {
                Toast.makeText(getActivity(), "There is no application", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
