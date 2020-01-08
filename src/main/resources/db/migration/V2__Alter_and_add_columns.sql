ALTER TABLE hungry_professional
DROP COLUMN vote_date;

ALTER TABLE vote
ADD COLUMN hungry_professional INTEGER NOT NULL;

ALTER TABLE vote
ADD FOREIGN KEY (hungry_professional)
REFERENCES hungry_professional(id);

