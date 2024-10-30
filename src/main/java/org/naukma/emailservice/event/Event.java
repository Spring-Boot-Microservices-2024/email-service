package org.naukma.emailservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.naukma.emailservice.user.User;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    private Long id;
    private String caption;
    private LocalDateTime dateTime;
    private float price;
    private List<User> participants;
    private String description;
    private boolean online;
    private int capacity;
    private String address;
    private User organiser;
}
