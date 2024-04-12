package com.example.namustudy.settings.form;

public class ZoneForm {

    private String zoneName;

    public String getCityName() { return zoneName.substring(0, zoneName.indexOf("("));}
}
