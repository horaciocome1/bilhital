package com.company.interfaces;

import com.company.model.bean.Event;
import com.company.model.bean.Ticket;

import java.io.IOException;
import java.util.List;

public interface DataFileHandler {

    void setupArchive() throws IOException;

    List<Event> readEvents() throws IOException;

    List<Event> readUpCommingEvents() throws IOException;

    List<Event> readPastEvents() throws IOException;

    void writeEvent(Event event) throws IOException;

    int generateId(String filePath) throws IOException;

    List<Ticket> readEventTickets(int eventId) throws IOException;

    int countEventSoldTickets(int eventId) throws IOException;

    String writeTicket(Ticket ticket, int eventId) throws IOException;

    boolean isTicketValid(String key) throws IOException;

    void invalidateTicket(String key) throws IOException;

}
