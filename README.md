# Library Management System (Java)

A console-based university group project demonstrating Java object-oriented programming. I led the implementation, with support from my teammates.

## Features
- Add, search, update, sort, and delete books
- Register members and track borrowing/returns
- Enforce a 10-book limit per member and unique IDs
- Save and reload library data with consistency checks
- Track real borrowing and return dates

## Requirements
Java 11 or later (Java 17 recommended).

## Run
```bash
javac *.java
java Main
```

Demo login (for local educational use only): `admin` / `1234`.
**This is a classroom demonstration, not a production authentication system.**

Use menu options to add books and members, borrow/return books, then save all data. The program writes `books.txt`, `members.txt`, and `borrow_records.txt` in the working directory. These are local runtime files and are excluded from Git.

## Notes
- Supports up to 100 books, 100 members, 100 loan records, and 10 simultaneous loans per member.
- Loan history consumes the 100-record limit even after returns.
- Data is saved as UTF-8 tab-separated text; the loader also accepts simple legacy comma-separated files.
- Do not manually modify saved files while the program is running.
- Authentication uses hardcoded demo credentials; no encryption or multi-user roles are implemented.

## Project contribution
Developed as a university group project. I was primarily responsible for the Java implementation, with help and feedback from my teammates.
