package com.company;

import com.company.helpers.DataFileHandler;
import com.company.interfaces.UserInteractions;
import com.company.model.bean.Event;
import com.company.model.bean.Ticket;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Main implements UserInteractions {

    private final static int VERIFICAR = 1;
    private final static int EVENTOS = 2;
    private final static int BILHETES = 3;
    private final static int RELATORIOS = 4;
    private final static int SOBRE = 5;
    private final static int FECHAR = 6;

    private final static String WELCOME_MESSAGE = " ################################################## " +
            "\n ########### Seja bem vindo ao Bilhital ########### " +
            "\n ################################################## " +
            "\n\n Obs: Por-favor, escolha sempre apenas uma das opcoes disponiveis, caso contrario tera que re-comecar.";

    private final static String MAIN_OPTIONS = "\n\n\n1. Verificar" +
            "\n2. Eventos" +
            "\n3. Bilhetes" +
            "\n4. Relatorios" +
            "\n5. Sobre" +
            "\n6. Fechar" +
            "\n > ";

    private final static String EVENTS_OPTIONS = "\n1. Cadastrar" +
            "\n2. Listar" +
            "\n > ";

    private final static String TICKETS_OPTIONS = "\n1. Vender" +
            "\n2. Recuperar" +
            "\n > ";

    private final static String LICENCE = "   Copyright 2018 Horácio Flávio Comé Júnior\n" +
            "\n" +
            "   Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
            "   you may not use this file except in compliance with the License.\n" +
            "   You may obtain a copy of the License at\n" +
            "\n" +
            "       http://www.apache.org/licenses/LICENSE-2.0\n" +
            "\n" +
            "   Unless required by applicable law or agreed to in writing, software\n" +
            "   distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
            "   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
            "   See the License for the specific language governing permissions and\n" +
            "   limitations under the License.";

    private static Scanner mReader;

    private static DataFileHandler mFileHandler;

    private final static String GOODBYE_MESSAGE = "\n\n . . . Volte sempre. :)";

    public static void main(String[] args) {
	// write your code here
        System.out.println(WELCOME_MESSAGE);
        boolean leave = false;
        mReader = new Scanner(System.in);
        mFileHandler = new DataFileHandler();
        try {
            mFileHandler.setupArchive();
        } catch (IOException e) {
            System.out.println("Ocorreu um erro fatal. Por-favor tente novamente mais tarde.");
            leave = true;
        }
        while (!leave) {
            System.out.print(MAIN_OPTIONS);
            switch (mReader.nextInt()) {
                case VERIFICAR:
                    verifyTicket();
                    break;
                case EVENTOS:
                    System.out.print(EVENTS_OPTIONS);
                    switch (mReader.nextInt()) {
                        case 1:
                            addEvent();
                            break;
                        case 2:
                            listEvents();
                            break;
                    }
                    break;
                case BILHETES:
                    System.out.print(TICKETS_OPTIONS);
                    switch (mReader.nextInt()) {
                        case 1:
                            sellTicket();
                            break;
                        case 2:
                            recoverTicket();
                            break;
                    }
                    break;
                case RELATORIOS:
                    displayReport();
                    break;
                case SOBRE:
                    System.out.println("\n" + LICENCE +
                            "Para mais informacoes visite https://www.github.com/horaciocome1/bilhital");
                    System.out.print("Pressione uma tecla, seguida de ENTER para continuar ... ");
                    break;
                case FECHAR:
                    System.out.println(GOODBYE_MESSAGE);
                    leave = true;
                    break;
            }
        }

    }

    private static void verifyTicket() {
        System.out.print("\nChave do bilhete > ");
        String key = mReader.next();
        try {
            if (mFileHandler.isTicketValid(key)) {
                mFileHandler.invalidateTicket(key);
                System.out.print("Bilhete valido!");
            }
            else System.out.print("Bilhete invalido!");
            System.out.print(" Pressione uma tecla, seguidade de ENTER para continuar ... ");
            mReader.next();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addEvent() {
        Event event = new Event();
        System.out.print("Tema > ");
        event.setTopic(mReader.next());
        System.out.print("Dia (yyyy-MM-dd) > ");
        String date = mReader.next();
        System.out.println(date);
        System.out.print("Hora (HH:mm) > ");
        date += " " + mReader.next() + ":00.000";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss.SSS");
        event.setDate(LocalDateTime.parse(date, formatter));
        System.out.print("Categoria > ");
        event.setCategory(mReader.next());

        System.out.print("\nEstes dados serao persistidos e poderao ser alterados mais tarde." +
                "\n1. Ok" +
                "\n2. Cancelar" +
                "\n > ");
        switch (mReader.nextInt()) {
            case 1:
                try {
                    mFileHandler.writeEvent(event);
                } catch (IOException e) {
                    System.out.println("Ocorreu um erro na escrita dos dados." +
                            "\nPor-favor tente novamente mais tarde.");
                }
                break;
        }
    }

    private static void listEvents() {
        System.out.print("\n1. Todos" +
                "\n2. Segundo a categoria" +
                "\n3. Proximos eventos" +
                "\n > ");
        switch (mReader.nextInt()) {
            case 1:
                try {
                    List<Event> events = mFileHandler.readEvents();
                    System.out.println("\n ################ Todos Eventos ################ \n");
                    for (int i = 0; i < events.size(); i++) {
                        System.out.println(" " + i + ". " + events.get(i).toString());
                    }
                    if (events.size() == 1) System.out.println(" " + events.size() + " evento.");
                    else System.out.println(" " + events.size() + " eventos.");
                    System.out.println("\n ############################################### ");
                    System.out.print("Pressione qualquer tecla para continuar ... ");
                    mReader.next();
                } catch (IOException e) {
                    System.out.println("Ocoreu um erro durante a leitura. Por-favor tente mais tarde.");
                }
                break;
            case 2:
                try {
                    List<Event> events = mFileHandler.readEvents();
                    List<String> categorias = new ArrayList<>();
                    for (Event event : events) {
                        boolean insert = true;
                        for (String categoria : categorias)
                            if (categoria.equalsIgnoreCase(event.getCategory()))
                                insert = false;
                        if (insert)
                            categorias.add(event.getCategory());
                    }
                    System.out.println("\n ################# Categorias ################## \n");
                    for (int i = 0; i < categorias.size(); i++) {
                        System.out.println(i + " - " + categorias.get(i));
                    }
                    System.out.println("\n ############################################### ");
                    System.out.print("Escola uma das categorias acima, e informe o numero correspondente > ");
                    int categoryPosition = mReader.nextInt();

                    int count = 0;
                    System.out.println("\n ################ Proximos Eventos ################ \n");
                    for (int i = 0; i < events.size(); i++) {
                        if (events.get(i).getCategory().equalsIgnoreCase(categorias.get(categoryPosition))) {
                            System.out.println(" " + i + ". " + events.get(i).toString());
                            count++;
                        }
                    }
                    if (count == 1) System.out.println(" " + count + " evento.");
                    else System.out.println(" " + count + " eventos.");
                    System.out.println("\n ################################################# ");
                    System.out.print("Pressione uma tecla e ENTER para continuar ... ");
                    mReader.next();
                } catch (IOException e) {
                    System.out.println("Lamento, ocorreu um erro durante a leitura.");
                }
                break;
            case 3:
                try {
                    List<Event> events = mFileHandler.readUpCommingEvents();
                    System.out.println("\n ################ Proximos Eventos ################ \n");
                    for (int i = 0; i < events.size(); i++) {
                        System.out.println(" " + i + ". " + events.get(i).toString());
                    }
                    if (events.size() == 1) System.out.println(" " + events.size() + " evento.");
                    else System.out.println(" " + events.size() + " eventos.");
                    System.out.println("\n ################################################# ");
                    System.out.print("Pressione qualquer tecla para continuar ... ");
                    mReader.next();
                } catch (IOException e) {
                    System.out.println("Ocoreu um erro durante a leitura. Por-favor tente mais tarde.");
                }
                break;
        }
    }

    private static void sellTicket() {
        try {
            System.out.println("\nPor-favor, Ecolha um dos eventos abaixo:");
            List<Event> events = mFileHandler.readUpCommingEvents();
            for (int i = 0; i < events.size(); i++)
                System.out.println( i + " " + events.get(i).toString());
            System.out.print("Evento > ");
            int eventId = mReader.nextInt();
            System.out.print("Primeiro nome > ");
            String nome = mReader.next();
            System.out.print("Ultimo nome > ");
            nome += " " + mReader.next();
            String key = mFileHandler.writeTicket(new Ticket(nome), eventId);
            System.out.print("Chave de validacao: " + key +
                    "\nPressione uma tecla e clique ENTER para continuar ... ");
            mReader.next();
        } catch (IOException e) {
            System.out.println("Lamento, ocorreu um erro.");
        }
    }

    private static void recoverTicket() {
        System.out.println("\nInforme o evento para o qual pretende recuperar o bilhete");
        try {
            List<Event> events = mFileHandler.readUpCommingEvents();
            System.out.println(" ################ Proximos Eventos ################ \n");
            for (int i = 0; i < events.size(); i++) {
                System.out.println(" " + i + ". " + events.get(i).toString());
            }
            if (events.size() == 1) System.out.println(" " + events.size() + " evento.");
            else System.out.println(" " + events.size() + " eventos.");
            System.out.println(" ################################################# " +
                    "\nEvento > ");
            List<Ticket> tickets = mFileHandler.readEventTickets(mReader.nextInt());
            System.out.println("\n ################ Bilhetes do evento ################ \n");
            for (int i = 0; i < tickets.size(); i++) {
                System.out.println(" " + i + ". " + tickets.get(i).toString());
            }
            if (tickets.size() == 1) System.out.println(" " + tickets.size() + " bilhete.");
            else System.out.println(" " + tickets.size() + " bilhetes.");
            System.out.println("\n ################################################# " +
                    "\nPressione uma tecla seguida de ENTER para continuar ... ");
            mReader.next();
        } catch (IOException e) {
            System.out.println("Lamento, ocorreu um erro durante a leitura. Por-favor, tente novamente mais tarde.");
        }

    }

    private static void displayReport() {
        try {
            List<Event> events = mFileHandler.readUpCommingEvents();
            System.out.println("\n ################ Proximos Eventos ################ \n");
            for (int i = 0; i < events.size(); i++) {
                int no_tickets = mFileHandler.countEventSoldTickets(events.get(i).getId());
                System.out.println(" " + i + ". " + events.get(i).toString() + "    " + "(" + no_tickets + ")");
            }
            if (events.size() == 1) System.out.println(" " + events.size() + " evento.");
            else System.out.println(" " + events.size() + " eventos.");

            events = mFileHandler.readPastEvents();
            System.out.println("\n ################ Eventos passados ################ \n");
            for (int i = 0; i < events.size(); i++) {
                int no_tickets = mFileHandler.countEventSoldTickets(events.get(i).getId());
                System.out.println(" " + i + ". " + events.get(i).toString() + "    " + "(" + no_tickets + ")");
            }
            if (events.size() == 1) System.out.println(" " + events.size() + " evento.");
            else System.out.println(" " + events.size() + " eventos.");
            System.out.print("\n ################################################# ");
            System.out.print("\nPressione uma tecla seguida de ENTER para continuar ... ");
            mReader.next();
        } catch (IOException e) {
            System.out.println("Lamento, ocorreu um erro durante a leitura. Por-favor, tente novamente mais tarde.");
        }
    }

}
