package com.oxysystem.general.repository.db1.general;

import com.oxysystem.general.dto.general.bank.view.BankViewDTO;
import com.oxysystem.general.model.db1.general.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankRepository extends JpaRepository<Bank, Long> {
    @Query("SELECT new com.oxysystem.general.dto.general.bank.view.BankViewDTO(CAST(b.bankId as string),b.name) FROM Bank b")
    List<BankViewDTO> getBanks();
}
