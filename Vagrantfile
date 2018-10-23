Vagrant.configure(2) do |config|
	config.vm.define "kubes" do |kubes|
		kubes.vm.box = "ubuntu/xenial64"
    #kubes.vm.network "private_network", ip: "192.168.199.9"
    #kubes.vm.hostname = "kubes.me"
		kubes.vm.network "forwarded_port", guest: 8080, host: 8080
    kubes.vm.provision "shell", path: "scripts/vagrant.sh"
    kubes.vm.provider "virtualbox" do |v|
      v.memory = 4096
      v.cpus = 2
    end
	end
end
