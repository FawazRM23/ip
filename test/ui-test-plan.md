# UI Test Plan

This file is the source of truth for command-driven console UI tests. Run the
cases in document order and stop at the first mismatch.

## Test environment

- Required JDK: Java 25
- Build command (PowerShell):

  ```powershell
  javac -d out src/main/java/bond/Bond.java src/main/java/bond/BondException.java src/main/java/bond/CommandType.java src/main/java/bond/Parser.java src/main/java/bond/Ui.java src/main/java/bond/Task.java src/main/java/bond/TaskList.java src/main/java/bond/Todo.java src/main/java/bond/Deadline.java src/main/java/bond/Event.java
  ```

- Run command:

  ```powershell
  java -cp out bond.Bond
  ```

- Session isolation: Start a fresh process for every test case.
- Output comparison: Compare exactly, except that CRLF and LF line endings are
  equivalent and terminal-generated input echo is ignored. Spaces, blank lines,
  case, punctuation, and program-generated output remain significant.

## Expected startup output

Every fresh process must first print:

```text
    ____________________________________________________________
    ____                  __
   / __ )____  ____  ____/ /
  / __  / __ \/ __ \/ __  /
 / /_/ / /_/ / / / / /_/ /
/_____/\____/_/ /_/\__,_/
    Good day! I'm Bond, James Bond.
    Agent 007 at your service, what can I do for you?
    ____________________________________________________________
```

## UI-1: Exit immediately

**Aim:** Verify that `bye` ends a new session with the farewell message.

### Input 1

```text
bye
```

### Expected output 1

```text
    ____________________________________________________________
    Bye. Hope to embark on a mission again soon!
    ____________________________________________________________
```

## UI-2: Add and list a to-do

**Aim:** Verify that a to-do is stored, counted, and displayed as not done.

### Input 1

```text
todo read book
```

### Expected output 1

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] read book
    Now you have 1 mission in the list.
    ____________________________________________________________
```

### Input 2

```text
list
```

### Expected output 2

```text
    ____________________________________________________________
    Here are the missions in your list:
    1.[T][ ] read book
    ____________________________________________________________
```

### Input 3

```text
bye
```

### Expected output 3

```text
    ____________________________________________________________
    Bye. Hope to embark on a mission again soon!
    ____________________________________________________________
```

## UI-3: Mark and unmark a to-do

**Aim:** Verify that completion status can be set and cleared within one session.

### Input 1

```text
todo submit report
```

### Expected output 1

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] submit report
    Now you have 1 mission in the list.
    ____________________________________________________________
```

### Input 2

```text
mark 1
```

### Expected output 2

```text
    ____________________________________________________________
    Nice work, agent! Another mission accomplished!:
      [T][X] submit report
    ____________________________________________________________
```

### Input 3

```text
unmark 1
```

### Expected output 3

```text
    ____________________________________________________________
    OK, I've marked this mission as not accomplished yet:
      [T][ ] submit report
    ____________________________________________________________
```

### Input 4

```text
bye
```

### Expected output 4

```text
    ____________________________________________________________
    Bye. Hope to embark on a mission again soon!
    ____________________________________________________________
```

## UI-4: Reject unknown commands without storing them

**Aim:** Verify that invalid commands between valid operations produce helpful
errors, do not add tasks, and do not prevent later commands from running.

### Input 1

```text
todo secure files
```

### Expected output 1

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] secure files
    Now you have 1 mission in the list.
    ____________________________________________________________
```

### Input 2

```text
blah
```

### Expected output 2

```text
    ____________________________________________________________
    Mission error: I don't recognize that command.
    Try: todo, deadline, event, list, mark, unmark, or bye.
    ____________________________________________________________
```

### Input 3

```text
list
```

### Expected output 3

```text
    ____________________________________________________________
    Here are the missions in your list:
    1.[T][ ] secure files
    ____________________________________________________________
```

### Input 4

```text
todo retrieve intel
```

### Expected output 4

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] retrieve intel
    Now you have 2 missions in the list.
    ____________________________________________________________
```

### Input 5

```text
list now
```

### Expected output 5

```text
    ____________________________________________________________
    Mission error: I don't recognize that command.
    Try: todo, deadline, event, list, mark, unmark, or bye.
    ____________________________________________________________
```

### Input 6

```text
list
```

### Expected output 6

```text
    ____________________________________________________________
    Here are the missions in your list:
    1.[T][ ] secure files
    2.[T][ ] retrieve intel
    ____________________________________________________________
```

