DELETE
FROM special_educational_need;
DELETE
FROM risk_consideration;
DELETE
FROM possible_outcome;
DELETE
FROM personal_eligibility;
DELETE
FROM criminogenic_need;
DELETE
FROM exclusion;
DELETE
FROM excluded_offence;
DELETE
FROM enabling_intervention;
DELETE
FROM eligible_offence;
DELETE
FROM delivery_method_setting;
DELETE
FROM delivery_method;
DELETE
FROM delivery_location;
DELETE
FROM intervention_catalogue_map;
DELETE
FROM intervention_catalogue_to_course_map;
DELETE
FROM intervention_catalogue;
DELETE
FROM message;
DELETE
FROM referral;

INSERT INTO intervention_catalogue (id, name, int_type, short_description, long_description, topic,
                                    session_detail, commencement_date, termination_date, created, created_by,
                                    last_modified, last_modified_by, reason_for_referral, time_to_complete)
VALUES
    -- CRS interventions
    ('47b0048c-cab2-4509-a64a-29a4218712c9', 'Accommodation', 'CRS',
     'provides male individuals on a community/suspended sentence order with a RAR or on license/post-sentence supervision with non-CAS accommodation interventions.',
     'The Accommodation service provides male individuals on a community/suspended sentence order with a RAR or on license/post-sentence supervision with accommodation interventions. These services will also be delivered pre-release for individuals who will be under probation supervision on release. The accommodation service provides a variety of interventions based upon the individual’s risk category (low / medium / high).',
     null, 'Variable', '2025-03-28', null, '2025-03-28 13:58:55.616122 +00:00',
     '00000000-0000-0000-0000-000000000000', null, null, '', null),
    ('c5d53fbd-b7e3-40bd-9096-6720a01a53bf', 'Dependency and Recovery', 'CRS',
     'provides support to individuals within the community to address addictions.', 'The D&R service provides support to individuals within the community to address addictions. The service offers the following interventions and programmes:
        - Relapse prevention
        - Interventions for alcohol
        - Chemsex screening, interventions and pathways
        - Programmes aimed at those who use cannabis and other psychoactive drugs.', null, 'Variable', '2025-03-28',
     null,
     '2025-03-28 13:58:56.156412 +00:00', '00000000-0000-0000-0000-000000000000', null, null, '', null),
    ('1e2370d7-be74-4faf-b973-68d258fea015', 'Finance, Benefit & Debt (FBD)', 'CRS',
     'Service to addresss immediate financial needs -', 'In custody FBD services focus on addressing immediate financial needs that arise from being in custody such as freezing debts and stopping benefits.
        Pre-release activity in relation to benefit eligibility and obtaining identification documents or bank accounts
        In community, supports individuals in managing their debt, managing their finances and provide advice on where to go for further support.
        The referral route for individuals in custody is through the resettlement teams and service providers on the wings.',
     null, 'Variable', '2025-03-28', null, '2025-03-28 13:58:56.217863 +00:00',
     '00000000-0000-0000-0000-000000000000', null, null, '', null),
    -- ACP Interventions
    ('7ce8b4ef-1429-4fc9-a7fe-706aab4dde78', 'Healthy Identity Intervention', 'ACP',
     'Healthy Identity Intervention (HII) targets the social and psychological drivers of extremist offending. It helps participants disengage from extremist groups, causes, or ideologies and to reconnect with their own personal values and beliefs.',
     'Healthy Identity Intervention (HII) targets the social and psychological drivers of extremist offending. It helps participants disengage from extremist groups, causes, or ideologies and to reconnect with their own personal values and beliefs.',
     null, '10 to 32 sessions', '2025-03-28', null, '2025-03-28 14:22:55.770539 +00:00',
     '00000000-0000-0000-0000-000000000000', null, null, '', null),
    ('8380e2d6-ba49-4309-8be7-cc83bf87f372', 'Becoming New Me Plus: general violence offence', 'ACP',
     'Becoming New Me Plus (BNM+) uses cognitive behavioural therapy. It is for men who are at high or very high risk of reoffending. This strand is for men convicted of a general violence offence.',
     'Becoming New Me Plus (BNM+) uses cognitive behavioural therapy. It is for men who are at high or very high risk of reoffending. This strand is for men convicted of a general violence offence.',
     null, 'Group and One-to-one in person
        85 group sessions + 7 individual sessions
        OR exclusively One-to-one
        Minimum 20 sessions long
        Depends on the needs of the individual', '2025-03-28', null, '2025-03-28 14:22:55.104239 +00:00',
     '00000000-0000-0000-0000-000000000000', null, null, '', null),
    ('c5363bae-bd0c-45be-816f-01863cf6396d', 'Horizon', 'ACP',
     'Horizon is for men convicted of a sexual or sexually-motivated offence who are medium risk or above. It helps address problematic factors and how they contribute to behaviour.',
     'Horizon is for men convicted of a sexual or sexually-motivated offence who are medium risk or above. It helps address problematic factors and how they contribute to behaviour.',
     null, '3 x One-to-one sessions; 31 x group sessions', '2025-03-28', null, '2025-03-28 14:22:55.856437 +00:00',
     '00000000-0000-0000-0000-000000000000', null, null, '', null),
    -- SI Interventions
    ('1bca8fb6-2e94-4680-b211-847ffbd9a294', 'SMART Inside Out', 'SI',
     'The SMART Inside Out program is designed specifically to help people currently involved in the criminal justice system. SMART recovery helps participants identify whether their substnce/alcohol use is a problematic behaviour for them, it builds up their motivation to make changes and offers a set of toll and techniques to support change.',
     '', null, '10 x group sessions (Closed group) 2hrs long', '2025-03-28', null, '2025-03-28 14:22:56.499915 +00:00',
     '00000000-0000-0000-0000-000000000000', null, null, '', null),
    ('7822b08d-1780-4866-8303-76f1632315ff', 'Better Solutions', 'SI',
     'Structured Intervention: Attitudes, Thinking & Behaviour',
     'Better Solutions is an Intervention designed for service users to develop their thinking skills. The course provides an important foundation, influencing ways to approach situations and choices.  - ',
     null, e'1 x pre-group session;
        6 x group sessions', '2025-03-28', null, '2025-03-28 14:22:55.246333 +00:00',
     '00000000-0000-0000-0000-000000000000',
     null, null, '', null),
    ('9c697406-e6ab-46c4-9420-1bddaac3193c', 'Stepwise Driving', 'SI',
     'Structured Intervention: Attitudes, Thinking & Behaviour (Driving)', e'Stepwise Driving is designed to support desistance from drink driving by focusing on areas such as awareness of safe driving; making decisions; and, consequential thinking.
        Stepwise Driving is comprised of insight-oriented psycho-educational content around: 1. safer driving (e.g., Reaction times, Stopping distances, etc); 2. Consequences of dangerous driving; 3. effects of alcohol and drugs on driving; 4. Coping with disqualification.',
     null, e'1 x One-to-one session;
        5 x group sessions', '2025-03-28', null, '2025-03-28 14:22:56.621586 +00:00',
     '00000000-0000-0000-0000-000000000000',
     null, null, '', null),
    -- TOOLKITS Interventions
    ('02bb45cc-8070-4bf6-a924-00cdb705550d', 'Pathways to Change', 'TOOLKITS',
     'The toolkit for Women convicted of sexual offences',
     'The toolkit was created to fill the existing gap in provision for work with Women convicted of sexual offences. It is gender specific and trauma-informed, underpinned by strengths based approaches and Bio-psycho-social model, and is designed to be delivered flexibly to best address the needs of the woman on probation.',
     null, e'
        Variable ', '2025-03-28', null, '2025-03-28 15:16:48.549285 +00:00', '00000000-0000-0000-0000-000000000000',
     null, null,
     '', null),
    ('bfbbe2a9-e1d1-453d-88a6-3589f4bda870', 'Maps for Change', 'TOOLKITS',
     'Developed for use with adult males convicted of sexual/sexuality motivated offences. ', e'Developed for use with adult males convicted of sexual/sexuality motivated offences. It does not replace an accredited programme and is aimed to suit those whose risk assessment scores are likely to be in the Low OSP/C and Low OSP/I category, however, the toolkit can be used for individuals who are unable to access an accredited programme e.g. for health or language reasons.
        Each MAP focuses on different areas of need and is underpinned by a strengths-based approach to help the individual develop strengths and capabilities to overcome risk factors.',
     null, 'Variable ', '2025-03-28', null, '2025-03-28 15:16:48.471486 +00:00', '00000000-0000-0000-0000-000000000000',
     null, null, '', null),
    ('d97a7462-4035-473e-abe0-afda8c28d1fc', 'Moving On', 'TOOLKITS',
     'Aimed at People on Probation who are experiencing negative thoughts and feelings about themselves, which are associated with their conviction/offending and its consequences. ',
     'Aimed at People on Probation who are experiencing negative thoughts and feelings about themselves, which are associated with their conviction/offending and its consequences. These thoughts and feelings (which may include conviction-related feelings of shame) will be blocking the individual, emotionally or psychologically, from moving forward positively with their life, potentially obstructing desistance',
     null, '8 x One-to-one sessions', '2025-03-28', null, '2025-03-28 15:16:48.485203 +00:00',
     '00000000-0000-0000-0000-000000000000', null, null, '', null);

