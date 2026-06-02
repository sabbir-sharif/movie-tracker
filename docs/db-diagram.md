[//]: # (# Database Diagram and ERD)

[//]: # ()
[//]: # (## Database Diagram &#40;Logical&#41;)

[//]: # ()
[//]: # (```mermaid)

[//]: # (flowchart LR)

[//]: # (    UserTable[&#40;user&#41;])

[//]: # (    MovieTable[&#40;movie&#41;])

[//]: # (    VerificationTokenTable[&#40;verification_token&#41;])

[//]: # (    ResetPasswordTable[&#40;reset_password&#41;])

[//]: # ()
[//]: # (    UserTable -- "1:N" --> MovieTable)

[//]: # (    UserTable -- "1:1" --> VerificationTokenTable)

[//]: # (    UserTable -- "1:1" --> ResetPasswordTable)

[//]: # (```)

[//]: # ()
[//]: # (## ERD &#40;Entities and Relationships&#41;)

[//]: # ()
[//]: # (```mermaid)

[//]: # (erDiagram)

[//]: # (    USER {)

[//]: # (        int id PK)

[//]: # (        string name)

[//]: # (        string email)

[//]: # (        string password)

[//]: # (        boolean enabled)

[//]: # (    })

[//]: # ()
[//]: # (    MOVIE {)

[//]: # (        int id PK)

[//]: # (        string title)

[//]: # (        string genre)

[//]: # (        string status)

[//]: # (        int user_id FK)

[//]: # (    })

[//]: # ()
[//]: # (    VERIFICATION_TOKEN {)

[//]: # (        int id PK)

[//]: # (        string token)

[//]: # (        int user_id FK)

[//]: # (        datetime expiry_date)

[//]: # (    })

[//]: # ()
[//]: # (    RESET_PASSWORD {)

[//]: # (        int id PK)

[//]: # (        string token)

[//]: # (        int user_id FK)

[//]: # (        datetime expiry_time)

[//]: # (    })

[//]: # ()
[//]: # (    USER ||--o{ MOVIE : has)

[//]: # (    USER ||--|| VERIFICATION_TOKEN : verifies)

[//]: # (    USER ||--|| RESET_PASSWORD : resets)

[//]: # (```)

[//]: # ()
[//]: # (## Notes)

[//]: # ()
[//]: # (- Table names are inferred from entity class names and JPA defaults.)

[//]: # (- The project currently uses `VerificationToken` for both email verification and password reset flows in controllers, even though `ResetPassword` is defined as an entity.)
