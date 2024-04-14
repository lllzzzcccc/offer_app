package com.bjtu.offer_app.controller;

import com.bjtu.offer_app.entity.Offer;
import com.bjtu.offer_app.service.OfferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/offer")
@Tag(name = "offer信息响应接口", description = "处理基本的增删改查操作")
public class OfferController {

    @Autowired
    private OfferService offerService;

    @Operation(summary = "添加Offer信息")
    @PostMapping("/")
    public String addOffer(@RequestBody Offer offer) {
        offerService.save(offer);
        return "添加成功！";
    }

    @Operation(summary = "删除Offer信息")
    @DeleteMapping("/{id}")
    public String deleteOffer(@PathVariable Long id) {
        offerService.removeById(id);
        return "删除成功！";
    }

    @Operation(summary = "替换Offer信息")
    @PutMapping("/")
    public String replaceOffer(@RequestBody Offer offer) {
        offerService.updateById(offer);
        return "替换成功！";
    }

    @Operation(summary = "更新Offer信息")
    @PutMapping("/update")
    public String updateOffer(@RequestBody Offer offer) {
        offerService.updateById(offer);
        return "更新成功！";
    }

    @Operation(summary = "获取Offer信息")
    @GetMapping("/{id}")
    public Offer getOffer(@PathVariable Long id) {
        return offerService.getById(id);
    }

    @Operation(summary = "获取Offer列表")
    @GetMapping("/list")
    public List<Offer> listOffer() {
        return offerService.list();
    }

    @Operation(summary = "部分检索Offer信息")
    @GetMapping("/find/{enterprise}")
    public List<Offer> findOffer(@PathVariable String enterprise) {
        return offerService.findOffer(enterprise);
    }
}
