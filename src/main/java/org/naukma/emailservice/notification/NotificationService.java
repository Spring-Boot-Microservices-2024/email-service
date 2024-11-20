package org.naukma.emailservice.notification;

import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.naukma.emailservice.booking.BookingEvent;
import org.naukma.emailservice.booking.BookingEventType;
import org.naukma.emailservice.email.EmailSender;
import org.naukma.emailservice.event.Event;
import org.naukma.emailservice.event.EventClient;
import org.naukma.emailservice.user.User;
import org.naukma.emailservice.user.UserClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@EnableJms
@Service
@Slf4j
public class NotificationService {

    private final UserClient userClient;

    private final EventClient eventClient;

    private final EmailSender emailSender;

    private static final String EMAIL_FROM = "no-reply@bookingapp.com";

    @Autowired
    public NotificationService(UserClient userClient, EventClient eventClient, EmailSender emailSender) {
        this.userClient = userClient;
        this.eventClient = eventClient;
        this.emailSender = emailSender;
    }

    @JmsListener(destination = "booking", containerFactory = "jmsListenerFactory")
    public void receiveBookingEvent(BookingEvent bookingEvent) {
        log.info("Received booking event {}", bookingEvent);

        try {
            if (bookingEvent.getType() == BookingEventType.USER_REGISTERED_FOR_EVENT) {
                sendRegistrationEmail(bookingEvent);
            } else if (bookingEvent.getType() == BookingEventType.USER_UNREGISTERED_FROM_EVENT) {
                sendUnregistrationEmail(bookingEvent);
            }
        } catch (Exception e) {
            log.error("Failed to process booking event: {}", bookingEvent, e);
        }
    }

//    @Scheduled(cron = "0 * * * * *")
    @Scheduled(cron = "0 0 12 * * Sat") //saturday at 12:00
    private void sendEmails() throws MessagingException {
        List<User> users = userClient.getAllUsers();
        List<Event> events = eventClient.getEventsForNextWeek();
        log.info("Sending information about the events on the next week");
        if (!events.isEmpty()) {
            for (User user : users) {
                sendDigestEmail(user, events);
            }
        }
    }

    private void sendRegistrationEmail(BookingEvent bookingEvent) throws MessagingException {
        Context context = new Context();
        context.setVariable("name", bookingEvent.getUser().getFirstname());
        context.setVariable("eventName", bookingEvent.getEvent().getCaption());
        context.setVariable("eventDate", formatDateTime(bookingEvent.getEvent().getDateTime()));
        context.setVariable("eventLocation", bookingEvent.getEvent().getAddress());

        emailSender.sendEmail(
                EMAIL_FROM,
                bookingEvent.getUser().getEmail(),
                "Registration Confirmation",
                "registration-email",
                context
        );
    }

    private void sendUnregistrationEmail(BookingEvent bookingEvent) throws MessagingException {
        Context context = new Context();
        context.setVariable("name", bookingEvent.getUser().getFirstname());
        context.setVariable("eventName", bookingEvent.getEvent().getCaption());

        emailSender.sendEmail(
                EMAIL_FROM,
                bookingEvent.getUser().getEmail(),
                "Unregistration Confirmation",
                "unregister-email",
                context
        );
    }

    private void sendDigestEmail(User user, List<Event> events) throws MessagingException {
        Context context = new Context();
        context.setVariable("name", user.getFirstname());
        List<Map<String, String>> formattedEvents = events.stream()
                .map(event -> Map.of(
                        "caption", event.getCaption(),
                        "dateTime", formatDateTime(event.getDateTime()),
                        "address", event.getAddress()
                )).toList();

        context.setVariable("events", formattedEvents);


        emailSender.sendEmail(
                EMAIL_FROM,
                user.getEmail(),
                "Your Weekly Events Digest",
                "digest-email",
                context
        );
    }

    private String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy 'at' h:mm a", Locale.ENGLISH);
        return dateTime.format(formatter);
    }

}
