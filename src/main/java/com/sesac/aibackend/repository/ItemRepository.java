package com.sesac.aibackend.repository;

import com.sesac.aibackend.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

// 인터페이스는 구현체가 필요한데 jpa가 관리해줘서 annotation 안달아도됨
// <Entity객체, Id 타입> 관리 객체 Item
public interface ItemRepository extends JpaRepository<Item, Long> { // extends 상속받기 위해

}
