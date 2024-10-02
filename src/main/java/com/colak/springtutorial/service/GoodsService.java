package com.colak.springtutorial.service;

import com.colak.springtutorial.dto.GoodsDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.generated.public_.tables.Goods;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class GoodsService {

    private final DSLContext dslContext;

    public GoodsDto create(GoodsDto goodsDto) {
        var id = UUID.randomUUID();
        var result = dslContext.insertInto(Goods.GOODS)
                .values(id,
                        goodsDto.name(),
                        goodsDto.price(),
                        goodsDto.totalCount(),
                        goodsDto.soldCount(),
                        goodsDto.deleted())
                .execute();
        log.info("Inserted with result: {}", result);
        return getById(id);
    }

    public List<GoodsDto> findAll() {
        return dslContext
                .select()
                .from(Goods.GOODS)
                .fetch()
                .stream()
                .map(GoodsService::toDto)
                .toList();
    }

    public GoodsDto update(GoodsDto goodsDto) {
        var updated = dslContext.update(Goods.GOODS)
                .set(Goods.GOODS.ID, goodsDto.id())
                .set(Goods.GOODS.NAME, goodsDto.name())
                .set(Goods.GOODS.PRICE, goodsDto.price())
                .set(Goods.GOODS.SOLD_COUNT, goodsDto.soldCount())
                .set(Goods.GOODS.TOTAL_COUNT, goodsDto.totalCount())
                .set(Goods.GOODS.DELETED, goodsDto.deleted())
                .execute();
        log.info("Successfully updated {} rows", updated);
        return this.getById(goodsDto.id());
    }

    public GoodsDto getById(UUID id) {
        var fetchedRecord = dslContext.select()
                .from(Goods.GOODS)
                .where(Goods.GOODS.ID.eq(id))
                .fetchOne();
        Assert.notNull(fetchedRecord, "Record with id = " + id + " is not exists");
        return toDto(fetchedRecord);
    }

    public void delete(UUID id) {
        dslContext.update(Goods.GOODS).
                set(Goods.GOODS.DELETED, true)
                .where(Goods.GOODS.ID.eq(id))
                .execute();

        log.info("Successfully deleted the good with id = [" + id + "]");
    }

    private static GoodsDto toDto(Record fetchedRecord) {
        return new GoodsDto(
                fetchedRecord.get(Goods.GOODS.ID),
                fetchedRecord.get(Goods.GOODS.NAME),
                fetchedRecord.get(Goods.GOODS.PRICE),
                fetchedRecord.get(Goods.GOODS.SOLD_COUNT),
                fetchedRecord.get(Goods.GOODS.TOTAL_COUNT),
                fetchedRecord.get(Goods.GOODS.DELETED)
        );
    }
}
