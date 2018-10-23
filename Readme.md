# Vagrant Box for Docker and Kubernetes

The requirements to run this box are to have the `Virtual Box Version 5.2.8` or greater and the latest version of `Vagrant` available.

- [Virtual Box download](https://www.virtualbox.org/wiki/Downloads)
- [Vagrant download](https://www.vagrantup.com/downloads.html)

You'll want to restart your computer after the installation processes have finished.

### Vagrant up

<<<<<<< HEAD
Running on `Ubuntu/Xenial64` (Version 16.04) this box comes preloaded with port forwarding, Docker, Kubernetes, and SSH. Check `scripts/vagrant.sh` for more details about the installation process. 
=======
Running on `Ubuntu/Xenial64` (Version 16.04) his box comes preloaded with port forwarding, Docker, Kubernetes, and SSH. Check `scripts/vagrant.sh` for more details about the installation process. 
>>>>>>> 35dbf700b6c98d97ca5cb053d326ef4fe9a66115

For a quick starter demo, you can clone this repository. The fat JAR has already been packaged, but you can recreate this for yourself by running `mvn clean && mvn install`. The Docker container is expecting the JAR with dependencies to live in the `/out` directory. Look in the Dockerfile to configure your own JAR.

The **Vagrantfile** houses all the configurations for the Vagrant box. This box makes use of port forwarding from the `host 8080` to `guest 8080`. Meaning your Vagrant box will communicate over `localhost:8080`.

---
- `vagrant up` -- this may take some time for your first boot. The box need to collect the ubuntu/xenial64 iso and provision.
- `vagrant ssh` -- once the vagrant box is booted, you can ssh into the box. All dependencies have been contained. Vagrant is synced to your local folder, so any changes you make locally will be reflected in the box (except for the Vagrantfile).
- `cd /vagrant` -- Our working directory.
- `docker build -f Dockerfile -t netty .` - Docker will attempt to build the container. `-f Dockerfile` tells Docker engine to build from a local file. `-t netty` is going to tag our container with "netty". The `.` refers to the current working directory.
- `docker run --name=netty -d -p 8080:8080 -t netty` - Docker engine will run the container. `--name=netty` will name the container at runtime. `-d` will put Docker in a detached state, if you would like to see the output from the Java program, remove the `-d` flag -- this will help with debugging. `-p 8080:8080` will map the Docker process from it's own port 8080 to the Vagrant port 8080 (which has already been forwarded to the local 8080). Docker will print out a hash meaning your container launched without failure.
---
#### Postman

Make a POST request to `http://localhost:8080/login` with a JSON payload.
```js
{
  "email": "foo@bar.com",
  "password": "echoecho"
}
```

#### Curl alternative
```
curl -XPOST -H "Content-type: application/json" -d '{
"email": "foo@bar.com",
"password": "echoecho"
}' 'http://localhost:8080/login' -v
```

The server echos your input and produces an output. It is expecting the email, password schema.
```js
{
  "noodle": "penne", // will not work, triggers error response
  "cheese": "mozz"
}
```

#### Graceful shutdown

- `docker stop netty`
- `exit` or `logout`
- `vagrant halt`

---
#### Editing the Vagrant file

You may uncomment the networking config, so that your Vagrant box is accessible via `http://kubes.me:8080/login`.


```
kubes.vm.network "private_network", ip: "192.168.199.9"
kubes.vm.hostname = "kubes.me"
```

Run `vagrant up --provision` to tell vagrant that you have made changes to your already provisioned box. `vagrant reload --provision` works about 50% of the time.







