CREATE TABLE user (
  id       INT                  AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(45) NOT NULL,
  password VARCHAR(45) NOT NULL,
  enabled  INT         NOT NULL DEFAULT 1
);

INSERT INTO user (username, password, enabled) VALUES ('username', 'password', TRUE);

# This table is for save user login token parameter Remember Me function
CREATE TABLE persistent_logins (
  username  VARCHAR(64) NOT NULL,
  series    VARCHAR(64) NOT NULL PRIMARY KEY,
  token     VARCHAR(64) NOT NULL,
  last_used TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);