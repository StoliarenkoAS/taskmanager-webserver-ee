package ru.stoliarenkoas.tm.webserver;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.cdi.Eager;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import ru.stoliarenkoas.tm.webserver.enumerate.Role;
import ru.stoliarenkoas.tm.webserver.model.dto.UserDTO;
import ru.stoliarenkoas.tm.webserver.model.entity.Project;
import ru.stoliarenkoas.tm.webserver.model.entity.Task;
import ru.stoliarenkoas.tm.webserver.model.entity.User;
import ru.stoliarenkoas.tm.webserver.repository.ProjectRepositoryPageable;
import ru.stoliarenkoas.tm.webserver.repository.TaskRepositoryPageable;
import ru.stoliarenkoas.tm.webserver.repository.UserRepositoryPageable;
import ru.stoliarenkoas.tm.webserver.util.CypherUtil;

@Eager
public class DataGenerator {

    private static final User ADMIN = new User();
    private static final User USER = new User();
    private static final User USER_TO_DELETE = new User();

    private static final Project NO_TASKS_PROJECT = new Project();
    private static final Project ONE_TASK_PROJECT = new Project();
    private static final Project TWO_TASKS_PROJECT = new Project();

    private static final Task SINGLE_TASK = new Task();
    private static final Task FIRST_OF_TWO_TASKS = new Task();
    private static final Task SECOND_OF_TWO_TASKS = new Task();

    private final UserRepositoryPageable userRepository;
    private final ProjectRepositoryPageable projectRepository;
    private final TaskRepositoryPageable taskRepository;


    @Autowired
    public DataGenerator(UserRepositoryPageable userRepository,
                         ProjectRepositoryPageable projectRepository,
                         TaskRepositoryPageable taskRepository) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;

        generateUsers();
        generateProjects();
        generateTasks();
    }

    private void generateUsers() {

        ADMIN.setId(DataConstants.ADMIN_ID);
        ADMIN.setLogin(DataConstants.ADMIN_LOGIN);
        ADMIN.setPasswordHash(CypherUtil.getMd5(DataConstants.PASSWORD));
        ADMIN.setRole(Role.ADMIN);
        userRepository.save(ADMIN);

        USER.setId(DataConstants.USER_ID);
        USER.setLogin(DataConstants.USER_LOGIN);
        USER.setPasswordHash(CypherUtil.getMd5(DataConstants.PASSWORD));
        USER.setRole(Role.USER);
        userRepository.save(USER);

        USER_TO_DELETE.setId(DataConstants.USER_TO_DELETE_ID);
        USER_TO_DELETE.setLogin(DataConstants.USER_TO_DELETE_LOGIN);
        USER_TO_DELETE.setPasswordHash(CypherUtil.getMd5(DataConstants.PASSWORD));
        USER_TO_DELETE.setRole(Role.USER);
        userRepository.save(USER_TO_DELETE);
    }

    private void generateProjects() {
        NO_TASKS_PROJECT.setId(DataConstants.NO_TASKS_PROJECT_ID);
        NO_TASKS_PROJECT.setUser(USER);
        NO_TASKS_PROJECT.setName("No tasks project");
        projectRepository.save(NO_TASKS_PROJECT);

        ONE_TASK_PROJECT.setId(DataConstants.ONE_TASK_PROJECT_ID);
        ONE_TASK_PROJECT.setUser(USER);
        ONE_TASK_PROJECT.setName("Project with one task");
        projectRepository.save(ONE_TASK_PROJECT);

        TWO_TASKS_PROJECT.setId(DataConstants.TWO_TASKS_PROJECT_ID);
        TWO_TASKS_PROJECT.setUser(ADMIN);
        TWO_TASKS_PROJECT.setName("Admin's project with two tasks");
        projectRepository.save(TWO_TASKS_PROJECT);
    }

    private void generateTasks() {
        SINGLE_TASK.setId(DataConstants.SINGLE_TASK_ID);
        SINGLE_TASK.setProject(ONE_TASK_PROJECT);
        SINGLE_TASK.setName("single task for a project");
        taskRepository.save(SINGLE_TASK);

        FIRST_OF_TWO_TASKS.setId(DataConstants.FIRST_OF_TWO_TASKS_ID);
        FIRST_OF_TWO_TASKS.setProject(TWO_TASKS_PROJECT);
        FIRST_OF_TWO_TASKS.setName("first of two project's tasks");
        taskRepository.save(FIRST_OF_TWO_TASKS);

        SECOND_OF_TWO_TASKS.setId(DataConstants.SECOND_OF_TWO_TASKS_ID);
        SECOND_OF_TWO_TASKS.setProject(TWO_TASKS_PROJECT);
        SECOND_OF_TWO_TASKS.setName("second of two project's tasks");
        taskRepository.save(SECOND_OF_TWO_TASKS);
    }

    public static User getADMIN() {
        return ADMIN;
    }

    public static User getUSER() {
        return USER;
    }

    public static Project getNoTasksProject() {
        return NO_TASKS_PROJECT;
    }

    public static Project getOneTaskProject() {
        return ONE_TASK_PROJECT;
    }

    public static Project getTwoTasksProject() {
        return TWO_TASKS_PROJECT;
    }

    public static Task getSingleTask() {
        return SINGLE_TASK;
    }

    public static Task getFirstOfTwoTasks() {
        return FIRST_OF_TWO_TASKS;
    }

    public static Task getSecondOfTwoTasks() {
        return SECOND_OF_TWO_TASKS;
    }

}
