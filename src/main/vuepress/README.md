# Welcome

<img alt="Foto do rosto de Pablo Saraiva" src="./images/pablo.jpg" width="30%" height="30%">

Hi, thanks for stopping by.

I use this to study some technologies I might be interested in or to test things in combination.

I might as well decide to do some blog posts here inspired by 
[Scott Hanselman](https://www.hanselman.com).

This is developed with [VuePress](https://vuepress.vuejs.org/).

It runs Tomcat as a web server using spring boot. (Which is not really used in the project so far).

Then everything goes into docker container and I deploy at the cloud.

Source code is [here](https://github.com/pablosaraiva/springboot-vue).

I use [Let's Encrypt](https://letsencrypt.org/)  for the SSL certificates and 
[nginx](https://www.nginx.com/) for reverse proxy, gzip, ssl, h2 and a bit more. 

Every new commit to master triggers a build via [CircleCi](https://circleci.com/) and deploys a new 
image to the [docker hub](https://hub.docker.com/).