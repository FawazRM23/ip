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
