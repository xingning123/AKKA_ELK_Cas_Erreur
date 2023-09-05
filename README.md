# Java App Template

## Local Installation

1. Install [IntelliJ community edition](https://www.jetbrains.com/fr-fr/idea/download/)
2. Download [JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
3. Configure JAVA_HOME environment variable on windows:
   - Go to environment variables
   - Add ```JAVA_HOME``` as the path to the previously downloaded SDK 17 folder
   - Add ```%JAVA_HOME%/bin``` to the Path variable
4. Open Project in IntelliJ
5. Add JDK to IntelliJ's config: ```File > Project Strcucture > SDKs``` 

Compile the project:
```shell
mvnw clean build package
```
Run the project:
```shell
java -jar .\application\target\application-1.0.0-jar-with-dependencies.jar
```

## Build Docker image

Install [Docker Desktop](https://www.docker.com/products/docker-desktop/)

To build the image, use this command in the project's folder (after compilation):
```shell
docker build -t <imageName>:<tag> .
```
(default tag is latest)

## Deploy image in Kubernetes with HELM

You can enable a local instance of Kubernetes to run using docker desktop. Go to ```Settings > Kubernetes > Enable Kubernetes```.
This also installs the ```kubectl``` command allowing interaction with the Kubernetes cluster.

1. Install [Helm](https://helm.sh/docs/intro/install/)
Run with wsl shell (because window doesn't support curl)
```shell
$ curl -fsSL -o get_helm.sh https://raw.githubusercontent.com/helm/helm/main/scripts/get-helm-3
$ chmod 700 get_helm.sh
$ ./get_helm.sh
```

To check HELM was installed using:
```shell
helm version
```
2. Deploy application in Kubernetes with HELM 

To deploy the app with helm template, run the following command:
```shell
helm install <imageName> ./helmChart
```

Check if the app was correctly deployed using:
```shell
kubectl get pods
```
or
```shell
kubectl get all
```

To run bash shell in pod
```shell
kubectl exec -it <podName> /bin/sh
```
then run application:
```shell
/app # java -jar application-1.0.0-jar-with-dependencies.jar
```