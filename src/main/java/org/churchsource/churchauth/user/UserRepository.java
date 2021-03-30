package org.churchsource.churchauth.user;

import java.util.List;

import org.churchsource.churchauth.repository.AbstractRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;

@Repository
@Transactional
public class UserRepository extends AbstractRepository<CPUserDetails> {

  public CPUserDetails findUserById(Long id) throws NoResultException {
    return entityManager.createNamedQuery(UserNamedQueryConstants.NAME_FIND_USER_BY_ID, CPUserDetails.class)
        .setParameter("id", id)
        .getSingleResult();
  }

  public List<CPUserDetails> getAllUsers() throws NoResultException {
    return entityManager.createNamedQuery(UserNamedQueryConstants.NAME_GET_ALL_USERS, CPUserDetails.class)
            .setParameter("includeDeleted", false)
            .getResultList();
  }

  public CPUserDetails findUserByUserName(String username) throws NoResultException {
    return entityManager.createNamedQuery(UserNamedQueryConstants.NAME_FIND_USER_BY_USERNAME, CPUserDetails.class)
            .setParameter("username", username)
            .getSingleResult();
  }

  public CPUserDetails updateUser(CPUserDetails user) {
    CPUserDetails existingUser = findUserById(user.getId());
    CPUserDetails updatedUser = new CPUserDetails();
    if ("".equals(user.getPassword()) || user.getPassword() == null) {
      existingUser.mergeEntities(user, updatedUser, "created", "modified", "password");
    } else {
      existingUser.mergeEntities(user, updatedUser);
    }
    return update(updatedUser);
  }

  public void deleteUser(Long userId) {
    CPUserDetails existingUserToDelete = findUserById(userId);
    existingUserToDelete.setDeleted(true);
    update(existingUserToDelete);
  }
}
