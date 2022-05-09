# API

This file contains api usage information

## Rest-Endpoints

| relative url          | type   | input | output¹ | session locked² | description                                                   |
|-----------------------|--------|-------|---------|-----------------|---------------------------------------------------------------|
| **SessionController** |        |       |         |                 |                                                               |
| /sessions/create      | GET    |       | Session | false           | creates the session used to call image manipulation endpoints |
| /sessions/update      | PATCH  |       | Session | true            | extends the current session                                   |
| /sessions/close       | DELETE |       |         | true            | wipes the session with all its belongings from the repos      |

¹ output objects are always wrapped within a ResponseEntity<> object.  
² if true, the header must contain the session-id (see more in "Sessions")

## Sessions
TODO
