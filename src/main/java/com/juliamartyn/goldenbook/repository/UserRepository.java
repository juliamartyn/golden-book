package com.juliamartyn.goldenbook.repository;

import com.juliamartyn.goldenbook.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByUsername(String username);

    User findUserById(Long id);

    Page<User> findAll(Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "update users set users.disabled = :disabled where users.id = :id", nativeQuery = true)
    int updateDisabled(Integer id, boolean disabled);

    @Modifying
    @Transactional
    @Query(value = "update users set users.username = :username where users.id = :id", nativeQuery = true)
    int updateUsername(Integer id, String username);


    @Modifying
    @Transactional
    @Query(value = "update users set users.delivery_address = :address where users.id = :id", nativeQuery = true)
    int updateDeliveryAddress(Integer id, String address);
}