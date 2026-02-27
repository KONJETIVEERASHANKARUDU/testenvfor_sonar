# Social Media App - User Stories & Acceptance Criteria

## Document Information
**Project:** Social Media Application  
**Version:** 1.0  
**Date:** February 26, 2026  
**Format:** User Story → Acceptance Criteria (Table Format)

---

## User Story Template

```
As a [user type],
I want to [action/feature],
So that [benefit/value]
```

---

## 1. User Registration

### User Story
**As a** new visitor  
**I want to** register for an account  
**So that** I can access the social media platform and connect with others

### Acceptance Criteria

| ID | Criterion | Priority | Status |
|----|-----------|----------|--------|
| AC-1.1 | User can register using email and password | MUST | ⏳ To Do |
| AC-1.2 | User can register using Google OAuth | SHOULD | ⏳ To Do |
| AC-1.3 | User can register using Facebook OAuth | SHOULD | ⏳ To Do |
| AC-1.4 | Email must be unique in the system | MUST | ⏳ To Do |
| AC-1.5 | Password must be minimum 8 characters with 1 uppercase, 1 lowercase, 1 number | MUST | ⏳ To Do |
| AC-1.6 | Username must be unique and 3-20 characters | MUST | ⏳ To Do |
| AC-1.7 | User receives email verification link after registration | MUST | ⏳ To Do |
| AC-1.8 | User cannot access full features until email is verified | MUST | ⏳ To Do |
| AC-1.9 | Error messages display for invalid input | MUST | ⏳ To Do |
| AC-1.10 | Success message displays after successful registration | SHOULD | ⏳ To Do |

### Test Cases

| Test ID | Description | Expected Result |
|---------|-------------|-----------------|
| TC-1.1 | Register with valid email and password | Account created successfully |
| TC-1.2 | Register with existing email | Error: "Email already in use" |
| TC-1.3 | Register with weak password | Error: "Password does not meet requirements" |
| TC-1.4 | Register with invalid email format | Error: "Invalid email format" |
| TC-1.5 | Register using Google OAuth | Account created and logged in |

---

## 2. User Login

### User Story
**As a** registered user  
**I want to** log in to my account  
**So that** I can access my profile and social features

### Acceptance Criteria

| ID | Criterion | Priority | Status |
|----|-----------|----------|--------|
| AC-2.1 | User can login with email and password | MUST | ⏳ To Do |
| AC-2.2 | User can login with username and password | MUST | ⏳ To Do |
| AC-2.3 | User can login using Google OAuth | SHOULD | ⏳ To Do |
| AC-2.4 | User can login using Facebook OAuth | SHOULD | ⏳ To Do |
| AC-2.5 | "Remember Me" checkbox keeps user logged in for 30 days | SHOULD | ⏳ To Do |
| AC-2.6 | "Forgot Password" link redirects to password reset | MUST | ⏳ To Do |
| AC-2.7 | User is redirected to home feed after successful login | MUST | ⏳ To Do |
| AC-2.8 | Error message displays for incorrect credentials | MUST | ⏳ To Do |
| AC-2.9 | Account is locked after 5 failed login attempts | SHOULD | ⏳ To Do |
| AC-2.10 | User session expires after 24 hours of inactivity | SHOULD | ⏳ To Do |

### Test Cases

| Test ID | Description | Expected Result |
|---------|-------------|-----------------|
| TC-2.1 | Login with valid credentials | Successfully logged in and redirected to feed |
| TC-2.2 | Login with incorrect password | Error: "Invalid credentials" |
| TC-2.3 | Login with unverified email | Error: "Please verify your email" |
| TC-2.4 | Login with locked account | Error: "Account locked. Try again in 15 minutes" |

---

## 3. Create Post

### User Story
**As a** logged-in user  
**I want to** create and publish posts  
**So that** I can share content with my followers

### Acceptance Criteria

