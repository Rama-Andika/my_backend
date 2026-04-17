package com.oxysystem.general.repository.tenant.posmaster;

import com.oxysystem.general.model.tenant.posmaster.Cogs;
import com.oxysystem.general.repository.tenant.posmaster.custom.CogsRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CogsRepository extends JpaRepository<Cogs, Long>, CogsRepositoryCustom {

}
