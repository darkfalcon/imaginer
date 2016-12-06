insert into user_group values (1, 'USER', 'Normál felhasználó: tud galériát létrehozni, képeket feltölteni és megtekinteni');
insert into user_group values (2, 'REPORT', 'Riport készítő felhasználó: létező felhasználó, csak a riportok oldalt látja');
ALTER TABLE user_group_to_user
  DROP PRIMARY KEY,
   ADD PRIMARY KEY(
     user_id,
     user_group_id);