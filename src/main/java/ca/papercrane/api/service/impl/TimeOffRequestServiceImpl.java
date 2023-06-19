package ca.papercrane.api.service.impl;

import ca.papercrane.api.exception.ResourceNotFoundException;
import ca.papercrane.api.repository.TimeOffRequestRepository;
import ca.papercrane.api.request.TimeOffRequest;
import ca.papercrane.api.service.TimeOffService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeOffRequestServiceImpl implements TimeOffService {

    private final TimeOffRequestRepository requestRepository;

    @Override
    public List<TimeOffRequest> getAll() throws ResourceNotFoundException {
        val requestList = requestRepository.findAll();
        if (requestList.isEmpty()) {
            throw new ResourceNotFoundException("No TimeOffRequests found!");
        }
        return requestList;
    }

    @Override
    public TimeOffRequest getByTimeOffId(Integer timeOffId) throws ResourceNotFoundException {
        return requestRepository.findByTimeOffId(timeOffId).orElseThrow(() -> new ResourceNotFoundException("TimeOffRequest not found with id: " + timeOffId));
    }

    @Override
    public List<TimeOffRequest> getAllByEmployeeId(Integer employeeId) throws ResourceNotFoundException {
        return requestRepository.findAllByEmployeeId(employeeId).orElseThrow(() -> new ResourceNotFoundException("No TimeOffRequests found for id: " + employeeId));
    }

    @Override
    public Integer create(Integer employeeId, LocalDate startDate, LocalDate endDate, String reason) {
        val createdRequest = requestRepository.save(new TimeOffRequest(employeeId, startDate, endDate, reason));
        return createdRequest.getTimeOffId();
    }

    @Override
    public Integer create(TimeOffRequest request) {
        val createdRequest = requestRepository.save(request);
        return createdRequest.getTimeOffId();
    }

    @Override
    public void update(Integer timeOffId, TimeOffRequest request) {
        val existingRequest = getByTimeOffId(timeOffId);
        existingRequest.setReason(request.getReason());
        existingRequest.setStatus(request.getStatus());
        existingRequest.setStartDate(request.getStartDate());
        existingRequest.setEndDate(request.getEndDate());
        save(existingRequest);
    }

    @Override
    public void save(TimeOffRequest request) {
        requestRepository.save(request);
    }

    @Override
    public void deleteByTimeOffId(Integer timeOffId) {
        requestRepository.findByTimeOffId(timeOffId).ifPresentOrElse(requestRepository::delete, () -> {
            throw new ResourceNotFoundException("Request not found for ID: " + timeOffId);
        });
    }

    @Override
    public Long totalCount() {
        return requestRepository.count();
    }

}