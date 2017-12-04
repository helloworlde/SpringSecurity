CREATE TABLE menu (
  id               INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
  value            VARCHAR(100) NOT NULL,
  display_value    VARCHAR(100) NOT NULL,
  url              VARCHAR(100) NOT NULL,
  category         INT,
  description      VARCHAR(100),
  active           BOOLEAN      NOT NULL DEFAULT TRUE,
  last_update_time TIMESTAMP             DEFAULT current_timestamp ON UPDATE current_timestamp
);

INSERT INTO menu (value, display_value, url, category, description, active, last_update_time)
VALUES ('/admin/dashboard', 'Admin Dashboard', '/admin/dashboard', 10, 'Admin Dashboard', TRUE, current_timestamp);

INSERT INTO menu (value, display_value, url, category, description, active, last_update_time)
VALUES ('/admin/user/list', 'User Management', '/admin/user/list', 10, 'Admin Manage User', TRUE, current_timestamp);

INSERT INTO menu (value, display_value, url, category, description, active, last_update_time)
VALUES ('/user/dashboard', 'User Dashboard', '/user/dashboard', 11, 'User Dashboard', TRUE, current_timestamp);

INSERT INTO menu (value, display_value, url, category, description, active, last_update_time)
VALUES ('/user/profile', 'User Profile', '/user/profile', 11, 'User Profile', TRUE, current_timestamp);
