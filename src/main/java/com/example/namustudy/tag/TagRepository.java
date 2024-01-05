package com.example.namustudy.tag;

import com.example.namustudy.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;


@Transactional(readOnly = true)
public interface TagRepository extends JpaRepository<Tag, Long> {
}
