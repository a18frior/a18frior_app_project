package com.example.a18frior_app_project;



import java.util.Comparator;
import java.util.Locale;
import java.util.jar.Attributes;

/**
 * Created by marcus on 2018-04-25.
 */

public class Destinations {

    public static final String COLUMN_NAME_ID = "ID";
    public static final String COLUMN_NAME_Name = "Name";
    public static final String COLUMN_NAME_Location = "Location";
    public static final String COLUMN_NAME_Citizen = "Citizen";
    public static final String COLUMN_NAME_Category = "Category";
    public static final String COLUMN_NAME_Info = "Info";


    private String Name;
    private String Location;
    private String Citizen;
    private String Category;
    private String Info;


    public Destinations(String inName, String inLocation, String inCitizen, String inCategory, String inInfo) {
        Name = inName;
        Location = inLocation;
        Citizen = inCitizen;
        Category = inCategory;
        Info = inInfo;

    }


    public Destinations(String inName) {
        Name = inName;
        Location = "";

    }

    @Override
    public String toString() {
        return Name;
    }

    public String info() {
        String str = Name;
        str += " is located in ";
        str += Location;
        str += " With ";
        str += Citizen;
        str += "Citizens. ";

        return str;
    }


    public void setName(String newName) {
        Name = newName;
    }

    public void setLocation(String newLocation) {
        Location = newLocation;
    }

    public void setInfo(String newInfo) {
        Info = newInfo;
    }

    public void setCategory(String newCategory) {
        Category = newCategory;

    }

    public void setCitizen(String newCitizen) {
        Citizen = newCitizen;
    }

    public String getName() {
        String getname = Name;
        return getname;
    }

    public String getLocation() {
        String getloc = Location;
        return getloc;
    }

    public String getCitizen() {
        String getCitizen = Citizen;
        return getCitizen;
    }

    public String getCategory() {
        String getCategory = Category;
        return getCategory;


    }

    public String getInfo() {
        String getInfo = Info;
        return getInfo;


    }
}

