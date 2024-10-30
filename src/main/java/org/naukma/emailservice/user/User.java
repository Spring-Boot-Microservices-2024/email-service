package org.naukma.emailservice.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String password;
    private String email;
    private String firstname;
    private String lastname;
}
