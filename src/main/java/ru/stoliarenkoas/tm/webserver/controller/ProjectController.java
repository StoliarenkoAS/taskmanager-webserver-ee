package ru.stoliarenkoas.tm.webserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import org.w3c.dom.Attr;
import ru.stoliarenkoas.tm.webserver.Attributes;
import ru.stoliarenkoas.tm.webserver.Status;
import ru.stoliarenkoas.tm.webserver.api.service.ProjectService;
import ru.stoliarenkoas.tm.webserver.api.service.SessionService;
import ru.stoliarenkoas.tm.webserver.api.service.UserService;
import ru.stoliarenkoas.tm.webserver.entity.Project;
import ru.stoliarenkoas.tm.webserver.entity.Session;
import ru.stoliarenkoas.tm.webserver.entity.User;
import ru.stoliarenkoas.tm.webserver.service.SessionServiceImpl;
import ru.stoliarenkoas.tm.webserver.service.UserServiceImpl;
import ru.stoliarenkoas.tm.webserver.util.CypherUtil;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Map;

@Controller
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

    @Autowired
    private SessionService sessionService;

    @GetMapping("/list")
    public String getProjectList(Model model, HttpSession httpSession) {
        System.out.println("project-list");
        try{
            final Session session = sessionService.getById((String) httpSession.getAttribute(Attributes.SESSION_ID));
            if (session == null) return "index";

            final User user = userService.get(session, session.getUserId());
            if (user == null) return "index";

            final Collection<Project> projectList = projectService.getAll(session);
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
            final Session session = sessionService.getById((String) httpSession.getAttribute(Attributes.SESSION_ID));
            if (session == null) throw new Exception("not authorized");

            final User user = userService.get(session, session.getUserId());
            if (user == null) throw new Exception("forbidden action");

            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            final Project newProject = new Project();
            newProject.setUserId(user.getId());
            newProject.setName(requestParams.get(Attributes.NAME));
            newProject.setDescription(requestParams.get(Attributes.DESCRIPTION));
            newProject.setStartDate(format.parse(requestParams.get(Attributes.START_DATE)));
            newProject.setEndDate(format.parse(requestParams.get(Attributes.END_DATE)));
            System.out.println("New project before saving: " + newProject);

            projectService.save(session, newProject);
        } catch (Exception e) {
            e.printStackTrace();
            return new RedirectView("/");
        }
        return new RedirectView("/project/list");
    }

    @GetMapping("/edit")
    public String getProjectEditPage(Model model, @RequestParam(Attributes.PROJECT_ID) String projectId, HttpSession httpSession) {
        System.out.println("project-edit[get]");
        try {
            final Session session = sessionService.getById((String) httpSession.getAttribute(Attributes.SESSION_ID));
            if (session == null) throw new Exception("not authorized");

            final User user = userService.get(session, session.getUserId());
            if (user == null) throw new Exception("forbidden action");

            final Project project = projectService.get(session, projectId);
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
            final Session session = sessionService.getById((String) httpSession.getAttribute(Attributes.SESSION_ID));
            if (session == null) throw new Exception("not authorized");

            final User user = userService.get(session, session.getUserId());
            if (user == null) throw new Exception("forbidden action");

            final Project editableProject = projectService.get(session, requestParams.get(Attributes.PROJECT_ID));
            if (editableProject == null) throw new Exception("invalid project");

            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            editableProject.setName(requestParams.get(Attributes.NAME));
            editableProject.setDescription(requestParams.get(Attributes.DESCRIPTION));
            editableProject.setStatus(Status.valueOf(requestParams.get(Attributes.STATUS)));
            editableProject.setStartDate(format.parse(requestParams.get(Attributes.START_DATE)));
            editableProject.setEndDate(format.parse(requestParams.get(Attributes.END_DATE)));

            projectService.save(session, editableProject);
        } catch (Exception e) {
            e.printStackTrace();
            return new RedirectView("/");
        }
        return new RedirectView("/project/list");
    }

    @PostMapping("/remove")
    public RedirectView removeProject(@RequestParam(Attributes.PROJECT_ID) String projectId, HttpSession httpSession) {
        try {
            final Session session = sessionService.getById((String) httpSession.getAttribute(Attributes.SESSION_ID));
            if (session == null) throw new Exception("not authorized");

            final User user = userService.get(session, session.getUserId());
            if (user == null) throw new Exception("forbidden action");

            projectService.delete(session, projectId);
        } catch (Exception e) {
            e.printStackTrace();
            return new RedirectView("/");
        }
        return new RedirectView("/project/list");
    }

}
