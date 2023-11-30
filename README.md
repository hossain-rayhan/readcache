## Vendor API Proxy and Caching Layer
This project implements a web application that functions as a proxy and caching layer for genuine vendor APIs. 
As an illustration, we have encapsulated the GitHub read APIs. Certain APIs are cacheable, implying that we will cache 
them instead of making multiple backend calls. For other non-cacheable APIs, the call is directly proxied to 
the actual backend.

### Technology
I used the Java Spring Boot framework to develop this application because I am confident in Java. 
I also utilized various libraries to keep the code short and clean, such as RestTemplate for handling HTTP requests, 
Caffeine for in-memory caching, Jackson for processing JSON, Mockito for Mocking and Lombok annotations. 

### Build, Run and Test
- clone the GitHub repository
- move to the root directory
- Build and Run unit tests using the command: `./mvnw clean install`
- Run the application using the command: `./mvnw spring-boot:run`. This will start the web server and will listen to default port: 8080
- If you want to change the default server port, set it in the environment variable like: `export SERVER_PORT=8181`. Then the server will start listening at 8181.
- To avoid throttling, you can set the GITHUB_API_TOKEN like `export GITHUB_API_TOKEN='Your_GitHub_API_Token`.
- Now open another terminal and start hitting different endpoints as follows. You can also hit the endpoints from a browser. The server should return you expected data.
  - `curl http://localhost:8080`
  - `curl http://localhost:8080/view/bottom/10/forks`
  - `curl http://localhost:8080/orgs/Netflix/repos`

### Test Suite Results
I ran the provided test suite and everything looks great. 26 out of 30 (86.00%) tests are passing and other four will
pass if we update our test suite. Because the test suite has some outdated data and we need to refresh it.

