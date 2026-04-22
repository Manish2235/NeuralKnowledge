package com.incapp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.incapp.entity.MyUser;

@Repository
public interface UserRepo extends JpaRepository<MyUser, String> {

}
