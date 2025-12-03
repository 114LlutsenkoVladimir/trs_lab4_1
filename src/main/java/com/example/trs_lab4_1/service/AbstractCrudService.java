package com.example.trs_lab4_1.service;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;


public class AbstractCrudService<T, ID, RP extends JpaRepository<T, ID>> {

    protected final RP repository;

    protected AbstractCrudService(RP repository) {
        this.repository = repository;
    }

    public void create(T entity) {
        repository.save(entity);
    }

    public Optional<T> findById(ID id) {
        return repository.findById(id);
    }

    public List<T> findAll() {
        return repository.findAll();
    }

    public T update(T entity) {
        return repository.save(entity);
    }

    public void delete(ID id) {
        repository.deleteById(id);
    }

}
