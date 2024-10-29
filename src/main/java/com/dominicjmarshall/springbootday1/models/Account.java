package com.dominicjmarshall.springbootday1.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Email(message = "Invalid email")
    @NotEmpty(message = "Please enter your email address")
    private String email;

    @NotEmpty(message = "Please enter a password")
    private String password;

    @NotEmpty(message = "Please enter your name")
    private String firstName;

    @NotEmpty(message = "Please enter your surname")
    private String lastName;

    private String gender;

    @Min(value = 18)
    @Max(value = 81)
    private Integer age;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate birthday;

    private String photo;

    // one-to-one relationship.
    private String role;

    @JsonManagedReference
    @OneToMany(mappedBy = "account")
    private List<Post> posts;

    @Column(name="token")
    private String passwordResetToken;
    
    private LocalDateTime passwordResetTokenExpiry;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name="account_authority",
        joinColumns = {@JoinColumn(name="account_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name="authority_id", referencedColumnName = "id")}
        )
    private Set<Authority> authorities = new HashSet<>();
}
