package org.repair.services;

import org.repair.dao.WorkerRepository;
import org.repair.model.Worker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

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

        public WorkerDetail(Worker worker) {
            this.worker = worker;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            GrantedAuthority grantedAuthority = worker::getRole;
            return Collections.singleton(grantedAuthority);
        }

        @Override
        public String getPassword() {
            return worker.getPassword();
        }

        @Override
        public String getUsername() {
            return worker.getName();
        }

        @Override
        public String getId() {
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;
            WorkerDetail that = (WorkerDetail) o;
            return Objects.equals(worker, that.worker);
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), worker);
        }
    }
}
