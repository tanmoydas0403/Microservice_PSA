package com.propertyservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.propertyservice.entity.Property;

public interface PropertyRepository extends JpaRepository<Property, Long> {

}
