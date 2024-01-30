package com.colak.springjooqtutorial.service;

import com.colak.springjooqtutorial.dto.GoodsDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Testcontainers
class GoodsServiceTest {

    @Container
    @ServiceConnection
    public static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres"))
                    .withPassword("inmemory")
                    .withUsername("inmemory");

    @Autowired
    private GoodsService goodsService;

    @Test
    void testFindAll() {
        GoodsDto goodsDto = new GoodsDto(
                null,
                "good1",
                BigDecimal.valueOf(100),
                50,
                10,
                false
        );
        GoodsDto savedGoodsDto = goodsService.create(goodsDto);
        assertNotNull(savedGoodsDto.id());

        List<GoodsDto> list = goodsService.findAll();
        assertEquals(savedGoodsDto.id(), list.getFirst().id());
    }
}
