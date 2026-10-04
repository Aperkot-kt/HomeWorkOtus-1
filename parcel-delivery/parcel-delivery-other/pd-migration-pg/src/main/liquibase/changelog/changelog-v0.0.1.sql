--liquibase formatted sql

--changeset sokatov:1 labels:v0.0.1
CREATE TYPE "pd_status_type" AS ENUM ('ACCEPTED', 'IN_TRANSIT', 'PENDING_DELIVERY', 'DELIVERED', 'NONE');

CREATE TABLE "pds" (
	"track_number" text primary key constraint pds_track_number_length_ctr check (length("track_number") < 64),
	"sender_id" text constraint pds_sender_id_length_ctr check (length("sender_id") < 64),
	"receiver_id" text constraint pds_receiver_id_length_ctr check (length("receiver_id") < 64),
	"weight" double precision not null default 0.0,
	"dimensions" text constraint pds_dimensions_length_ctr check (length("dimensions") < 64),
	"status" pd_status_type,
	"delivery_address" text constraint pds_delivery_address_length_ctr check (length("delivery_address") < 4096),
	"created_at" timestamp with time zone,
	"updated_at" timestamp with time zone,
	"lock" text constraint pds_lock_length_ctr check (length("lock") < 64)
);

CREATE INDEX pds_sender_id_idx on "pds" using hash ("sender_id");

CREATE INDEX pds_receiver_id_idx on "pds" using hash ("receiver_id");

CREATE INDEX pds_status_idx on "pds" using hash ("status");
