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
import ru.stoliarenkoas.tm.webserver.api.service.TaskServicePageable;
import ru.stoliarenkoas.tm.webserver.api.service.UserServicePageable;
import ru.stoliarenkoas.tm.webserver.model.dto.ProjectDTO;
import ru.stoliarenkoas.tm.webserver.model.dto.TaskDTO;
import ru.stoliarenkoas.tm.webserver.model.dto.UserDTO;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskServicePageable taskService;

    @Autowired
    private ProjectServicePageable projectService;

    @Autowired
    private UserServicePageable userService;

    @GetMapping("/list")
    public String getTaskList(
            @NotNull final Model model,
            @RequestParam(name = Attributes.PAGE, required = false) Integer pageNumber,
            @NotNull final HttpSession httpSession) {
        System.out.println("task-list");
        try{
            final String loggedUserId = (String) httpSession.getAttribute(Attributes.USER_ID);
            final UserDTO user = userService.findOne(loggedUserId, loggedUserId);
            if (user == null) return "index";

            if (pageNumber == null) pageNumber = 0;
            model.addAttribute(Attributes.PAGE, pageNumber);
            final PageRequest page = PageRequest.of(pageNumber, 1);
            final List<ProjectDTO> projectList = projectService.findAllByUserId(user.getId());
            final Page<TaskDTO> taskList = taskService.findAllByUserId(user.getId(), page);
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
            final String loggedUserId = (String) httpSession.getAttribute(Attributes.USER_ID);
            final UserDTO user = userService.findOne(loggedUserId, loggedUserId);
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

            taskService.persist(user.getId(), newTask);
        } catch (Exception e) {
            e.printStackTrace();
            return new RedirectView("/");
        }
        return new RedirectView("/task/list");
    }

    @GetMapping("/edit")
    public String getTaskEditPage(
            final Model model,
            @RequestParam(Attributes.TASK_ID) final String taskId,
            final HttpSession httpSession) {
        System.out.println("task-edit[get]");
        try {
            final String loggedUserId = (String) httpSession.getAttribute(Attributes.USER_ID);
            final UserDTO user = userService.findOne(loggedUserId, loggedUserId);;
            if (user == null) throw new Exception("forbidden action");

            final TaskDTO task = taskService.findOne(user.getId(), taskId);
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
            final String loggedUserId = (String) httpSession.getAttribute(Attributes.USER_ID);
            final UserDTO user = userService.findOne(loggedUserId, loggedUserId);
            if (user == null) throw new Exception("forbidden action");

            final TaskDTO editableTask = taskService.findOne(user.getId(), requestParams.get(Attributes.TASK_ID));
            if (editableTask == null) throw new Exception("invalid task");

            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            editableTask.setName(requestParams.get(Attributes.NAME));
            editableTask.setDescription(requestParams.get(Attributes.DESCRIPTION));
            editableTask.setStatus(Status.valueOf(requestParams.get(Attributes.STATUS)));
            editableTask.setStartDate(format.parse(requestParams.get(Attributes.START_DATE)));
            editableTask.setEndDate(format.parse(requestParams.get(Attributes.END_DATE)));

            taskService.merge(user.getId(), editableTask);
        } catch (Exception e) {
            e.printStackTrace();
            return new RedirectView("/");
        }
        return new RedirectView("/task/list");
    }

    @PostMapping("/remove")
    public RedirectView removeTask(@RequestParam(Attributes.TASK_ID) String taskId, HttpSession httpSession) {
        try {
            final String loggedUserId = (String) httpSession.getAttribute(Attributes.USER_ID);
            final UserDTO user = userService.findOne(loggedUserId, loggedUserId);
            if (user == null) throw new Exception("forbidden action");

            taskService.remove(user.getId(), taskId);
        } catch (Exception e) {
            e.printStackTrace();
            return new RedirectView("/");
        }
        return new RedirectView("/task/list");
    }

}
