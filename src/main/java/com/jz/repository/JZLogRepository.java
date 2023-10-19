package com.jz.repository;

import com.jz.pojo.JZStarterLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

/**
 * @author jack zhang
 * @version 1.0
 * @description: TODO
 * @date 2023/10/18 23:59
 */
public interface JZLogRepository extends JpaRepository<JZStarterLog, Serializable> {
}
