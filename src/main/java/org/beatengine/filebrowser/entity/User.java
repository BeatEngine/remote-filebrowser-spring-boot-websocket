package org.beatengine.filebrowser.entity;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Column(columnDefinition = "BIGINT")
    private Long id;
    @Column(unique = true)
    private String email;
    @Column
    private String displayName;
    @Column
    private String sha256Pass;

    //Profile picture
    //@Column(columnDefinition = "BIGINT")
    private Long pictureId;

/*    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(name = "UserRole",
            joinColumns = { @JoinColumn(name = "userId") },
            inverseJoinColumns = { @JoinColumn(name = "roleId") })
    public Set<Role> roles;

    @OneToMany
    @JoinColumn(name = "userId")
    public Set<Order> orders;

    @OneToMany
    @JoinColumn(name = "userId")
    public Set<Rating> ratings;

    @OneToOne
    @JoinColumn(name = "pictureId", insertable = false, updatable = false)
    public Picture picture;*/

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPictureId() {
        return pictureId;
    }

    public void setPictureId(Long pictureId) {
        this.pictureId = pictureId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getSha256Pass() {
        return sha256Pass;
    }

    public void setSha256Pass(String sha256Pass) {
        this.sha256Pass = sha256Pass;
    }

    // UserDetails Interface:
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        final List<SimpleGrantedAuthority> auths = new ArrayList<>();
        /*roles.forEach(r -> auths.add(new SimpleGrantedAuthority(r.getName())));*/
        return auths;
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return "";
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return sha256Pass != null;
    }
}
