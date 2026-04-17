# User Management API — Test Suite

End-to-end REST Assured tests for the User Management API (`/dev` and `/prod` environments).

## Running Tests

```bash
# Full pipeline (sequential then parallel)
./gradlew :api-testing:testPipeline -Denv=dev
./gradlew :api-testing:testPipeline -Denv=prod

# All tests (local/IDE)
./gradlew :api-testing:test -Denv=dev
./gradlew :api-testing:test -Denv=prod
```

`-Denv` is mandatory (`dev` or `prod`).

---

## Latest Report

[Allure Report DEV](https://robertopegoraro.github.io/user-management-api-testing/dev/)

[Allure Report PROD](https://robertopegoraro.github.io/user-management-api-testing/prod/)

## Bug Report

Results from test run against `http://localhost:3000` (Docker). Bugs reproduced in both environments unless noted.

| ID    | Endpoint                | Expected                       | Actual                                        | Environments |
|-------|-------------------------|--------------------------------|-----------------------------------------------|--------------|
| BUG-1 | `GET /users/{email}`    | 404 when user not found        | 500 Internal Server Error                     | dev, prod    |
| BUG-2 | `POST /users`           | 409 on duplicate email         | 500 Internal Server Error                     | dev, prod    |
| BUG-3 | `DELETE /users/{email}` | 401 without valid token        | 204 (deletes without auth)                    | **dev only** |
| BUG-4 | `PUT /users/{email}`    | changes persisted to DB        | DB unchanged; response echoes request payload | dev, prod    |
| BUG-5 | `PUT /users/{email}`    | response reflects stored email | response echoes request body email instead    | dev, prod    |
| BUG-6 | `POST /users`           | 400 on invalid email format    | 201 (user created with invalid email)         | dev, prod    |

### BUG-1 — GET /users/{email} returns 500 for missing user

`GET /users/nonexistent@example.com` returns `500 Internal Server Error` instead of `404`. Callers cannot distinguish a
missing user from a server crash. Same 500 is triggered after a DELETE when the record no longer exists.

**Failing tests:** `GetUserTest.getUserByEmail_shouldReturn404WhenUserNotFound`,
`DeleteUserTest.deleteUser_shouldMakeUserUnreachableAfterDeletion`

---

### BUG-2 — POST /users returns 500 on duplicate email

Creating a user with an email that already exists returns `500 Internal Server Error` instead of `409 Conflict`. The
uniqueness constraint violation is not handled and leaks as a 500.

**Failing test:** `CreateUserTest.createUser_shouldReturn409WhenEmailAlreadyExists`

---

### BUG-3 — DELETE /users/{email} ignores authentication (dev only)

`DELETE /users/{email}` returns `204` regardless of whether the `Authentication` header is absent or contains an invalid
token. Any unauthenticated caller can delete any user. Works correctly in prod.

**Failing tests:** `TokenOnNonAuthEndpointsTest.deleteUser_shouldReturn401WhenNoToken`,
`TokenOnNonAuthEndpointsTest.deleteUser_shouldReturn401WhenInvalidToken`

---

### BUG-4 — PUT /users/{email} does not persist changes

`PUT` returns `200` with the updated payload, but a subsequent `GET` returns the original data. The database is never
updated — the endpoint is non-functional.

**Failing test:** `UpdateUserTest.updateUser_shouldReturn200WithUpdatedData`

---

### BUG-5 — PUT /users/{email} echoes request email instead of stored email

When a `PUT` request includes a different email in the body than the path parameter, the `200` response echoes the
request body email rather than the actual stored email. The stored email is unchanged, so the response is misleading.

**Failing test:** `UpdateUserTest.updateUser_shouldReturn200WithUpdatedData`

---

### BUG-6 — POST /users accepts invalid email format

`POST /users` with `"email": "ABC"` (not a valid email address) returns `201` and creates the user. No format validation
is applied to the email field.

**Failing test:** `CreateUserTest.createUser_shouldReturn400WhenEmailIsInvalid`
