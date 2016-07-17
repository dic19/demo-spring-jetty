package com.mp.apiswitch.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
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
    name = "profileIdGenerator", 
    table = "id_generation_table",
    pkColumnName = "table_identifier", pkColumnValue = "profile", valueColumnName = "last_value",
    initialValue = 1, allocationSize = 100
)
public class Profile implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "profileIdGenerator")
    private Long id;
    
    @Basic
    private String name;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "current_status")
    private ProfileStatus currentStatus;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "previous_status")
    private ProfileStatus previousStatus;
    
    @Convert(converter = LocalDateTimeConverter.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_date")
    private LocalDateTime creationDate;
    
    @Convert(converter = LocalDateTimeConverter.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_switched_date")
    private LocalDateTime lastSwitchedDate;
    
    @Convert(converter = LocalDateTimeConverter.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;
    
    @OneToMany(mappedBy = "profile")
    private List<ProfileRule> profileRules = new LinkedList<>();
    
    public Profile() {
        super();
    }

    public Profile(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProfileStatus getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(ProfileStatus currentStatus) {
        this.currentStatus = currentStatus;
    }

    public ProfileStatus getPreviousStatus() {
        return previousStatus;
    }

    public void setPreviousStatus(ProfileStatus previousStatus) {
        this.previousStatus = previousStatus;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getLastSwitchedDate() {
        return lastSwitchedDate;
    }

    public void setLastSwitchedDate(LocalDateTime lastSwitchedDate) {
        this.lastSwitchedDate = lastSwitchedDate;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public List<ProfileRule> getProfileRules() {
        return Collections.unmodifiableList(profileRules);
    }

    public void setProfileRules(List<ProfileRule> profileRules) {
        this.profileRules.clear();
        this.profileRules.addAll(profileRules);
    }
    
    public void addProfileRule(ProfileRule profileRule) {
        profileRules.add(profileRule);
    }
    
    public void addProfileRules(List<ProfileRule> profileRules) {
        this.profileRules.addAll(profileRules);
    }
    
    public void updateCurrentStatus(ProfileStatus newStatus) {
        setPreviousStatus(getCurrentStatus());
        setCurrentStatus(newStatus);
    }
    
    @PrePersist
    private void prePersist() {
        this.creationDate = LocalDateTime.now();
        if (this.currentStatus == null) {
            this.currentStatus = ProfileStatus.NOT_SWITCHED;
        }
        if (this.previousStatus == null) {
            this.previousStatus = ProfileStatus.UNKNOWN;
        }
    }
    
    @PreUpdate
    private void preUpdate() {
        this.lastModifiedDate = LocalDateTime.now();
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
        if (object instanceof Profile) {
            Profile other = (Profile) object;
            return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("%s[id=%d]", getClass().getCanonicalName(), id);
    }
}