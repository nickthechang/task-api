# Task API

Simple REST API built with Spring Boot.

## Features
- Create tasks
- Get all tasks
- In-memory storage (no database yet)

## Tech Stack
- Java
- Spring Boot
- Maven

## Endpoints

GET /tasks  
POST /tasks  

## Example Request

POST /tasks
{
  "title": "Learn Spring",
  "completed": false
}