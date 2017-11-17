CREATE TABLE user (
  id               INT                  AUTO_INCREMENT PRIMARY KEY,
  username         VARCHAR(45) NOT NULL,
  password         VARCHAR(45) NOT NULL,
  enabled          BOOLEAN     NOT NULL DEFAULT TRUE,
  expired          BOOLEAN     NOT NULL DEFAULT TRUE,
  locked           BOOLEAN     NOT NULL DEFAULT TRUE,
  last_update_time TIMESTAMP   NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp,
  comment          VARCHAR(100)
);

INSERT INTO user (username, password, enabled, expired, locked, last_update_time)
VALUES ('admin', 'admin', TRUE, FALSE, FALSE, current_timestamp);
INSERT INTO user (username, password, enabled, expired, locked, last_update_time)
VALUES ('user', 'password', TRUE, FALSE, FALSE, current_timestamp);