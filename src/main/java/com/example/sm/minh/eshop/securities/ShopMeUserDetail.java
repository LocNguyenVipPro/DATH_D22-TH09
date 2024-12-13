    package com.example.sm.minh.eshop.securities;
    
    import com.example.sm.minh.eshop.models.User;
    import com.example.sm.minh.eshop.models.Role;
    import lombok.AllArgsConstructor;
    import lombok.Data;
    import org.springframework.security.core.GrantedAuthority;
    import org.springframework.security.core.authority.SimpleGrantedAuthority;
    import org.springframework.security.core.userdetails.UserDetails;

    import java.util.ArrayList;
    import java.util.Collection;
    import java.util.List;
    @Data
    @AllArgsConstructor
    public class ShopMeUserDetail implements UserDetails {
        private User users;
        private static final long serialVersionUID = 8434638013158790457L;

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            Collection<Role> roles=  users.getListRoles();
            List<SimpleGrantedAuthority> authorities=new ArrayList<>();
            for(Role role : roles)
            {
                authorities.add(new SimpleGrantedAuthority(role.getName()));
            }
            return authorities;
        }
        public Integer getUserId() {
            return this.users.getId();
        }
        @Override
        public String getPassword() {
            return this.users.getPassword();
        }
    
        @Override
        public String getUsername() {
            return this.users.getUserName();
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
            return this.users.getActive();
        }
    }
