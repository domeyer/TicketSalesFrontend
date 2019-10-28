package at.fhv.ticketsales.service;

import at.fhv.ticketsales.dto.EventDto;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface DisplayEventsService extends Remote {

    public List<EventDto> getAllEvents() throws RemoteException;
}
