CREATE TABLE role_menu_xref (
  id               INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  role_id          INT NOT NULL,
  menu_id          INT NOT NULL,
  last_update_time TIMESTAMP    DEFAULT current_timestamp ON UPDATE current_timestamp,
  CONSTRAINT FOREIGN KEY fk_role_menu_xref_role_id (role_id) REFERENCES role (id),
  CONSTRAINT FOREIGN KEY fk_role_menu_xref_menu_id(menu_id) REFERENCES menu (id),
  CONSTRAINT UNIQUE (role_id, menu_id)
);

INSERT role_menu_xref (role_id, menu_id, last_update_time)
VALUES (1, 1, current_timestamp);

INSERT role_menu_xref (role_id, menu_id, last_update_time)
VALUES (1, 2, current_timestamp);

INSERT role_menu_xref (role_id, menu_id, last_update_time)
VALUES (2, 3, current_timestamp);

INSERT role_menu_xref (role_id, menu_id, last_update_time)
VALUES (2, 4, current_timestamp);

