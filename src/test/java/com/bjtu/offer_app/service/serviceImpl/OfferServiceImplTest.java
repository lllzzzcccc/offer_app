package com.bjtu.offer_app.service.serviceImpl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OfferServiceImplTest {

    @Autowired
    OfferServiceImpl offerService;

    @Test
    void getOfferList() {
        offerService.removeById(20);
    }

}
