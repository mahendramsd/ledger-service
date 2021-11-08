package com.msd.ledgerservice.dto;

import lombok.Data;

@Data
public class TokenValidity {
    private String userId;
    private String tokenId;
    private String tokenHash;


    public TokenValidity(String userId, String tokenId, String tokenHash) {
        super();
        this.userId = userId;
        this.tokenId = tokenId;
        this.tokenHash = tokenHash;
    }


}
