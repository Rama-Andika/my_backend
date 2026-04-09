package com.oxysystem.general.repository.db1.posmaster;

import com.oxysystem.general.model.db1.posmaster.Cogs;
import com.oxysystem.general.repository.db1.posmaster.custom.CogsRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CogsRepository extends JpaRepository<Cogs, Long>, CogsRepositoryCustom {

}
