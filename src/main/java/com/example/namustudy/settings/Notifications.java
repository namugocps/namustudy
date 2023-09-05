package com.example.namustudy.settings;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Notifications {

    private boolean studyCreatedByEamil;

    private boolean studyCreatedByWeb;

    private boolean studyEnrollmentResultByEamil;

    private boolean studyEnrollmentResultByWeb;
}
