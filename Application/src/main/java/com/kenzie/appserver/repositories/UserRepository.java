package com.kenzie.appserver.repositories;

import com.kenzie.appserver.repositories.model.UserProfileRecord;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface UserRepository extends CrudRepository<UserProfileRecord, String> {
    public UserProfileRecord findByUsername(String username);
}
