package com.findthinks.delay.job.share.repository.mapper;

import com.findthinks.delay.job.share.repository.entity.Account;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AccountExtMapper {

    List<Account> loadAccount(@Param("account") String account);
}