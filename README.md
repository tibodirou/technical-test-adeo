# Java - Spring

## Technical Test - Offer `TM13506ADON` - Candidate `Thibaud Dirou` - Candidate ID `D3121SOF` - Date `04/10/2024`

### Candidate's notes

- The `@Transactional` annotation was parameterized with `readOnly = true`, which is why the reviews were not saved, updated, or deleted. I removed this parameter.
- The `@Transactional` annotation was on the repository layer; it should be on the service layer for the separation of concerns. I moved it to the service layer.
- I deleted the `deleteById` method in the repository layer because it was overriding the default method of the `CrudRepository` interface for no reason.
- I added DTO classes for encapsulation and to avoid the use of entities in the controller layer (only for the filter method I implemented).
- In the examples of the filter method, some information about the events was not displayed, so I removed them from their respective DTOs.
- I added a `ControllerAdvice` class to display the exceptions in a more user-friendly way. I also added a custom exception class to handle the case when the event is not found in the db.
- I added some caching to the service layer to improve performance.
- I created a unit test for the controller layer (annotated with `@WebMvcTest`) where nominal cases as well as error cases are tested.


- Question 2 (BONUS) example is wrong, the number of child items of "GrasPop Metal Meeting" should be 1, not 2.

---

## My Event

- My Event is an application to manage musical events.

## Technical stack

- This is a maven project.
- It uses HSQLDB as an in-memory database.
- It starts using this maven lifecycle `mvn spring-boot:run` or using the IDE
- The user interface is available at [http://localhost:8080]
- The API resources are available at [http://localhost:8080/api/]

## Context

- The user interface is tested and holds no identified issues.
- We Identified a few things not working on the API.
- Your job is to fix the issues and add a new feature to the API.

## Identified Issues:

```
Please keep track (notes) of how you analysed and fixed the issues to help us
understand the steps during the interview
```

1. Adding review does not work
2. Using the delete button works but elements comes back when i refresh the page

## New Feature

```
Except for the testing libraries, No library/modules should be added to the dependencies
(use only pure java)
```

1. We would like to enable a new route for the API `/search/{query}`. It will allow us
   to display filtered `events`.
   The events are displayed only if at least one band has a member with the name matching the given
   pattern.

Example: `/search/Wa`

```json
[{
    "title": "GrasPop Metal Meeting",
    "imgUrl": "img/1000.jpeg",
    "bands": [{
        "name": "Metallica",
        "members": [
            {
              "name": "Queen Anika Walsh"
            }
        ]
    },…
}…]
```

2. (BONUS) Add a `[count]` at each event and band
   to display the number of child items.

Example: `/search/Wa`

```json
[{
    "title": "GrasPop Metal Meeting [2]",
    "imgUrl": "img/1000.jpeg",
    "bands": [{
        "name": "Metallica [1]",
        "members": [
            {
              "name": "Queen Anika Walsh"
            }
        ]
    },…
}…]
```

## Requirements

The code must be available in a GIT repository

## Team Appreciation

Team overall appreciation will be based on:

- Code readability, structure and consistency
- Tests, how they are written
- Bonus: usage of Functional concepts