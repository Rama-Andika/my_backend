package com.oxysystem.general.model.db1.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "sysuser")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "secure-id")
    @GenericGenerator(name = "secure-id", strategy = "com.oxysystem.general.util.GenerateRandomIdUtils")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "user_id")
    private Long userId;

    @Transient
    private String id;

    @Column(name = "login_id", unique = true)
    private String username;

    @JsonIgnore
    private String password;

    @Column(name = "full_name")
    private String fullName;

    @Column(unique = true)
    private String email;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "last_login_date")
    @JsonIgnore
    private LocalDateTime lastLoginDate;

    @Column(name = "last_login_ip")
    @JsonIgnore
    private String lastLoginIp;

    @Column(name = "user_key")
    @JsonIgnore
    private String userKey;

    @Column(name = "reset_password")
    @JsonIgnore
    private int resetPassword;

    @Column(name = "user_level")
    private Integer userLevel;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "group_id")
    private UserGroup userGroup;
}
