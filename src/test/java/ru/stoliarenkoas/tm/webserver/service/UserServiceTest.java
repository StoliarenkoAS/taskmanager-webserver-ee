package ru.stoliarenkoas.tm.webserver.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import ru.stoliarenkoas.tm.webserver.api.service.UserServicePageable;
import ru.stoliarenkoas.tm.webserver.enumerate.Role;
import ru.stoliarenkoas.tm.webserver.exception.AccessForbiddenException;
import ru.stoliarenkoas.tm.webserver.exception.IncorrectDataException;
import ru.stoliarenkoas.tm.webserver.model.dto.UserDTO;
import ru.stoliarenkoas.tm.webserver.model.entity.User;
import ru.stoliarenkoas.tm.webserver.repository.UserRepositoryPageable;
import ru.stoliarenkoas.tm.webserver.util.CypherUtil;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        loader = AnnotationConfigWebContextLoader.class,
        classes = {ru.stoliarenkoas.tm.webserver.configuration.JpaConfiguration.class,
                   ru.stoliarenkoas.tm.webserver.service.UserServicePageableImpl.class})
public class UserServiceTest {

    private UserRepositoryPageable userRepository;
    @Autowired
    public void setUserRepository(UserRepositoryPageable userRepository) {
        this.userRepository = userRepository;
    }

    private UserServicePageable userService;
    @Autowired
    public void setUserService(UserServicePageable userService) {
        this.userService = userService;
    }

    private final String adminLogin = "adminLogin";
    private final String userLogin = "userLogin";
    private final String password = "password";
    private final User admin = new User();
    private final User user = new User();

    @Before
    public void init() {
        admin.setId("test-admin-id");
        admin.setLogin(adminLogin);
        admin.setPasswordHash(CypherUtil.getMd5(password));
        admin.setRole(Role.ADMIN);
        userRepository.save(admin);
        
        user.setId("test-user-id");
        user.setLogin(userLogin);
        user.setPasswordHash(CypherUtil.getMd5(password));
        user.setRole(Role.USER);
        userRepository.save(user);
    }

    @After
    public void destroy() {
        userRepository.deleteAll();
    }

    @Test
    public void findOneTest() throws AccessForbiddenException {
        final UserDTO userDTO = userService.findOne(admin.getId(), admin.getId());
        assertNotNull(userDTO);
        assertEquals(adminLogin, userDTO.getLogin());
    }

    @Test
    public void persistNewTest() throws AccessForbiddenException, IncorrectDataException {
        final int count = 10;
        for (int i = 0; i < count; i++) {
            final UserDTO newUser = new UserDTO();
            newUser.setId("User#" + (i+1) + "Id");
            newUser.setLogin("User#" + (i+1));
            newUser.setRole(i%2 == 0 ? Role.ADMIN : Role.USER);
            userService.persist(i%2 == 0 ? admin.getId() : user.getId(), newUser);
        }
        final List<UserDTO> users = userService.findAll(admin.getId());
        assertNotNull(users);
        assertEquals(count + 2, users.size());
        for (int i = 0; i < count; i++) {
            final UserDTO persistedUser = userService.findOne(this.admin.getId(), "User#" + (i+1) + "Id");
            assertNotNull(persistedUser);
            assertEquals("User#" + (i+1), persistedUser.getLogin());
        }
    }

    @Test(expected = AccessForbiddenException.class)
    public void persistExistingTest() throws AccessForbiddenException, IncorrectDataException {
        final UserDTO persistableUser = new UserDTO();
        persistableUser.setId("twice-persisted");
        persistableUser.setLogin("newLogin");
        userService.persist(admin.getId(), persistableUser);
        userService.persist(admin.getId(), persistableUser);
    }

    @Test(expected = AccessForbiddenException.class)
    public void persistWrongRoleTest() throws AccessForbiddenException, IncorrectDataException {
        final UserDTO persistableUser = new UserDTO();
        persistableUser.setId("twice-persisted");
        persistableUser.setLogin("newLogin");
        persistableUser.setRole(Role.ADMIN);
        userService.persist(user.getId(), persistableUser);
    }

    @Test(expected = IncorrectDataException.class)
    public void persistEmptyLoginTest() throws AccessForbiddenException, IncorrectDataException {
        final UserDTO persistableUser = new UserDTO();
        userService.persist(user.getId(), persistableUser);
    }

    @Test(expected = AccessForbiddenException.class)
    public void persistUnauthorizedTest() throws AccessForbiddenException, IncorrectDataException {
        final UserDTO persistableUser = new UserDTO();
        userService.persist(null, persistableUser);
    }

    @Test
    public void mergeExistingTest() throws AccessForbiddenException, IncorrectDataException {
        final UserDTO newUser = new UserDTO();
        newUser.setLogin("PersistedUser");
        userService.persist(admin.getId(), newUser);

        UserDTO persistedUser;
        persistedUser = userService.findOne(this.admin.getId(), newUser.getId());
        assertNotNull(persistedUser);
        assertEquals(newUser.getLogin(), persistedUser.getLogin());

        newUser.setLogin("UpdatedUser");
        newUser.setRole(Role.ADMIN);
        userService.merge(admin.getId(), newUser);
        persistedUser = userService.findOne(this.admin.getId(), newUser.getId());
        assertNotNull(persistedUser);
        assertEquals(newUser.getLogin(), persistedUser.getLogin());
    }
    
    @Test(expected = IncorrectDataException.class)
    public void mergeNewTest() throws AccessForbiddenException, IncorrectDataException {
        final UserDTO newUser = new UserDTO();
        newUser.setLogin("NewUser");
        userService.merge(admin.getId(), newUser);
    }

    @Test(expected = AccessForbiddenException.class)
    public void mergeWrongRoleTest() throws AccessForbiddenException, IncorrectDataException {
        final UserDTO newUser = new UserDTO();
        newUser.setLogin("newLogin");
        userService.persist(user.getId(), newUser);

        newUser.setLogin("updatedLogin");
        userService.merge(user.getId(), newUser);
    }

    @Test
    public void authTest() throws IncorrectDataException {
        final UserDTO preregistredUser = userService.login(adminLogin, password);
        assertNotNull(preregistredUser);
        assertEquals(admin.getId(), preregistredUser.getId());

        final String newUserLogin = "newUserLogin";
        final String newUserPassword = "newUserPassword";
        userService.register(newUserLogin, newUserPassword);
        final UserDTO registredUser = userService.login(newUserLogin, newUserPassword);
        assertNotNull(registredUser);
        assertEquals(newUserLogin, registredUser.getLogin());
    }

}
