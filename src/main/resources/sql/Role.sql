CREATE TABLE role (
  id               INT         NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name             VARCHAR(50) NOT NULL UNIQUE,
  value            VARCHAR(50) NOT NULL UNIQUE,
  display_value    VARCHAR(50),
  description      VARCHAR(100),
  active           BOOLEAN     NOT NULL DEFAULT TRUE,
  last_update_time TIMESTAMP            DEFAULT current_timestamp ON UPDATE current_timestamp
);

INSERT INTO role (name, value, display_value, description, active, last_update_time)
VALUES ('Admin', 'ROLE_ADMIN', 'Admin', 'Administrator', TRUE, current_timestamp);
INSERT INTO role (name, value, display_value, description, active, last_update_time)
VALUES ('User', 'ROLE_USER', 'User', 'User', TRUE, current_timestamp);
