--POPULATE WORKER TABLE
INSERT INTO worker (id, name, password, role) VALUES (1, 'vova', '$2a$11$N6PHp0OR0dtbRsPPU6.Hc.5s3vV2ATV60KkqhOIMuIjjUPdCwWobK', 'ADMIN');
INSERT INTO worker (id, name, password, role) VALUES (2, 'andriy', '$2a$11$N6PHp0OR0dtbRsPPU6.Hc.5s3vV2ATV60KkqhOIMuIjjUPdCwWobK', 'USER');

--POPULATE ADDRESS TABLE
INSERT INTO address (id, apartment, city, postal_code, street, street_number) VALUES (1, 1, 'Lviv', '78048', 'Stusa', '111');
INSERT INTO address (id, apartment, city, postal_code, street, street_number) VALUES (2, 15, 'Lviv', '78046', 'Pekarska', '12');

--POPULATE TASK TABLE
INSERT INTO task (id, descr, short_desc, tariff) values (1, 'walls painting', 'walls painting', 15.0);
INSERT INTO task (id, descr, short_desc, tariff) values (2, 'bricks construction', 'bricks construction', 35.0);

--POPULATE PROJECT TABLE
INSERT INTO project (id, ceiling, client_name, client_phone, floor, slopes, walls, address_id, worker_id)
    values (1, 10.0, 'roman', '0970007254', 15.0, 5.0, 20.0, 1, 1);
INSERT INTO project (id, ceiling, client_name, client_phone, floor, slopes, walls, address_id, worker_id)
    values (2, 10.0, 'yura', '0970003715', 15.0, 5.0, 20.0, 1, 1);

--POPULATE PROJECT_TASKS TABLE
INSERT INTO project_tasks (prj_id, task_id, qty) values (1, 1, 100);
INSERT INTO project_tasks (prj_id, task_id, qty) values (1, 2, 10);