### Input 7

```text
bye
```

### Expected output 7

```text
    ____________________________________________________________
    Bye. Hope to embark on a mission again soon!
    ____________________________________________________________
```

## UI-5: Reject empty and case-mismatched commands

**Aim:** Verify that edge-case commands do not change tasks and that Bond
continues to accept valid commands after each error.

### Input 1

```text
todo inspect equipment
```

### Expected output 1

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] inspect equipment
    Now you have 1 mission in the list.
    ____________________________________________________________
```

### Input 2 (empty line)

```text

```

### Expected output 2

```text
    ____________________________________________________________
    Mission error: I don't recognize that command.
    Try: todo, deadline, event, list, mark, unmark, or bye.
    ____________________________________________________________
```

### Input 3

```text
list
```

### Expected output 3

```text
    ____________________________________________________________
    Here are the missions in your list:
    1.[T][ ] inspect equipment
    ____________________________________________________________
```

### Input 4

```text
LIST
```

### Expected output 4

```text
    ____________________________________________________________
    Mission error: I don't recognize that command.
    Try: todo, deadline, event, list, mark, unmark, or bye.
    ____________________________________________________________
```

### Input 5

```text
list
```

### Expected output 5

```text
    ____________________________________________________________
    Here are the missions in your list:
    1.[T][ ] inspect equipment
    ____________________________________________________________
```

### Input 6 (one leading space)

```text
 list
```

### Expected output 6

```text
    ____________________________________________________________
    Mission error: I don't recognize that command.
    Try: todo, deadline, event, list, mark, unmark, or bye.
    ____________________________________________________________
```

### Input 7

```text
list
```

### Expected output 7

```text
    ____________________________________________________________
    Here are the missions in your list:
    1.[T][ ] inspect equipment
    ____________________________________________________________
```

### Input 8

```text
bye
```

### Expected output 8

```text
    ____________________________________________________________
    Bye. Hope to embark on a mission again soon!
    ____________________________________________________________
```

## UI-6: Reject a todo without a description

**Aim:** Verify that a missing todo description does not add a task and that
valid todo commands before and after the error remain correct.

### Input 1

```text
todo survey perimeter
```

### Expected output 1

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] survey perimeter
    Now you have 1 mission in the list.
    ____________________________________________________________
```

### Input 2

```text
todo
```

### Expected output 2

```text
    ____________________________________________________________
    Mission error: This todo mission has no description.
    Brief me with: todo <description>.
    ____________________________________________________________
```

### Input 3

```text
list
```

### Expected output 3

```text
    ____________________________________________________________
    Here are the missions in your list:
    1.[T][ ] survey perimeter
    ____________________________________________________________
```

### Input 4

```text
todo prepare escape route
```

### Expected output 4

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] prepare escape route
    Now you have 2 missions in the list.
    ____________________________________________________________
```

### Input 5

```text
list
```

### Expected output 5

```text
    ____________________________________________________________
    Here are the missions in your list:
    1.[T][ ] survey perimeter
    2.[T][ ] prepare escape route
    ____________________________________________________________
```

### Input 6

```text
bye
```

### Expected output 6

```text
    ____________________________________________________________
    Bye. Hope to embark on a mission again soon!
    ____________________________________________________________
```

## UI-7: Validate deadline mission details

**Aim:** Verify that each required deadline component is reported specifically,
invalid deadlines are not stored, and valid deadlines remain correct.

### Input 1

```text
deadline file report /by Friday
```

### Expected output 1

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [D][ ] file report (by: Friday)
    Now you have 1 mission in the list.
    ____________________________________________________________
```

### Input 2

```text
deadline
```

### Expected output 2

```text
    ____________________________________________________________
    Mission error: This deadline mission has no description.
    Brief me with: deadline <description> /by <date or time>.
    ____________________________________________________________
```

### Input 3

```text
list
```

### Expected output 3

```text
    ____________________________________________________________
    Here are the missions in your list:
    1.[D][ ] file report (by: Friday)
    ____________________________________________________________
```

### Input 4

```text
deadline secure documents
```

### Expected output 4

```text
    ____________________________________________________________
    Mission error: This deadline mission is missing its /by marker.
    Brief me with: deadline <description> /by <date or time>.
    ____________________________________________________________
```

### Input 5

```text
deadline secure documents /by
```

### Expected output 5

