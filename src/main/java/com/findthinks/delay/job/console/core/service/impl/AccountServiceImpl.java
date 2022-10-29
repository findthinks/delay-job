package com.findthinks.delay.job.console.core.service.impl;

import com.findthinks.delay.job.console.core.service.AccountService;
import com.findthinks.delay.job.console.web.rr.AccountInfo;
import com.findthinks.delay.job.share.lib.enums.ExceptionEnum;
import com.findthinks.delay.job.share.lib.exception.DelayJobException;
import com.findthinks.delay.job.share.lib.utils.CollectionUtils;
import com.findthinks.delay.job.share.repository.entity.Account;
import com.findthinks.delay.job.share.repository.mapper.AccountExtMapper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    @Resource
    private AccountExtMapper accountExtMapper;

    @Override
    public void authentication(AccountInfo info) {
        List<Account> select = accountExtMapper.loadAccount(info.getAccount());
        if (CollectionUtils.isEmpty(select)) {
            throw new DelayJobException(ExceptionEnum.USERNAME_PWD_ERROR);
        }
        Account persist = select.get(0);
        if (!persist.getPasswd().equals(info.getPwd())) {
            throw new DelayJobException(ExceptionEnum.USERNAME_PWD_ERROR);
        }
    }
}