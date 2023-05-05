package our.pharmacy.service;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import our.pharmacy.dto.ConsumerRegistrationForm;
import our.pharmacy.dto.WorkerRegistrationForm;
import our.pharmacy.model.Consumer;
import our.pharmacy.model.Role;
import our.pharmacy.model.User;
import our.pharmacy.model.Worker;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentSkipListMap;

@Mapper
public interface UserMapper {

    default UserDetails map(User user) {
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), map(user.getRole()));
    }

    default Collection<? extends GrantedAuthority> map(Role role) {
        return Collections.singleton(new SimpleGrantedAuthority(role.name()));
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passport", ignore = true)
    @Mapping(target = "password", source="password", qualifiedByName = "encodePassword")
    Consumer map(ConsumerRegistrationForm registrationForm, @Context PasswordEncoder passwordEncoder);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", source="password", qualifiedByName = "encodePassword")
    Worker map(WorkerRegistrationForm registrationForm, @Context PasswordEncoder passwordEncoder);

    @Named(value="encodePassword")
    default String encodePassword(String password, @Context PasswordEncoder passwordEncoder) {
        return passwordEncoder.encode(password);
    }
}
