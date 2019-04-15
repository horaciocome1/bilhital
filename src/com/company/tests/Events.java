package com.company.tests;

import com.company.helpers.DataFileHandler;
import com.company.model.bean.Event;

import java.io.IOException;
import java.time.LocalDateTime;

class Events {

    private static DataFileHandler fileHandler;

    public static void main(String[] args) throws IOException {
        fileHandler = new DataFileHandler();
        fileHandler.setupArchive();
        listEvents();
    }

    private static void listEvents() throws IOException {
        for (Event event: fileHandler.readUpCommingEvents()) {
            System.out.println(event.toString());
        }
    }

    private static void writeEvents() throws IOException {
        Event event = new Event();
        event.setTopic("Historia_das_Ideias");
        event.setCategory("Filosofia");
        event.setDate(LocalDateTime.now());
        fileHandler.writeEvent(event);

        event = new Event();
        event.setTopic("Blockchain_-_Para_alem_de_criptomoedas");
        event.setCategory("Tecnologia");
        event.setDate(LocalDateTime.now());
        fileHandler.writeEvent(event);

        event = new Event();
        event.setTopic("Elsa_Teresa");
        event.setCategory("Filme_-_Comedia");
        event.setDate(LocalDateTime.now());
        fileHandler.writeEvent(event);
    }

}
