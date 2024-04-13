package com.bjtu.offer_app.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Offer implements Serializable {

    private Long offerId;
    private String enterprise;
    private String job;
    private Integer salary;
}
