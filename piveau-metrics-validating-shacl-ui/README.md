# piveau-shacl-validator-ui

## Project setup

### 1. Install [Node.js v12.13.0+ and NPM 6.10.0+](https://nodejs.org/en/) on your system.

### 2. Clone or download the piveau-shacl-validator-ui repository:
```
git clone https://gitlab.fokus.fraunhofer.de/piveau/metrics/piveau-metrics-validating-shacl-ui.git
cd piveau-metrics-validating-shacl-ui
```

### 3. Install NPM packages:
```
npm install
```

### 4. Run for development
```
npm run serve
```

### 5. Run for production
```
npm run build
```

## Configuration reference

### Environment variables

| Key                | Description                                                                   |
|--------------------|-------------------------------------------------------------------------------|
| `VUE_APP_SHACL_API_URL`           | SHACL validator service URL                     |
| `VUE_APP_CORSPROXY_API_URL`           | Corsproxy service URL. If empty, will attempt non-proxy requests              |
