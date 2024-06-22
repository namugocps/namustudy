package com.example.namustudy.zone;

import com.example.namustudy.domain.Zone;

import java.util.List;

public interface ZoneRepository extends JpaRepositorytory<Zone, Long>{
    Zone findByCityAndProvince(String cityName, String provinceName);
        int count();

    void saveAll(List<Zone> zoneList);
}