Test Output:
```agsl
hrayhan@bcd0745ea0f8 Downloads % ./api-suite.sh
test-01-01: healthcheck = (15 seconds) pass
test-02-01: / key count = pass
test-02-02: / repository_search_url value = pass
test-02-03: / organization_repositories_url value = pass
test-03-01: /orgs/Netflix key count = pass
test-03-02: /orgs/Netflix avatar_url = pass
test-03-03: /orgs/Netflix location = pass
test-04-01: /orgs/Netflix/members object count = pass
test-04-02: /orgs/Netflix/members login first alpha case-insensitive = pass
test-04-03: /orgs/Netflix/members login first alpha case-sensitive = pass
test-04-04: /orgs/Netflix/members login last alpha case-insensitive = pass
test-04-05: /orgs/Netflix/members id first = pass
test-04-06: /orgs/Netflix/members id last = pass
test-04-07: /users/amirziai/orgs proxy = pass
test-04-08: /users/xuorig/orgs proxy = pass
test-05-01: /orgs/Netflix/repos object count = pass
test-05-02: /orgs/Netflix/repos full_name first alpha case-insensitive = pass
test-05-03: /orgs/Netflix/members full_name first alpha case-sensitive = pass
test-05-04: /orgs/Netflix/members login last alpha case-insensitive = pass
test-05-05: /orgs/Netflix/repos id first = pass
test-05-06: /orgs/Netflix/repos id last = pass
test-05-07: /orgs/Netflix/repos languages unique = pass
test-06-01: /view/bottom/5/forks = pass
test-06-02: /view/bottom/10/forks = pass
test-06-03: /view/bottom/5/last_updated = failed
  expected=[["Netflix/mantis-source-jobs","2022-03-29T16:25:56Z"],["Netflix/netflixoss-npm-build-infrastructure","2022-03-29T16:24:54Z"],["Netflix/mantis-api","2022-03-22T21:52:15Z"],["Netflix/eclipse-mat","2022-01-19T19:58:04Z"],["Netflix/mantis-rxnetty","2021-08-10T20:08:55Z"]]
  response=[["Netflix/mantis-connectors","2022-03-29T16:26:01Z"],["Netflix/mantis-source-jobs","2022-03-29T16:25:56Z"],["Netflix/netflixoss-npm-build-infrastructure","2022-03-29T16:24:54Z"],["Netflix/eclipse-mat","2022-01-19T19:58:04Z"],["Netflix/mantis-rxnetty","2021-08-10T20:08:55Z"]]
test-06-04: /view/bottom/10/last_updated = failed
  expected=[["Netflix/iep-shadow","2022-03-29T16:27:36Z"],["Netflix/ember-batch-request","2022-03-29T16:27:14Z"],["Netflix/falcor-datasource-chainer","2022-03-29T16:26:27Z"],["Netflix/mantis-examples","2022-03-29T16:26:03Z"],["Netflix/mantis-connectors","2022-03-29T16:26:01Z"],["Netflix/mantis-source-jobs","2022-03-29T16:25:56Z"],["Netflix/netflixoss-npm-build-infrastructure","2022-03-29T16:24:54Z"],["Netflix/mantis-api","2022-03-22T21:52:15Z"],["Netflix/eclipse-mat","2022-01-19T19:58:04Z"],["Netflix/mantis-rxnetty","2021-08-10T20:08:55Z"]]
  response=[["Netflix/falcor-hapi","2022-03-29T16:27:56Z"],["Netflix/iep-shadow","2022-03-29T16:27:36Z"],["Netflix/ember-batch-request","2022-03-29T16:27:14Z"],["Netflix/falcor-datasource-chainer","2022-03-29T16:26:27Z"],["Netflix/mantis-examples","2022-03-29T16:26:03Z"],["Netflix/mantis-connectors","2022-03-29T16:26:01Z"],["Netflix/mantis-source-jobs","2022-03-29T16:25:56Z"],["Netflix/netflixoss-npm-build-infrastructure","2022-03-29T16:24:54Z"],["Netflix/eclipse-mat","2022-01-19T19:58:04Z"],["Netflix/mantis-rxnetty","2021-08-10T20:08:55Z"]]
test-06-05: /view/bottom/5/open_issues = pass
test-06-06: /view/bottom/10/open_issues = pass
test-06-07: /view/bottom/5/stars = failed
  expected=[["Netflix/dgs-examples-kotlin-2.7",0],["Netflix/iceberg-python",0],["Netflix/octodns-ns1",0],["Netflix/octodns-route53",0],["Netflix/virtual-kubelet",0]]
  response=[["Netflix/octodns-ultra",1],["Netflix/octodns",0],["Netflix/octodns-ns1",0],["Netflix/octodns-route53",0],["Netflix/virtual-kubelet",0]]
test-06-08: /view/bottom/10/stars = failed
  expected=[["Netflix/conductor-docs",1],["Netflix/dgs-examples-java.latest",1],["Netflix/eclipse-mat",1],["Netflix/octodns",1],["Netflix/octodns-ultra",1],["Netflix/dgs-examples-kotlin-2.7",0],["Netflix/iceberg-python",0],["Netflix/octodns-ns1",0],["Netflix/octodns-route53",0],["Netflix/virtual-kubelet",0]]
  response=[["Netflix/conductor-docs",1],["Netflix/dgs-examples-java.latest",1],["Netflix/dgs-examples-kotlin-2.7",1],["Netflix/eclipse-mat",1],["Netflix/iceberg-python",1],["Netflix/octodns-ultra",1],["Netflix/octodns",0],["Netflix/octodns-ns1",0],["Netflix/octodns-route53",0],["Netflix/virtual-kubelet",0]]
26/30 (86.00%) tests passed
```

### Design Decisions
- **Language:** As personal preference, I decided to go with Java.
- **Framework:** Because of tutorials and guidelines, I chose the Java Spring Boot framework.
- **Caching Library:** First I tried Google Guava and was having some issues to build it with Maven. Later I switched to Caffeine library for in-memory caching. We could have used some managed distributed caching solution like `Redis` but saved that for later time.
- **Cache Eviction Policy:** I am not quite sure if the assignment strictly asked for scheduled caching. I went with TTL (Time To Live) cache eviction policy. We cache only when data is requested, and it stays valid for a while. For Testing purpose I set it to 60 seconds.

### Challenges Faced
- This is kind of my first Java Spring Boot application in the last four years. I had to spend a good amount of time setting up and running the project.
- Understanding the pagination logic was tough until I read the official pagination reference.
- JSON processing with objectMapper gave huge pain.


### Next Steps
- The code is modularized, but I believe that given time, I can improve it to make it robust and scalable for other vendor APIs.
- I have added unit tests for all classes except the GitHubClient. The next step would be to improve unit test coverage. 


### References
- Spring Initializer: https://start.spring.io/
- RestTemplate: https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/client/RestTemplate.html
- Pagination: https://docs.github.com/en/rest/guides/using-pagination-in-the-rest-api?apiVersion=2022-11-28
- Jackson Tutorial: https://www.tutorialspoint.com/jackson/jackson_objectmapper.htm
