package com.korbiak.service.repos;

import com.korbiak.service.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {

    User findByFirstNameAndSecondName(String firstName, String secondName);

    List<User> findAllByCompanyIdAndIsAdmin(int id, int isAdmin);

    List<User> findAllByIsAdmin(int adminType);
}
