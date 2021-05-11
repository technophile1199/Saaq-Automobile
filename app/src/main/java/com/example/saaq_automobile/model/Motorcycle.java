package com.example.saaq_automobile.model;

import androidx.annotation.NonNull;

public class Motorcycle {
    public String v_ID;
    private String vOwner;
    private String vCell;
    private String vEmail;
    private String vAppointmentNumber;

    public Motorcycle() {
    }

    public Motorcycle(String vOwner, String vCell, String vEmail, String vAppointmentNumber) {
        this.vOwner = vOwner;
        this.vCell = vCell;
        this.vEmail = vEmail;
        this.vAppointmentNumber = vAppointmentNumber;
    }

    public String getvOwner() {
        return vOwner;
    }

    public void setvOwner(String vOwner) {
        this.vOwner = vOwner;
    }

    public String getvCell() {
        return vCell;
    }

    public void setvCell(String vCell) {
        this.vCell = vCell;
    }

    public String getvEmail() {
        return vEmail;
    }

    public void setvEmail(String vEmail) {
        this.vEmail = vEmail;
    }

    public String getvAppointmentNumber() {
        return vAppointmentNumber;
    }

    public void setvAppointmentNumber(String vAppointmentNumber) {
        this.vAppointmentNumber = vAppointmentNumber;
    }

    @NonNull
    @Override
    public String toString() {
        return "Vehicle Owner is : "+vOwner+" having contact details "+vCell+" "+vEmail+" \n Provided with Appointment Number: "+vAppointmentNumber;
    }
}