INSERT INTO delivery_method (id, intervention_id, delivery_method_description, delivery_format, attendance_type)
VALUES
    -- CRS Interventions
    ('57b42ecb-8774-400f-805b-a7fc7efb0a66', 'c5d53fbd-b7e3-40bd-9096-6720a01a53bf', null, 'Group and One-to-one',
     'In Person and Online'),
    ('dd3acfe4-b62e-4366-955d-296d975cbe34', '47b0048c-cab2-4509-a64a-29a4218712c9', null, 'Group or One-to-one',
     'In Person'),
    ('0855cf84-1249-40ac-8ed5-321b7deb106c', '1e2370d7-be74-4faf-b973-68d258fea015', null, 'Group and One-to-one',
     'In Person or Online'),
    -- ACP Interventions
    ('bb8ec055-7448-4594-bf1a-6fbdad1c2ec9', '8380e2d6-ba49-4309-8be7-cc83bf87f372', null, 'Group or One-to-one',
     'In Person and Online'),
    ('95837c47-955a-4364-b73e-0ccefd12681a', '7ce8b4ef-1429-4fc9-a7fe-706aab4dde78', null, 'One-to-one', null),
    ('f1ba6326-6eb7-4422-8c1f-0e312952c48f', 'c5363bae-bd0c-45be-816f-01863cf6396d', null, 'Group', 'In Person'),
    -- SI Interventions
    ('f2433173-2fa7-40a4-8780-04f8c446c61e', '7822b08d-1780-4866-8303-76f1632315ff', null, 'Group', 'In Person'),
    ('4eba09ca-b601-4f3d-b3f0-f4c925600320', '1bca8fb6-2e94-4680-b211-847ffbd9a294', null, 'Group', 'Online'),
    ('06c3f1fd-b46f-43e1-a581-c3458877b993', '9c697406-e6ab-46c4-9420-1bddaac3193c', null, 'Group and One-to-one',
     'In Person or Online'),
    -- TOOLKITS Interventions
    ('598154bd-58bd-40dd-a0a5-9c82899b22b1', 'bfbbe2a9-e1d1-453d-88a6-3589f4bda870', null, 'One-to-one', null),
    ('1f512d29-723b-486c-9bee-3bf9bd28e165', 'd97a7462-4035-473e-abe0-afda8c28d1fc', null, 'One-to-one', null),
    ('6cb91c26-9f99-43e2-8aad-9e9258008b2c', '02bb45cc-8070-4bf6-a924-00cdb705550d', null, 'One-to-one', null);

