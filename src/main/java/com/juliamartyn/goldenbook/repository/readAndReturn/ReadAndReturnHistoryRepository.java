package com.juliamartyn.goldenbook.repository.readAndReturn;

import com.juliamartyn.goldenbook.entities.readAndReturn.ReadAndReturnHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ReadAndReturnHistoryRepository extends JpaRepository<ReadAndReturnHistory, Integer> {
    @Query(value = "select read_return_history.* from read_return_history where expected_return_date > current_date", nativeQuery = true)
    List<ReadAndReturnHistory> findAllRentedBooks();

    @Query(value = "select read_return_history.* from read_return_history where expected_return_date = current_date", nativeQuery = true)
    List<ReadAndReturnHistory> findAllWithLastRentDay();

    @Modifying
    @Transactional
    @Query(value = "update  read_return_history h set h.email_reminding = :emailReminding" +
                   " where h.id = :id", nativeQuery = true)
    int updateEmailReminding(Integer id, boolean emailReminding);
}
