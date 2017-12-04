CREATE TABLE codetable (
  id               INT PRIMARY KEY AUTO_INCREMENT,
  name             VARCHAR(100),
  value            VARCHAR(100),
  display_value    VARCHAR(100),
  descprition      VARCHAR(200),
  active           BOOLEAN         DEFAULT TRUE,
  last_update_time TIMESTAMP       DEFAULT current_timestamp ON UPDATE current_timestamp,
  CONSTRAINT uq_codetable_name UNIQUE (name)
);

INSERT INTO codetable (id, name, value, display_value, descprition, active, last_update_time)
VALUES (1, 'Admin', 'ADMIN', 'Admin', 'Account Administrator', TRUE, current_timestamp);

INSERT INTO codetable (id, name, value, display_value, descprition, active, last_update_time)
VALUES (2, 'User', 'USER', 'User', 'Account User', TRUE, current_timestamp);

INSERT INTO codetable (id, name, value, display_value, descprition, active, last_update_time)
VALUES (10, 'Admin Menu', 'ADMIN_MENU', 'Admin Menu', 'Administrator Menu ', TRUE, current_timestamp);

INSERT INTO codetable (id, name, value, display_value, descprition, active, last_update_time)
VALUES (11, 'User Menu', 'USER_MENU', 'User Menu', 'User Menu', TRUE, current_timestamp);
