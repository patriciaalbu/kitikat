package com.ciutz.kitikat.service;

import com.ciutz.kitikat.entities.User;
import com.ciutz.kitikat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthenticationService {

    private UserRepository userRepository;

    @Autowired
    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserByCredentials(String email, String password) {
        Optional<User> holder = userRepository.findByEmailAndPassword(email, password);
        if (holder.isPresent()) {
            return holder.get();
        } else {
            return null;
        }
    }

    public User getUserByEmail(String email) {
        Optional<User> holder = userRepository.findByEmail(email);
        if (holder.isPresent()) {
            return holder.get();
        } else {
            return null;
        }
    }

    public boolean userExists(String email) {
        Optional<User> holder = userRepository.findByEmail(email);
        return holder.isPresent();
    }


    public User registerUser(String email, String password, String name) {
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setPassword(password);
        user.setImgSrc("https://www.vettedpetcare.com/vetted-blog/wp-content/uploads/2017/09/How-To-Travel-With-a-Super-Anxious-Cat-square.jpeg");
        user.setBirthdate("-");
        user.setRace("-");
        user.setHobbies("-");
        return userRepository.save(user);
    }

    public User updateUser(User user){
        return userRepository.save(user);
    }
}
