package com.example.userService.service;

import com.example.userService.DTO.UserDTO;
import com.example.userService.Mapper.UserMapper;
import com.example.userService.model.Postal;
import com.example.userService.model.User;
import com.example.userService.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository repo;
    private final PostalService postServ;
    private final UserMapper mapper;

    public UserService(UserRepository repo, PostalService postServ, UserMapper mapper){
        this.repo = repo;
        this.postServ = postServ;
        this.mapper = mapper;
    }

    public List<UserDTO> listAll(){
        List<User> users = repo.findAll();

        return users.stream()
            .map(mapper::toDto)
            .toList();
    }

    public Optional<UserDTO> get(Long id){
        return repo.findById(id)
            .map(mapper::toDto);
    }

    @Transactional 
    public UserDTO save(User user) throws IOException {
        Postal postal;
        if(postServ.checkExistence(user.getPostal().getPostal_code()).isEmpty()){
            postal = postServ.apiLookup(user.getPostal().getPostal_code());
            postServ.savePostal(postal);
        } else {
            postal = postServ.getPostal(user.getPostal().getPostal_code());
        }
        user.setPostal(postal);

        if(dateValidation(user.getBirth().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())){
            repo.save(user);
            return mapper.toDto(user);
        } else throw new IllegalArgumentException();
    }

    public void delete(Long id){
        repo.deleteById(id);
    }

    public List<Object[]> byState(){
        return postServ.byState();
    }

    private boolean dateValidation(LocalDate date){
        LocalDate today = LocalDate.now();

        return !(Period.between(date, today).getDays() < 0);
    }

    public List<Object[]> byCity() {
        return postServ.byCity();
    }
}