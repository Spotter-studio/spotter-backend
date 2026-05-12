# Spotter API Endpoints

## Authentication

| Method | Endpoint | Description |
| --- | --- | --- |
| `POST` | `/api/users/signup` | Sign up with email, username, and password. |
| `POST` | `/api/users/login` | Log in and issue a JWT access token. |
| `GET` | `/api/users/me` | Get the current authenticated user's profile. |
| `PATCH` | `/api/users/me` | Update the current user's name, email, or password. |
| `DELETE` | `/api/users/me` | Delete the current user account and owned user relations. |

## Categories

| Method | Endpoint | Description |
| --- | --- | --- |
| `GET` | `/api/categories` | Get all place categories. |

## Shared Posts

Shared posts are intermediate records created when a user shares an SNS post before confirming the final places to save.

| Method | Endpoint | Description |
| --- | --- | --- |
| `POST` | `/api/shared-posts` | Upload a shared SNS post with caption screenshot images, source URL, source type, and optional OCR text. |
| `GET` | `/api/shared-posts/pending` | Get the current user's shared posts that are waiting for review. |
| `GET` | `/api/shared-posts/{sharedPostId}` | Get one shared post with images, source URL, OCR text, and extracted place candidates. |
| `POST` | `/api/shared-posts/{sharedPostId}/confirm` | Confirm the final user-verified place list and create `locations` and `scraps`. |
| `DELETE` | `/api/shared-posts/{sharedPostId}` | Delete or discard an unconfirmed shared post. |

## Place Search

| Method | Endpoint | Description |
| --- | --- | --- |
| `GET` | `/api/places/search?query={query}` | Search places through the external map/place provider, such as Naver Maps. |

## Scraps

| Method | Endpoint | Description |
| --- | --- | --- |
| `GET` | `/api/scraps` | Get the current user's saved places. |
| `GET` | `/api/scraps?categoryId={categoryId}` | Get the current user's saved places filtered by category. |
| `PATCH` | `/api/scraps/{scrapId}` | Update scrap metadata, such as memo or visit count. |
| `DELETE` | `/api/scraps/{scrapId}` | Delete a saved place from the current user's scraps. |

## Locations

| Method | Endpoint | Description |
| --- | --- | --- |
| `GET` | `/api/locations/map?minLat={minLat}&maxLat={maxLat}&minLng={minLng}&maxLng={maxLng}` | Get locations within the current map bounding box. |

## Friendships

| Method | Endpoint | Description |
| --- | --- | --- |
| `POST` | `/api/friends/requests` | Send a friend request. |
| `GET` | `/api/friends/requests/incoming` | Get incoming pending friend requests. |
| `POST` | `/api/friends/requests/{requestId}/accept` | Accept a friend request. |
| `POST` | `/api/friends/requests/{requestId}/reject` | Reject a friend request. |
| `GET` | `/api/friends` | Get the current user's accepted friends. |
| `GET` | `/api/friends/{friendId}/common-locations` | Get locations saved by both the current user and the selected friend. |

## Meetups

| Method | Endpoint | Description |
| --- | --- | --- |
| `POST` | `/api/meetups` | Create a meetup. |
| `GET` | `/api/meetups` | Get all visible meetups. |
| `GET` | `/api/meetups/{meetupId}` | Get details for one meetup. |
| `POST` | `/api/meetups/{meetupId}/join` | Join an existing meetup. |
| `POST` | `/api/meetups/{meetupId}/cancel` | Cancel a meetup. Only the meetup host can use this endpoint. |
| `POST` | `/api/meetups/{meetupId}/leave` | Leave a meetup. Only non-host participants can use this endpoint. |

## Meetup Invitations

Private meetups use invitations to control access.

| Method | Endpoint | Description |
| --- | --- | --- |
| `POST` | `/api/meetups/{meetupId}/invitations` | Invite users to a private meetup. |
| `GET` | `/api/meetup-invitations/incoming` | Get incoming meetup invitations for the current user. |
| `POST` | `/api/meetup-invitations/{invitationId}/accept` | Accept a meetup invitation. |
| `POST` | `/api/meetup-invitations/{invitationId}/reject` | Reject a meetup invitation. |

## Recommendations

| Method | Endpoint | Description |
| --- | --- | --- |
| `GET` | `/api/recommendations/locations` | Recommend unsaved locations based on the current user's preferred categories. |
| `GET` | `/api/recommendations/friends` | Recommend unsaved locations based on places saved by friends. |
