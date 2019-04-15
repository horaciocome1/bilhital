package com.company.helpers;

import com.company.model.bean.Event;
import com.company.model.bean.Ticket;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DataFileHandler implements com.company.interfaces.DataFileHandler {

    private final String APP_DATA_DIR_PATH = "data";
    private final String EVENTS_FILE_PATH = APP_DATA_DIR_PATH + "/events.txt";
    private final String TICKETS_FILE_PATH = APP_DATA_DIR_PATH + "/tickets.txt";
    private final String EVENTS_TICKETS_DIR_PATH = APP_DATA_DIR_PATH + "/events_tickets";
    private final String EVENT_TICKETS_PARTIAL_FILE_PATH = EVENTS_TICKETS_DIR_PATH + "/event_"; // append event id and extension
    private final String EVENTS_INVALID_TICKETS_DIR_PATH = APP_DATA_DIR_PATH + "/invalid_tickets";
    private final String INVALID_TICKETS_PARTIAL_FILE_PATH = EVENTS_INVALID_TICKETS_DIR_PATH + "/invalid_"; // append event id and extension
    private final String TEXT_EXTENSION = ".txt";
    private final String NEW_OBJECT_INDICATOR = "!@#$%^&*()_~<>?{}|";
    private final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private final int TICKET_KEY_LENGTH = 8;

    @Override
    public void setupArchive() throws IOException {
        File file = new File(APP_DATA_DIR_PATH);
        file.mkdir();
        file = new File(EVENTS_FILE_PATH);
        file.createNewFile();
        file = new File(EVENTS_TICKETS_DIR_PATH);
        file.mkdir();
        file = new File(TICKETS_FILE_PATH);
        file.createNewFile();
        file = new File(EVENTS_INVALID_TICKETS_DIR_PATH);
        file.mkdir();
    }

    @Override
    public List<Event> readEvents() throws IOException {
        FileReader fileReader = new FileReader(EVENTS_FILE_PATH);
        BufferedReader reader = new BufferedReader(fileReader);
        List<Event> events = new ArrayList<>();
        Event event;
        String line = reader.readLine();
        while (line != null) {
            if (line.equalsIgnoreCase(NEW_OBJECT_INDICATOR)) {
                event = new Event();
                event.setId(Integer.parseInt(reader.readLine()));
                event.setTopic(reader.readLine());
                event.setDate(LocalDateTime.parse(reader.readLine()));
                event.setCategory(reader.readLine());
                events.add(event);
            }
            line = reader.readLine();
        }
        return events;
    }

    @Override
    public List<Event> readUpCommingEvents() throws IOException {
        FileReader fileReader = new FileReader(EVENTS_FILE_PATH);
        BufferedReader reader = new BufferedReader(fileReader);
        List<Event> events = new ArrayList<>();
        Event event;
        String line = reader.readLine();
        while (line != null) {
            if (line.equalsIgnoreCase(NEW_OBJECT_INDICATOR)) {
                event = new Event();
                event.setId(Integer.parseInt(reader.readLine()));
                event.setTopic(reader.readLine());
                event.setDate(LocalDateTime.parse(reader.readLine()));
                event.setCategory(reader.readLine());
                if (event.isUpComming()) events.add(event);
            }
            line = reader.readLine();
        }
        return events;
    }

    @Override
    public List<Event> readPastEvents() throws IOException {
        FileReader fileReader = new FileReader(EVENTS_FILE_PATH);
        BufferedReader reader = new BufferedReader(fileReader);
        List<Event> events = new ArrayList<>();
        Event event;
        String line = reader.readLine();
        while (line != null) {
            if (line.equalsIgnoreCase(NEW_OBJECT_INDICATOR)) {
                event = new Event();
                event.setId(Integer.parseInt(reader.readLine()));
                event.setTopic(reader.readLine());
                event.setDate(LocalDateTime.parse(reader.readLine()));
                event.setCategory(reader.readLine());
                if (!event.isUpComming()) events.add(event);
            }
            line = reader.readLine();
        }
        return events;
    }

    @Override
    public Event readEvents(int eventId) throws IOException {
        FileReader fileReader = new FileReader(EVENTS_FILE_PATH);
        BufferedReader reader = new BufferedReader(fileReader);
        Event event;
        String line = reader.readLine();
        while (line != null) {
            if (line.equalsIgnoreCase(NEW_OBJECT_INDICATOR)) {
                event = new Event();
                event.setId(Integer.parseInt(reader.readLine()));
                event.setTopic(reader.readLine());
                event.setDate(LocalDateTime.parse(reader.readLine()));
                event.setCategory(reader.readLine());
                if (event.getId() == eventId)
                    return event;
            }
            line = reader.readLine();
        }
        return null;
    }

    @Override
    public void writeEvent(Event event) throws IOException {
        FileWriter fileWriter = new FileWriter(EVENTS_FILE_PATH, true);
        BufferedWriter writer = new BufferedWriter(fileWriter);
        writer.write(NEW_OBJECT_INDICATOR);
        writer.newLine();
        writer.write(String.valueOf(generateId(EVENTS_FILE_PATH)));
        writer.newLine();
        writer.write(event.getTopic());
        writer.newLine();
        writer.write(event.getDate().toString());
        writer.newLine();
        writer.write(event.getCategory());
        writer.newLine();
        writer.close();
        fileWriter.close();
    }

    @Override
    public int generateId(String filePath) throws IOException {
        FileReader fileReader = new FileReader(filePath);
        BufferedReader reader = new BufferedReader(fileReader);
        int id = 0;
        String line = reader.readLine();
        while (line != null) {
            if (line.equalsIgnoreCase(NEW_OBJECT_INDICATOR))
                id = Integer.parseInt(reader.readLine());
            line = reader.readLine();
        }
        return id + 1;
    }

    @Override
    public List<Ticket> readEventTickets(int eventId) throws IOException {
        List<Ticket> tickets = new ArrayList<>();
        FileReader fileReader = new FileReader(EVENT_TICKETS_PARTIAL_FILE_PATH + eventId + TEXT_EXTENSION);
        BufferedReader reader = new BufferedReader(fileReader);
        String line = reader.readLine();
        while (line != null) {
            if (line.equalsIgnoreCase(NEW_OBJECT_INDICATOR)) {
                Ticket ticket = new Ticket();
                ticket.setId(Integer.parseInt(reader.readLine()));
                ticket.setOrderName(reader.readLine());
                ticket.setDate(LocalDateTime.parse(reader.readLine()));
                tickets.add(ticket);
            }
            line = reader.readLine();
        }
        reader.close();
        fileReader.close();
        return tickets;
    }

    @Override
    public int countEventSoldTickets(int eventId) throws IOException {
        int j = 0;
        try {
            FileReader fileReader = new FileReader(EVENT_TICKETS_PARTIAL_FILE_PATH + eventId + TEXT_EXTENSION);
            BufferedReader reader = new BufferedReader(fileReader);
            String line = reader.readLine();
            while (line != null) {
                if (line.equalsIgnoreCase(NEW_OBJECT_INDICATOR)) j++;
                line = reader.readLine();
            }
            reader.close();
            fileReader.close();
        } catch (FileNotFoundException ex) {
            j = 0;
        }
        return j;
    }

    @Override
    public Ticket readTicket(int tickedId) {
        return null;
    }

    @Override
    public String writeTicket(Ticket ticket, int eventId) throws IOException {
        FileWriter fileWriter = new FileWriter(EVENT_TICKETS_PARTIAL_FILE_PATH + eventId + TEXT_EXTENSION,
                true);
        BufferedWriter writer = new BufferedWriter(fileWriter);
        writer.write(NEW_OBJECT_INDICATOR);
        int ticketId = generateId(EVENT_TICKETS_PARTIAL_FILE_PATH + eventId + TEXT_EXTENSION);
        writer.newLine();
        writer.write(String.valueOf(ticketId));
        writer.newLine();
        writer.write(ticket.getOrderName());
        writer.newLine();
        writer.write(ticket.getDate().toString());
        writer.newLine();
        writer.close();
        fileWriter.close();

        fileWriter = new FileWriter(TICKETS_FILE_PATH, true);
        writer = new BufferedWriter(fileWriter);
        String ticketKey = generateKey();
        while (!isKeyUnique(ticketKey)) ticketKey = generateKey();
        writer.write(NEW_OBJECT_INDICATOR);
        writer.newLine();
        writer.write(ticketKey);
        writer.newLine();
        writer.write(String.valueOf(eventId));
        writer.newLine();
        writer.write(String.valueOf(ticketId));
        writer.newLine();
        writer.close();
        fileWriter.close();

        return ticketKey;
    }

    @Override
    public boolean isTicketValid(String key) throws IOException {
        FileReader fileReader = new FileReader(TICKETS_FILE_PATH);
        BufferedReader reader = new BufferedReader(fileReader);
        String line = reader.readLine();
        while (line != null) {
            if (line.equalsIgnoreCase(key)) {
                reader.close();
                fileReader.close();
                fileReader = new FileReader(INVALID_TICKETS_PARTIAL_FILE_PATH + getEventIdFromKey(key) +
                        TEXT_EXTENSION);
                reader = new BufferedReader(fileReader);
                line = reader.readLine();
                while (line != null) {
                    if (line.equalsIgnoreCase(key)) {
                        reader.close();
                        fileReader.close();
                        return false;
                    }
                    line = reader.readLine();
                }
                reader.close();
                fileReader.close();
                return true;
            }
            line = reader.readLine();
        }
        reader.close();
        fileReader.close();
        return false;
    }

    @Override
    public void invalidateTicket(String key) throws IOException {
        FileWriter fileWriter = new FileWriter(INVALID_TICKETS_PARTIAL_FILE_PATH + getEventIdFromKey(key) +
                TEXT_EXTENSION, true);
        BufferedWriter writer = new BufferedWriter(fileWriter);
        writer.write(key);
        writer.newLine();
        writer.close();
        fileWriter.close();
    }

    private String generateKey() {
        int count = TICKET_KEY_LENGTH;
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int characterPosition = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(characterPosition));
        }
        return builder.toString();
    }

    private boolean isKeyUnique(String key) throws IOException {
        FileReader fileReader = new FileReader(TICKETS_FILE_PATH);
        BufferedReader reader = new BufferedReader(fileReader);
        String line = reader.readLine();
        while (line != null) {
            if (line.equalsIgnoreCase(key)) {
                reader.close();
                fileReader.close();
                return false;
            }
            line = reader.readLine();
        }
        reader.close();
        fileReader.close();
        return true;
    }

    private int getEventIdFromKey(String key) throws IOException {
        FileReader fileReader = new FileReader(TICKETS_FILE_PATH);
        BufferedReader reader = new BufferedReader(fileReader);
        int eventId = -1;
        String line = reader.readLine();
        while (line != null) {
            if (line.equalsIgnoreCase(key)) {
                eventId = Integer.parseInt(reader.readLine());
                break;
            }
            line = reader.readLine();
        }
        reader.close();
        fileReader.close();
        return eventId;
    }

}

