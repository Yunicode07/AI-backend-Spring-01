package com.sesac.aibackend.repository;

import com.sesac.aibackend.domain.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StaffRepository extends JpaRepository<Staff, Long> {
    List<Staff> findByDeptIdOrderByHireDateDesc(Long deptId);

    @Query("""
          select s from Staff s
          join fetch s.dept
          where s.dept.id = :deptId
          order by s.hireDate desc
          """)
    List<Staff> findByDeptIdWithDept(Long deptId);
}
