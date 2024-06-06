package kr.co.sboard.repository;

import kr.co.sboard.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    public Optional<User> findUserByNameAndEmail(String name, String email);
    public Optional<User> findUserByUidAndEmail(String uid, String email);

}