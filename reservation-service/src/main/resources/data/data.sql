
-- Users
INSERT INTO user (id, username, password, name, phone_number, email, birth_date, account_id, account_password, role, status)
VALUES
    (1, '아이디1', 'password123', 'John Doe', '010-1234-5678', 'john@example.com', '1990-01-01', 'acc123', 'accPass123', 'USER', 'ACTIVE'),
    (2, '아이디2', 'password123', 'Jane Doe', '010-8765-4321', 'jane@example.com', '1995-02-15', 'acc456', 'accPass456', 'INSTRUCTOR', 'ACTIVE')
    (3, '아이디3', 'password123', 'John Doe', '010-1234-5678', 'john@example.com', '1990-01-01', 'acc123', 'accPass123', 'USER', 'ACTIVE'),
(4, '아이디4', 'password123', 'John Doe', '010-1234-5678', 'john@example.com', '1990-01-01', 'acc123', 'accPass123', 'USER', 'ACTIVE'),
(5, '아이디5', 'password123', 'John Doe', '010-1234-5678', 'john@example.com', '1990-01-01', 'acc123', 'accPass123', 'USER', 'ACTIVE'),
(6, '아이디6', 'password123', 'John Doe', '010-1234-5678', 'john@example.com', '1990-01-01', 'acc123', 'accPass123', 'USER', 'ACTIVE'),
(7, '아이디7', 'password123', 'John Doe', '010-1234-5678', 'john@example.com', '1990-01-01', 'acc123', 'accPass123', 'USER', 'ACTIVE'),
;

-- Courses
INSERT INTO course (id, name, course_category, course_duration_start_date, course_duration_end_date, course_schedule_days, course_schedule_start_time, course_schedule_end_time, instructor_id, status, enrollment_capacity_max, enrollment_capacity_current, price, place, enrollment_deadline)
VALUES
    (1, 'Java Programming Basics', 'PROGRAMMING', '2025-02-01', '2025-03-01', 'Monday,Wednesday', '10:00:00', '12:00:00', 2, 'OPEN', 50, 0, 100000, 'Online', '2025-02-01'),
    (2, 'Advanced Python Programming', 'PROGRAMMING', '2025-03-15', '2025-04-15', 'Tuesday,Thursday', '14:00:00', '16:00:00', 2, 'OPEN', 30, 0, 150000, 'Offline Classroom', '2025-01-16'),
