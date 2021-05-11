package com.example.saaq_automobile.model;

import androidx.annotation.NonNull;

public class Vehicle {
    public String v_ID;
    private String vOwner;
    private String vCell;
    private String vEmail;
    private String vAppointmentNumber;

    private Automobile automobile;
    private busOrMinibus busOrMinibus;
    private Motorcycle motorcycle;

    public Vehicle() {
    }

    public Vehicle(String v_ID, String vOwner, String vCell, String vEmail, String vAppointmentNumber, Automobile automobile, com.example.saaq_automobile.model.busOrMinibus busOrMinibus, Motorcycle motorcycle) {
        this.v_ID = v_ID;
        this.vOwner = vOwner;
        this.vCell = vCell;
        this.vEmail = vEmail;
        this.vAppointmentNumber = vAppointmentNumber;
        this.automobile = automobile;
        this.busOrMinibus = busOrMinibus;
        this.motorcycle = motorcycle;
    }

    //Constructor for automobile
    public Vehicle(String v_ID, String vOwner, String vCell, String vEmail, String vAppointmentNumber, Automobile automobile) {
        this.v_ID = v_ID;
        this.vOwner = vOwner;
        this.vCell = vCell;
        this.vEmail = vEmail;
        this.vAppointmentNumber = vAppointmentNumber;
        this.automobile = automobile;
    }

    //Constructor for Motorcycle
    public Vehicle(String v_ID,String vOwner, String vCell, String vEmail, String vAppointmentNumber, Motorcycle motorcycle) {
        this.v_ID = v_ID;
        this.vOwner = vOwner;
        this.vCell = vCell;
        this.vEmail = vEmail;
        this.vAppointmentNumber = vAppointmentNumber;
        this.motorcycle = motorcycle;
    }

    //Constructor for Bus or Minibus
    public Vehicle(String v_ID,String vOwner, String vCell, String vEmail, String vAppointmentNumber, com.example.saaq_automobile.model.busOrMinibus busOrMinibus) {
        this.v_ID = v_ID;
        this.vOwner = vOwner;
        this.vCell = vCell;
        this.vEmail = vEmail;
        this.vAppointmentNumber = vAppointmentNumber;
        this.busOrMinibus = busOrMinibus;
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
