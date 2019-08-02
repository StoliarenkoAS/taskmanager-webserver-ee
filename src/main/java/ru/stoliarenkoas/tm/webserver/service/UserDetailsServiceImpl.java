package ru.stoliarenkoas.tm.webserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.stoliarenkoas.tm.webserver.repository.UserRepositoryPageable;

public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepositoryPageable userRepository;
    @Autowired
    public void setUserRepository(UserRepositoryPageable userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final boolean usernameIsInvalid = username == null || username.isEmpty();
        if (usernameIsInvalid) throw new UsernameNotFoundException("empty name");
        final ru.stoliarenkoas.tm.webserver.model.entity.User user = userRepository.findByLogin(username);
        final boolean userNotExist = user == null;
        if (userNotExist) throw new UsernameNotFoundException("no such user");
        final User.UserBuilder builder = User.withUsername(username);
        builder.password(user.getPasswordHash());
        builder.roles(user.getRole().toString());
        return builder.build();
    }

}
