package com.juliamartyn.goldenbook.repository.readAndReturn;

import com.juliamartyn.goldenbook.entities.readAndReturn.ReadAndReturnTariff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ReadAndReturnTariffRepository extends JpaRepository<ReadAndReturnTariff, Integer> {
    @Query(value = "select read_return_tariff.* from read_return_tariff", nativeQuery = true)
    List<ReadAndReturnTariff> findAllReadAndReturnBooks();

    @Modifying
    @Transactional
    @Query(value = "update read_return_tariff t set t.price_per_day = :pricePerDay where t.id = :id", nativeQuery = true)
    int updatePricePerDay(Integer id, BigDecimal pricePerDay);
}
