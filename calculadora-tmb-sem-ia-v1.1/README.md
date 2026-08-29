# Calculadora TMB

Aplicativo Android para cálculo de Taxa Metabólica Basal (TMB), gasto calórico diário e acompanhamento de refeições, com dados armazenados localmente.

## Build

O projeto usa Gradle e pode gerar um APK de debug com:

```bash
./gradlew assembleDebug
```

O APK será gerado em `app/build/outputs/apk/debug/app-debug.apk`.

## GitHub Actions

O workflow em `.github/workflows/build-apk.yml` gera o APK automaticamente e o disponibiliza como Artifact.

## Versão atual

**1.1 (versionCode 2)**
