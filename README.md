# Git-Intel

Git-Intel is a modern GitHub Crawler based on Spring and using the GitHub GraphQL v4 API.

Using Git-Intel you can quickly gain access to all kinds of information from any public organisation registered on GitHub.
You might even find some dirt on private organisations aswell...

Try it Out!

# Git-Intel-Spring

This repository contains the code for the backend of the Git-Intel project which currently consists of two separate applications:
1) The Git-Intel Backend (This repository)
2) The Git-Intel UI (https://github.com/adessoAG/Git-Intel-UI)

# Using Git-Intel
The easiest way to try and play around with Git-Intel is by using the composed docker image.  
To do so you need to have [Docker](https://www.docker.com/get-started) and [Docker Compose](https://docs.docker.com/compose/install/) installed.

Once you have Docker and Docker Compose installed, make sure Docker is running and copy the [Docker Compose file](https://github.com/adessoAG/git-intel-docker) for Git-Intel to your machine.

Inside the directory the docker-compose.yaml file is in, use the Docker command ```docker-compose up``` to run Git-Intel in a Docker environment.

Access the UI via port 3000 on your ```localhost```

# Working on Git-Intel

To modify and test the application as it is now, we recommend the following approach:

1) Fork and Clone the Git-Intel-Spring repository to your local machine.
2) Fork and Clone the Git-Intel-UI repository to your local machine.
3) Install and run mongoDB.
4) Create a [GitHub API Token](https://blog.github.com/2013-05-16-personal-api-tokens/).
5) Replace the ```API_TOKEN``` in the *Config.java* file inside the **Git-Intel-Spring** project with the one you created.
6) Start Git-Intel-Spring using the gradle task ```bootRun```, start Git-Intel-UI using the angular command ```ng serve``` or ```npm start```.
7) The application should now be running on port 4200 on your ```localhost```.

# Troubleshooting

+ **The application makes calls to the GitHub API without any user interaction.**
  * If it's not the first time you run the application there may be some leftover queries stuck in the mongoDB.  
    Try removing all entries from the *query* collection inside your mongoDB. If this does not solve the problem, try clearing all three     collections ```query```, ```organizationWrapper``` and ```processingOrganization```.
    
+ **The ```docker-compose up``` command throws an error.**
  * Try restarting Docker and run the command againg.  
  if this does not solve the problem, try removing all the containers associated with the Git-Intel project, restart Docker and run the
  command again.
  
* **An authorization error occurs when using the project without Docker.**
  * There might be a problem with the Personal Access Token you are using.
  Check https://github.com/settings/tokens to see if the token you are using is still valid. 
  
* **An authorization error occurs when using the project WITH Docker.**
  * This most likely means we messed up and the token used in the Docker build has been invalidated...  
  Please let us know when this happens so we can fix our authorization mechanism.
