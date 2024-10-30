package org.naukma.emailservice.event;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "SPRING-MODULITH", contextId = "eventClient")
public interface EventClient {

    @GetMapping("/events/next-week")
    List<Event> getEventsForNextWeek();

}
