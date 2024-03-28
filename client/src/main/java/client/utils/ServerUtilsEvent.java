/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.utils;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import client.Config;
import commons.Event;
import commons.Expense;
import commons.Participant;
import jakarta.ws.rs.BadRequestException;
import org.glassfish.jersey.client.ClientConfig;

import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import org.jetbrains.annotations.NotNull;

public class ServerUtilsEvent {
    private static final String SERVER = "http://localhost:8080/";

    public Event getByID(Long id) {
        Event event;
        try {
            event = ClientBuilder.newClient(new ClientConfig()) //
                    .target(SERVER).path("api/events/" + id) //
                    .request(APPLICATION_JSON) //
                    .accept(APPLICATION_JSON) //
                    .get(new GenericType<>() {
                    });
        } catch(BadRequestException e) {
            event = null;
        }
        return event;
    }

    public Event addEvent(Event event) {
        Event saved = ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/events") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(event, APPLICATION_JSON), Event.class);
        System.out.println("Add event" + saved);

        Config.get().addPastID(String.valueOf(saved.getId()));

        return saved;
    }

    public Expense addExpense(Expense expense) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(getServer()).path("/api/expenses") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(expense, APPLICATION_JSON), Expense.class);
    }

    public Event editEventTitle(String editedTitle, Event event) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(getServer()).path("/api/events/" + event.getId()+ "/title")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .put(Entity.entity(editedTitle, APPLICATION_JSON), Event.class);
    }

    public Participant addParticipant(Participant participant, Event event) {
        //TODO
        return null;
    }

    //    public Event modifyEvent(Event event) {
//        //System.out.println("Add event" + event);
//
//        return ClientBuilder.newClient(new ClientConfig()) //
//                .target(SERVER).path("api/events") //
//                .request(APPLICATION_JSON) //
//                .accept(APPLICATION_JSON) //
//                .post(Entity.entity(event, APPLICATION_JSON), Event.class);
//    }
    private @NotNull String getServer() {
        return Config.get().getHost();
    }

    public void deleteEvent(Event event) {
        ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/events/" + event.getId()) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .delete();
        System.out.println("Event deleted:" + event);

        //Config.get().addPastID(String.valueOf(saved.getId()));
    }

}
