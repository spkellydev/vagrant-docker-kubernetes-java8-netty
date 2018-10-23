#!/bin/sh
set -x

#create new ssh key
[[ ! -f /home/ubuntu/.ssh/mykey ]] \
&& mkdir -p /home/ubuntu/.ssh \
&& ssh-keygen -f /home/ubuntu/.ssh/mykey -N '' \
&& chown -R ubuntu:ubuntu /home/ubuntu/.ssh

#PACKAGES

#install docker
apt-get update
apt-get -y install docker.io

# add privileges
usermod -G docker vagrant

#install kubernetes
apt-get update
apt-get install -y apt-transport-https
curl -s https://packages.cloud.google.com/apt/doc/apt-key.gpg | sudo apt-key add -
echo "deb http://apt.kubernetes.io/ kubernetes-xenial main" | sudo tee -a /etc/apt/sources.list.d/kubernetes.list
apt-get update -y
apt-get install -y kubectl kubeadm kubelet
apt-get update -y

# kubernetes privileges? Should be sudo?
#usermod -G kubectl ubuntu
#usermod -G kubeadm ubuntu
#usermod -G kubelet ubuntu
