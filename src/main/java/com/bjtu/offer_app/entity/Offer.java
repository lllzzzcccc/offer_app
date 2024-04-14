package com.bjtu.offer_app.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

@Data
public class Offer implements Serializable {


    @TableId
    private Long offerId;

    private String enterprise;
    private String job;
    private Integer salary;
}
