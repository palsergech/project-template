{{/*
Returns env variables for service
*/}}
{{- define "msvc.env" -}}
{{- with .Values.envVars }}
{{ include "msvc.tplvalues.render" ( dict "value" (toYaml .) "context" $ ) }}
{{- end }}
{{- with .Values.extraEnvVars }}
{{ include "msvc.tplvalues.render" ( dict "value" (toYaml .) "context" $ ) }}
{{- end }}
{{- $envSecret := merge (.Values.envSecretVars | default (dict)) (.Values.extraEnvSecretVars | default (dict)) }}
{{- range $k, $v := $envSecret }}
- name: {{ $k }}
  valueFrom:
    secretKeyRef:
      key: {{ $k }}
      name: {{ $.secretName }}
{{- end }}
{{- end -}}
