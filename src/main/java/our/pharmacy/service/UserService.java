package our.pharmacy.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import our.pharmacy.dto.ConsumerRegistrationForm;
import our.pharmacy.dto.WorkerRegistrationForm;
import our.pharmacy.model.Consumer;
import our.pharmacy.model.User;
import our.pharmacy.model.Worker;
import our.pharmacy.repository.ConsumerRepository;
import our.pharmacy.repository.UserRepository;
import our.pharmacy.repository.WorkerRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, ConsumerRepository consumerRepository, UserMapper userMapper, PasswordEncoder passwordEncoder, WorkerRepository workerRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username).orElseThrow();
    }

    public void signUpConsumer(ConsumerRegistrationForm registrationForm) {
        Consumer consumer = userMapper.map(registrationForm, passwordEncoder);
        userRepository.save(consumer);
    }

    public void signUpWorker(WorkerRegistrationForm registrationForm) {
        Worker worker = userMapper.map(registrationForm, passwordEncoder);
        userRepository.save(worker);
    }
}
