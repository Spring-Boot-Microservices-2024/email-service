package org.naukma.emailservice.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.naukma.emailservice.user.User;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Event {

    private Long id;

    private String caption;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dateTime;

    private float price;

    private List<User> participants;

    private String description;

    private boolean online;

    private int capacity;

    private String address;

    private User organiser;
}
