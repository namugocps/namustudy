package com.example.namustudy.settings;

import com.example.namustudy.domain.Account;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Notifications {

    private boolean studyCreatedByEamil;

    private boolean studyCreatedByWeb;

    private boolean studyEnrollmentResultByEamil;

    private boolean studyEnrollmentResultByWeb;

    private boolean studyUpdatedByEmail;

    private boolean studyUpdatedByWeb;

    public Notifications(Account account){
        this.studyCreatedByEamil = account.isStudyCreatedByEmail();
    }
}
