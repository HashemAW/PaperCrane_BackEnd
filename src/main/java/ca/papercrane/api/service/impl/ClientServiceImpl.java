package ca.papercrane.api.service.impl;

import ca.papercrane.api.entity.Client;
import ca.papercrane.api.entity.role.UserRole;
import ca.papercrane.api.exception.ResourceNotFoundException;
import ca.papercrane.api.repository.ClientRepository;
import ca.papercrane.api.service.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Override
    public List<Client> getAll() throws ResourceNotFoundException {
        val clientList = clientRepository.findAll().stream().filter(e -> e.getRole().equals(UserRole.CLIENT)).collect(Collectors.toList());
        if (clientList.isEmpty()) {
            throw new ResourceNotFoundException("No clients found!");
        }
        return clientList;
    }

    @Override
    public Client getByUserId(Integer userId) {
        return clientRepository.findByUserId(userId).orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + userId));
    }

    @Override
    public Client getByEmail(String email) {
        return clientRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Client not found with email: " + email));
    }

    @Override
    public Integer addNewClient(Client client) {
        val clientOptional = clientRepository.findByEmail(client.getEmail());
        if (clientOptional.isPresent()) {
            throw new IllegalArgumentException("Email already taken.");
        }
        val savedClient = clientRepository.save(client);
        return savedClient.getUserId();
    }

    @Override
    @Transactional
    public void update(Integer userId, Client client) {
        val existingClient = getByUserId(userId);
        if (client.getClientName() != null && client.getClientName().length() > 0 && !Objects.equals(existingClient.getClientName(), client.getClientName())) {
            existingClient.setClientName(client.getClientName());
        }
        if (client.getCompanyName() != null && client.getCompanyName().length() > 0 && !Objects.equals(existingClient.getCompanyName(), client.getCompanyName())) {
            existingClient.setCompanyName(client.getCompanyName());
        }
        if (client.getEmail() != null && client.getEmail().length() > 0 && !Objects.equals(existingClient.getEmail(), client.getEmail())) {
            final Optional<Client> clientOptional = clientRepository.findByEmail(client.getEmail());
            if (clientOptional.isPresent()) {
                throw new IllegalArgumentException("Email is already taken.");
            }
            existingClient.setEmail(client.getEmail());
        }
        if (client.getPassword() != null && client.getPassword().length() > 0 && !Objects.equals(existingClient.getPassword(), client.getPassword())) {
            existingClient.setPassword(client.getPassword());
        }
        save(existingClient);
    }

    @Override
    public void save(Client client) {
        clientRepository.save(client);
    }

    @Override
    public void deleteByUserId(Integer userId) {
        clientRepository.findByUserId(userId).ifPresentOrElse(clientRepository::delete, () -> {
            throw new ResourceNotFoundException("Client not found for ID: " + userId);
        });
    }

    @Override
    public boolean exists(String email) {
        return clientRepository.findByEmail(email).isPresent();
    }

    @Override
    public Long totalCount() {
        return clientRepository.count();
    }

}