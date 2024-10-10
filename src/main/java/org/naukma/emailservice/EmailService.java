package org.naukma.emailservice;

import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@EnableJms
@Service
public class EmailService {

    @JmsListener(destination = "email", containerFactory = "jmsListenerFactory")
    public void receiveMessage(String email) {
        System.out.println("Received <" + email + ">");
    }
}
