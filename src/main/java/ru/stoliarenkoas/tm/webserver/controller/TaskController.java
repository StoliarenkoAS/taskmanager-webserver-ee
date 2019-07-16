package ru.stoliarenkoas.tm.webserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import ru.stoliarenkoas.tm.webserver.Attributes;
import ru.stoliarenkoas.tm.webserver.Status;
import ru.stoliarenkoas.tm.webserver.api.service.ProjectService;
import ru.stoliarenkoas.tm.webserver.api.service.SessionService;
import ru.stoliarenkoas.tm.webserver.api.service.TaskService;
import ru.stoliarenkoas.tm.webserver.api.service.UserService;
import ru.stoliarenkoas.tm.webserver.model.dto.ProjectDTO;
import ru.stoliarenkoas.tm.webserver.model.dto.SessionDTO;
import ru.stoliarenkoas.tm.webserver.model.dto.TaskDTO;
import ru.stoliarenkoas.tm.webserver.model.dto.UserDTO;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Map;

@Controller
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

    @Autowired
    private SessionService sessionService;

    @GetMapping("/list")
    public String getTaskList(Model model, HttpSession httpSession) {
        System.out.println("task-list");
        try{
            final SessionDTO session = sessionService.getById((String) httpSession.getAttribute(Attributes.SESSION_ID));
            if (session == null) return "index";

            final UserDTO user = userService.get(session, session.getUserId());
            if (user == null) return "index";

            final Collection<ProjectDTO> projectList = projectService.getAll(session);
            final Collection<TaskDTO> taskList = taskService.getAll(session);
            model.addAttribute(Attributes.TASK_LIST, taskList);
            model.addAttribute(Attributes.PROJECT_LIST, projectList);
            return "tasks";
        } catch (Exception e) {
            e.printStackTrace();
            return "index";
        }
    }

    @PostMapping("/create")
    public RedirectView createTask(@RequestParam Map<String, String> requestParams, HttpSession httpSession) {
        System.out.println("create-task");
        try {
            final SessionDTO session = sessionService.getById((String) httpSession.getAttribute(Attributes.SESSION_ID));
            if (session == null) throw new Exception("not authorized");

            final UserDTO user = userService.get(session, session.getUserId());
            if (user == null) throw new Exception("forbidden action");

            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            final TaskDTO newTask = new TaskDTO();
            newTask.setUserId(user.getId());
            newTask.setProjectId(requestParams.get(Attributes.PROJECT_ID));
            newTask.setName(requestParams.get(Attributes.NAME));
            newTask.setDescription(requestParams.get(Attributes.DESCRIPTION));
            newTask.setStartDate(format.parse(requestParams.get(Attributes.START_DATE)));
            newTask.setEndDate(format.parse(requestParams.get(Attributes.END_DATE)));
            System.out.println("New task before saving: " + newTask);

            taskService.save(session, newTask);
        } catch (Exception e) {
            e.printStackTrace();
            return new RedirectView("/");
        }
        return new RedirectView("/task/list");
    }

    @GetMapping("/edit")
    public String getTaskEditPage(Model model, @RequestParam(Attributes.TASK_ID) String taskId, HttpSession httpSession) {
        System.out.println("task-edit[get]");
        try {
            final SessionDTO session = sessionService.getById((String) httpSession.getAttribute(Attributes.SESSION_ID));
            if (session == null) throw new Exception("not authorized");

            final UserDTO user = userService.get(session, session.getUserId());
            if (user == null) throw new Exception("forbidden action");

            final TaskDTO task = taskService.get(session, taskId);
            if (task == null) return "index";
            model.addAttribute(Attributes.TASK, task);
            return "task-edit";
        } catch (Exception e) {
            e.printStackTrace();
            return "index";
        }
    }

    @PostMapping("/edit")
    public RedirectView editTask(@RequestParam Map<String, String> requestParams, HttpSession httpSession) {
        System.out.println("task-edit[post]");
        try {
            final SessionDTO session = sessionService.getById((String) httpSession.getAttribute(Attributes.SESSION_ID));
            if (session == null) throw new Exception("not authorized");

            final UserDTO user = userService.get(session, session.getUserId());
            if (user == null) throw new Exception("forbidden action");

            final TaskDTO editableTask = taskService.get(session, requestParams.get(Attributes.TASK_ID));
            if (editableTask == null) throw new Exception("invalid task");

            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            editableTask.setName(requestParams.get(Attributes.NAME));
            editableTask.setDescription(requestParams.get(Attributes.DESCRIPTION));
            editableTask.setStatus(Status.valueOf(requestParams.get(Attributes.STATUS)));
            editableTask.setStartDate(format.parse(requestParams.get(Attributes.START_DATE)));
            editableTask.setEndDate(format.parse(requestParams.get(Attributes.END_DATE)));

            taskService.save(session, editableTask);
        } catch (Exception e) {
            e.printStackTrace();
            return new RedirectView("/");
        }
        return new RedirectView("/task/list");
    }

    @PostMapping("/remove")
    public RedirectView removeTask(@RequestParam(Attributes.TASK_ID) String taskId, HttpSession httpSession) {
        try {
            final SessionDTO session = sessionService.getById((String) httpSession.getAttribute(Attributes.SESSION_ID));
            if (session == null) throw new Exception("not authorized");

            final UserDTO user = userService.get(session, session.getUserId());
            if (user == null) throw new Exception("forbidden action");

            taskService.delete(session, taskId);
        } catch (Exception e) {
            e.printStackTrace();
            return new RedirectView("/");
        }
        return new RedirectView("/task/list");
    }

}
