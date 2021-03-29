package com.juliamartyn.goldenbook.entities;


import lombok.*;
import org.springframework.boot.context.properties.bind.DefaultValue;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private String email;

    private String phone;

    private String deliveryAddress;

    @Builder.Default
    private Boolean disabled = false;

    @ManyToOne
    @JoinColumn(name = "role_id",  referencedColumnName = "id")
    private Role role;

    @OneToMany(mappedBy = "buyer")
    private Set<Order> orders;

}
