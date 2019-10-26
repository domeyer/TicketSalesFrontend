package at.fhv.TicketSalesFrontend.MainView;

import java.time.LocalDate;

public class EventDto {

    private String actor;
    private String event;
    private String destination;
    private String genre;
    private LocalDate date;


    public EventDto(String actor, String event, String destination, String genre, LocalDate date) {
        this.actor = actor;
        this.event = event;
        this.destination = destination;
        this.genre = genre;
        this.date = date;
    }

    public String getActor() {
        return actor;
    }

    public String getEvent() {
        return event;
    }

    public String getDestination() {
        return destination;
    }

    public String getGenre() {
        return genre;
    }

    public LocalDate getDate() {
        return date;
    }


}
