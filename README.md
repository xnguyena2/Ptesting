# gradle-getting-started

A barebones Gradle app, which can easily be deployed to Heroku.

This application support the [Getting Started with Gradle on Heroku](https://devcenter.heroku.com/articles/getting-started-with-gradle-on-heroku) article - check it out.

## Running Locally

Make sure you have Java installed.  Also, install the [Heroku Toolbelt](https://toolbelt.heroku.com/).

```sh
$ git clone https://github.com/heroku/gradle-getting-started.git
$ cd gradle-getting-started
$ ./gradlew stage
$ gradle build -x test;heroku local web
```

Your app should now be running on [localhost:5000](http://localhost:5000/).

If you're going to use a database, ensure you have a local `.env` file that reads something like this:

```
DATABASE_URL=postgres://localhost:5432/gradle_database_name
```

## Deploying to Heroku

```sh
$ heroku create
$ git push heroku master
$ heroku open
```

## Documentation

Show log of server:
```shell
heroku logs --tail --remote dev
```

full test:
```shell
gradle clean test
```

Ttest Authentication:
```shell
gradle clean test --tests com.example.heroku.AuthenticationTest
```
