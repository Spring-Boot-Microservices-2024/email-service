package org.naukma.emailservice.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.naukma.emailservice.event.Event;
import org.naukma.emailservice.user.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BookingEvent {
    private Event event;
    private User user;
    private BookingEventType type;
}
