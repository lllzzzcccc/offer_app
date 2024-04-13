package com.bjtu.offer_app.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjtu.offer_app.dao.OfferMapper;
import com.bjtu.offer_app.entity.Offer;
import com.bjtu.offer_app.service.OfferService;
import org.springframework.stereotype.Service;

@Service
public class OfferServiceImpl extends ServiceImpl<OfferMapper, Offer> implements OfferService {

}
