package com.bjtu.offer_app.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bjtu.offer_app.entity.Offer;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OfferMapper extends BaseMapper<Offer> {
}
