package com.bjtu.offer_app.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjtu.offer_app.dao.OfferMapper;
import com.bjtu.offer_app.entity.Offer;
import com.bjtu.offer_app.service.OfferService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OfferServiceImpl extends ServiceImpl<OfferMapper, Offer> implements OfferService {

    public List<Offer> findOffer(String enterprise) {
        QueryWrapper<Offer> wrapper = Wrappers.query();
        wrapper.like("enterprise", enterprise);
        return baseMapper.selectList(wrapper);
    }

}
