package com.findthinks.delay.job.share.repository.mapper;

import com.findthinks.delay.job.share.repository.entity.Job;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface JobExtMapper {

    List<Job> selectSubmitJobs(Map<String, Object> parameters);

    List<Job> selectShardJobs(Map<String, Object> parameters);

    List<Job> selectNoneSuccessJobs(Map<String, Object> parameters);

    int selectNoneSuccessJobsCount(Map<String, Object> parameters);

    Long selectOneUnSuccessJobId(Map<String, Object> parameters);

    int updateJobState(Map<String, Object> parameters);

    int updateJobsState(Map<String, Object> parameters);

    int deleteJobs(Map<String, Object> parameters);

    int insertJob(Job job);

    int insertBatchJobs(@Param("jobShardId") Integer jobShardId,@Param("jobs") List<Job> jobs);

    List<Job> loadJob(Map<String, Object> parameters);
}