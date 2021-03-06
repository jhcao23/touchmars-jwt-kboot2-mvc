- 3.0.0-SNAPSHOT: Upgrade major versions.
- 2.3.1-SNAPSHOT: Temporarily fixed `TouchMarsAuditorAwareImpl` issue as it was returning `TouchUser.toString` by
    make `TouchUser implements AuthenticatedPrincipal`.
- 2.3.0-SNAPSHOT: Update a few versions. Change `getOne` to `findById`.
- 2.1.1-SNAPSHOT: Upgrade `kotlin` to `1.2.60`.
- 2.1.0-SNAPSHOT: Upgrade version: `Spring Cloud` and `spring-cloud-starter-openfeign`.
- 2.0.3-SNAPSHOT: Upgrade versions: boot and kotlin.
- 2.0.2-SNAPSHOT: Upgrade versions: boot, spring-io, spring-cloud, spring-data, swagger, command-text, common-lang
- 2.0.1-SNAPSHOT: Add `ContactRepository` and `UserContactRepository`.
- 2.0.0-SNAPSHOT [NEW]: Change project id to `touchmars-spring-template-kboot2-mvc`.
- 2.0.0-SNAPSHOT: Upgrade Spring Boot to `2.0.0.RELEASE`; Change from Java to Kotlin; remove social dependencies.
- 0.0.21-SNAPSHOT: Upgrade kotlin to 1.2.30.
- 0.0.20-SNAPSHOT: Upgrade kotlin to 1.2.21. Upgrade Spring Boot to 1.5.10.
- 0.0.19-SNAPSHOT: 
    -   Update `JwtTokenService` to utilize `FirebaseService`
    -   Add `FirebaseService`
    -   Update `JwtClaimService`
- 0.0.18-SNAPSHOT: Update all Date to ZonedDateTime.
- 0.0.17-SNAPSHOT: Update `kotlin` to `1.2.10`. 
    `Firebase` is still in a mess.
- 0.0.16-SNAPSHOT: Update `spring-boot` version to `1.5.9`.
- 0.0.15-SNAPSHOT: 
    - Add `Firebase` dependency. 
    - Add `F-AUTH-TOKEN` header to `JwtAuthenticationSuccessHandler`.
    - Update `<spring-cloud.version>Edgware.RELEASE</spring-cloud.version>`.    
- 0.0.14-SNAPSHOT: add `JwtClaimService` for `JwtTokenService`.
- 0.0.13-SNAPSHOT: update kotlin to `1.1.60`.
- 0.0.12-SNAPSHOT: Add `PostSignupService`. Polish `GoogleServerSideAppAuthenticationFilter` and `GoogleSigninServiceUtil`.
- 0.0.11-SNAPSHOT: add `kotlin` dependencies!
- 0.0.10-SNAPSHOT: add java 8 time support, update `JsonZonedDateTimeSerializer`.
- 0.0.9-SNAPSHOT: change date format to `yyyy-MM-dd'T'HH:mm:ss.SSSZ`.
- 0.0.8-SNAPSHOT: add DateModule and JsonDateSerializer. add JacksonConfig.
- 0.0.7-SNAPSHOT: add Jpa Auditing ability. Upgrade versions.
- 0.0.6-SNAPSHOT: update to Spring Boot 1.5.7.RELEASE. Add Google Server Side Auth filter.
- 0.0.2-SNAPSHOT: init version working and well organized pom.xml