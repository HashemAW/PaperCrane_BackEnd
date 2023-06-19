package ca.papercrane.api.service.impl;

import ca.papercrane.api.exception.ResourceNotFoundException;
import ca.papercrane.api.project.Project;
import ca.papercrane.api.repository.ProjectRepository;
import ca.papercrane.api.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    @Override
    public List<Project> getAll() throws ResourceNotFoundException {
        val projectList = projectRepository.findAll();
        if (projectList.isEmpty()) {
            throw new ResourceNotFoundException("No projects found!");
        }
        return projectList;
    }

    @Override
    public Project getByProjectId(Integer projectId) {
        return projectRepository.findByProjectId(projectId).orElseThrow(() -> new ResourceNotFoundException("Project not found!"));
    }

    @Override
    public List<Project> getAllByClientId(Integer clientId) {
        return projectRepository.findAllByClientId(clientId).orElseThrow(() -> new ResourceNotFoundException("No projects found with clientId!"));
    }

    @Override
    public Integer addNewProject(Project project) {
        val projectOptional = projectRepository.findByProjectId(project.getProjectId());
        if (projectOptional.isPresent()) {
            throw new IllegalArgumentException("Project with id already exists.");
        }
        val savedProject = projectRepository.save(project);
        return savedProject.getProjectId();
    }

    @Override
    public void update(Integer projectId, Project project) {
        val existingProject = getByProjectId(projectId);
        existingProject.setClientId(project.getClientId());
        existingProject.setProjectLeadId(project.getProjectLeadId());
        existingProject.setProjectDescription(project.getProjectDescription());
        projectRepository.save(existingProject);
    }

    @Override
    public void save(Project project) {
        projectRepository.save(project);
    }

    @Override
    public void deleteById(Integer projectId) {
        projectRepository.findByProjectId(projectId).ifPresentOrElse(projectRepository::delete, () -> {
            throw new ResourceNotFoundException("Project not found for ID: " + projectId);
        });
    }

    @Override
    public Long totalCount() {
        return projectRepository.count();
    }

}