{{- $namespace := requiredEnv "K8S_NAMESPACE"     }}
{{- $envName   := requiredEnv "ENV_NAME"          }}

registries:
  - host: registry-1.docker.io

# General options
.options: &options
  wait: true
  wait_for_jobs: true
  timeout: 300s
  create_namespace: false
  atomic: true
  max_history: 10
  pending_release_strategy: rollback

releases:
  - name: keycloak-{{ $envName }}
    namespace: {{ $namespace }}
    chart:
      name: oci://registry-1.docker.io/bitnamicharts/keycloak
      version: 25.2.3
    values:
      - src: ./values/_common.yaml
        renderer: sprig
        delimiter_left: "[["
        delimiter_right: "]]"
      - src: ./values/{{ $envName }}.yaml
        renderer: sprig
        delimiter_left: "[["
        delimiter_right: "]]"
    <<: *options
