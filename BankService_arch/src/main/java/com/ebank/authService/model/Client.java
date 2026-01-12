package com.ebank.authService.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@ToString(exclude = "user")
@EqualsAndHashCode(exclude = "user")
@Table(
        name = "clients",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "cin"),
                @UniqueConstraint(columnNames = "email")
        }
)
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String firstName;

    @Column(nullable = false, length = 80)
    private String lastName;

    @Column(nullable = false, length = 8)
    private String cin;

    @Column(nullable = false, length = 120)
    private String email;

    @Column(nullable = false, length = 200)
    private String address;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false)
    private boolean active = true;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @JsonIgnore
    private User user;

}