```text
    ____________________________________________________________
    Mission error: This deadline mission has no date or time.
    Brief me with: deadline <description> /by <date or time>.
    ____________________________________________________________
```

### Input 6

```text
deadline /by Monday
```

### Expected output 6

```text
    ____________________________________________________________
    Mission error: This deadline mission has no description.
    Brief me with: deadline <description> /by <date or time>.
    ____________________________________________________________
```

### Input 7

```text
deadline contact M /by Monday
```

### Expected output 7

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [D][ ] contact M (by: Monday)
    Now you have 2 missions in the list.
    ____________________________________________________________
```

### Input 8

```text
list
```

### Expected output 8

```text
    ____________________________________________________________
    Here are the missions in your list:
    1.[D][ ] file report (by: Friday)
    2.[D][ ] contact M (by: Monday)
    ____________________________________________________________
```

### Input 9

```text
bye
```

### Expected output 9

```text
    ____________________________________________________________
    Bye. Hope to embark on a mission again soon!
    ____________________________________________________________
```

## UI-8: Validate event mission details

**Aim:** Verify that each required event component is reported specifically,
invalid events are not stored, and valid commands still update state correctly.

### Input 1

```text
event gala /from 8pm /to 10pm
```

### Expected output 1

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [E][ ] gala (from: 8pm to: 10pm)
    Now you have 1 mission in the list.
    ____________________________________________________________
```

### Input 2

```text
event
```

### Expected output 2

```text
    ____________________________________________________________
    Mission error: This event mission has no description.
    Brief me with: event <description> /from <start> /to <end>.
    ____________________________________________________________
```

### Input 3

```text
list
```

### Expected output 3

```text
    ____________________________________________________________
    Here are the missions in your list:
    1.[E][ ] gala (from: 8pm to: 10pm)
    ____________________________________________________________
```

### Input 4

```text
event surveillance
```

### Expected output 4

```text
    ____________________________________________________________
    Mission error: This event mission is missing its /from marker.
    Brief me with: event <description> /from <start> /to <end>.
    ____________________________________________________________
```

### Input 5

```text
event surveillance /from
```

### Expected output 5

```text
    ____________________________________________________________
    Mission error: This event mission has no starting date or time.
    Brief me with: event <description> /from <start> /to <end>.
    ____________________________________________________________
```

### Input 6

```text
event surveillance /from 8pm
```

### Expected output 6

```text
    ____________________________________________________________
    Mission error: This event mission is missing its /to marker.
    Brief me with: event <description> /from <start> /to <end>.
    ____________________________________________________________
```

### Input 7

```text
event surveillance /from /to 10pm
```

### Expected output 7

```text
    ____________________________________________________________
    Mission error: This event mission has no starting date or time.
    Brief me with: event <description> /from <start> /to <end>.
    ____________________________________________________________
```

### Input 8

```text
event surveillance /from 8pm /to
```

### Expected output 8

```text
    ____________________________________________________________
    Mission error: This event mission has no ending date or time.
    Brief me with: event <description> /from <start> /to <end>.
    ____________________________________________________________
```

### Input 9

```text
event /from 8pm /to 10pm
```

### Expected output 9

```text
    ____________________________________________________________
    Mission error: This event mission has no description.
    Brief me with: event <description> /from <start> /to <end>.
    ____________________________________________________________
```

### Input 10

```text
todo file findings
```

### Expected output 10

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] file findings
    Now you have 2 missions in the list.
    ____________________________________________________________
```

### Input 11

```text
list
```

### Expected output 11

```text
    ____________________________________________________________
    Here are the missions in your list:
    1.[E][ ] gala (from: 8pm to: 10pm)
    2.[T][ ] file findings
    ____________________________________________________________
```

### Input 12

```text
bye
```

### Expected output 12

```text
    ____________________________________________________________
    Bye. Hope to embark on a mission again soon!
    ____________________________________________________________
```

## UI-9: Validate mark mission numbers

**Aim:** Verify that malformed and unavailable mark selections leave mission
state unchanged and that a later valid selection still succeeds.

### Input 1

```text
mark 1
```

### Expected output 1

```text
    ____________________________________________________________
    Mission error: The mission dossier is empty.
    Add a mission before trying to mark it.
    ____________________________________________________________
```

### Input 2

```text
todo inspect safe house
```

### Expected output 2

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] inspect safe house
    Now you have 1 mission in the list.
    ____________________________________________________________
```

### Input 3

```text
mark
```

### Expected output 3

