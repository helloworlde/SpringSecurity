CREATE TABLE user (
  id       INT                  AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(45) NOT NULL,
  password VARCHAR(45) NOT NULL,
  enabled  INT         NOT NULL DEFAULT 1
);

INSERT INTO user (username, password, enabled) VALUES ('admin', 'admin', TRUE);
INSERT INTO user (username, password, enabled) VALUES ('user', 'user', TRUE);