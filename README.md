# Retro 3D Java Prototype

A self-contained Java scaffold for a low-poly third-person action adventure
prototype inspired by PlayStation 1 and Nintendo 64 era visuals.

The project uses only the JDK and Swing. It renders a fixed low-resolution
framebuffer, then scales it up for a chunky retro look.

## Requirements

- Java JDK 8 or newer
- PowerShell on Windows

## Run

```powershell
.\scripts\run.ps1
```

That script compiles the app into `build/classes` and starts the prototype.

## Controls

- `WASD` - move and face the character in that direction
- `Arrow Left` / `Arrow Right` - turn the character and follow camera
- `Arrow Up` / `Arrow Down` - look up/down
- `Space` / `Ctrl` - sword attack
- `Shift` - move faster
- `H` / `J` - damage/heal test keys
- `Escape` - quit

## Project Layout

```text
src/main/java/com/retro3d/
  Main.java                 Application entry point
  game/                     Window, loop, input, camera, player, world
  math/                     Small vector utilities
  render/                   Software triangle renderer
```

## Direction

This is intended as a starting point, not an engine. Good next steps:

- Add enemy behavior and sword hit detection.
- Add texture sampling with affine warping for a stronger PS1 style.
- Add an entity/component layer once prototype mechanics are clear.
- Replace Swing with LWJGL later if hardware acceleration becomes necessary.
