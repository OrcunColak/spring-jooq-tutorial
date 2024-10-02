package com.colak.springtutorial.service.goods;

import com.colak.springtutorial.dto.GoodsDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.generated.public_.tables.Goods;
import org.jooq.impl.SQLDataType;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.UUID;

import static org.jooq.generated.public_.Tables.GOODS;
import static org.jooq.impl.DSL.constraint;
import static org.jooq.impl.DSL.field;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoodsService {

    private final DSLContext dslContext;

    // See https://medium.com/javajams/java-creating-tables-on-the-fly-d0d9408de326
    // This is just an example to create a table on the fly
    public void createTable() {
        // Create the GOODS table
        dslContext.createTable("GOODS")
                .column("id", SQLDataType.UUID.nullable(false)) // UUID primary key
                .column("name", SQLDataType.VARCHAR(255).nullable(false)) // VARCHAR(255)
                .column("price", SQLDataType.NUMERIC.nullable(true)) // Numeric type
                .column("total_count", SQLDataType.INTEGER.nullable(true)) // Integer
                .column("sold_count", SQLDataType.INTEGER.nullable(true)) // Integer
                .column("deleted", SQLDataType.BOOLEAN.nullable(true)) // Boolean
                .constraints(
                        constraint("pk_goods").primaryKey("id") // Primary key constraint
                )
                .execute();
    }

    public GoodsDto insert(GoodsDto goodsDto) {
        var id = UUID.randomUUID();
        var result = dslContext
                .insertInto(Goods.GOODS)
                .columns(
                        field("id"),
                        field("name"),
                        field("price"),
                        field("total_count"),
                        field("sold_count"),
                        field("deleted")
                )
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
                .from(GOODS)
                .fetch()
                .stream()
                .map(GoodsService::toDto)
                .toList();
    }

    public GoodsDto update(GoodsDto goodsDto) {
        var updated = dslContext.update(Goods.GOODS)
                .set(GOODS.ID, goodsDto.id())
                .set(GOODS.NAME, goodsDto.name())
                .set(GOODS.PRICE, goodsDto.price())
                .set(GOODS.SOLD_COUNT, goodsDto.soldCount())
                .set(GOODS.TOTAL_COUNT, goodsDto.totalCount())
                .set(GOODS.DELETED, goodsDto.deleted())
                .execute();
        log.info("Successfully updated {} rows", updated);
        return this.getById(goodsDto.id());
    }

    public GoodsDto getById(UUID id) {
        var fetchedRecord = dslContext.select()
                .from(GOODS)
                .where(GOODS.ID.eq(id))
                .fetchOne();
        Assert.notNull(fetchedRecord, "Record with id = " + id + " is not exists");
        return toDto(fetchedRecord);
    }

    public void delete(UUID id) {
        dslContext.update(Goods.GOODS).
                set(GOODS.DELETED, true)
                .where(GOODS.ID.eq(id))
                .execute();

        log.info("Successfully deleted the good with id = [{}]", id);
    }

    private static GoodsDto toDto(Record fetchedRecord) {
        return new GoodsDto(
                fetchedRecord.get(GOODS.ID),
                fetchedRecord.get(GOODS.NAME),
                fetchedRecord.get(GOODS.PRICE),
                fetchedRecord.get(GOODS.SOLD_COUNT),
                fetchedRecord.get(GOODS.TOTAL_COUNT),
                fetchedRecord.get(GOODS.DELETED)
        );
    }
}
