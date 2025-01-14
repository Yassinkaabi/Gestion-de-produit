# Application de Blog avec Spring Boot

## Table des Matières

1. [Introduction](#introduction)
2. [Fonctionnalités](#fonctionnalités)
3. [Prérequis](#prérequis)
4. [Configuration et Déploiement](#configuration-et-déploiement)
   - [Dockerisation](#dockerisation)
   - [Déploiement Kubernetes](#déploiement-kubernetes)
   - [Monitoring avec Prometheus et Grafana](#monitoring-avec-prometheus-et-grafana)
   - [CI/CD avec Jenkins](#cicd-avec-jenkins)
5. [Utilisation](#utilisation)
6. [Commandes Utiles](#commandes-utiles)
7. [Extension (Bonus)](#extension-bonus)

---

## Introduction

Cette application est un blog développé avec Spring Boot. Le projet est conçu pour être conteneurisé avec Docker, déployé sur Kubernetes, et monitoré avec Prometheus et Grafana. Il prend également en charge CI/CD avec Jenkins et propose une extension GitOps avec ArgoCD.

## Fonctionnalités

- Gestion des utilisateurs, articles, et photos.
- API REST sécurisée avec Spring Security.
- Intégration avec MySQL pour la gestion des données.

## Prérequis

1. **Outils** :
   - Docker & Docker Compose
   - Kubernetes (Minikube ou Kind)
   - Helm
   - Jenkins
2. **Comptes nécessaires** :
   - Docker Hub
   - Repository Git (ex. GitHub)

---

## Configuration et Déploiement

### Dockerisation

1. Créez un fichier `Dockerfile` :
   ```dockerfile
   FROM openjdk:17-jdk-alpine
   ARG JAR_FILE=target/blog-app.jar
   COPY ${JAR_FILE} app.jar
   EXPOSE 8080
   ENTRYPOINT ["java", "-jar", "/app.jar"]
   ```
2. Construisez l'image :
   ```bash
   mvn clean package
   docker build -t blog-app:1.0 .
   ```
3. Poussez l'image sur Docker Hub :
   ```bash
   docker tag blog-app:1.0 <votre-utilisateur-docker>/blog-app:1.0
   docker push <votre-utilisateur-docker>/blog-app:1.0
   ```

### Déploiement Kubernetes

#### Déploiement de l'application

**Fichier ********************`k8s-deployment.yml`******************** :**

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: blog-app-deployment
  labels:
    app: blog-app
spec:
  replicas: 3
  selector:
    matchLabels:
      app: blog-app
  template:
    metadata:
      labels:
        app: blog-app
    spec:
      containers:
        - name: <votre-utilisateur-docker>
         image: <votre-utilisateur-docker>/blog-app:1.0
          ports:
            - containerPort: 8080
          env:
            - name: SPRING_PROFILES_ACTIVE
              value: "prod"
            - name: SPRING_DATASOURCE_URL
              value: "jdbc:mysql://mysql-service:3306/blogSpring"
            - name: SPRING_DATASOURCE_USERNAME
              value: "admin"
            - name: SPRING_DATASOURCE_PASSWORD
              value: "password"
```

**Fichier ********************`k8s-service.yml`******************** :**

```yaml
apiVersion: v1
kind: Service
metadata:
  name: blog-app-service
spec:
  type: NodePort
  selector:
    app: blog-app
  ports:
    - protocol: TCP
      port: 8080
      targetPort: 8080
      nodePort: 30007
```

#### Déploiement de MySQL

**Fichier ********************`mysql-deployment.yml`******************** :**

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: mysql-deployment
  labels:
    app: mysql
spec:
  replicas: 1
  selector:
    matchLabels:
      app: mysql
  template:
    metadata:
      labels:
        app: mysql
    spec:
      containers:
        - name: mysql
          image: mysql:8.0
          ports:
            - containerPort: 3306
          env:
            - name: MYSQL_ROOT_PASSWORD
              value: rootpassword
            - name: MYSQL_DATABASE
              value: blogSpring
            - name: MYSQL_USER
              value: admin
            - name: MYSQL_PASSWORD
              value: password
---
apiVersion: v1
kind: Service
metadata:
  name: mysql-service
spec:
  type: ClusterIP
  selector:
    app: mysql
  ports:
    - protocol: TCP
      port: 3306
      targetPort: 3306
```

1. Appliquez les fichiers de déploiement :
   ```bash
   kubectl apply -f mysql-deployment.yml
   kubectl apply -f k8s-deployment.yml
   kubectl apply -f k8s-service.yml
   ```
2. Vérifiez les ressources Kubernetes :
   ```bash
   kubectl get pods
   kubectl get services
   ```

### Monitoring avec Prometheus et Grafana

1. Installez Prometheus et Grafana via Helm :
   ```bash
   helm install prometheus prometheus-community/prometheus
   helm install grafana prometheus-community/grafana
   ```
2. Configurez Grafana pour utiliser Prometheus comme source de données.

### CI/CD avec Jenkins

1. Configurez un pipeline Jenkins :
   ```groovy
   pipeline {
       agent any
       stages {
           stage('Build') {
               steps {
                   sh 'mvn clean package'
               }
           }
           stage('Docker Build & Push') {
               steps {
                   sh 'docker build -t <votre-utilisateur-docker>/blog-app:${BUILD_NUMBER} .'
                   sh 'docker push <votre-utilisateur-docker>/blog-app:${BUILD_NUMBER}'
               }
           }
           stage('Deploy to Kubernetes') {
               steps {
                   sh 'kubectl apply -f k8s-deployment.yml'
               }
           }
       }
   }
   ```

---

## Utilisation

1. Accédez à l'application :
   ```bash
   minikube service blog-app-service
   ```
2. Surveillez les performances via Grafana à l'adresse exposée.

## Commandes Utiles

- Lister les pods :
  ```bash
  kubectl get pods
  ```
- Lister les services :
  ```bash
  kubectl get services
  ```
- Voir les logs d'un pod :
  ```bash
  kubectl logs <nom-du-pod>
  ```

## Extension (Bonus)

- **Déploiement sur AKS/EKS** : Configurez les credentials pour accéder au cluster et appliquez les manifestes.
- **ArgoCD** : Implémentez GitOps pour synchroniser automatiquement les modifications depuis GitHub.

---

Ce projet est conçu pour être flexible et extensible afin de s'adapter à des cas d'utilisation réels. N'hésitez pas à contribuer ou à signaler des problèmes dans le repository GitHub !

