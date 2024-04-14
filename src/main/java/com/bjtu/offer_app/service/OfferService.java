package com.bjtu.offer_app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bjtu.offer_app.entity.Offer;

import java.util.List;

public interface OfferService extends IService<Offer> {

    List<Offer> findOffer(String enterprise);

}
