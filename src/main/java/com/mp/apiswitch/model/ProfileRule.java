package com.mp.apiswitch.model;

import java.io.Serializable;
import java.time.LocalTime;
import java.time.ZoneId;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author Delcio Amarillo
 */
@Entity
@Table(name = "profile")
@TableGenerator(
    name = "profileRuleIdGenerator", 
    table = "id_generation_table",
    pkColumnName = "table_identifier", pkColumnValue = "profile_rule", valueColumnName = "last_value",
    initialValue = 1, allocationSize = 100
)
public class ProfileRule implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "profileRuleIdGenerator")
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "profile_id", referencedColumnName = "id")
    private Profile profile;
    
    @Basic
    private Boolean active;
    
    @Temporal(TemporalType.TIME)
    @Column(name = "begin_time")
    private LocalTime beginTime;
    
    @Temporal(TemporalType.TIME)
    @Column(name = "begin_time")
    private LocalTime endTime;
    
    @Column(name = "warning_value")
    private Integer warningValue;
    
    @Column(name = "danger_value")
    private Integer dangerValue;

    public ProfileRule() {
        super();
    }

    public Long getId() {
        return id;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public LocalTime getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(LocalTime beginTime) {
        this.beginTime = beginTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public Integer getWarningValue() {
        return warningValue;
    }

    public void setWarningValue(Integer warningValue) {
        this.warningValue = warningValue;
    }

    public Integer getDangerValue() {
        return dangerValue;
    }

    public void setDangerValue(Integer dangerValue) {
        this.dangerValue = dangerValue;
    }
    
    public Boolean isActiveRightNow() {
        return isActiveRightNow(ZoneId.systemDefault());
    }
    
    public Boolean isActiveRightNow(ZoneId zone) {
        return isActiveAt(LocalTime.now(zone));
    }
    
    public Boolean isActiveAt(LocalTime time) {
        if (!isActive()) {
            return false;
        }
        
        if (beginTime.equals(time) || endTime.equals(time)) {
            return true;
        }
        
        if (beginTime.isBefore(endTime)) {
            return beginTime.isBefore(time) && endTime.isAfter(time);
        }
        
        return beginTime.isBefore(time) || endTime.isAfter(time);
    }

        
    /*
     * Overriden from Object
     */

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof ProfileRule) {
            ProfileRule other = (ProfileRule) object;
            return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("%s[id=%d]", getClass().getCanonicalName(), id);
    }
}
