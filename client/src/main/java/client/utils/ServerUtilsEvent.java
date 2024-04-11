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
import com.fasterxml.jackson.databind.MapperFeature;
import commons.*;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class ServerUtilsEvent {
    private static final String SERVER = Config.get().getHost();

    private @NotNull String getServer() {
        return Config.get().getHost();
    }

    public Event getByID(String id) {
        try {
            return ClientBuilder.newClient(new ClientConfig()) //
                    .target(SERVER).path("api/events/" + id) //
                    .request(APPLICATION_JSON) //
                    .accept(APPLICATION_JSON) //
                    .get(new GenericType<>() {
                    });
        } catch (NotFoundException e) {
            System.out.println("NOT_FOUND: Event with ID:" + id + " was not found");
            return null;
        }
    }

    public List<Expense> getExpensesByEventId(Event event){
        try{
           return ClientBuilder.newClient(new ClientConfig())
                   .target(SERVER).path("api/expenses/event/" + event.getId())
                   .request(APPLICATION_JSON)
                   .accept(APPLICATION_JSON)
                   .get(new GenericType<>(){
                   });
       }catch(BadRequestException | NotFoundException e){
            System.out.println("NOT_FOUND || BAD_REQUEST: Expenses by EventID:" + event.getId());
           return null;
       }
    }



    public Participant getParticipantByID(Long id) {
        try {
            return ClientBuilder.newClient(new ClientConfig()) //
                    .target(SERVER).path("api/participants/" + id) //
                    .request(APPLICATION_JSON) //
                    .accept(APPLICATION_JSON) //
                    .get(new GenericType<>() {
                    });
        } catch (NotFoundException e) {
            System.out.println("NOT_FOUND: Participant with ID:" + id + " was not found");
            return null;
        }
    }

    public Event addEvent(Event event) {
        Event saved = ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/events") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(event, APPLICATION_JSON), Event.class);

        Config.get().addPastCode(String.valueOf(saved.getId()));
        return saved;
    }

    public Event addJsonEvent(Event event) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/events") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(event, APPLICATION_JSON), Event.class);
    }

    public Expense addExpense(Expense expense, Event event) {
        try {
            return ClientBuilder.newClient(new ClientConfig()) //
                    .target(getServer()).path("/api/expenses/event/" + event.getId()) //
                    .request(APPLICATION_JSON) //
                    .accept(APPLICATION_JSON) //
                    .post(Entity.entity(expense, APPLICATION_JSON), Expense.class);
        } catch (BadRequestException | NotFoundException e) {
            System.out.println("NOT_FOUND || BAD_REQUEST: while adding Expense for Event: " + event.getId() + '\n' + expense);
            return null;
        }
    }

    public Tag addTag(Tag food, Event event) {
        try{
            return ClientBuilder.newClient(new ClientConfig())
                    .target(getServer()).path("/api/tags/event/" + event.getId())
                    .request(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .post(Entity.entity(food, APPLICATION_JSON), Tag.class);
        }catch (BadRequestException | NotFoundException e){
            System.out.println("NOT_FOUND || BAD_REQUEST: while adding Tag for Event: " + event.getId());
            return null;
        }
    }

    public Event editEventTitle(String editedTitle, Event event) {
        try {
            return ClientBuilder.newClient(new ClientConfig())
                    .target(getServer()).path("/api/events/" + event.getId()+ "/title")
                    .request(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .put(Entity.entity(editedTitle, APPLICATION_JSON), Event.class);
        } catch (BadRequestException | NotFoundException e) {
            System.out.println("NOT_FOUND || BAD_REQUEST: while changing title to: " + editedTitle + " at Event: " + event.getId());
            return null;
        }
    }

    public Participant addParticipant(Participant participant, Event event) {
        try {
            return ClientBuilder.newClient(new ClientConfig()) //
                    .target(getServer()).path("/api/participants/event/" + event.getId()) //
                    .request(APPLICATION_JSON) //
                    .accept(APPLICATION_JSON) //
                    .post(Entity.entity(participant, APPLICATION_JSON), Participant.class);
        } catch (BadRequestException | NotFoundException e) {
            System.out.println("NOT_FOUND || BAD_REQUEST: while adding Participant for Event: " + event.getId() + '\n' + participant);
            return null;
        }
    }

    public void deleteEvent(Event event) {
        ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/events/" + event.getId()) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .delete();
    }

    public void deleteParticipant(Participant participant) {
        ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/participants/" + participant.getId()) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .delete();
    }

    public boolean checkToken(String token) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(getServer()).path("api/admin/verify/" + token) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(token, APPLICATION_JSON), Boolean.class);
    }

    public List<Event> getAllEvents() {
        List<Event> events;
        events = ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/events/") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<>() {
                });
        return events;
    }
    public List<Participant> getEventParticipants(Event event) {
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

    public List<Debt> addAllDebts(List<Debt> debts, Event event) {
        List<Debt> savedDebts = new ArrayList<>();
        try {
            for(Debt d : debts) {
                d.setEvent(event);
                Debt saved = ClientBuilder.newClient(new ClientConfig()) //
                        .target(SERVER).path("api/debts/event/" + event.getId()) //
                        .request(APPLICATION_JSON) //
                        .accept(APPLICATION_JSON) //
                        .post(Entity.entity(d, APPLICATION_JSON), Debt.class);
                saved.setEvent(event);
                savedDebts.add(saved);
            }
        } catch(BadRequestException e) {
            System.out.println("Failed to add all debts");
        }
        return savedDebts;
    }

    public List<Debt> getDebtsByEvent(Event event) {
        List<Debt> debts = new ArrayList<>();

        debts.addAll(ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/debts/event/" + event.getId())
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<>() {
                }));

        return debts;
    }

    public List<Debt> getDebtsByCreditor(Participant creditor) {
        List<Debt> debts = new ArrayList<>();

        debts.addAll(ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/debts/creditor/" + creditor.getId())
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<>() {
                }));

        return debts;
    }

    public List<Debt> getDebtsByDebtor(Participant debtor) {
        List<Debt> debts = new ArrayList<>();

        debts.addAll(ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/debts/debtor/" + debtor.getId())
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<>() {
                }));

        return debts;
    }

    public void updateDebtAmount(double amount, Debt debt) {
        ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/debts/" + debt.getId() + "/amount")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .put(Entity.entity(amount, APPLICATION_JSON), Debt.class);
    }

    public void deleteDebt(Debt debt) {
        ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/debts/" + debt.getId())
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .delete();
    }

    public void deleteExpense(Expense expense) {
        ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/expenses/" + expense.getId())
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .delete();
    }

    public Expense getExpenseById(long id) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/expenses/" + id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<>() {
                });
    }

    private final StompSession session = connect("ws://localhost:8080/websocket");

    private StompSession connect(String url) {
        var client = new StandardWebSocketClient();
        var stomp = new WebSocketStompClient(client);
        MappingJackson2MessageConverter x = new MappingJackson2MessageConverter();

        x.getObjectMapper().configure(MapperFeature.USE_ANNOTATIONS, false);
        stomp.setMessageConverter(x);
        try {
            return stomp.connectAsync(url, new StompSessionHandlerAdapter() {
            }).get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        throw new IllegalStateException();
    }

    public <T> void registerForMessages(String dest, Class<T> type, Consumer<T> consumer) {
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

    public void send(String dest, Object o) {
        session.send(dest, o);
    }

    private static final ExecutorService EXEC = Executors.newSingleThreadExecutor();

    public void registerForAddUpdates(Consumer<Event> consumer) {
        EXEC.submit(() -> {
            while (!Thread.interrupted()) {
                var res = ClientBuilder.newClient(new ClientConfig()) //
                        .target(SERVER).path("api/events/addUpdates") //
                        .request(APPLICATION_JSON) //
                        .accept(APPLICATION_JSON) //
                        .get(Response.class);
                if (res.getStatus() == 204) {
                    continue;
                }
                var q = res.readEntity(Event.class);
                consumer.accept(q);
            }
        });
    }

    private static final ExecutorService EXECdel = Executors.newSingleThreadExecutor();

    public void registerForDeleteUpdates(Consumer<Event> consumer) {
        EXECdel.submit(() -> {
            while (!Thread.interrupted()) {
                var res = ClientBuilder.newClient(new ClientConfig()) //
                        .target(SERVER).path("api/events/deleteUpdates") //
                        .request(APPLICATION_JSON) //
                        .accept(APPLICATION_JSON) //
                        .get(Response.class);
                if (res.getStatus() == 204) {
                    continue;
                }
                var q = res.readEntity(Event.class);
                System.out.println(q);
                consumer.accept(q);
            }
        });
    }
    private static final ExecutorService EXECed = Executors.newSingleThreadExecutor();

    public void registerForEditUpdates(Consumer<Event> consumer) {
        EXECed.submit(() -> {
            while (!Thread.interrupted()) {
                var res = ClientBuilder.newClient(new ClientConfig()) //
                        .target(SERVER).path("api/events/editUpdates") //
                        .request(APPLICATION_JSON) //
                        .accept(APPLICATION_JSON) //
                        .get(Response.class);
                if (res.getStatus() == 204) {
                    continue;
                }
                var q = res.readEntity(Event.class);
                System.out.println(q);
                consumer.accept(q);
            }
        });
    }

    public void stop() {
        EXEC.shutdownNow();
        EXECdel.shutdownNow();
        EXECed.shutdownNow();
    }

}
