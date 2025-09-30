{{- $namespace := requiredEnv "K8S_NAMESPACE"     }}
{{- $envName   := requiredEnv "ENV_NAME"          }}

registries:
  - host: {{ requiredEnv "REGISTRY" }}
    username: token
    password: "{{ requiredEnv "GH_REGISTRY_CONTAINER_READ" }}"

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
  - name: backend-{{ $envName }}
    namespace: {{ $namespace }}
    chart:
      name: ../common-charts/msvc
      version: 0.1.0
    values:
      - src: ./values/_common.yaml
        renderer: sprig
      - src: ./values/{{ $envName }}.yaml
        renderer: sprig
    <<: *options
