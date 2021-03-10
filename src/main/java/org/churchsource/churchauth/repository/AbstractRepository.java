package org.churchsource.churchauth.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.ParameterizedType;

@Transactional
@Repository
public abstract class AbstractRepository<Entity> implements InitializingBean {

  public Object clazz = null;

  @PersistenceContext
  protected EntityManager entityManager;

  @Override
  public void afterPropertiesSet() throws Exception {
    clazz = (Class<Entity>) ((ParameterizedType) getClass()
        .getGenericSuperclass()).getActualTypeArguments()[0];
  }

  public Entity save(Entity entity, boolean flush) {
    entityManager.persist(entity);
    if (flush) {
      entityManager.flush();
    }
    return entity;
  }

  public Entity save(Entity entity) {
    return save(entity, true);
  }

  public Entity findEntityById(Long id) {
    return entityManager.find((Class<Entity>) clazz, id);
  }

  @Transactional(propagation = Propagation.MANDATORY)
  public Entity update(Entity entity) {
    return entityManager.merge(entity);
  }
}
