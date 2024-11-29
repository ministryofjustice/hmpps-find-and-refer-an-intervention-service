    {{/* vim: set filetype=mustache: */}}
{{/*
Environment variables for web and worker containers
*/}}
{{- define "deployment.envs" }}
env:
  - name: POSTGRES_URI
    valueFrom:
      secretKeyRef:
        name: rds-instance-output
        key: rds_instance_endpoint

  - name: POSTGRES_DB
    valueFrom:
      secretKeyRef:
        name: rds-instance-output
        key: database_name

  - name: POSTGRES_USERNAME
    valueFrom:
      secretKeyRef:
        name: rds-instance-output
        key: database_username

  - name: POSTGRES_PASSWORD
    valueFrom:
      secretKeyRef:
        name: rds-instance-output
        key: database_password
{{- end -}}