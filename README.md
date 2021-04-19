# Project Requirements

This project was developed against JDK 11

Project Stack
- Kotlin 1.4.30
- Micronaut 2.4.2



# IntelliJ Setup

1. Turn on annotation processing
    - In IntelliJ `Preferences`, Search for "annotations"
    - Nagivate to `Build, Execution, Deployment` -> `Compiler` -> `Annotation Processors`
    - Check `Enable annotation processing`
2. Update Configurations (Not required in newer IntelliJ versions)
   - Run Configuration, add new task before launch
      - In the `Run Configuration` window, `Before Launch` -> `+`
      - Select `Run Grade Task`
      - Under `project`, select this project
      - Under `Tasks`, add `classes`
   - Test Config
      - In the `Run Configuration` window, `Before Launch` -> `+`
      - Select `Run Grade Task`
      - Under `project`, select this project
      - Under `Tasks`, add `testClasses`



## Micronaut 2.4.2 Documentation

- [User Guide](https://docs.micronaut.io/2.4.2/guide/index.html)
- [API Reference](https://docs.micronaut.io/2.4.2/api/index.html)
- [Configuration Reference](https://docs.micronaut.io/2.4.2/guide/configurationreference.html)
- [Micronaut Guides](https://guides.micronaut.io/index.html)
---



## Feature http-client documentation

- [Micronaut HTTP Client documentation](https://docs.micronaut.io/latest/guide/index.html#httpClient)
