package ru.stoliarenkoas.tm.webserver.controller;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import ru.stoliarenkoas.tm.webserver.Attributes;
import ru.stoliarenkoas.tm.webserver.Status;
import ru.stoliarenkoas.tm.webserver.api.service.ProjectServicePageable;
import ru.stoliarenkoas.tm.webserver.api.service.UserServicePageable;
import ru.stoliarenkoas.tm.webserver.model.dto.ProjectDTO;
import ru.stoliarenkoas.tm.webserver.model.dto.UserDTO;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Map;

@Controller
@RequestMapping("/project")
public class ProjectControllerJSP {

    private ProjectServicePageable projectService;
    @Autowired
    public void setProjectService(ProjectServicePageable projectService) {
        this.projectService = projectService;
    }

    private UserServicePageable userService;
    @Autowired
    public void setUserService(UserServicePageable userService) {
        this.userService = userService;
    }

    @GetMapping("/list")
    public String getProjectList(
            @NotNull final Model model,
            @RequestParam(name = Attributes.PAGE, required = false) Integer pageNumber,
            @NotNull final HttpSession httpSession) {
        System.out.println("project-list");
        try{
            final String loggedUserId = (String) httpSession.getAttribute(Attributes.USER_ID);
            final UserDTO user = userService.findOne(loggedUserId, loggedUserId);
            if (user == null) return "index";

            if (pageNumber == null) pageNumber = 0;
            model.addAttribute(Attributes.PAGE, pageNumber);
            final PageRequest page = PageRequest.of(pageNumber, 1);
            final Page<ProjectDTO> projectList = projectService.findAllByUserId(user.getId(), page);
            model.addAttribute(Attributes.PROJECT_LIST, projectList);
            return "projects";
        } catch (Exception e) {
            e.printStackTrace();
            return "index";
        }
    }

    @PostMapping("/create")
    public RedirectView createProject(@RequestParam Map<String, String> requestParams, HttpSession httpSession) {
        System.out.println("create-project");
        try {
            final String loggedUserId = (String) httpSession.getAttribute(Attributes.USER_ID);
            final UserDTO user = userService.findOne(loggedUserId, loggedUserId);
            if (user == null) throw new Exception("forbidden action");

            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            final ProjectDTO newProject = new ProjectDTO();
            newProject.setUserId(user.getId());
            newProject.setName(requestParams.get(Attributes.NAME));
            newProject.setDescription(requestParams.get(Attributes.DESCRIPTION));
            newProject.setStartDate(format.parse(requestParams.get(Attributes.START_DATE)));
            newProject.setEndDate(format.parse(requestParams.get(Attributes.END_DATE)));
            System.out.println("New project before saving: " + newProject);

            projectService.persist(user.getId(), newProject);
        } catch (Exception e) {
            e.printStackTrace();
            return new RedirectView("/");
        }
        return new RedirectView("/project/list");
    }

    @GetMapping("/edit")
    public String getProjectEditPage(
            @NotNull final Model model,
            @RequestParam(Attributes.PROJECT_ID) String projectId,
            @NotNull final HttpSession httpSession) {
        System.out.println("project-edit[get]");
        try {
            final String loggedUserId = (String) httpSession.getAttribute(Attributes.USER_ID);
            final UserDTO user = userService.findOne(loggedUserId, loggedUserId);
            if (user == null) throw new Exception("forbidden action");

            final ProjectDTO project = projectService.findOne(user.getId(), projectId);
            if (project == null) return "index";
            model.addAttribute(Attributes.PROJECT, project);
            return "project-edit";
        } catch (Exception e) {
            e.printStackTrace();
            return "index";
        }
    }

    @PostMapping("/edit")
    public RedirectView editProject(@RequestParam Map<String, String> requestParams, HttpSession httpSession) {
        System.out.println("project-edit[post]");
        try {
            final String loggedUserId = (String) httpSession.getAttribute(Attributes.USER_ID);
            final UserDTO user = userService.findOne(loggedUserId, loggedUserId);
            if (user == null) throw new Exception("forbidden action");

            final ProjectDTO editableProject;
            editableProject = projectService.findOne(user.getId(), requestParams.get(Attributes.PROJECT_ID));
            if (editableProject == null) throw new Exception("invalid project");

            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            editableProject.setName(requestParams.get(Attributes.NAME));
            editableProject.setDescription(requestParams.get(Attributes.DESCRIPTION));
            editableProject.setStatus(Status.valueOf(requestParams.get(Attributes.STATUS)));
            editableProject.setStartDate(format.parse(requestParams.get(Attributes.START_DATE)));
            editableProject.setEndDate(format.parse(requestParams.get(Attributes.END_DATE)));

            projectService.merge(user.getId(), editableProject);
        } catch (Exception e) {
            e.printStackTrace();
            return new RedirectView("/");
        }
        return new RedirectView("/project/list");
    }

    @PostMapping("/remove")
    public RedirectView removeProject(
            @RequestParam(Attributes.PROJECT_ID) String projectId,
            @NotNull final HttpSession httpSession) {
        try {
            final String loggedUserId = (String) httpSession.getAttribute(Attributes.USER_ID);
            final UserDTO user = userService.findOne(loggedUserId, loggedUserId);
            if (user == null) throw new Exception("forbidden action");

            projectService.remove(user.getId(), projectId);
        } catch (Exception e) {
            e.printStackTrace();
            return new RedirectView("/");
        }
        return new RedirectView("/project/list");
    }

}
