package ca.papercrane.api.service;

import ca.papercrane.api.request.TimeOffRequest;

import java.time.LocalDate;
import java.util.List;

public interface TimeOffService {

    List<TimeOffRequest> getAll();

    TimeOffRequest getByTimeOffId(Integer timeOffId);

    List<TimeOffRequest> getAllByEmployeeId(Integer userId);

    Integer create(Integer employeeId, LocalDate startDate, LocalDate endDate, String reason);

    Integer create(TimeOffRequest request);

    void update(Integer timeOffId, TimeOffRequest request);

    void save(TimeOffRequest request);

    void deleteByTimeOffId(Integer timeOffId);

    Long totalCount();

}