CREATE TABLE check_in (
    id INTEGER NOT NULL PRIMARY KEY IDENTITY,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    attendee_id VARCHAR(255) NOT NULL,
    CONSTRAINT checkin_attendee_id_fkey FOREIGN KEY (attendee_id)
      REFERENCES attendee(id) ON DELETE RESTRICT ON UPDATE CASCADE
);
