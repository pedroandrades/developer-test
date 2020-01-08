create sequence hungry_professional_id_seq
owned by hungry_professional.id;

alter table hungry_professional
alter column id set default nextval('hungry_professional_id_seq');

create sequence vote_id_seq
owned by vote.id;

alter table vote
alter column id set default nextval('vote_id_seq');

create sequence restaurant_id_seq
owned by restaurant.id;

alter table restaurant
alter column id set default nextval('restaurant_id_seq');
