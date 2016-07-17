package com.mp.apiswitch.repositories;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.springframework.core.GenericTypeResolver;
import org.springframework.stereotype.Repository;

/**
 * @author Delcio Amarillo
 * @param <E> The entity class
 * @param <I> The entity id class
 */
@Repository
public abstract class SimpleBaseRepository<E, I> {
    
    private final Class<E> entityClass;
    private final Class<I> entityIdClass;
    
    public SimpleBaseRepository() {
        super();
        Class<?>[] argumentTypes = GenericTypeResolver.resolveTypeArguments(getClass(), SimpleBaseRepository.class);
        this.entityClass = (Class<E>) argumentTypes[0];
        this.entityIdClass = (Class<I>) argumentTypes[1];
    }
    
    protected final Class<E> getEntityClass() {
        return entityClass;
    }

    protected final Class<I> getEntityIdClass() {
        return entityIdClass;
    }
    
    protected abstract EntityManager getEntityManager();
    
    protected String getIdName() {
        for (Field field : entityClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(javax.persistence.Id.class)) {
                return field.getName();
            }
        }
        return "UNKONW_ID";
    };

    public E find(I entityId) {
        EntityManager entityManager = getEntityManager();
        return entityManager.find(entityClass, entityId);
    }
    
    public List<E> find(Collection<I> entitiesIdList) {
        EntityManager entityManager = getEntityManager();
        String jpql = String.format("SELECT e FROM %1$s e WHERE e.%2$s IN :idList", entityClass.getSimpleName(), getIdName());
        TypedQuery<E> query = entityManager.createQuery(jpql, entityClass);
        query.setParameter("idList", entitiesIdList);
        List<E> entities = query.getResultList();
        return entities;
    }

    public Collection<E> findAll() {
        EntityManager entityManager = getEntityManager();
        String jpql = String.format("SELECT e FROM %1$s e", entityClass.getSimpleName());
        TypedQuery<E> query = entityManager.createQuery(jpql, entityClass);
        List<E> entities = query.getResultList();
        return entities;
    }

    public E save(E entity) {
        EntityManager entityManager = getEntityManager();
        entityManager.persist(entity);
        return entity;
    }

    public E update(E entity) {
        EntityManager entityManager = getEntityManager();
        E mergedEntity = entityManager.merge(entity);
        return mergedEntity;
    }

    public E delete(I entityId) {
        EntityManager entityManager = getEntityManager();
        E attachedEntity = entityManager.find(entityClass, entityId);
        if (attachedEntity != null) {
            entityManager.remove(attachedEntity);
            return attachedEntity;
        }
        return null;
    }
    
    public Collection<E> delete(Collection<I> entitiesIdList) {
        List<E> entities = new LinkedList<>();
        EntityManager entityManager = getEntityManager();
        entitiesIdList.forEach((entityId) -> {
            E attachedEntity = entityManager.find(entityClass, entityId);
            if (attachedEntity != null) {
                entityManager.remove(attachedEntity);
                entities.add(attachedEntity);
            }
        });
        return entities;
    }
}