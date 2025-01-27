alter table intervention_catalogue rename column short_decription to short_description;
alter table intervention_catalogue rename column long_decription to long_description;

-- removing constraints as data not mandatory for all intervention types
alter table intervention_catalogue alter column long_description text null;
alter table intervention_catalogue alter column topic text null;
alter table intervention_catalogue alter column session_detail text null;
alter table intervention_catalogue alter column commencement_date date null;
alter table intervention_catalogue alter column termination_date date null;

-- adding column in this table until final decision is made on where this data is displayed
alter table intervention_catalogue add column reasons_for_referral text null;
