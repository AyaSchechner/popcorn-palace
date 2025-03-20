INSERT INTO task ( description, completed) VALUES
                                               ( 'description1', TRUE),
                                               ('description2', FALSE);

INSERT INTO movies (title, genre, duration, rating, release_year) VALUES
('Inception', 'Sci-Fi', 148, 8.8, 2010),
('Interstellar', 'Sci-Fi', 169, 8.6, 2014);

INSERT INTO showtimes (movie_id, theater, start_time, end_time, price) VALUES
(1, 'CinemaX Hall 2', '2025-03-19 18:00:00', '2025-03-19 20:30:00', 15),
(2, 'CinemaX Hall 2', '2025-03-19 21:00:00', '2025-03-19 23:30:00', 15)
ON CONFLICT (theater, start_time) DO NOTHING;

INSERT INTO tickets (showtime_id, customer_name, seat_number) VALUES
(1, 'John Doe', 5),
(1, 'Jane Doe', 6),
(2, 'Alice Smith', 3);