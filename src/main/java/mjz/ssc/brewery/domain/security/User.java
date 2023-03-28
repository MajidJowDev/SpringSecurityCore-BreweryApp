package mjz.ssc.brewery.domain.security;

import lombok.*;
import mjz.ssc.brewery.domain.Customer;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class User implements UserDetails, CredentialsContainer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String username;
    private String password;

    @Singular // (this annotation can only be used with builder pattern) so that in the builder pattern we will have a property by the name authority not authorities
    @ManyToMany(cascade = {CascadeType.MERGE/*, CascadeType.PERSIST*/}, fetch = FetchType.EAGER) // make sure to set this as EAGER (Because we are going to run this in our context and then we are going to pass it to spring security).... CascadeType.PERSIST removed due to Error "Failed to load ApplicationContext" in bootstrap (and tests)
    @JoinTable(name = "user_role",
            joinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")})
    private Set<Role> roles;

    @ManyToOne(fetch = FetchType.EAGER) // customer has multiple users
    private Customer customer;

    //@Transient //Specifies that the property or field is not persistent
    //private Set<Authority> authorities;

    @Transient
    public Set<GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(Role :: getAuthorities)
                .flatMap(Set :: stream)
                .map(authority -> {
                    return new SimpleGrantedAuthority(authority.getPermission());
                })
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Builder.Default // if we do not add this annotation the builder pattern will set the value of attribs to null by default
    private boolean accountNonExpired = true;
    @Builder.Default
    private boolean accountNonLocked = true;
    @Builder.Default
    private boolean credentialsNonExpired = true;
    @Builder.Default
    private boolean enabled = true;

    @Override
    public void eraseCredentials() {
        this.password = null;
    }


}
