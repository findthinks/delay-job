package com.findthinks.delay.job.scheduler;

import com.findthinks.delay.job.share.lib.utils.CollectionUtils;
import com.findthinks.delay.job.share.repository.mapper.GlobalRecExtMapper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public int updateGlobalRecTriggerTime(Integer id, Long triggerTime) {
        Map<String, Object> parameters = new HashMap<>(4);
        parameters.put("recId", id);
        parameters.put("triggerTime", triggerTime);
        return globalRecExtMapper.updateGlobalRecTriggerTime(parameters);
    }
}
