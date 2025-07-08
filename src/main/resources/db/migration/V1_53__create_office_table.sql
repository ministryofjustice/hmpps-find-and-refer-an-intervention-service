CREATE TABLE office
(
    id          UUID PRIMARY KEY UNIQUE       NOT NULL,
    office_name       contact_email   text  NOT NULL,
    created_at timestamp with time zone not null,
    created_by_user text not null,
    deleted_at text,
);

ALTER TABLE referral
    ADD COLUMN office_id UUID;

ALTER TABLE referral
    ADD CONSTRAINT fk_referral_office
        FOREIGN KEY (office_id)
            REFERENCES office(id)
            ON DELETE SET NULL;