INSERT INTO delivery_method_setting (id, intervention_id, setting)
VALUES ('f8312eb8-3a64-482b-b4f4-7d071c563b38', '47b0048c-cab2-4509-a64a-29a4218712c9', 'COMMUNITY'),
       ('45d7df27-cb81-460a-82fd-1eb6712e40d5', '47b0048c-cab2-4509-a64a-29a4218712c9', 'REMAND'),
       ('510af70f-da38-4bef-89ea-f269e4c1ca97', '47b0048c-cab2-4509-a64a-29a4218712c9', 'PRE_RELEASE'),
       ('8a232b12-198a-4a6a-8d3f-cba54fb03645', '47b0048c-cab2-4509-a64a-29a4218712c9', 'COMMUNITY'),
       ('c4ff3bd0-4f6f-4c8b-a332-8b2499f9c546', '47b0048c-cab2-4509-a64a-29a4218712c9', 'REMAND'),
       ('70593b2a-7274-4934-a431-373ac59dc83b', '47b0048c-cab2-4509-a64a-29a4218712c9', 'PRE_RELEASE'),
       ('bdca032c-8f4f-49ea-9e7d-aee92179c055', '8380e2d6-ba49-4309-8be7-cc83bf87f372', 'CUSTODY'),
       ('2bb0bd06-263c-4db0-9ac1-5ac5a9f17ed2', '8380e2d6-ba49-4309-8be7-cc83bf87f372', 'CUSTODY'),
       ('d9f09803-5c69-4891-a752-5692d56a5960', 'c5d53fbd-b7e3-40bd-9096-6720a01a53bf', 'COMMUNITY'),
       ('7e0ffedf-8bd4-48c0-88d4-ab5b722d0a7a', 'c5d53fbd-b7e3-40bd-9096-6720a01a53bf', 'COMMUNITY'),
       ('516b4a47-cacf-4e38-8e05-35fdbad2e2ba', '1e2370d7-be74-4faf-b973-68d258fea015', 'COMMUNITY'),
       ('873204bf-0711-46fe-8288-5a38f750fd90', '1e2370d7-be74-4faf-b973-68d258fea015', 'REMAND'),
       ('9370b747-4e9f-45cf-a8f5-6f32f3c64df8', '1e2370d7-be74-4faf-b973-68d258fea015', 'PRE_RELEASE'),
       ('43e941b6-7a95-4a96-bf57-73a612ea884f', '1e2370d7-be74-4faf-b973-68d258fea015', 'CUSTODY'),
       ('08648820-4723-41f6-ab56-15047348e33c', '1e2370d7-be74-4faf-b973-68d258fea015', 'COMMUNITY'),
       ('51b67deb-2c29-4bdb-ab18-37068f8ad580', '1e2370d7-be74-4faf-b973-68d258fea015', 'REMAND'),
       ('de9580c4-201f-4ca6-907c-45b9aad579fa', '1e2370d7-be74-4faf-b973-68d258fea015', 'PRE_RELEASE'),
       ('bbaa8fde-1293-4573-8676-296491e517de', '1e2370d7-be74-4faf-b973-68d258fea015', 'CUSTODY'),
       ('dbbeb01f-9193-49ea-8289-d2532b17de35', '7ce8b4ef-1429-4fc9-a7fe-706aab4dde78', 'COMMUNITY'),
       ('80f96b2f-5ca6-4d62-b6b9-05a1b94e0d22', '7ce8b4ef-1429-4fc9-a7fe-706aab4dde78', 'CUSTODY'),
       ('ca8877f8-0614-45da-9c22-6cd837e4ec2e', 'c5363bae-bd0c-45be-816f-01863cf6396d', 'COMMUNITY'),
       ('79471451-8a03-4620-88c6-b3b27e78f112', 'c5363bae-bd0c-45be-816f-01863cf6396d', 'CUSTODY'),
       -- SI Interventions
       ('0b78addc-02d8-4948-aed9-da64f03c1d9e', '7822b08d-1780-4866-8303-76f1632315ff', 'COMMUNITY'),
       ('83cd92e1-60c7-4051-9547-54c36df794cd', '1bca8fb6-2e94-4680-b211-847ffbd9a294', 'COMMUNITY'),
       ('ac6c9982-c249-420f-97b5-d6a397dcbe02', '9c697406-e6ab-46c4-9420-1bddaac3193c', 'COMMUNITY'),
       ('e94e8dbe-002a-4b3c-8fe3-7059cb091fc8', '9c697406-e6ab-46c4-9420-1bddaac3193c', 'COMMUNITY'),
       -- TOOLKITS Interventions
       ('448e50b7-7361-478a-81a9-8f8df3cdd857', 'bfbbe2a9-e1d1-453d-88a6-3589f4bda870', 'COMMUNITY'),
       ('059f0c22-0815-49c2-a432-8298bef72882', '02bb45cc-8070-4bf6-a924-00cdb705550d', 'COMMUNITY'),
       ('0dacf6c1-705c-4f51-8d0c-38e8f2e0a0a8', 'd97a7462-4035-473e-abe0-afda8c28d1fc', 'COMMUNITY');

