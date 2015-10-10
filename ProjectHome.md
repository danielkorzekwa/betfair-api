JAVA API for Betfair Betting Exchange.

[Blog on Betfair API](http://blog.danmachine.com/2009/02/betfair-java-api-with-apache-cxf-spring.html)

How to use:
http://code.google.com/p/betfair-api/source/browse/trunk/src/test/java/dk/bot/betfairservice/BetFairServiceImplIntegrationTest.java

Before running this integration test, the following environment properties have to be set:
bfUser, bfPassword and bfProductId - they define betfair account that integration test will use.

Maven 2 repository:
```
...
<dependency>
   <groupId>dk.flexibet</groupId>
   <artifactId>flexibet-betfair</artifactId>
   <version>0.7</version>
</dependency>
...

...
  <repository>
   <id>dk-maven2-repo-releases</id>
   <name>dk-maven2 maven repository (releases)</name>
   <url>http://dk-maven2-repo.googlecode.com/svn/maven-repo/releases</url>
  </repository>

...
```