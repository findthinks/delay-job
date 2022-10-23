package com.findthinks.delay.job.share.repository.mapper;

import com.findthinks.delay.job.share.repository.entity.GlobalRec;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface GlobalRecExtMapper {

    GlobalRec selectRecByOutJobNo(@Param("outJobNo") String outJobNo);

    int insertRec(GlobalRec rec);

    int insertRecs(@Param("grcs") List<GlobalRec> grcs);

    int deleteGlobalRecords(@Param("outJobNos") List<String> outJobNos);

    int updateGlobalRecTriggerTime(Map<String, Object> parameters);
}