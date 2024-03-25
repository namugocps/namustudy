package com.example.namustudy.zone;

import com.example.namustudy.domain.Zone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ZoneRepository extends JpaRepositorytory<Zone, Long>{
        int count();

    void saveAll(List<Zone> zoneList);
}
