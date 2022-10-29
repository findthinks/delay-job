package com.findthinks.delay.job.scheduler;

import java.util.List;

public interface IGlobalRecManager {

    int deleteGlobalRec(List<String> outJobNos);

    int updateGlobalRecTriggerTime(Integer id, Long triggerTime);
}
