package com.findthinks.delay.job.scheduler;

import com.findthinks.delay.job.share.lib.utils.CollectionUtils;
import com.findthinks.delay.job.share.repository.mapper.GlobalRecExtMapper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

@Service
public class GlobalRecManager implements IGlobalRecManager {

    @Resource
    private GlobalRecExtMapper globalRecExtMapper;

    @Override
    public int deleteGlobalRec(List<String> outJobNos) {
        if (CollectionUtils.isEmpty(outJobNos)) {
            return 0;
        }
        return globalRecExtMapper.deleteGlobalRecords(outJobNos);
    }
}
