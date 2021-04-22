# Overview

This project was developed against JDK 11

Project Stack
- Kotlin 1.4.30
- Micronaut 2.4.2

The data from the csv was parsed into a custom graph data structure. Dijkstra's algorithm was used to find the shortest path in anticipation of potential improvements (line specific, peak hour, and interchange travel times).

At the moment all edges have the same weight of 10, so the algorithm will optimize for the least number of stations in between the start and end stations.

Only 1 solution is presented currently, the one that dijkstra's algorithm finds. 

To keep things simple, the csv file was parsed as a data source. Native kotlin functions were used instead of a CSV library as the CSV was generally well formatted.

# Assumptions

There was a parsing error for the Canberra station which had an opening date of "December 2019". This error could not be fixed programmatically without causing inaccuracies. The date was manually patched the date in the CSV to "2 November 2019", using data from [wikipedia](https://en.wikipedia.org/wiki/Canberra_MRT_station).

---

# API

host: http://localhost:8080

#### GET /route
query parameters
- start: quoted station name (required)
- end: quoted station name (required)

examples:
- `http://localhost:8080/route?start="changi airport"&end="ang mo kio"`
- `http://localhost:8080/route?start=%22changi%20airport%22&end=%22ang%20mo%20kio%22`

responses:
- 200: json string array containing instructions
- 400: invalid station names
- 500: internal server error

---

# IntelliJ Setup

1. Turn on annotation processing
    - In IntelliJ `Preferences`, Search for "annotations"
    - Nagivate to `Build, Execution, Deployment` -> `Compiler` -> `Annotation Processors`
    - Check `Enable annotation processing`
2. Update Configurations (Not required with newer IntelliJ versions)
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


---

## Micronaut 2.4.2 Documentation

- [User Guide](https://docs.micronaut.io/2.4.2/guide/index.html)
- [API Reference](https://docs.micronaut.io/2.4.2/api/index.html)
- [Configuration Reference](https://docs.micronaut.io/2.4.2/guide/configurationreference.html)
- [Micronaut Guides](https://guides.micronaut.io/index.html)



## Feature http-client documentation

- [Micronaut HTTP Client documentation](https://docs.micronaut.io/latest/guide/index.html#httpClient)

