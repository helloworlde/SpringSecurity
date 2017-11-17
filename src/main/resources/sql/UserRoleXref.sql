CREATE TABLE user_role_xref (
  id               INT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
  user_id          INT       NOT NULL,
  role_id          INT       NOT NULL,
  last_update_time TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp,
  CONSTRAINT FOREIGN KEY fK_user_role_xref_user_id_user_id (user_id) REFERENCES user (id),
  CONSTRAINT FOREIGN KEY fk_user_role_xref_role_id_role_id (role_id) REFERENCES role (id)
);

INSERT INTO user_role_xref (user_id, role_id, last_update_time) VALUES (1, 1, CURRENT_TIMESTAMP);
INSERT INTO user_role_xref (user_id, role_id, last_update_time) VALUES (2, 2, CURRENT_TIMESTAMP);