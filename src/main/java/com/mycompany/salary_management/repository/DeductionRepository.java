package com.mycompany.salary_management.repository;

import com.mycompany.salary_management.entity.Deduction;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DeductionRepository extends JpaRepository<Deduction, Long> {
}
