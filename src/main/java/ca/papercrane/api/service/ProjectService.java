package ca.papercrane.api.service;

import ca.papercrane.api.project.Project;

import java.util.List;

public interface ProjectService {

    List<Project> getAll();

    Project getByProjectId(Integer projectId);

    List<Project> getAllByClientId(Integer clientId);

    Integer addNewProject(Project project);

    void update(Integer projectId, Project project);

    void save(Project project);

    void deleteById(Integer projectId);

    Long totalCount();

}