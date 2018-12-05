package org.repair.security;

import org.repair.model.Worker;
import org.repair.services.LoginDetailService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.security.core.userdetails.UserDetails;

@TestConfiguration
public class MockLoginDetailService extends LoginDetailService {

    @Override
    public UserDetails loadUserByUsername(String username) {
        Worker worker = new Worker("vova", "$2a$11$N6PHp0OR0dtbRsPPU6.Hc.5s3vV2ATV60KkqhOIMuIjjUPdCwWobK");
        worker.setRole("ADMIN");
        LoginDetailService.WorkerDetail workerDetail = new LoginDetailService.WorkerDetail(worker);
        return workerDetail;
    }
}