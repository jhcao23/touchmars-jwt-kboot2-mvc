- Exception `Caused by: com.mysql.cj.exceptions.CJException: Public Key Retrieval is not allowed` is temporarily removed by rewriting jdbc connection url by appending `&verifyServerCertificate=false`.
  [This post](https://community.atlassian.com/t5/Confluence-questions/MySQL-Public-Key-Retrieval-is-not-allowed/qaq-p/778956) is helpful.
- rank is a sensitive keyword in new MySQL database.
- Spring Data JPA `getOne` returns a reference so might cause Hibernate LazyInitializationException.
- Hibernate only supports up to 1 eager loading List, 
    but it supports additional multiple Set as eager loading.
- javadoc issue: `javadoc: error - class file for javax.interceptor.InterceptorBinding not found`. add `<dependency><groupId>javax.interceptor</groupId><artifactId>javax.interceptor-api</artifactId></dependency>`.
	See the [article on medium.com](https://medium.com/@ray.gomez/jdk8-error-class-file-for-javax-interceptor-interceptorbinding-not-found-4523376f6dcd). 