CREATE TABLE role (
  id               INT         NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name             VARCHAR(50) NOT NULL UNIQUE,
  description      VARCHAR(100),
  is_active        BOOLEAN     NOT NULL DEFAULT TRUE,
  last_update_time TIMESTAMP            DEFAULT current_timestamp ON UPDATE current_timestamp
);

INSERT INTO role (name, description, is_active, last_update_time)
VALUES ('ROLE_ADMIN', 'Administrator', TRUE, current_timestamp);
INSERT INTO role (name, description, is_active, last_update_time)
VALUES ('ROLE_USER', 'User', TRUE, current_timestamp);