package com.findthinks.delay.job.share.id;

public class SequenceKey {
    private Integer id;

    private String key;

    private Long startWith;

    private Integer incSpan;

    private String description;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return key
     */
    public String getKey() {
        return key;
    }

    /**
     * @param key
     */
    public void setKey(String key) {
        this.key = key == null ? null : key.trim();
    }

    /**
     * @return start_with
     */
    public Long getStartWith() {
        return startWith;
    }

    /**
     * @param startWith
     */
    public void setStartWith(Long startWith) {
        this.startWith = startWith;
    }

    /**
     * @return inc_span
     */
    public Integer getIncSpan() {
        return incSpan;
    }

    /**
     * @param incSpan
     */
    public void setIncSpan(Integer incSpan) {
        this.incSpan = incSpan;
    }

    /**
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     */
    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }
}