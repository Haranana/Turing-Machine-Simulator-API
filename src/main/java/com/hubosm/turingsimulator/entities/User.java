package com.hubosm.turingsimulator.entities;

import com.hubosm.turingsimulator.mappers.AccountStatusConverter;
import com.hubosm.turingsimulator.utils.AccountStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email" , nullable = false)
    @Email
    private String email;

    @Column(name = "password_hash" , nullable = false)
    private String passwordHash;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "status" , nullable = false)
    @Convert(converter = AccountStatusConverter.class)
    private AccountStatus status;

    @Column(name = "activation_token", unique = true)
    private String activationToken;

    @Column(name = "activation_token_expires_at")
    private OffsetDateTime activationTokenExpiresAt;

    @Column(name = "password_change_token", unique = true)
    private String passwordChangeToken;

    @Column(name = "password_change_token_expires_at")
    private OffsetDateTime passwordChangeTokenExpiresAt;

    @Column(name = "delete_account_token", unique = true)
    private String deleteAccountToken;

    @Column(name = "delete_account_token_expires_at")
    private OffsetDateTime deleteAccountTokenExpiresAt;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<TuringMachine> turingMachines = new ArrayList<>();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return status != AccountStatus.BLOCKED; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return status == AccountStatus.ACTIVE; }
}
