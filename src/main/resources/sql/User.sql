CREATE TABLE user (
  id               INT                  AUTO_INCREMENT PRIMARY KEY,
  username         VARCHAR(45) NOT NULL,
  password         VARCHAR(45) NOT NULL,
  enabled          BOOLEAN     NOT NULL DEFAULT TRUE,
  expired          BOOLEAN     NOT NULL DEFAULT TRUE,
  locked           BOOLEAN     NOT NULL DEFAULT TRUE,
  category         INT         NOT NULL,
  add_time         TIMESTAMP   NOT NULL DEFAULT current_timestamp,
  last_update_time TIMESTAMP   NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp,
  comment          VARCHAR(100)
);


INSERT INTO user (username, password, enabled, expired, locked, category, add_time, last_update_time, comment)
VALUES ('admin', 'admin', TRUE, FALSE, FALSE, 1, current_timestamp, current_timestamp, 'Administrator');
INSERT INTO user (username, password, enabled, expired, locked, category, add_time, last_update_time, comment)
VALUES ('user', 'password', TRUE, FALSE, FALSE, 2, current_timestamp, current_timestamp, 'User');