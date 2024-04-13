package com.example.namustudy.settings.form;

import lombok.Data;

@Data
public class ZoneForm {

    private String zoneName;

    public String getCityName() { return zoneName.substring(0, zoneName.indexOf("("));}

    public String getProvinceName(){
        return zoneName.substring(zoneName.indexOf("/") +1);
    }
}
