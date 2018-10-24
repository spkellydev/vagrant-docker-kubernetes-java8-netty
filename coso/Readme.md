# Vagrant Box for Docker and Kubernetes

The requirements to run this box are to have the `Virtual Box Version 5.2.8` or greater and the latest version of `Vagrant` available.

- [Virtual Box download](https://www.virtualbox.org/wiki/Downloads)
- [Vagrant download](https://www.vagrantup.com/downloads.html)

You'll want to restart your computer after the installation processes have finished.

### Vagrant up

Running on `Ubuntu/Xenial64` (Version 16.04) this box comes preloaded with port forwarding, Docker, Kubernetes, and SSH. Check `scripts/vagrant.sh` for more details about the installation process. 

For a quick starter demo, you can clone this repository. The fat JAR has already been packaged, but you can recreate this for yourself by running `mvn clean && mvn install`. The Docker container is expecting the JAR with dependencies to live in the `/out` directory. Look in the Dockerfile to configure your own JAR.

The **Vagrantfile** houses all the configurations for the Vagrant box. This box makes use of port forwarding from the `host 8080` to `guest 8080`. Meaning your Vagrant box will communicate over `localhost:8080`.

[Vagrantfile](https://gist.github.com/spkellydev/8ea49704039b562d5278af93847b5f5b), or from this directory -- but Vagrant must be ran from the project root to sync available folders.

### Process
---
- `vagrant up` -- this may take some time for your first boot. The box need to collect the ubuntu/xenial64 iso and provision.
- `vagrant ssh` -- once the vagrant box is booted, you can ssh into the box. All dependencies have been contained. Vagrant is synced to your local folder, so any changes you make locally will be reflected in the box (except for the Vagrantfile).
- `cd /vagrant` -- Our working directory.
- `cd coso20-server`
- link the common dependencies
- `mvn install:install-file -Dfile=../coso20-common/target/common-0.0.1-SNAPSHOT.jar -DgroupId=coso -DartifactId=common -Dversion=0.0.1-SNAPSHOT -Dpackaging=jar`
- `export DOCKER_HOST=unix:///var/run/docker.sock` - we need to open port 2375 for the **dockerfile-maven-plugin**
- `mvn clean package`
- `cd ../coso20-docker-images/nginx` - build the nginx image
- `cd ../coso20-docker-images/coso-insights-server/dev` 
- `docker-compose up -d`
---

### Results

`curl http://localhost:8080` -> 200 'content goes here'
`curl -v -X POST -H "Content-Type: application/json" -d '{"domain":"insights", "api":"test.helloworld"}' http://127.0.0.1:8080/api` -> server exception: host.docker.internal: Name does not resolve
```java
server_1     | host.docker.internal: Name does not resolve
server_1     | 19:14:29.231 [nioEventLoopGroup-41-1] WARN  com.coso.server.HttpMessageHandler - returning error 500:
server_1     | java.net.UnknownHostException: host.docker.internal: Name does not resolve
server_1     |  at java.net.Inet6AddressImpl.lookupAllHostAddr(Native Method) ~[?:1.8.0_171]
server_1     |  at java.net.InetAddress$2.lookupAllHostAddr(InetAddress.java:928) ~[?:1.8.0_171]
server_1     |  at java.net.InetAddress.getAddressesFromNameService(InetAddress.java:1323) ~[?:1.8.0_171]
server_1     |  at java.net.InetAddress.getAllByName0(InetAddress.java:1276) ~[?:1.8.0_171]
server_1     |  at java.net.InetAddress.getAllByName(InetAddress.java:1192) ~[?:1.8.0_171]
server_1     |  at java.net.InetAddress.getAllByName(InetAddress.java:1126) ~[?:1.8.0_171]
server_1     |  at java.net.InetAddress.getByName(InetAddress.java:1076) ~[?:1.8.0_171]
server_1     |  at io.netty.util.internal.SocketUtils$8.run(SocketUtils.java:146) ~[coso20server.jar:?]
server_1     |  at io.netty.util.internal.SocketUtils$8.run(SocketUtils.java:143) ~[coso20server.jar:?]
```
