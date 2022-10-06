package com.example.getdriversservice.getdrivers;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GetDriversRepository extends JpaRepository<Driver, String> { }
