# playful-edu

Config these env variables:
- **AAC_CLIENT_ID** : client id
- **AAC_ISSUER_URI** : e.g. https://aac.platform.smartcommunitylab.it
- **LOG_LEVEL** : default log level, e.g. info
- **LOGS** : where store the logs file, e.g. /home/dev/playfuledu/logs
- **CONTEXT_PATH** : application context path
- **SPRING_DATA_MONGODB_URL** : e.g. mongodb://localhost:27017/admin
- **SPRING_DATA_MONGODB_DB** : the data db, e.g. playful-edu
- **X_AUTH_TOKEN** : a security API key token; set it as `x-auth` header key

To add an `admin` role, in MongoDb create a collection _**roles**_ and add a document 
```sh
{
    "preferredUsername" : "test@test.com",
    "role" : "admin"
}
```

To add a `domain` role, e.g. for the _**test**_ domain, in MongoDb create a collection _**roles**_ and add a document 
```sh
{
    "preferredUsername" : "test@test.com",
    "role" : "domain",
    "entityId" : "test"
}
```