```text
    ____________________________________________________________
    Mission error: I need a mission number for that mark order.
    Brief me with: mark <mission number>.
    ____________________________________________________________
```

### Input 4

```text
list
```

### Expected output 4

```text
    ____________________________________________________________
    Here are the missions in your list:
    1.[T][ ] inspect safe house
    ____________________________________________________________
```

### Input 5

```text
mark one
```

### Expected output 5

```text
    ____________________________________________________________
    Mission error: That mission number is not a valid whole number.
    Brief me with: mark <mission number>.
    ____________________________________________________________
```

### Input 6

```text
mark 0
```

### Expected output 6

```text
    ____________________________________________________________
    Mission error: Mission numbers start at 1, agent.
    Brief me with: mark <mission number>.
    ____________________________________________________________
```

### Input 7

```text
mark -3
```

### Expected output 7

```text
    ____________________________________________________________
    Mission error: Mission numbers start at 1, agent.
    Brief me with: mark <mission number>.
    ____________________________________________________________
```

### Input 8

```text
mark 2
```

### Expected output 8

```text
    ____________________________________________________________
    Mission error: Mission 2 is not in the dossier.
    Choose mission number 1.
    ____________________________________________________________
```

### Input 9

```text
mark 999999999999999999999999
```

### Expected output 9

```text
    ____________________________________________________________
    Mission error: That mission number is not a valid whole number.
    Brief me with: mark <mission number>.
    ____________________________________________________________
```

### Input 10

```text
mark 1
```

### Expected output 10

```text
    ____________________________________________________________
    Nice work, agent! Another mission accomplished!:
      [T][X] inspect safe house
    ____________________________________________________________
```

### Input 11

```text
list
```

### Expected output 11

```text
    ____________________________________________________________
    Here are the missions in your list:
    1.[T][X] inspect safe house
    ____________________________________________________________
```

### Input 12

```text
bye
```

### Expected output 12

```text
    ____________________________________________________________
    Bye. Hope to embark on a mission again soon!
    ____________________________________________________________
```

## UI-10: Validate unmark mission numbers

**Aim:** Verify that malformed and unavailable unmark selections preserve a
completed mission and that a later valid selection can still clear its status.

### Input 1

```text
todo decode message
```

### Expected output 1

```text
    ____________________________________________________________
    Got it. I've added this mission:
      [T][ ] decode message
    Now you have 1 mission in the list.
    ____________________________________________________________
```

### Input 2

```text
mark 1
```

### Expected output 2

```text
    ____________________________________________________________
    Nice work, agent! Another mission accomplished!:
      [T][X] decode message
    ____________________________________________________________
```

### Input 3

```text
unmark
```

### Expected output 3

```text
    ____________________________________________________________
    Mission error: I need a mission number for that unmark order.
    Brief me with: unmark <mission number>.
    ____________________________________________________________
```

### Input 4

```text
list
```

### Expected output 4

```text
    ____________________________________________________________
    Here are the missions in your list:
    1.[T][X] decode message
    ____________________________________________________________
```

### Input 5

```text
unmark 1.5
```

### Expected output 5

```text
    ____________________________________________________________
    Mission error: That mission number is not a valid whole number.
    Brief me with: unmark <mission number>.
    ____________________________________________________________
```

### Input 6

```text
unmark 0
```

### Expected output 6

```text
    ____________________________________________________________
    Mission error: Mission numbers start at 1, agent.
    Brief me with: unmark <mission number>.
    ____________________________________________________________
```

### Input 7

```text
unmark 2
```

### Expected output 7

```text
    ____________________________________________________________
    Mission error: Mission 2 is not in the dossier.
    Choose mission number 1.
    ____________________________________________________________
```

### Input 8

```text
unmark 1 2
```

### Expected output 8

```text
    ____________________________________________________________
    Mission error: That mission number is not a valid whole number.
    Brief me with: unmark <mission number>.
    ____________________________________________________________
```

### Input 9

```text
unmark 1
```

### Expected output 9

```text
    ____________________________________________________________
    OK, I've marked this mission as not accomplished yet:
      [T][ ] decode message
    ____________________________________________________________
```

### Input 10

```text
list
```

### Expected output 10

```text
    ____________________________________________________________
    Here are the missions in your list:
    1.[T][ ] decode message
    ____________________________________________________________
```

### Input 11

```text
bye
```

### Expected output 11

```text
    ____________________________________________________________
    Bye. Hope to embark on a mission again soon!
    ____________________________________________________________
```
