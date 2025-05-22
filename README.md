
# Chess Engine

**A Java-based chess engine focusing on move generation, board state management, and game rules enforcement.**

---

## Table of Contents

- [Overview](#overview)  
- [Features](#features)  
- [Getting Started](#getting-started)  
- [Usage](#usage)  
- [Testing](#testing)  
- [Project Structure](#project-structure)  
- [Contributing](#contributing)  
- [License](#license)  

---

## Overview

This project implements the core logic of a chess engine written in Java. It manages:

- Chess board representation and state
- Piece movement and validation
- Move history tracking
- Basic game rules enforcement (turns, captures)
- Planned features: castling, en passant, promotion, undo moves

The engine aims to provide a solid foundation for building a complete chess AI or GUI client.

---

## Features

- 8x8 Board representation with piece positioning  
- Support for all standard chess pieces (Pawn, Knight, Bishop, Rook, Queen, King)  
- Move validation including basic rules and turn management  
- Move history recording for undo and analysis  
- Test-driven development with comprehensive unit tests  
- Fluent API for flexible board setup in tests (e.g., `board.place("e2", new Pawn(Color.WHITE))`)  

---

## Getting Started

### Prerequisites

- Java 17 or later  
- Maven or Gradle for build management (if applicable)  
- JUnit 5 for running tests  

### Clone the repository

```bash
git clone https://github.com/mc4/chess-engine.git
cd chess-engine
```

### Build

Use your IDE or command line tools to build the project. For example, with Maven:

```bash
mvn clean install
```

---

## Usage

The core class is `Board`, which represents the chessboard and controls game flow.

```java
Board board = new Board();  // initializes standard starting position

boolean moveSuccessful = board.makeMove(new Position("e2"), new Position("e4"));

if (moveSuccessful) {
    System.out.println("Move played successfully!");
} else {
    System.out.println("Invalid move!");
}
```

For custom board setups (useful in testing or engine scenarios):

```java
Board board = Board.emptyBoard()
                   .place("e2", new Pawn(Color.WHITE))
                   .place("e1", new King(Color.WHITE))
                   .place("e8", new King(Color.BLACK));
```

---

## Testing

Unit tests are written with JUnit 5 and cover key features like:

- Initial board setup validation  
- Piece placement and retrieval  
- Legal and illegal move execution  
- Move history tracking  

Run tests with:

```bash
mvn test
```

or directly in your IDE.

---

## Project Structure

```
src/
 └─ main/
     └─ java/
         └─ com.optimism.chess.engine/
             ├─ board/          # Board and Position classes
             ├─ pieces/         # Piece hierarchy: Pawn, King, Queen, etc.
             ├─ move/           # Move and related classes
             └─ core/           # Core enums and utilities (Color, etc.)
 └─ test/
     └─ java/
         └─ com.optimism.chess.engine.board/  # BoardTest and related tests
```

---

## Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository  
2. Create a feature branch (`git checkout -b feature/my-feature`)  
3. Commit your changes (`git commit -m 'Add new feature'`)  
4. Push to your branch (`git push origin feature/my-feature`)  
5. Open a Pull Request  

Please write tests for any new features or bug fixes.

---

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---
