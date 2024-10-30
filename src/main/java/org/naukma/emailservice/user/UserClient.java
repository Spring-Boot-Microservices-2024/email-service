package org.naukma.emailservice.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "SPRING-MODULITH", contextId = "userClient")
public interface UserClient {

    @GetMapping("/users")
    List<User> getAllUsers();

}
