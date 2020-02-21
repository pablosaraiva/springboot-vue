---
description: New commits to master are deployed automatically to my staging environment  
---
# Continuous Deployment

Today I started the implementation of Continuous Deployment to my web application.

Now, all changes that happen at the master branch are automatically deployed to the staging 
environment.

Then I have to manually promote them to production after approved.

Later I will change this workflow a bit, so master will deploy to production and a different
branch will deploy to the staging environment.

## Staging Environment

The staging environment is a copy of my web application that runs at a different port.

[https://pablosaraiva.com:8080](https://pablosaraiva.com:8080)

Every change on master generates a new docker image that gets deployed to this environment 
automatically.

## How it works

First, I created a nginx-dev.conf and a docker-compose-dev.yml for the staging environment.

They are very similar to the production version, except that it runs the server in a different port.

Then I created a cronjob that from time to time verifies if there is a new image available.

When a new image is available, it gets pulled from the docker hub and replaces the one running
at the staging environment.

``` text
*/5 * * * * docker-compose -f docker-compose-dev.yml pull && docker-compose -f docker-compose-dev.yml up -d
```

That's it! I will find time to do the second part of this soon. 

:tada: Drink water! :tada:
