alter table intervention_catalogue rename column short_decription to short_description
alter table intervention_catalogue rename column long_decription to long_description

alter table intervention_catalogue alter column long_description text null;
alter table intervention_catalogue alter column topic text null;
alter table intervention_catalogue alter column session_detail text null;
alter table intervention_catalogue alter column commencement_date date null;
alter table intervention_catalogue alter column termination_date date null;

alter table intervention_catalogue add column reasons_for_referral text;
