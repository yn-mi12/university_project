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

import client.Config;
import commons.Event;
import commons.Expense;
import commons.Participant;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import org.glassfish.jersey.client.ClientConfig;
import org.jetbrains.annotations.NotNull;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class ServerUtilsEvent {
    private static final String SERVER = Config.get().getHost();
    private List<Event> events = new ArrayList<>();

    public Event getByInviteCode(String inviteCode) {
        Event event;
        try {
            event = ClientBuilder.newClient(new ClientConfig())
                    .target(SERVER).path("api/events/code=" + inviteCode)
                    .request(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .get(new GenericType<>() {
                    });
        } catch(BadRequestException e) {
            event = null;
        }
        return event;
    }

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

    public Expense getExpenseById(Long id) {
        Expense expense;
        try{
            expense = ClientBuilder.newClient(new ClientConfig())
                    .target(SERVER).path("api/expenses/" + id)
                    .request(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .get(new GenericType<>(){});
        }catch (BadRequestException e){
            expense = null;
        }
        return expense;
    }

    public List<Expense> getExpensesByEventId(Event event){
       List<Expense> expenses;
       try{
           expenses = ClientBuilder.newClient(new ClientConfig())
                   .target(SERVER).path("api/expenses/event/" + event.getId())
                   .request(APPLICATION_JSON)
                   .accept(APPLICATION_JSON)
                   .get(new GenericType<>(){
                   });
       }catch(BadRequestException | NotFoundException e){
           expenses = null;
       }
        return expenses;

    }
    public Participant getParticipantByID(Long id) {
        Participant participant;
        try {
            participant = ClientBuilder.newClient(new ClientConfig()) //
                    .target(SERVER).path("api/participants/" + id) //
                    .request(APPLICATION_JSON) //
                    .accept(APPLICATION_JSON) //
                    .get(new GenericType<>() {
                    });
        } catch(BadRequestException e) {
            participant = null;
        }
        return participant;
    }

    public Event addEvent(Event event) {
        Event saved = ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/events") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(event, APPLICATION_JSON), Event.class);
        System.out.println("Add event" + saved);

        Config.get().addPastCode(String.valueOf(saved.getInviteCode()));

        return saved;
    }

    public Expense addExpense(Expense expense, Event event) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(getServer()).path("/api/expenses/event/" + event.getId()) //
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
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(getServer()).path("/api/events/" + event.getInviteCode() + "/participants") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(participant, APPLICATION_JSON), Participant.class);
    }

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
    }

    public Event updateEvent(Event updated) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(getServer()).path("/api/events/" + updated.getId())
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .put(Entity.entity(updated, APPLICATION_JSON), Event.class);
    }

    public void deleteParticipant(Participant participant) {
        ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/participants/" + participant.getId()) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .delete();
        System.out.println("Participant deleted:" + participant);
    }

    public boolean checkToken(String token) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(getServer()).path("api/admin/verify/" + token) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(token, APPLICATION_JSON), Boolean.class);
    }

    public List<Event> getAllEvents()
    {
        List<Event> events;
        events = ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/events/") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                    .get(new GenericType<>() {
                    });
        return events;
    }
    public List<Participant> getEventParticipants(Event event)
    {
        List<Participant> participants;
        participants = ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/participants/event/" + event.getId()) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<>() {
                });
        return participants;
    }
    public void updateParticipant(Participant participant) {
        ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/participants/" + participant.getId()) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .put(Entity.entity(participant, APPLICATION_JSON), Participant.class);
    }

    private final StompSession session = connect("ws://localhost:8080/websocket");

    private StompSession connect(String url)
    {
        var client = new StandardWebSocketClient();
        var stomp = new WebSocketStompClient(client);
        stomp.setMessageConverter(new MappingJackson2MessageConverter());
        try{
            return stomp.connectAsync(url, new StompSessionHandlerAdapter() {
            }).get();
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
        } catch(ExecutionException e){
            throw new RuntimeException(e);
        }
        throw new IllegalStateException();
    }

    public <T> void registerForMessages(String dest,Class <T> type,Consumer<T> consumer){
        session.subscribe(dest, new StompFrameHandler() {
            @Override
            public @NotNull Type getPayloadType(@NotNull StompHeaders headers) {
                return type;
            }

            @Override
            @SuppressWarnings("unchecked")
            public void handleFrame(@NotNull StompHeaders headers, Object payload) {
                consumer.accept((T) payload);
            }
        });
    }

    public void send(String dest, Object o){
        session.send(dest,o);
    }
}
