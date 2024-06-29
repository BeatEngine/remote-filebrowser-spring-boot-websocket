package org.beatengine.filebrowser.repository;

import org.beatengine.filebrowser.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

/**
 * User Queries
 */
@Repository
public interface UserRepository
        extends JpaRepository<User, BigInteger> {

    <S extends User> S save(S entity);

    List<User> findAll();

    Long countByEmail(final String email);
  
    void deleteById(BigInteger primaryKey);

    User findByEmail(String email);
    // Needs the full path: org.beatengine.filebrowser.entity.mapping.AccountDetails()

    /*Native IDEA: @Query("SELECT new org.beatengine.filebrowser.entity.mapping.AccountDetails(u.id, u.email, u.displayName, LIST_AGR(r.name),u.pictureId) FROM User u " +
            "LEFT JOIN UserRole ur ON u.id = ur.userId " +
            "LEFT JOIN Role r ON ur.roleId = r.id " +
            "WHERE u.id = :userId " +
            "GROUP BY u.id")*/
/*    @Query("SELECT new org.beatengine.filebrowser.entity.mapping.AccountDetail(u.id, u.email, u.displayName, r.name, u.pictureId) FROM User u " +
            "LEFT JOIN u.roles r  " + // JPA Join many-to-many
            "WHERE u.id = :userId ")
    List<AccountDetail> findAccountDetails(@Param("userId") BigInteger userId);*/

}