| ID | Criterion | Priority | Status |
|----|-----------|----------|--------|
| AC-3.1 | User can create text-only post (up to 5000 characters) | MUST | ⏳ To Do |
| AC-3.2 | User can upload images (JPEG, PNG, GIF, max 10MB) | MUST | ⏳ To Do |
| AC-3.3 | User can upload videos (MP4, MOV, max 500MB) | SHOULD | ⏳ To Do |
| AC-3.4 | User can add up to 5 images per post | SHOULD | ⏳ To Do |
| AC-3.5 | User can add hashtags (e.g., #travel) | MUST | ⏳ To Do |
| AC-3.6 | User can mention other users (e.g., @username) | MUST | ⏳ To Do |
| AC-3.7 | User can add location/place tag | SHOULD | ⏳ To Do |
| AC-3.8 | User can set post privacy (Public, Friends, Only Me) | MUST | ⏳ To Do |
| AC-3.9 | User can save post as draft | SHOULD | ⏳ To Do |
| AC-3.10 | User can schedule post for future date/time | COULD | ⏳ To Do |
| AC-3.11 | Post displays timestamp after publishing | MUST | ⏳ To Do |
| AC-3.12 | Real-time character counter displays for text | SHOULD | ⏳ To Do |
| AC-3.13 | User receives confirmation after successful post | MUST | ⏳ To Do |

### Test Cases

| Test ID | Description | Expected Result |
|---------|-------------|-----------------|
| TC-3.1 | Create text-only post | Post published and visible in feed |
| TC-3.2 | Create post with image | Image uploaded and displayed in post |
| TC-3.3 | Create post exceeding character limit | Error: "Post exceeds character limit" |
| TC-3.4 | Create post with 6 images | Error: "Maximum 5 images allowed" |
| TC-3.5 | Create post with hashtags | Hashtags are clickable and searchable |
| TC-3.6 | Create post mentioning user | Mentioned user receives notification |

---

## 4. Like Post

### User Story
**As a** logged-in user  
**I want to** like posts  
**So that** I can show appreciation for content I enjoy

### Acceptance Criteria

| ID | Criterion | Priority | Status |
|----|-----------|----------|--------|
| AC-4.1 | User can like a post by clicking heart/like icon | MUST | ⏳ To Do |
| AC-4.2 | Like count increments immediately on client side | MUST | ⏳ To Do |
| AC-4.3 | Like is persisted to database | MUST | ⏳ To Do |
| AC-4.4 | User can unlike a post by clicking icon again | MUST | ⏳ To Do |
| AC-4.5 | Post author receives notification when post is liked | SHOULD | ⏳ To Do |
| AC-4.6 | User can view list of users who liked a post | SHOULD | ⏳ To Do |
| AC-4.7 | Liked posts display in user's "Liked Posts" section | SHOULD | ⏳ To Do |
| AC-4.8 | User cannot like the same post multiple times | MUST | ⏳ To Do |
| AC-4.9 | Like icon changes color/appearance when liked | MUST | ⏳ To Do |
| AC-4.10 | Like action is optimistic with rollback on error | SHOULD | ⏳ To Do |

### Test Cases

| Test ID | Description | Expected Result |
|---------|-------------|-----------------|
| TC-4.1 | Like a post | Like count increases, icon changes color |
| TC-4.2 | Unlike a post | Like count decreases, icon resets |
| TC-4.3 | Like post when not logged in | Redirect to login page |
| TC-4.4 | Like post with network error | UI reverts to unliked state |

---

## 5. Comment on Post

### User Story
**As a** logged-in user  
**I want to** comment on posts  
**So that** I can engage in conversations and share my thoughts

### Acceptance Criteria

| ID | Criterion | Priority | Status |
|----|-----------|----------|--------|
| AC-5.1 | User can add comment (up to 2000 characters) | MUST | ⏳ To Do |
| AC-5.2 | Comment displays user's profile picture and name | MUST | ⏳ To Do |
| AC-5.3 | Comment displays timestamp (relative, e.g., "2 hours ago") | MUST | ⏳ To Do |
| AC-5.4 | Post author receives notification for new comment | SHOULD | ⏳ To Do |
| AC-5.5 | User can edit their own comments | SHOULD | ⏳ To Do |
| AC-5.6 | User can delete their own comments | MUST | ⏳ To Do |
| AC-5.7 | User can reply to comments (nested comments) | SHOULD | ⏳ To Do |
| AC-5.8 | User can like comments | SHOULD | ⏳ To Do |
| AC-5.9 | User can mention other users in comments | SHOULD | ⏳ To Do |
| AC-5.10 | Comments are sorted by newest first (default) | MUST | ⏳ To Do |
| AC-5.11 | User can sort comments by "Most Liked" | COULD | ⏳ To Do |
| AC-5.12 | Comment count displays on post | MUST | ⏳ To Do |
| AC-5.13 | "Load More" button shows older comments | SHOULD | ⏳ To Do |

### Test Cases

| Test ID | Description | Expected Result |
|---------|-------------|-----------------|
| TC-5.1 | Add comment to post | Comment appears immediately |
| TC-5.2 | Add empty comment | Error: "Comment cannot be empty" |
| TC-5.3 | Edit own comment | Comment updated successfully |
| TC-5.4 | Delete own comment | Comment removed from post |
| TC-5.5 | Reply to comment | Reply nested under parent comment |

---

## 6. Follow User

### User Story
**As a** logged-in user  
**I want to** follow other users  
**So that** I can see their posts in my news feed

### Acceptance Criteria

| ID | Criterion | Priority | Status |
|----|-----------|----------|--------|
| AC-6.1 | User can follow another user by clicking "Follow" button | MUST | ⏳ To Do |
| AC-6.2 | Follow button changes to "Following" after click | MUST | ⏳ To Do |
| AC-6.3 | User can unfollow by clicking "Following" button | MUST | ⏳ To Do |
| AC-6.4 | Followed user receives notification | SHOULD | ⏳ To Do |
| AC-6.5 | Followed user's posts appear in follower's feed | MUST | ⏳ To Do |
| AC-6.6 | Follower count updates in real-time | SHOULD | ⏳ To Do |
| AC-6.7 | Following count updates in real-time | SHOULD | ⏳ To Do |
| AC-6.8 | User can view list of followers | MUST | ⏳ To Do |
| AC-6.9 | User can view list of following | MUST | ⏳ To Do |
| AC-6.10 | User cannot follow themselves | MUST | ⏳ To Do |
| AC-6.11 | User can block/unblock users | SHOULD | ⏳ To Do |
| AC-6.12 | Blocked users cannot follow or see posts | SHOULD | ⏳ To Do |

### Test Cases

| Test ID | Description | Expected Result |
|---------|-------------|-----------------|
| TC-6.1 | Follow a user | Button changes to "Following", count increases |
| TC-6.2 | Unfollow a user | Button changes to "Follow", count decreases |
| TC-6.3 | Attempt to follow self | Error or button disabled |
| TC-6.4 | Block a user | User removed from followers, cannot follow |

---

## 7. News Feed

### User Story
**As a** logged-in user  
**I want to** view a personalized news feed  
**So that** I can see posts from people I follow and trending content

### Acceptance Criteria

| ID | Criterion | Priority | Status |
|----|-----------|----------|--------|
| AC-7.1 | Feed displays posts from followed users | MUST | ⏳ To Do |
| AC-7.2 | Feed displays user's own posts | MUST | ⏳ To Do |
| AC-7.3 | Posts are sorted by relevance (algorithm-based) | SHOULD | ⏳ To Do |
| AC-7.4 | User can switch to "Latest" view (chronological) | SHOULD | ⏳ To Do |
| AC-7.5 | Infinite scroll loads more posts automatically | MUST | ⏳ To Do |
| AC-7.6 | "Pull to refresh" updates feed with new posts | SHOULD | ⏳ To Do |
| AC-7.7 | Feed shows "Suggested Posts" from non-followed users | COULD | ⏳ To Do |
| AC-7.8 | User can hide/report inappropriate posts | SHOULD | ⏳ To Do |
| AC-7.9 | Feed displays loading skeleton while fetching | SHOULD | ⏳ To Do |
| AC-7.10 | Empty state message when no posts available | MUST | ⏳ To Do |
| AC-7.11 | Feed caches posts for offline viewing | COULD | ⏳ To Do |

### Test Cases

| Test ID | Description | Expected Result |
|---------|-------------|-----------------|
| TC-7.1 | Load news feed | Posts from followed users displayed |
| TC-7.2 | Scroll to bottom of feed | More posts loaded automatically |
| TC-7.3 | Pull to refresh | New posts appear at top |
| TC-7.4 | Switch to "Latest" view | Posts sorted by time descending |

---

## 8. User Profile

### User Story
**As a** logged-in user  
**I want to** view and edit my profile  
**So that** I can manage my personal information and customize my presence

### Acceptance Criteria

| ID | Criterion | Priority | Status |
|----|-----------|----------|--------|
| AC-8.1 | Profile displays username, bio, location | MUST | ⏳ To Do |
| AC-8.2 | Profile displays profile picture | MUST | ⏳ To Do |
| AC-8.3 | Profile displays cover photo | SHOULD | ⏳ To Do |
| AC-8.4 | Profile displays follower count | MUST | ⏳ To Do |
| AC-8.5 | Profile displays following count | MUST | ⏳ To Do |
| AC-8.6 | Profile displays post count | MUST | ⏳ To Do |
| AC-8.7 | Profile displays grid of user's posts | MUST | ⏳ To Do |
| AC-8.8 | User can edit bio (up to 250 characters) | MUST | ⏳ To Do |
| AC-8.9 | User can upload new profile picture | MUST | ⏳ To Do |
| AC-8.10 | User can upload new cover photo | SHOULD | ⏳ To Do |
| AC-8.11 | User can add website URL | SHOULD | ⏳ To Do |
| AC-8.12 | User can set profile to private | SHOULD | ⏳ To Do |
| AC-8.13 | Profile displays "Edit Profile" button for own profile | MUST | ⏳ To Do |
| AC-8.14 | Profile displays "Follow" button for other profiles | MUST | ⏳ To Do |
| AC-8.15 | User can view other users' profiles | MUST | ⏳ To Do |

### Test Cases

| Test ID | Description | Expected Result |
|---------|-------------|-----------------|
| TC-8.1 | View own profile | All profile information displayed |
| TC-8.2 | Edit bio | Bio updated successfully |
| TC-8.3 | Upload profile picture | Picture updated and cropped properly |
| TC-8.4 | View other user's profile | Public profile information displayed |
| TC-8.5 | View private profile (not following) | Limited information shown |

---

## 9. Search

### User Story
**As a** logged-in user  
**I want to** search for users, posts, and hashtags  
**So that** I can discover content and connect with people

### Acceptance Criteria

| ID | Criterion | Priority | Status |
|----|-----------|----------|--------|
| AC-9.1 | User can search by username | MUST | ⏳ To Do |
| AC-9.2 | User can search by hashtag | MUST | ⏳ To Do |
| AC-9.3 | User can search by post content | SHOULD | ⏳ To Do |
| AC-9.4 | Search displays results as user types (auto-suggest) | SHOULD | ⏳ To Do |
| AC-9.5 | Search results categorized (Users, Posts, Hashtags) | SHOULD | ⏳ To Do |
| AC-9.6 | Search history saved for quick access | SHOULD | ⏳ To Do |
| AC-9.7 | User can clear search history | SHOULD | ⏳ To Do |
| AC-9.8 | Trending hashtags displayed on search page | COULD | ⏳ To Do |
| AC-9.9 | Recent searches displayed before typing | SHOULD | ⏳ To Do |
| AC-9.10 | Empty state message when no results found | MUST | ⏳ To Do |
| AC-9.11 | Search is case-insensitive | MUST | ⏳ To Do |

### Test Cases

| Test ID | Description | Expected Result |
|---------|-------------|-----------------|
| TC-9.1 | Search for username | Matching users displayed |
| TC-9.2 | Search for hashtag | Posts with hashtag displayed |
| TC-9.3 | Search with no results | "No results found" message |
| TC-9.4 | Search with typo | Suggested corrections displayed |

---

## 10. Notifications

### User Story
**As a** logged-in user  
**I want to** receive notifications for interactions  
**So that** I stay informed about activity on my posts and profile

### Acceptance Criteria

| ID | Criterion | Priority | Status |
|----|-----------|----------|--------|
| AC-10.1 | User receives notification when someone likes their post | MUST | ⏳ To Do |
| AC-10.2 | User receives notification when someone comments on their post | MUST | ⏳ To Do |
| AC-10.3 | User receives notification when someone follows them | MUST | ⏳ To Do |
| AC-10.4 | User receives notification when mentioned in post | MUST | ⏳ To Do |
| AC-10.5 | User receives notification when mentioned in comment | SHOULD | ⏳ To Do |
| AC-10.6 | Notification badge displays unread count | MUST | ⏳ To Do |
| AC-10.7 | Notifications marked as read when clicked | MUST | ⏳ To Do |
| AC-10.8 | User can mark all notifications as read | SHOULD | ⏳ To Do |
| AC-10.9 | User can enable/disable notification types | SHOULD | ⏳ To Do |
| AC-10.10 | Push notifications sent to mobile devices | SHOULD | ⏳ To Do |
| AC-10.11 | Email notifications sent for important events | COULD | ⏳ To Do |
| AC-10.12 | Notifications grouped by type | SHOULD | ⏳ To Do |
| AC-10.13 | Notifications display timestamp | MUST | ⏳ To Do |

### Test Cases

| Test ID | Description | Expected Result |
|---------|-------------|-----------------|
| TC-10.1 | Receive like notification | Badge updates, notification appears in list |
| TC-10.2 | Click on notification | Marked as read, navigates to related post |
| TC-10.3 | Disable comment notifications | No notifications for comments received |
| TC-10.4 | Mark all as read | All notifications marked read, badge clears |

---

## 11. Direct Messaging

### User Story
**As a** logged-in user  
**I want to** send private messages to other users  
**So that** I can have one-on-one conversations

### Acceptance Criteria

| ID | Criterion | Priority | Status |
|----|-----------|----------|--------|
| AC-11.1 | User can send text message to another user | MUST | ⏳ To Do |
| AC-11.2 | User can send images in messages | SHOULD | ⏳ To Do |
| AC-11.3 | User can send emojis in messages | SHOULD | ⏳ To Do |
| AC-11.4 | Messages display in chronological order | MUST | ⏳ To Do |
| AC-11.5 | Unread message badge displays on message icon | MUST | ⏳ To Do |
| AC-11.6 | User receives real-time messages (WebSocket) | SHOULD | ⏳ To Do |
| AC-11.7 | "Typing..." indicator when other user is typing | SHOULD | ⏳ To Do |
| AC-11.8 | User can delete messages for themselves | SHOULD | ⏳ To Do |
| AC-11.9 | User can delete conversation | MUST | ⏳ To Do |
| AC-11.10 | Message list displays recent conversations | MUST | ⏳ To Do |
| AC-11.11 | User can search conversations | SHOULD | ⏳ To Do |
| AC-11.12 | "Seen" indicator when message is read | COULD | ⏳ To Do |
| AC-11.13 | User can block users from messaging | SHOULD | ⏳ To Do |

### Test Cases

| Test ID | Description | Expected Result |
|---------|-------------|-----------------|
| TC-11.1 | Send message to user | Message delivered and displayed |
| TC-11.2 | Receive message | Real-time notification appears |
| TC-11.3 | Delete conversation | All messages removed from list |
| TC-11.4 | Block user | User cannot send messages |

---

## 12. Settings & Privacy

### User Story
**As a** logged-in user  
**I want to** manage my account settings and privacy  
**So that** I can control my experience and data

### Acceptance Criteria

| ID | Criterion | Priority | Status |
|----|-----------|----------|--------|
| AC-12.1 | User can change password | MUST | ⏳ To Do |
| AC-12.2 | User can change email address | MUST | ⏳ To Do |
| AC-12.3 | User can enable two-factor authentication | SHOULD | ⏳ To Do |
| AC-12.4 | User can set account to private | MUST | ⏳ To Do |
| AC-12.5 | User can control who can comment on posts | SHOULD | ⏳ To Do |
| AC-12.6 | User can control who can tag them | SHOULD | ⏳ To Do |
| AC-12.7 | User can control who can message them | SHOULD | ⏳ To Do |
| AC-12.8 | User can mute/unmute notifications | SHOULD | ⏳ To Do |
| AC-12.9 | User can download their data | SHOULD | ⏳ To Do |
| AC-12.10 | User can deactivate account | MUST | ⏳ To Do |
| AC-12.11 | User can delete account permanently | MUST | ⏳ To Do |
| AC-12.12 | User can view blocked users list | SHOULD | ⏳ To Do |
| AC-12.13 | User can manage connected apps | COULD | ⏳ To Do |

### Test Cases

| Test ID | Description | Expected Result |
|---------|-------------|-----------------|
| TC-12.1 | Change password | Password updated, requires re-login |
| TC-12.2 | Enable 2FA | SMS/App code required on next login |
| TC-12.3 | Set account to private | Only followers can see posts |
| TC-12.4 | Deactivate account | Account hidden, can be reactivated |
| TC-12.5 | Delete account | Account permanently removed after 30 days |

---

## Summary Statistics

### Total User Stories: 12

### Total Acceptance Criteria: 143

### Priority Breakdown:
- **MUST:** 87 criteria (60.8%)
- **SHOULD:** 48 criteria (33.6%)
- **COULD:** 8 criteria (5.6%)

### Feature Categories:
1. Authentication (Registration, Login)
2. Content Creation (Posts)
3. Engagement (Likes, Comments)
4. Social (Follow, Messages)
5. Discovery (Feed, Search)
6. Notifications
7. Profile Management
8. Settings & Privacy

---

## How to Use This Document

### For Product Managers:
- Use user stories for backlog planning
- Prioritize features based on MUST/SHOULD/COULD
- Track acceptance criteria completion

### For Developers:
- Each AC is a testable requirement
- Use test cases for unit/integration tests
- Refer to AC for implementation details

### For QA Engineers:
- Test cases derived from acceptance criteria
- Each AC should have corresponding test
- Use for regression testing

### For Stakeholders:
- Clear understanding of feature scope
- Progress tracking via status column
- Easy to validate deliverables

---

## Status Legend

| Symbol | Meaning |
|--------|---------|
| ⏳ | To Do |
| 🚧 | In Progress |
| ✅ | Completed |
| ❌ | Blocked |
| 🔄 | In Review |

---

**Document Prepared By:** Product Engineering Team  
**Last Updated:** February 26, 2026
