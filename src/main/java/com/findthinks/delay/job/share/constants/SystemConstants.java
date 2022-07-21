package com.findthinks.delay.job.share.constants;

/**
 * @author YuBo
 */
public interface SystemConstants {

    int V_CPU_CORES = Runtime.getRuntime().availableProcessors();

    int JOB_SUCCESS = 0;

    int JOB_FAIL = -1;

    String JOB_SUCCESS_MSG = "OK";

    String JOB_FAIL_MSG = "ERROR";
}