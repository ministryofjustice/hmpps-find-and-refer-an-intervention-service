    {{/* vim: set filetype=mustache: */}}
{{/*
Environment variables for web and worker containers
*/}}
{{- define "deployment.envs" }}
env:
  - name: SERVER_PORT
    value: "{{ .Values.image.ports.app }}"

  {{ range $key, $value := .Values.env }}
  - name: {{ $key }}
    value: "{{ $value }}"
  {{ end }}
{{- end -}}
