package com.oxysystem.general.repository.db1.posmaster;

import com.oxysystem.general.model.db1.posmaster.ItemGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemGroupRepository extends JpaRepository<ItemGroup, Long> {
}
