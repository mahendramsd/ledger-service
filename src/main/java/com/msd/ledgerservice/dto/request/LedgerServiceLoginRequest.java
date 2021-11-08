package com.msd.ledgerservice.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@NoArgsConstructor
@EqualsAndHashCode
public class LedgerServiceLoginRequest implements Serializable {

    private String username;
    private String password;
}
