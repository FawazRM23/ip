# UI Test Plan

This file is the source of truth for command-driven console UI tests. Run the
cases in document order and stop at the first mismatch.

## Test environment

- Required JDK: Java 25
- Build command (PowerShell):

  ```powershell
  javac -d out src/main/java/bond/Bond.java src/main/java/bond/Task.java src/main/java/bond/TaskList.java src/main/java/bond/Todo.java src/main/java/bond/Deadline.java src/main/java/bond/Event.java
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
