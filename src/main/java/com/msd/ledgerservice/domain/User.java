package com.msd.ledgerservice.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "ledger_user")
public class User extends BaseEntityMaster {

    @Column(name = "user_name")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "access_token")
    private String accessToken;
}
