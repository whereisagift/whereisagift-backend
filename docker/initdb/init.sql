insert into users (id, name) values
(1, 'Alice'),
(2, 'Bob')
ON CONFLICT DO NOTHING;