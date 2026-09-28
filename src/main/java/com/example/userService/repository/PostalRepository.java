package com.example.userService.repository;

import com.example.userService.model.Postal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostalRepository extends JpaRepository<Postal, Long> {

    @Query(nativeQuery = true, value = "select zip_codes.state, COUNT(users.id) AS number_of_people from zip_codes LEFT JOIN users ON zip_codes.postal_code = users.postal_postal_code GROUP by zip_codes.postal_code")
    List<Object[]> usersInStates();

    @Query(nativeQuery = true, value = "select zip_codes.city, COUNT(users.id) AS number_of_people from zip_codes LEFT JOIN users ON zip_codes.postal_code = users.postal_postal_code GROUP by zip_codes.postal_code")
    List<Object[]> usersInCities();
}
