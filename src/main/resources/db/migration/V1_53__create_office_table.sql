CREATE TABLE office
(
    id          UUID PRIMARY KEY UNIQUE       NOT NULL,
    referral_id UUID REFERENCES referral (id) NOT NULL,
    office_name       contact_email   text  NOT NULL,
    created_at timestamp with time zone not null,
    created_by_user text not null,
    deleted_at text,
);