INSERT INTO personal_eligibility (id, intervention_id, min_age, max_age, males, females)
VALUES -- CRS Interventions
       ('d99f65b5-39fc-4c6e-8186-802f27297072', '47b0048c-cab2-4509-a64a-29a4218712c9', null, null, true, false),
       ('bb0c548d-326b-46e2-9700-fa388356974e', 'c5d53fbd-b7e3-40bd-9096-6720a01a53bf', null, null, true, false),
       ('63c8bacc-edf7-4916-ac25-c75854e665e8', '1e2370d7-be74-4faf-b973-68d258fea015', null, null, true, false),
       -- ACP Interventions
       ('3eafc80b-f0c0-40b4-9482-9a926241f192', '8380e2d6-ba49-4309-8be7-cc83bf87f372', null, null, true, false),
       ('d2845970-9db1-485e-933c-1e31ca574298', '7ce8b4ef-1429-4fc9-a7fe-706aab4dde78', null, null, true, true),
       ('876d66c3-ebb5-4922-a852-59460c497ec3', 'c5363bae-bd0c-45be-816f-01863cf6396d', null, null, true, false),
       -- SI Interventions
       ('30fc94b0-45c2-4aca-800a-35f6c6f56c2a', '7822b08d-1780-4866-8303-76f1632315ff', null, null, true, true),
       ('67de7339-d0f2-49bb-8a12-35ab0b6a0475', '1bca8fb6-2e94-4680-b211-847ffbd9a294', null, null, true, true),
       ('4875b964-8799-481c-8f47-a79aee885d75', '9c697406-e6ab-46c4-9420-1bddaac3193c', null, null, true, true),
       -- TOOLKITS Interventions
       ('9fb4bdea-a96b-47c7-b9e6-f2bd8a9ddd55', 'bfbbe2a9-e1d1-453d-88a6-3589f4bda870', null, null, true, false),
       ('7ece83c9-8c97-4f1b-a0e1-e48a6c8a34cc', 'd97a7462-4035-473e-abe0-afda8c28d1fc', null, null, true, false),
       ('daf5b90a-ad5b-43e8-aa16-4e07e719fc95', '02bb45cc-8070-4bf6-a924-00cdb705550d', null, null, false, true);

