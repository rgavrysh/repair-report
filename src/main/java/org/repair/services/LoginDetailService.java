package org.repair.services;

import org.repair.dao.WorkerRepository;
import org.repair.model.Worker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LoginDetailService implements UserDetailsService {
    @Autowired
    private WorkerRepository workerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        Worker worker = workerRepository.findOneByName(username);
        if (worker != null) {
            return new WorkerDetail(worker);
        } else {
            throw new UsernameNotFoundException("User " + username + " not found.");
        }
    }

    public static final class WorkerDetail extends Worker implements UserDetails {
        private final Worker worker;

        private WorkerDetail(Worker worker) {
            this.worker = worker;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            //todo: decompose role to separate table
            GrantedAuthority grantedAuthority = (GrantedAuthority) () -> worker.getRole();
            return Collections.singleton(grantedAuthority);
        }

        @Override
        public String getPassword() {
            //todo: use BCrypt to encode password
            return worker.getPassword();
        }

        @Override
        public String getUsername() {
            return worker.getName();
        }

        @Override
        public Long getId() {
            return worker.getId();
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
            return true;
        }

        public Worker getWorker() {
            return worker;
        }
    }
}
