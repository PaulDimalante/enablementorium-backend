# Reference MS Crud

Simple reference architecture supporting Spring Data REST for CRUD
microservices

## Profiles

- run as 'local' to have a non-secured endpoint
- run as 'default' to have an OAuth2 compliant secured resource

gitlab
=======
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


## DependencyCheck

- `./gradlew dependencyCheckAnalyze`
- `./gradlew htmlDependencyReport`
- 
trigger
=======
## Key Setup

`keytool -genkeypair -alias mykey -storepass [password] -keypass [password] -keyalg RSA -keystore keystore.jks`
`keytool -importkeystore -srckeystore keystore.jks -destkeystore keystore.jks -deststoretype pkcs12`


404 POD Not Found Notes - feel free to delete if these do not add value
=======
The reference-ms-crud project was a big help in getting our(404 POD Not Found) CRUD services built however we ran into some issues and we recommend the below changes to the reference-ms-crud project
or your own implementation in order to save some time. Feel free to use other routes to resolve these issues and feel free to delete these suggestions from the readMe file after they are resolved. We just
want to save other pods time. Thanks!

1. Replace all occurences with compile to implementation and testCompile to testImplementation in the build.gradle file.
    - compile has been deprecated
    - when adding to pipeline the compile was failing for some reason and the implementation resolved it

2. Comment out Eureka dependency so the service logs do not constantly fill up with errors (note this may also require commenting out other pieces of code for compiling project)
    implementation('org.springframework.cloud:spring-cloud-starter-netflix-eureka-client') {
        exclude group: 'org.springframework.security', module: 'spring-security-rsa'
    }

3. Comment out these lines in build.gradle in order to allow swagger to work properly, otherwise the html page will not show. (note this may also require commenting out other pieces of code for compiling project)
    implementation('org.springframework.cloud:spring-cloud-stream')
    implementation('org.springframework.cloud:spring-cloud-stream-binder-rabbit')

4. Blue-green deploy does not work with the gradle reference-ms-crud project. Refer to the build.gradle in the person-crud-ms project to assist with this.