INSERT INTO risk_consideration (id, intervention_id, cn_score_guide, extremism_risk_guide,
                                sara_partner_score_guide, sara_other_score_guide, osp_score_guide,
                                osp_dc_icc_combination_guide, ogrs_score_guide, ovp_guide, ogp_guide, pna_guide,
                                rosh_level, rsr_guide)
VALUES -- CRS Interventions
       ('fda90986-cb0a-458a-a2a9-0e6e2af65b60', '47b0048c-cab2-4509-a64a-29a4218712c9', 'NA', null, null, null, null,
        null, null, null, null, null, null, null),
       ('75c1f7e0-81aa-463e-84f1-12753a47e143', 'c5d53fbd-b7e3-40bd-9096-6720a01a53bf', null, null, null, null, null,
        null, null, null, null, null, null, null),
       ('4e2f04b9-5363-4c64-bed1-c0542a428e1e', '1e2370d7-be74-4faf-b973-68d258fea015', null, null, null, null, null,
        null, null, null, null, null, null, null),
       -- ACP Interventions
       ('c0ac906d-fd80-41d5-8eb2-cab9f5ddaefa', '8380e2d6-ba49-4309-8be7-cc83bf87f372', null, null, null, null, null,
        null, 'High OGRS (Offender Group Reconviction Scale)', 'High OVP (OASys Violence Predictor)', null, null, null,
        null),
       ('2a9e55a8-25c3-4864-8b6d-6237aefad4b6', '7ce8b4ef-1429-4fc9-a7fe-706aab4dde78', null, null, null, null, null,
        null, null, null, null, '22+ ERG (Extremism Risk Guidance) and an assessment', null, null),
       ('4400a127-811b-4056-916d-df6845c92196', 'c5363bae-bd0c-45be-816f-01863cf6396d', null, null, null, null,
        'Medium, high or very high OSP (OASys Sexual Reconviction Predictor)', null, null, null, null, null, null,
        null),
       -- SI Interventions
       ('122881bc-57b7-4154-9683-dd2015960555', '7822b08d-1780-4866-8303-76f1632315ff', e'Need is evidenced by a total score of 5+
        ● 2.6 - Recognises the impact and consequences of offending
        on victim, community / wider society
        ● 7.2 - Regular activities encourage offending
        ● 11.4 - Temper control
        ● 11.6 - Problem solving
        ● 11.7 - Awareness of consequences
        ● 11.9 - Understands other people’s point of view
        ● 12.1 - Pro-criminal attitudes', null, null, null, null, null, 'OGRS3 (Over 2 years): score 25+', null, null,
        null,
        null, null),
       ('ae528066-e567-4292-81b0-1215413f8a83', '1bca8fb6-2e94-4680-b211-847ffbd9a294', null, null, null, null, null,
        null, null, null, null, null, null, null),
       ('9a1a5439-1d2b-44e6-9033-7cb5f764418f', '9c697406-e6ab-46c4-9420-1bddaac3193c', null, null, null, null, null,
        null, 'OGRS3 (Over 2 years): score 25+', null, null, null, null, null),
       -- TOOLKITS Interventions
       ('3875b128-d039-4c6c-b25b-1f1a19626af0', 'bfbbe2a9-e1d1-453d-88a6-3589f4bda870', null, null, null, null, e'Lo
            But if Medium+  then non suitability for AP route applies', null, null, null, null, null, null, null),
       ('6a306016-8672-4619-bd44-e3341459187e', 'd97a7462-4035-473e-abe0-afda8c28d1fc', null, null, null, null, null,
        null, null, null, null, null, null, null),
       ('bdfca030-f04b-4d01-8cc2-a2c80bf7c876', '02bb45cc-8070-4bf6-a924-00cdb705550d', null, null, null, null, null,
        null, null, null, null, null, null, null);

INSERT INTO special_educational_need (id, intervention_id, literacy_level_guide, learning_disability_catered_for,
                                      equivalent_non_ldc_programme_guide)
VALUES -- CRS Interventions
       ('e72cc92c-27db-47d5-bba1-f38c59799ede', '47b0048c-cab2-4509-a64a-29a4218712c9', null, null, null),
       ('8377589b-4506-4481-8942-92b6f1c58a71', 'c5d53fbd-b7e3-40bd-9096-6720a01a53bf', null, null, null),
       ('32119694-4cf3-4fbf-969d-a6c2ee186774', '1e2370d7-be74-4faf-b973-68d258fea015', null, null, null),
       -- ACP Interventions
       ('2dd62a62-1f27-46ef-9dc8-d99cb3841191', '8380e2d6-ba49-4309-8be7-cc83bf87f372', null, 'Yes', null),
       ('2aaedd25-170c-4a42-8c4e-93f45c4c7f6d', '7ce8b4ef-1429-4fc9-a7fe-706aab4dde78', null,
        'Yes, but reviewed case by case', null),
       ('34d7266d-d31f-4f12-923f-434287ab84af', 'c5363bae-bd0c-45be-816f-01863cf6396d', null, 'No', null),
       -- SI Interventions
       ('d0546b8e-03da-4fd6-9d5d-6126767b67f7', '7822b08d-1780-4866-8303-76f1632315ff',
        'Able to read and understand simple sentence', null, null),
       ('0bc5144c-be9a-4eed-b47a-5070c369dabc', '1bca8fb6-2e94-4680-b211-847ffbd9a294',
        'Participants should be sufficiently literate (able to read and understand simple sentences)',
        'Support can be provided for service users that present with Learning Difficulties and Challenges (Case by case basis)',
        null),
       ('1aa42580-894f-41e9-b8b6-725731b9f280', '9c697406-e6ab-46c4-9420-1bddaac3193c',
        'Participants should be sufficiently literate (able to read and understand simple sentences)', null, null),
       -- TOOLKITS Interventions
       ('29670d63-b529-469b-a080-bf4b9dc8ed8a', 'bfbbe2a9-e1d1-453d-88a6-3589f4bda870', null, null, null),
       ('1123b3f3-b617-48a5-b7d6-df8dac0df30f', 'd97a7462-4035-473e-abe0-afda8c28d1fc', null, null, null),
       ('9134d9bc-66e4-47b9-93e5-0ac2a76b4868', '02bb45cc-8070-4bf6-a924-00cdb705550d', null, null, null);

INSERT INTO exclusion (id, intervention_id, min_remaining_sentence_duration_guide,
                       remaining_license_community_order_guide, alcohol_drug_problem_guide,
                       mental_health_problem_guide, other_preferred_method_guide, same_type_rule_guide,
                       schedule_frequency_guide)
VALUES -- ACP Interventions
       ('abb49a68-48f0-4c93-b964-88601b7da85c', '8380e2d6-ba49-4309-8be7-cc83bf87f372', null, null, null, null, null,
        null, null),
       ('84970ca8-eb3a-4b16-92c7-b783cc9c8a3d', '7ce8b4ef-1429-4fc9-a7fe-706aab4dde78', null, null, null, null, null,
        null, null),
       ('d2d8210a-d3f6-458d-bad7-0024faff32f7', 'c5363bae-bd0c-45be-816f-01863cf6396d', null, null, null, null, null,
        'Only 1', 'Over-ride if exception'),
       -- TOOLKITS Interventions
       ('5771e066-91a2-4977-97bd-b3c3cbcbf143', 'bfbbe2a9-e1d1-453d-88a6-3589f4bda870', null, null, null, null, null,
        null, null),
       ('32bcb6bd-5704-486b-897d-7dd3877e9e79', 'd97a7462-4035-473e-abe0-afda8c28d1fc', null, null, null, null, null,
        null, null),
       ('4af6537d-20da-4405-8f42-436648e1ce29', '02bb45cc-8070-4bf6-a924-00cdb705550d', null, null, null, null, null,
        null, null);

INSERT INTO eligible_offence (id, offence_type_id, intervention_id, victim_type, motivation)
VALUES -- SI Interventions
       ('b0517921-0d72-4f10-bea3-be10d6036e4f', 'b0517921-0d72-4f10-bea3-be10d6036e4f',
        '9c697406-e6ab-46c4-9420-1bddaac3193c', '', ''),
       ('c19708f5-85ac-43f6-bd46-fbac6f9dae2b', 'c19708f5-85ac-43f6-bd46-fbac6f9dae2b',
        '9c697406-e6ab-46c4-9420-1bddaac3193c', '', ''),
       -- TOOLKITS Interventions
       ('6a6d601f-d79c-4584-a910-92f572232a1a', 'cdca173d-8189-4c3d-9618-4b40bda56274',
        'bfbbe2a9-e1d1-453d-88a6-3589f4bda870', '', 'Sexual'),
       ('6212ead3-0fe6-4d6f-9189-22b950103fc3', 'cdca173d-8189-4c3d-9618-4b40bda56274',
        '02bb45cc-8070-4bf6-a924-00cdb705550d', '', '');

INSERT INTO excluded_offence (id, offence_type_id, intervention_id, victim_type, motivation)
VALUES -- SI Interventions
       ('021772d6-ae56-40b5-8894-fbdae09f78f7', 'cdca173d-8189-4c3d-9618-4b40bda56274',
        '7822b08d-1780-4866-8303-76f1632315ff', '', null),
       ('6b2992af-6339-40dd-97cb-f0ec68ec1ba8', '1585e943-d552-4e2c-bd5d-08229646f99c',
        '7822b08d-1780-4866-8303-76f1632315ff', '', null),
       ('6365ecd8-c4c4-45b8-a414-0b78509e95de', 'cdca173d-8189-4c3d-9618-4b40bda56274',
        '9c697406-e6ab-46c4-9420-1bddaac3193c', '', null),
       ('7ff3b221-d2b5-4b7a-8424-2efcb950eef2', '1585e943-d552-4e2c-bd5d-08229646f99c',
        '9c697406-e6ab-46c4-9420-1bddaac3193c', '', null);

INSERT INTO intervention_catalogue_map(intervention_catalogue_id, intervention_id)
VALUES ('c5d53fbd-b7e3-40bd-9096-6720a01a53bf', '65301f01-2e52-474a-a8ec-df94c7e6fced');







