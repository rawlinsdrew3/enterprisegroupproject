# Spring Boot Movie Recommendation System 

## Introduction

Our Movie Recommendation System allows users to be recommended movies based on their preferences and what movies they interact with. 
It will incorporate a user service that has the user's profiles, a movie service that handles all the movie data, and a recommendation 
service that uses the user's preferences and selections of movies to be suggested movies. 

## Storyboard

<img width="1031" alt="Screenshot 2024-09-25 at 9 48 33 PM" src="https://github.com/user-attachments/assets/ca52fc04-a4b4-4189-ad7a-a42a6834d1a0">


## Requirements

1. As a User I want to log into my account, so that I can view my personalized dashboard

**Given**: I have already registered an account 

**When**: I enter my email and password on the login page, then click Login  

**Then**: I am redirected to my dashboard, showing my recommendations

---

2. As a User I want to browse a list of available movies, so that I can discover new films to watch

**Given**: I am logged into my account

**When**: I navigate to the "Browse Movies" section

**Then**: I see a paginated list of movies with titles, genres, and ratings

3. As a User I want to rate a movie, so that I can contribute to its popularity and improve recommendations

**Given**: I am viewing a specific movie's details

**When**: I select a rating from 1 to 5 stars and click "Submit" 

**Then**: My rating is saved, and the movie's average rating is updated

---

4. As a User I want to receive movie recommendations based on my preferences, so that I can watch films that align with my tastes

**Given**: I have rated several movies and followed other users

**When**: I visit my recommendation page

**Then**: I see a list of top 5 recommended movies tailored to my preferences

## Class Diagram

![Blank diagram (6)](https://github.com/user-attachments/assets/e1a60bc3-1a52-42a8-ab01-c11e91b8f7bd)


### Class Diagram Description

This diagram represents a **Movie Recommendation System** with three layers: UI, Service, and Database. The UI displays movie details like rating, genre, and title, managed by the `MovieRecommendation` class. The Service layer fetches movie data via `MovieDAO`, while the Database layer stores movie attributes like `MovieRating` and `MovieGenre`.

## Json Schema 

This is what we plan to export to another service.

> {
>  "type": "object",
>  "properties": {
>    "userId": {
>      "type": "string"
>    },
>    "name": {
>      "type": "string"
>    },
>    "email": {
>      "type": "string",
>      "format": "email"
>    }

## Scrum Roles 

- Drew Rawlins - Product Owner
- Ravi Nayak - ?
- Marko Nisiama - ?
- Ethan Beach - Developer
- Earl Schreck - Developer

## Milestones

[Milestone 1](https://github.com/rawlinsdrew3/enterprisegroupproject/milestones?with_issues=no)

## Standup

[We meet on 6:00pm on Wednesday on Teams](https://teams.microsoft.com/l/meetup-join/19%3ameeting_ZjNhOTEwMzctMjExNS00MGQ3LThkYmYtNTQ2MWVjN2RmYWVk%40thread.v2/0?context=%7b%22Tid%22%3a%22f5222e6c-5fc6-48eb-8f03-73db18203b63%22%2c%22Oid%22%3a%2231499c1f-a1b9-437c-a06b-b213ee708c1c%22%7d)









