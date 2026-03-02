# Lab 4 - Library Alert System

## Overview
This project implements a `LibraryService` that checks out a library resource and notifies a member via email.
Unit tests use Mockito to mock dependencies so no real database updates or emails occur during testing.

## Specification (Behavior)
- If `resourceId` is null, return `false`.
- If the resource is not available, return `false`.
- If the resource is available:
    - Update its status (checkout)
    - Send an email notification
    - Return `true`
- If updating the status fails, throw `DatabaseFailureException`.
- If sending the email fails, throw `EmailFailureException`.

## Part 1: Test Design (Decision Table)
| Case | resourceId | Available? | updateStatus | sendEmail | Expected |
|------|------------|------------|-------------|----------|----------|
| 1 | null | - | - | - | false |
| 2 | valid | false | - | - | false |
| 3 | valid | true | false | - | DatabaseFailureException |
| 4 | valid | true | true | false | EmailFailureException |
| 5 | valid | true | true | true | true |

## Part 2: Test Implementation
Tests are located in:
- `src/test/java/com/dakotah/library/LibraryServiceTest.java`

Mocks used:
- `ResourceRepository`
- `EmailProvider`

Key verifications:
- No dependency calls when `resourceId` is null
- No update/email when resource unavailable
- No email if update fails
- Exceptions thrown for update/email failures
- Successful checkout returns true