package com.company.tests;

import com.company.helpers.DataFileHandler;
import com.company.model.bean.Ticket;

import java.io.IOException;
import java.util.List;

public class Tickets {

    private static DataFileHandler fileHandler;

    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public static void main(String[] args) throws IOException {
        fileHandler = new DataFileHandler();
        fileHandler.setupArchive();
        for (int i = 0; i < 9999; i++) {
            writeTicket();
            System.out.println(i);
        }
//        System.out.println(generateKey(12).concat(""));
    }

    private static void writeTicket() throws IOException {
        Ticket ticket = new Ticket();
        ticket.setOrderName("Yamk Orpot");
        fileHandler.writeTicket(ticket, 3);
    }

    private static void listTickets(int eventId) throws IOException {
        List<Ticket> tickets = fileHandler.readEventTickets(eventId);
        for (Ticket ticket: tickets) System.out.println(ticket.toString());
    }

    public static String generateKey(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int characterPosition = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(characterPosition));
        }
        return builder.toString();
    }


}
