package com.jz.repository;

import com.jz.pojo.JZStarterMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

/**
 * @author jack zhang
 * @version 1.0
 * @description: TODO
 * @date 2023/10/19 11:10
 */
public interface JZMessageRepository extends JpaRepository<JZStarterMessage, Serializable> {
}
