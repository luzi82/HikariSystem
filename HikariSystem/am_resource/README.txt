am_resource

Feature
=======

- game can have different type of resource
- user can own different type of resource
- resource count max on single type of level
- resource increase by time on single type of level

Depends
=======

- django.contrib.auth
- am_i18n
- am_level

Limitation
==========

- Limitation can only apply on single type of level
 - If more, hard to implement logic
- When level increase, does not check count max
- When level increase, resource increase calculation may not accurate
 