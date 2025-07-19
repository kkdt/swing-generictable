>Ran into a very neat implementation of `javax.swing.table.DefaultTableModel` using a custom tuple and lambdas to define column names, indices, and column types. That pattern became very repetitive so I wanted to see if I can generalize that via annotations.  

# Swing Generic Table Model

Goals
1. Annotate columns right at the UI data model definition.
2. Table model exposes the underlying UI data model rather than an array of array of objects.

# Quick Start

```
./gradlew clean build publishToMavenLocal
```