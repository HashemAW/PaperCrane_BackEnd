package ca.papercrane.api.service;

import ca.papercrane.api.entity.Client;

import java.util.List;

public interface ClientService {

    List<Client> getAll();

    Client getByUserId(Integer userId);

    Client getByEmail(String email);

    Integer addNewClient(Client client);

    void update(Integer userId, Client client);

    void save(Client client);

    void deleteByUserId(Integer userId);

    boolean exists(String email);

    Long totalCount();

}
