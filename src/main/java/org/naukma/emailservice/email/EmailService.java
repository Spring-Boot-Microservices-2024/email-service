package org.naukma.emailservice.email;

import lombok.extern.slf4j.Slf4j;
import org.naukma.emailservice.event.Event;
import org.naukma.emailservice.event.EventClient;
import org.naukma.emailservice.user.User;
import org.naukma.emailservice.user.UserClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@EnableJms
@Service
@Slf4j
public class EmailService {

    private final UserClient userClient;

    private final EventClient eventClient;

    @Autowired
    public EmailService(UserClient userClient, EventClient eventClient) {
        this.userClient = userClient;
        this.eventClient = eventClient;
    }

    @JmsListener(destination = "email", containerFactory = "jmsListenerFactory")
    public void receiveMessage(String email) {
        System.out.println("Received <" + email + ">");
    }

    @Scheduled(cron = "0 * * * * *")
    private void sendEmails() {
        List<User> users = userClient.getAllUsers();
        List<Event> events = eventClient.getEventsForNextWeek();
        log.info("Sending information about the events on the next week: {}", events);
        for (User user : users) {
            log.info("Sending email <{}>", user.getEmail());
        }
    }
}
