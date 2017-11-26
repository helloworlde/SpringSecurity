CREATE TABLE menu (
  id               INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
  value            VARCHAR(100) NOT NULL,
  display_value    VARCHAR(100) NOT NULL,
  url              VARCHAR(100) NOT NULL,
  category         INT,
  description      VARCHAR(100),
  is_active        BOOLEAN      NOT NULL DEFAULT TRUE,
  last_update_time TIMESTAMP             DEFAULT current_timestamp ON UPDATE current_timestamp
);

INSERT INTO menu (value, display_value, url, description, is_active, last_update_time)
VALUES ('/admin/dashboard', 'Admin Dashboard', '/admin/dashboard', 'Admin Dashboard', TRUE, current_timestamp);

INSERT INTO menu (value, display_value, url, description, is_active, last_update_time)
VALUES ('/admin/profile', 'Admin Profile', '/admin/profile', 'Admin Profile', TRUE, current_timestamp);


INSERT INTO menu (value, display_value, url, description, is_active, last_update_time)
VALUES ('/user/dashboard', 'User Dashboard', '/user/dashboard', 'User Dashboard', TRUE, current_timestamp);

INSERT INTO menu (value, display_value, url, description, is_active, last_update_time)
VALUES ('/user/profile', 'User Profile', '/user/profile', 'User Profile', TRUE, current_timestamp);
