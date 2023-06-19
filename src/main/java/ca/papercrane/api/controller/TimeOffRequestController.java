package ca.papercrane.api.controller;

import ca.papercrane.api.exception.ResourceNotFoundException;
import ca.papercrane.api.request.TimeOffRequest;
import ca.papercrane.api.service.impl.TimeOffRequestServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/time_off_requests")
public class TimeOffRequestController {

    private final TimeOffRequestServiceImpl requestService;

    @GetMapping("")
    public ResponseEntity<List<TimeOffRequest>> getAll() {
        try {
            val requestList = requestService.getAll();
            return new ResponseEntity<>(requestList, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Creates a new TimeOffRequest.
     *
     * @param request The new request being created.
     * @return The requests generated requestId.
     */
    @PostMapping("/create")
    public ResponseEntity<Integer> createRequest(@RequestBody TimeOffRequest request) {
        try {
            val createdRequestId = requestService.create(request);
            return new ResponseEntity<>(createdRequestId, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates an existing request.
     *
     * @param requestId The id of the existing request.
     * @param request   The new request details.
     * @return The response of the request.
     */
    @PutMapping("/update/{requestId}")
    public ResponseEntity<HttpStatus> updateRequest(@PathVariable Integer requestId, @RequestBody TimeOffRequest request) {
        try {
            requestService.update(requestId, request);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes a time off request by its corresponding requestId.
     *
     * @param requestId The id of the request being deleted.
     * @return The response status.
     */
    @DeleteMapping("/delete/{requestId}")
    public ResponseEntity<HttpStatus> deleteRequest(@PathVariable Integer requestId) {
        try {
            requestService.deleteByTimeOffId(requestId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Gets a time off request by its request id.
     *
     * @param requestId The time off requests id.
     * @return The found request data.
     */
    @GetMapping("/{requestId}")
    public ResponseEntity<TimeOffRequest> getRequest(@PathVariable Integer requestId) {
        try {
            val request = requestService.getByTimeOffId(requestId);
            return new ResponseEntity<>(request, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}