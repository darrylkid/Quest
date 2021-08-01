Original App Design Project - README Template
===

# Quest

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview

### Description
Welcome to Quest, a question & answer note-taking app heavily inspired by the Cornell Note System.

### App Evaluation
- **Category:** 
    - Education
- **Mobile:** 
    - Quest gives you the ability to take notes and read notes from anywhere. Which means the only thing you need in lecture is a phone. 
- **Story:**
    - From high school, you're furiously write down what is on the lecture slides before the teacher changes the slides whilst the teacher audibly enters side points and details. Not only is this stressful but it is impractical especially in college where most professors will move on without waiting for you.
    - After finishing taking notes lecture or reading the textbook, you are left with a wall of text: body text, some bullet points sprinked in, and a few title/highlighted/bold text. This is a terrible way to review your notes because your goalless. You read sequentially line by line, maybe skipping around, hoping that it will stick. Ok, you're done, but there is no feedback to ***prove*** you understand the material.
    - Quest provides the structure to take notes by requiring you to formulate questions and answer them, just like the Cornell Note System. Why? Because we access thought in terms of questions. It's always "What will I eat for lunch?" before "I'll have some salad for lunch." Or "What is the value of pi?" before "pi is 3.14" by yourself or someone else. Rather than aimlessly reading through notes, you're on a quest to focus on each question and test your ability to retrieve the answer.
- **Market:**
    - Quest is for anyone who wants to take notes for learning or research. It can be used in personal life to remember things, such as remembering how to tie a tie. Learning is applicable to everyone and so is the app.
- **Habit:**
    - The recall aspects of the question & answer system helps keep users come back to the app. 
    - For further motivation, the home page will show the total questiions the user has asked.
- **Scope:**
    - The main challenge will be the way I execute the user stories in a way that is natural to the user. For example, I want the user to delete a subject or even multiple subjects. What's the best way to go about this that will give the user the least fuss? 
    - I believe it will be possible to complete in 4 weeks. 

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* **Account**
  * User can sign up
  * User can login
* **Notes Organization**
  * User can create a new subject/section/page
  * User can delete a new subject/section/page
  * User can edit a new subject/section/page
* **Core Notes**
  * User can create new questions & answers
  * User can edit the question & answers by double tapping
  * User can delete the question & answer by uncollapsing to reveal a delete icon
* **Search**
  * User can search their questions and go straight to the question by clicking on it
* **Other**
  * Database persistence
  * User can export a PDF (use of API)

**Optional Nice-to-have Stories**
* **Data Visualization**
    * User can see subjects, sections, pages, questions all hierarchically linked in a mind map
    * User can see a graph of the number of questions answered over a period of time
    * User can scroll through their most recent questions
* **Multimedia**
    *  User can attach audio to an answer
    *  User can attach images to an answer
    *  User can enter code in a wide selection of languages
* **Style creation**
    * User can create different styling; for example, user creates a red & bold style for vocabulary words;
* **Search Filter**
    * Users can filter their search by subject

### 2. Screen Archetypes

* Login / Regiter
* Stream
* Creation
* Other
    

### 3. Navigation 

**Flow Navigation** (Screen to Screen)
* Login
  -> Sign Up
  -> Main Menu
* Sign Up
  -> Login
  -> Main Menu
* Home
  -> Search
  -> Subjects
* Search
  -> Home
  -> Subjects
  -> Notes
* Subjects
  -> Home
  -> Search
  -> Sections
* Sections
  -> Home
  -> Search
  -> Pages
* Pages
  -> Home
  -> Search
  -> Notes
* Notes
  

## Wireframes

<img src="https://github.com/darrylkid/Quest/blob/master/Quest_Wireframe.png"/>

## Schema 
### Models
* **Subject**
    | Type | Name | Description |
    | ---- | ---- | ----------- |
    | String | description | the text contents of the subject |

* **Section**
    | Type | Name | Description |
    | ---- | ---- | ----------- |
    | String | description | the text contents of the section | 
    | Subject | parent | the subject this section falls under |
* **Page**
    | Type | Name | Description |
    | ---- | ---- | ----------- |
    | String | description | the text contents of the page |
    | Section | parent | the section this page falls under | 
* **Question**
    | Type | Name | Description |
    | ---- | ---- | ----------- |
    | String | description | the text contents of the question |
    | Answer | answer | the answer object that encapsulates its description, image, etc |
    | Page | parent | the page the question falls under |
* **Answer**
    | Type | Name | Description |
    | ---- | ---- | ----------- |
    | String | description | the text contents of the answer |
    | Question | parent | the question this answer falls under | 
### Networking
- **Subject/Section/Page Screen**
     - (Create/POST) Create a new subject/section/page under the current user
     - (Read/GET) Query all subjects/sections/pages where the user is the author
     - (Update/PUT) Update the name of the current subject/section/page
     - (Delete) Delete the inner child categories
- **Core Notes Screen**
    - (Create/POST) Create a new question with or without an answer under the current user and parent page
    - (Create/POST) Create a new answer
    - (Read/GET) Query all questions under the current user and parent page
    - (Update/PUT) Update a question and aswer under the current user and parent page
    - (Delete) Delete a question and answer under a page
- **Search Screen**
    - (Read/GET) Query all questions under the current user

