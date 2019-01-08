[![pipeline status](https://git.unreleased.work/labs-dallas/sample-apps/reference-ms-crud/badges/master/pipeline.svg)](https://git.unreleased.work/labs-dallas/sample-apps/reference-ms-crud/commits/master)

# Reference MS Crud

Simple reference architecture supporting Spring Data REST for CRUD
microservices

## Profiles

- run as 'local' to have a non-secured endpoint
- run as 'default' to have an OAuth2 compliant secured resource

trigger

## Set up your project on Jenkins

- Go to Jenkins.unreleased.work
- Create New Item
    - Name same as your project
    - Under 'If you want to create a new item from other existing, you can use this option:' type in 'reference-ms-crud-merge'
    - Replace all the 'https://git.unreleased.work/labs-dallas/sample-apps/reference-ms-crud.git' URLs with your Gitlab URL, (should end in .git)
    - Save and apply changes

## Set up Jenkins Webhooks In Gitlab
 
 - From your Gitlab Repo, go to 'Settings -> Integrations'
    - Create a new 'Merge Request Event' by selecting from the checkbox (uncheck push event)
    - For the URL, get from your Jenkins project under Configure
        - Look for where it says 'Build when a change is pushed to GitLab. GitLab webhook URL:' `<yoururl@example.com>` This is your URL
    - For the Secret Token, click the advanced tab under Build Triggers, and scroll down to secret token. This is your Token